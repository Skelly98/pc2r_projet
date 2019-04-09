type t = {mutable id : int; (** mutable for easy delete *)
          mtx : Mutex.t;
          mutable coord_x : float; mutable coord_y : float;
          mutable speed_x : float; mutable speed_y : float;
          mutable angle : float; mass : float; radius : float}

let thrust_power = Values.thrust_power

let turn_speed = Values.turn_speed

(** default object with id = -1 *)
let default = {id = -1 ; mtx = Mutex.create () ; coord_x = 0. ; coord_y = 0. ; speed_x = 0.; speed_y = 0.; angle = 0.; mass = 0.; radius = 0.}

(** simply set id to -1 *)
let delete obj =
  Mutex.lock obj.mtx;
  obj.id <- -1;
  Mutex.unlock obj.mtx

let coords obj = (obj.coord_x, obj.coord_y)

let move obj =
  Mutex.lock obj.mtx;
  if obj.id <> -1 then
  begin
    (match obj.coord_x +. obj.speed_x with
    |x when x >= -.Values.half_width && x < Values.half_width -> obj.coord_x <- x
    |x when x < -.Values.half_width -> obj.coord_x <- x +. 2. *. Values.half_width
    |x -> obj.coord_x <- x +. 2. *. -.Values.half_width); (** when x >= Values.half_width  *)

    (match obj.coord_y +. obj.speed_y with
    |y when y >= -.Values.half_height && y < Values.half_height -> obj.coord_y <- y
    |y when y < -.Values.half_height -> obj.coord_y <- y +. 2. *. Values.half_height
    |y -> obj.coord_y <- y +. 2. *. -.Values.half_height) (** when y >= Values.half_height  *)
  end;
  Mutex.unlock obj.mtx

let create id mass radius =
  {id=id; mtx=Mutex.create ();
  coord_x=((Random.float (2. *. Values.half_width)) -. Values.half_width);
  coord_y=((Random.float (2. *. Values.half_height)) -. Values.half_height);
  speed_x=0.; speed_y=0.; angle=0.; mass=mass; radius=radius}

let turn obj angle =
  Mutex.lock obj.mtx;
  obj.angle <- (obj.angle +. angle);
  Mutex.unlock obj.mtx

let accelerate obj thrust =
  Mutex.lock obj.mtx;
  obj.speed_x <- obj.speed_x +. (float_of_int thrust) *. (cos obj.angle);
  obj.speed_y <- obj.speed_y +. (float_of_int thrust) *. (sin obj.angle);
  Mutex.unlock obj.mtx

(** used inside collision so no mutex (deadlock) *)
(** also used for checking the objectives, but the only thread using that function
is also the only thread moving the objects, so we don't need any mutex *)
let touching obj1 obj2 =
  obj1.id <> obj2.id && ((obj1.coord_x -. obj2.coord_x) ** 2. +. (obj1.coord_y -. obj2.coord_y) ** 2. <= (obj1.radius +. obj2.radius) ** 2.)

(** no interblocage as this is the only function using 2 mutex *)
let collision_comp obj1 obj2 =
  Mutex.lock obj1.mtx;
  Mutex.lock obj2.mtx;
  (if touching obj1 obj2
    then begin
      obj1.speed_x <- -. obj1.speed_x;
      obj1.speed_y <- -. obj1.speed_y
    end);
  Mutex.unlock obj2.mtx;
  Mutex.unlock obj1.mtx

(** no interblocage as this is the only function using 2 mutex *)
let collision obj1 obj2 =
  Mutex.lock obj1.mtx;
  Mutex.lock obj2.mtx;
  (if touching obj1 obj2
    then begin
      (** src : http://owl-ge.ch/IMG/pdf/choc_2D_avec_citation.pdf (page 12) *)
      (** definitions of constants *)
      let direction_1 = acos (obj1.speed_x +. obj1.speed_y)
      and direction_2 = acos (obj2.speed_x +. obj2.speed_y)
      and speed_1 = sqrt (obj1.speed_x**2. +. obj1.speed_y**2.)
      and speed_2 = sqrt (obj2.speed_x**2. +. obj2.speed_y**2.)
      and m1_m2_div_m1_m2 = (obj1.mass -. obj2.mass) /. (obj1.mass +. obj2.mass)
      and m2_m1_div_m2_m1 = (obj2.mass -. obj1.mass) /. (obj2.mass +. obj1.mass)
      and _2_m2_div_m1_m2 = (2. *. obj2.mass ) /. (obj1.mass +. obj2.mass)
      and _2_m1_div_m1_m2 = (2. *. obj1.mass ) /. (obj1.mass +. obj2.mass) in

      (** calcul *)
      let new_direction_1 = atan (m1_m2_div_m1_m2 *. (tan direction_1)
        +. (_2_m2_div_m1_m2 *. speed_2 *. (sin direction_2)) /. (speed_1 *. (cos direction_1)))
      and new_direction_2 = atan (m2_m1_div_m2_m1 *. (tan direction_2)
        +. (_2_m1_div_m1_m2 *. speed_1 *. (sin direction_1)) /. (speed_2 *. (cos direction_2))) in
      let new_speed_1 = sqrt ((m1_m2_div_m1_m2 *. speed_1 *. (sin direction_1) +. _2_m2_div_m1_m2 *. speed_2 *. (sin direction_2))**2. +. (speed_1 *. (cos direction_1))**2.)
      and new_speed_2 = sqrt ((m2_m1_div_m2_m1 *. speed_2 *. (sin direction_2) +. _2_m1_div_m1_m2 *. speed_1 *. (sin direction_1))**2. +. (speed_2 *. (cos direction_2))**2.) in

      (** new speeds *)
      obj1.speed_x <- new_speed_1 *. (cos new_direction_1);
      obj1.speed_y <- new_speed_1 *. (sin new_direction_1);
      obj2.speed_x <- new_speed_2 *. (cos new_direction_2);
      obj2.speed_y <- new_speed_2 *. (sin new_direction_2)
    end);
  Mutex.unlock obj2.mtx;
  Mutex.unlock obj1.mtx
