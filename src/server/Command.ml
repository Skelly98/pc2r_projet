type t =
  |EXIT
  |CLOCK
  |ANTICLOCK
  |THRUST

let of_string msg =
    match msg with
    |"0" -> EXIT
    |"1" -> CLOCK
    |"2" -> ANTICLOCK
    |"3" -> THRUST
    |_   -> EXIT (** force client exit if unrecognized command *)
