type t = {mutable id : int; (** mutable for easy delete *)
          mtx : Mutex.t;
          mutable coord_x : float; mutable coord_y : float;
          mutable speed_x : float; mutable speed_y : float;
          mutable angle : float}

let thrust_power = Values.thrust_power

let turn_speed = Values.turn_speed

(** default object with id = -1 *)
let default = {id = -1 ; mtx = Mutex.create () ; coord_x = 0. ; coord_y = 0. ; speed_x = 0.; speed_y = 0.; angle = 0.}

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

let create id = {id=id; mtx=Mutex.create ();
                 coord_x=((Random.float (2. *. Values.half_width)) -. Values.half_width);
                 coord_y=((Random.float (2. *. Values.half_height)) -. Values.half_height);
                 speed_x=0.;speed_y=0.;angle=0.}

let pi = 4. *. atan 1.

(** commands *)

let turn obj angle =
  Mutex.lock obj.mtx;
  obj.angle <- (obj.angle +. angle);
  Mutex.unlock obj.mtx

let accelerate obj thrust =
  Mutex.lock obj.mtx;
  obj.speed_x <- obj.speed_x +. (float_of_int thrust) *. (cos obj.angle);
  obj.speed_y <- obj.speed_y +. (float_of_int thrust) *. (sin obj.angle);
  Mutex.unlock obj.mtx
