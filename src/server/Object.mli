type t = {mutable id : int; (** mutable for easy delete *)
          mtx : Mutex.t;
          mutable coord_x : float; mutable coord_y : float;
          mutable speed_x : float; mutable speed_y : float;
          mutable angle : float; mass : float; radius : float}

(** default object with id = -1 *)
val default : t

(** simply set id to -1 *)
val delete : t -> unit

val coords : t -> float * float

val move : t -> unit

val create : int -> float -> float -> t

val turn : t -> float -> unit

val accelerate : t -> int -> unit

val touching : t -> t -> bool

val collision_comp : t -> t -> unit

val collision : t -> t -> unit
