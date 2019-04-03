(** all the objects in the game *)
let objects = Array.make Values.max_objects Object.default

(** Array.iter of move *)
let move_all () = Array.iter Object.move objects

(** create a new object with given id *)
(** not mutex, we suppose objects.(id) was an Object.default *)
let add_object id = objects.(id) <- Object.create id

(** set id of a given object to -1 *)
let remove_object id = Object.delete objects.(id)

(** commands *)

let turn id angle = Object.turn objects.(id) angle

let accelerate id thrust = Object.accelerate objects.(id) thrust
