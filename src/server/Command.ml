module FromClient = struct

type t =
  |CONNECT of string (** user *)
  |EXIT of string (** user *)
  |NEWCOM of float * int (** angle/thrust *)

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


end

module FromServer = struct

type t =
  |WELCOME of string * (string * int) list * (float * float) (** user/scores/coord *)
  |DENIED
  |NEWPLAYER of string (** user *)
  |PLAYERLEFT of string (** user *)
  |SESSION of (string * (float * float)) list (** coords *)
  |WINNER of (string * int) list (** scores *)
  |TICK of (string * (float * float) * (float * float)) list (** vcoords *)
  |NEWOBJ of (float * float) * (string * int) list (** coord/scores *)

let to_string cmd =
  match cmd with
  |_ -> "none"

end
