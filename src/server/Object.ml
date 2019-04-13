type t = {mutable id : int; (** mutable for easy delete *)
          mtx : Mutex.t;
          mutable coord_x : float; mutable coord_y : float;
          mutable speed_x : float; mutable speed_y : float;
          mutable angle : float; mass : float; radius : float}

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
  speed_x=0.; speed_y=0.; angle= -.Values.pi /. 2.; mass=mass; radius=radius}

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
(** also used for checking the objectifs, but the only thread using that function
is also the only thread moving the objects, so we don't need any mutex *)
let touching obj1 obj2 =
  (** chaining float operations gives unexpected results, so we decompose everything *)
  if obj1.id <> -1 && obj2.id <> -1 && obj1.id <> obj2.id
    then begin
      let x = (obj2.coord_x -. obj1.coord_x)
      and y = (obj2.coord_y -. obj1.coord_y) in
      let x_square = x ** 2.
      and y_square = y ** 2.
      and d = obj1.radius +. obj2.radius in
      let somme = x_square +. y_square in
      let d_square = d ** 2. in
      somme <= d_square
    end else false

(** no interblocage as this is the only function using 2 mutex *)
let collision_comp obj1 obj2 =
  Mutex.lock obj1.mtx;
  (if obj1.id <> obj2.id
    then Mutex.lock obj2.mtx);
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
  (if obj1.id <> obj2.id
    then Mutex.lock obj2.mtx);
  (if touching obj1 obj2
    then begin
      let xDist = obj1.coord_x -. obj2.coord_x
      and yDist = obj1.coord_y -. obj2.coord_y in
      let xDistSquared = xDist ** 2.
      and yDistSquared = yDist ** 2. in
      let distSquared = xDistSquared +. yDistSquared
      and xVelocity = obj2.speed_x -. obj1.speed_x
      and yVelocity = obj2.speed_y -. obj1.speed_y in
      let x_dotProduct = xDist *. xVelocity
      and y_dotProduct = yDist *. yVelocity in
      let dotProduct = x_dotProduct +. y_dotProduct in
      (** Neat vector maths, used for checking if the objects moves towards one another. *)
      (if dotProduct > 0.
        then begin
          let collisionScale = dotProduct /. distSquared in
          let xCollision = xDist *. collisionScale
          and yCollision = yDist *. collisionScale
          (** The Collision vector is the speed difference projected on the Dist vector, *)
          (** thus it is the component of the speed difference needed for the collision. *)
          and combinedMass = obj1.mass +. obj2.mass in
          let _2_mass_1 = 2. *. obj1.mass
          and _2_mass_2 = 2. *. obj2.mass in
          let collisionWeight1 = _2_mass_2 /. combinedMass
          and collisionWeight2 = _2_mass_1 /. combinedMass in

          let speed_x_1 = collisionWeight1 *. xCollision
          and speed_y_1 = collisionWeight1 *. yCollision
          and speed_x_2 = collisionWeight2 *. xCollision
          and speed_y_2 = collisionWeight2 *. yCollision in

          let new_speed_x_1 = obj1.speed_x +. speed_x_1
          and new_speed_y_1 = obj1.speed_y +. speed_y_1
          and new_speed_x_2 = obj2.speed_x -. speed_x_2
          and new_speed_y_2 = obj2.speed_y -. speed_y_2 in

          obj1.speed_x <- new_speed_x_1;
          obj1.speed_y <- new_speed_y_1;
          obj2.speed_x <- new_speed_x_2;
          obj2.speed_y <- new_speed_y_2

        end);
    end);
  Mutex.unlock obj2.mtx;
  Mutex.unlock obj1.mtx

let freeze obj =
  obj.speed_x <- 0.;
  obj.speed_y <- 0.;
  obj.angle <- -.Values.pi /. 2.
