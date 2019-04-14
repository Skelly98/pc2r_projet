let server_tickrate = 60.

(** use a multiple of server_tickrate would be better *)
let server_refresh_tickrate = 10. *. server_tickrate

let max_objects = 40

let max_players = 20

let nb_asteroids = 15

let max_score = 1

let countdown = 10.

let half_width = 500.

let half_height = 500.

let pi = 4.0 *. atan 1.0

let ship_mass = 100.

let ship_radius = 10.

let objectif_radius = 20.

let asteroid_mass = 1000.

let asteroid_radius = 50.

let phase = ref "attente"

let compatibility_mode = ref false
