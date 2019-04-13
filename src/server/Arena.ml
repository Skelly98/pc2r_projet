open Object

(** all the objects in the game *)
let objects = Array.make Values.max_objects Object.default

let new_object_id = ref 0

let mtx = Mutex.create ()

let refresh_id () =
  Mutex.lock mtx;
  let rec loop ind =
    match (objects.(ind)) with
    |obj when obj.id = -1 -> (new_object_id := ind; Mutex.unlock mtx; true)
    |_ -> if ind < Values.max_objects then loop (ind + 1) else (Mutex.unlock mtx; false)
  in loop 0

(** create a new object with given id *)
let add_object id mass radius =
  let rec loop () =
    let obj = Object.create id mass radius in
    if List.exists (fun o -> (Mutex.lock o.mtx; let res = (Object.touching obj o) in Mutex.unlock o.mtx; res)) (Array.to_list objects)
      then loop ()
      else objects.(id) <- obj
  in loop ()

let add_object_no_id mass radius =
  if refresh_id ()
    then (add_object !new_object_id mass radius; !new_object_id)
    else -1

(** Array.iter of move *)
let move_all () = Array.iter Object.move objects

let move_all_ids ids = List.iter (fun id -> Object.move objects.(id)) ids

let objectif_id = add_object_no_id 0. Values.objectif_radius

let list_integers =
  let rec loop n acc =
    match n with
    |0 -> acc
    |x -> loop (x - 1) (x::acc)
  in loop (Values.max_objects - 1) []


let collision_all () =
  let rec loop ids acc =
    match ids with
    |hd::tl -> ((Array.iter (fun obj -> if (not (List.mem obj.id acc)) then Object.collision objects.(hd) obj) objects); loop tl (hd::acc))
    |[] -> ()
  in loop list_integers [objectif_id] (** we don't want a moving objectif *)

(** set id of a given object to -1 *)
let remove_object id = Object.delete objects.(id)

let turn id angle = Object.turn objects.(id) angle

let accelerate id thrust = Object.accelerate objects.(id) thrust
