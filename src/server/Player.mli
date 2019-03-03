type t = {mutable name: string; mutable ship_id: int; mutable score: int}

val create : string -> int -> t

val fake : t

val coords : t -> float * float

val speed : t -> float * float

val name : t -> string
