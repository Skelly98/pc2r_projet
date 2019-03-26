exception Client_exit

(** id for new object *)
val new_object_id : int ref

val players : ((in_channel * out_channel) * Player.t) array

val message : ?id:int -> Command.FromServer.t -> unit

val create_server : int -> int -> Unix.file_descr

val server_process : Unix.file_descr -> ((in_channel * out_channel) * int -> 'a) -> unit

val server_service : (in_channel * out_channel) * int -> unit
