type t = {mutable id: int; (** mutable for easy delete *)
          mutable coord_x: float; mutable coord_y: float;
          mutable speed_x: float; mutable speed_y: float;
          mutable angle: int}

val thrust_power : float

val turn_speed : int

(** default object with id = -1 *)
val fake : t

(** simply set id to -1 *)
val delete : t -> unit

val move : t -> unit

val create : int -> t

val pi : float

val rad_of_degree : int -> float

(** commands *)

val clock : t -> unit

val anticlock : t -> unit

val thrust : t -> unit
