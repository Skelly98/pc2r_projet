(** all the objects in the game *)
let objects = Array.make Values.max_objects Object.default

let new_object_id = ref 0

let refresh_id () =
  let rec loop ind =
    match (objects.(ind)) with
    |obj when obj.id = -1 -> new_object_id := ind; true
    |_ -> if ind < Values.max_objects then loop (ind + 1) else false
  in loop 0

(** Array.iter of move *)
let move_all () = Array.iter Object.move objects

(** create a new object with given id *)
let add_object id mass radius = objects.(id) <- Object.create id mass radius

let add_object_no_id mass radius =
  if refresh_id ()
    then (objects.(!new_object_id) <- Object.create !new_object_id mass radius; !new_object_id)
    else -1

(** set id of a given object to -1 *)
let remove_object id = Object.delete objects.(id)

let turn id angle = Object.turn objects.(id) angle

let accelerate id thrust = Object.accelerate objects.(id) thrust
