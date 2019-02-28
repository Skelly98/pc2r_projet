type t = float array array

(** constants *)
let max_objects = Constants.max_objects

let max_x = Constants.max_x

let may_y = Constants.max_y

let min_x = Constants.min_x

let min_y = Constants.min_y

let origin = (0., 0.)

(** all the objects in the game *)
let objects = Array.make max_objects Object.fake

(** Array.iter of move *)
let move_all () =
    Array.iter Object.move objects

(** create a new object with given id *)
let add_object id = objects.(id) <- Object.create id

(** set id of a given onject to -1 *)
let remove_object id = Object.delete objects.(id)

(** commands *)

let clock id = Object.clock objects.(id)

let anticlock id = Object.anticlock objects.(id)

let thrust id = Object.thrust objects.(id)
