type t = {name: string; ship_id: int; mutable score: int}

let create name id =
  Arena.add_object id;
  {name = name; ship_id = id; score = 0}

let fake = {name = "none"; ship_id = -1; score = 0}

let coords p = (Arena.objects.(p.ship_id).coord_x, Arena.objects.(p.ship_id).coord_y)

let speed p = (Arena.objects.(p.ship_id).speed_x, Arena.objects.(p.ship_id).speed_y)

let vcoord p = (p.name,(coords p),(speed p),Arena.objects.(p.ship_id).angle)

let name p = p.name
