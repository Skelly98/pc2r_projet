type t = float array array

(** the number of objects the server can manage *)
val max_objects : int

val max_x : float

val may_y : float

val min_x : float

val min_y : float

val origin : float * float

(** all the objects in the game *)
val objects : Object.t array

(** Array.iter of move *)
val move_all : unit -> unit

(** create a new object with given id *)
val add_object : int -> unit

(** set id of a given onject to -1 *)
val remove_object : int -> unit

(** commands *)

val clock : int -> unit

val anticlock : int -> unit

val thrust : int -> unit
