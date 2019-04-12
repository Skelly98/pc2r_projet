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
  |WELCOME_COMP of user * scores * coord * (float * float) list * int
  |WELCOME of user * scores * coord * coords * int
  |DENIED
  |NEWPLAYER of user
  |PLAYERLEFT of user
  |SESSION_COMP of coords * coord * (float * float) list
  |SESSION of coords * coord * coords
  |WINNER of scores
  |TICK_COMP of vcoords
  |TICK of vcoords * vcoords
  |NEWOBJ of coord * scores

val string_of_scores : scores -> string

val string_of_coords : coords -> string

val string_of_coords_no_name : (float * float) list -> string

val string_of_vcoords : vcoords -> string

val to_string : t -> string

end
