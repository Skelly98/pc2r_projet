type user = string
type scores = (string * int) list
type coord = (float * float)
type coords = (string * (float * float)) list
type vcoords = (string * (float * float) * (float * float) * float) list

module FromClient : sig

type t =
  |CONNECT of string (** user *)
  |EXIT of string (** user *)
  |NEWCOM of float * int (** angle/thrust *)
  |UNRECOGNIZED

val of_string : string -> t

end

module FromServer : sig

type t =
  |WELCOME of user * scores * coord * (float * float) list
  |DENIED
  |NEWPLAYER of user
  |PLAYERLEFT of user
  |SESSION of coords * coord * (float * float) list
  |WINNER of scores
  |TICK of vcoords
  |NEWOBJ of coord * scores

val string_of_scores : scores -> string

val string_of_coords : coords -> string

val string_of_coords_no_name : (float * float) list -> string

val string_of_vcoords : vcoords -> string

val to_string : t -> string

end
