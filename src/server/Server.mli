exception Client_exit

(** constants *)

val refresh_tickrate : float

(** the number of objects the server can manage *)
val max_objects : int

val max_x : float

val max_y : float

val min_x : float

val min_y : float

val thrust_power : float

val turn_speed : int

(** id for new object *)
val new_object_id : int ref

(*val send_new_coordonates : Unix.file_descr -> Object.t array -> unit*)

val create_server : int -> int -> Unix.file_descr

val server_process : Unix.file_descr -> ((in_channel * out_channel) * int -> 'a) -> unit

val server_service : (in_channel * out_channel) * int -> unit
