type t =
  |CLOCK
  |ANTICLOCK
  |THRUST

val of_string : string -> t
