type t = {name: string; ship_id: int; mutable score: int}

let create player_name =
  let id = Arena.add_object_no_id Values.ship_mass Values.ship_radius in
  {name = player_name; ship_id = id; score = 0}

let default = {name = ""; ship_id = -1; score = 0}

let coords p = (Arena.objects.(p.ship_id).coord_x, Arena.objects.(p.ship_id).coord_y)

let speed p = (Arena.objects.(p.ship_id).speed_x, Arena.objects.(p.ship_id).speed_y)

let vcoords p = (p.name,(coords p),(speed p),Arena.objects.(p.ship_id).angle)

let touching p obj = Object.touching Arena.objects.(p.ship_id) obj

let name p = p.name
