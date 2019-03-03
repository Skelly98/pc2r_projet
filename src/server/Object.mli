type t = {mutable id: int; (** mutable for easy delete *)
          mutable coord_x: float; mutable coord_y: float;
          mutable speed_x: float; mutable speed_y: float;
          mutable angle: float}

val thrust_power : float

val turn_speed : int

(** default object with id = -1 *)
val fake : t

(** simply set id to -1 *)
val delete : t -> unit

val move : t -> unit

val create : int -> t

val pi : float

(** commands *)

val turn : t -> float -> unit

val accelerate : t -> int -> unit
