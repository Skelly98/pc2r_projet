exception Client_exit

(** id for new object *)
val new_object_id : int ref

(*val send_new_coordonates : Unix.file_descr -> Object.t array -> unit*)

val create_server : int -> int -> Unix.file_descr

val server_process : Unix.file_descr -> ((in_channel * out_channel) * int -> 'a) -> unit

val server_service : (in_channel * out_channel) * int -> unit
