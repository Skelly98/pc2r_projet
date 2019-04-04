(** all the objects in the game *)
val objects : Object.t array

val new_object_id : int ref

(** return false if there is no place for a new object *)
val refresh_id : unit -> bool

(** Array.iter of move *)
val move_all : unit -> unit

val move_all_ids : int list -> unit

(** create a new object with given id *)
val add_object : int -> float -> float -> unit

(** create a new object *)
val add_object_no_id : float -> float -> int

(** set id of a given onject to -1 *)
val remove_object : int -> unit

(** commands *)

val turn : int -> float -> unit

val accelerate : int -> int -> unit
