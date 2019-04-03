exception Client_exit

val new_player_id : int ref

(** has the countdown begun ? *)
val starting : bool ref

(** has a player won ? *)
val ended : bool ref

val players : ((in_channel * out_channel) * Player.t) array

(** send a command to one/all players *)
val message : ?id:int -> Command.FromServer.t -> unit

val create_server : int -> int -> Unix.file_descr

(** return false if there is no place for a new player *)
val refresh_id : unit -> bool

val server_process : Unix.file_descr -> ((in_channel * out_channel) * int -> 'a) -> unit

(** send all messages *)
val game : unit -> unit

(** receive and respond to messages send by a client *)
val server_service : (in_channel * out_channel) * int -> unit
