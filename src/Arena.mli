type t = float array array

val objects : Object.t array

val max_x : float

val may_y : float

val min_x : float

val min_y : float

val origin : float * float

val move : Object.t -> unit

val move_all : Object.t array -> unit

val add_object : int -> unit

(** commands *)

val clock : int -> unit

val anticlock : int -> unit

val thrust : int -> unit
