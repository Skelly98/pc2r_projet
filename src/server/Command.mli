type t =
  |EXIT
  |CLOCK
  |ANTICLOCK
  |THRUST

val of_string : string -> t
