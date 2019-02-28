type t = {mutable id : int; (** mutable for easy delete *)
          mutable coord_x : float; mutable coord_y : float;
          mutable speed_x : float; mutable speed_y : float;
          mutable angle : int}

let thrust_power = Constants.thrust_power

let turn_speed = Constants.turn_speed

(** default object with id = -1 *)
let fake = {id = -1 ; coord_x = 0. ; coord_y = 0. ; speed_x = 0.; speed_y = 0.; angle = 0}

(** simply set id to -1 *)
let delete obj = obj.id <- -1

let move obj =
  (match obj.coord_x +. obj.speed_x with
  |x when x >= Constants.min_x && x < Constants.max_x -> obj.coord_x <- x 
  |x when x < Constants.min_x -> obj.coord_x <- x +. 2. *. Constants.max_x
  |x when x >= Constants.max_x -> obj.coord_x <- x +. 2. *. Constants.min_x);

  match obj.coord_y +. obj.speed_y with
  |y when y >= Constants.min_y && y < Constants.max_y -> obj.coord_y <- y 
  |y when y < Constants.min_y -> obj.coord_y <- y +. 2. *. Constants.max_y
  |y when y >= Constants.max_y -> obj.coord_y <- y +. 2. *. Constants.min_y


let create id = {id=id; coord_x=0.; coord_y=0.;speed_x=0.;speed_y=0.;angle=0}

let pi = 4.0 *. atan 1.0

let rad_of_degree angle = (float_of_int angle) *. pi /. 180.

(** commands *)

let clock obj = obj.angle <- (obj.angle + turn_speed) mod 360

let anticlock obj = obj.angle <- (obj.angle - turn_speed) mod 360

let thrust obj = obj.speed_x <- obj.speed_x +. thrust_power *. (cos (rad_of_degree obj.angle));
                 obj.speed_y <- obj.speed_y +. thrust_power *. (sin (rad_of_degree obj.angle))
