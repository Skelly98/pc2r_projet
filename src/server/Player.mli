type t = {name: string; ship_id: int; mutable score: int}

val create : string -> t

val default : t

val coords : t -> float * float

val speed : t -> float * float

val vcoords : t -> string * (float * float) * (float * float) * float

val touching : t -> Object.t -> bool

val name : t -> string
