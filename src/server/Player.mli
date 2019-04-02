type t = {name: string; ship_id: int; mutable score: int}

val create : string -> int -> t

val fake : t

val coords : t -> float * float

val speed : t -> float * float

val vcoords : t -> string * (float * float) * (float * float) * float

val name : t -> string
