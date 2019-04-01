module FromClient = struct

type t =
  |CONNECT of string (** user *)
  |EXIT of string (** user *)
  |NEWCOM of float * int (** angle/thrust *)
  |UNRECOGNIZED

let of_string cmd =
  match String.split_on_char '/' cmd with
  |["CONNECT";name] -> CONNECT(name)
  |["EXIT";name] -> EXIT(name)
  |["NEWCOM";comms] -> (if comms.[0] = 'A' (** A or T *)
                          then  match String.split_on_char 'T' cmd with
                                |[a_to_float; to_int] -> NEWCOM( (float_of_string (String.sub a_to_float 1 (String.length a_to_float))),
                                                                 (int_of_string to_int))
                          else match String.split_on_char 'A' cmd with
                                |[to_float; t_to_int] -> NEWCOM( (float_of_string to_float),
                                                                 (int_of_string (String.sub t_to_int 1 (String.length t_to_int)))))
  |_ -> UNRECOGNIZED

end

module FromServer = struct

let string_of_scores scores =
  let rec loop scores acc =
    match scores with
    |[] -> acc
    |(user,score)::[] -> acc ^ user ^ ":" ^ (string_of_int score)
    |(user,score)::tl -> loop tl (acc ^ user ^ ":" ^ (string_of_int score) ^ "|")
  in loop scores ""

let string_of_coords coords =
  let rec loop coords acc =
    match coords with
    |[] -> acc
    |(user,(x,y))::[] -> Printf.sprintf "%s%s:X%fY%f" acc user x y
    |(user,(x,y))::tl -> loop tl (Printf.sprintf "%s%s:X%fY%f|" acc user x y)
  in loop coords ""

let string_of_vcoords vcoords =
  let rec loop vcoords acc =
    match vcoords with
    |[] -> acc
    |(user,(x,y),(vx,vy),angle)::[] -> Printf.sprintf "%s%s:X%fY%f|VX%fVY%f|T%f" acc user x y vx vy angle
    |(user,(x,y),(vx,vy),angle)::tl -> loop tl (Printf.sprintf "%s%s:X%fY%f|VX%fVY%f|T%f|" acc user x y vx vy angle)
  in loop vcoords ""

type t =
  |WELCOME of string * (string * int) list * (float * float) (** user/scores/coord *)
  |DENIED
  |NEWPLAYER of string (** user *)
  |PLAYERLEFT of string (** user *)
  |SESSION of (string * (float * float)) list * (float * float) (** coords/coord *)
  |WINNER of (string * int) list (** scores *)
  |TICK of (string * (float * float) * (float * float) * float) list (** vcoords *)
  |NEWOBJ of (float * float) * (string * int) list (** coord/scores *)

let to_string cmd =
  match cmd with
  |WELCOME(user, scores, (x,y)) -> Printf.sprintf "WELCOME/%s/%sX%fY%f/" !Values.phase (string_of_scores scores) x y
  |DENIED -> "DENIED/"
  |NEWPLAYER user -> "NEWPLAYER/" ^ user ^ "/"
  |PLAYERLEFT user -> "PLAYERLEFT/" ^ user ^ "/"
  |SESSION(coords, (x,y)) -> Printf.sprintf "SESSION/%s/X%fY%f/" (string_of_coords coords) x y
  |WINNER scores -> "WINNER/" ^ (string_of_scores scores)
  |TICK vcoords -> "TICK/" ^ (string_of_vcoords vcoords)
  |NEWOBJ((x,y), scores) ->  Printf.sprintf "NEWOBJ/X%fY%f/%s" x y (string_of_scores scores)

end
