val refresh_tickrate : float

val cpt : int ref

(*val send_new_coordonates : Unix.file_descr -> Object.t array -> unit*)

val create_server : int -> int -> Unix.file_descr

val server_process : Unix.file_descr -> (in_channel * out_channel -> 'a) -> unit

val server_service : in_channel * out_channel -> unit
