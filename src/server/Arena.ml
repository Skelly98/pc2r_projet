type t = float array array

(** constants *)
let max_objects = Constants.max_objects

(** all the objects in the game *)
let objects = Array.make max_objects Object.fake

(** Array.iter of move *)
let move_all () = Array.iter Object.move objects

(** create a new object with given id *)
let add_object id = objects.(id) <- Object.create id

(** set id of a given onject to -1 *)
let remove_object id = Object.delete objects.(id)

(** commands *)

let turn id angle = Object.turn objects.(id) angle

let accelerate id thrust = Object.accelerate objects.(id) thrust
