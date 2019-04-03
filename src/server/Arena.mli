(** all the objects in the game *)
val objects : Object.t array

(** Array.iter of move *)
val move_all : unit -> unit

(** create a new object with given id *)
val add_object : int -> unit

(** set id of a given onject to -1 *)
val remove_object : int -> unit

(** commands *)

val turn : int -> float -> unit

val accelerate : int -> int -> unit
