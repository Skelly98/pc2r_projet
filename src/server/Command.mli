module FromClient : sig

type t =
  |CONNECT of string (** user *)
  |EXIT of string (** user *)
  |NEWCOM of float * int (** angle/thrust *)

val of_string : string -> t

end

module FromServer : sig

type t =
  |WELCOME of string * (string * int) list * (float * float) (** user/scores/coord *)
  |DENIED
  |NEWPLAYER of string (** user *)
  |PLAYERLEFT of string (** user *)
  |SESSION of (string * (float * float)) list (** coords *)
  |WINNER of (string * int) list (** scores *)
  |TICK of (string * (float * float) * (float * float)) list (** vcoords *)
  |NEWOBJ of (float * float) * (string * int) list (** coord/scores *)

val to_string : t -> string

end
