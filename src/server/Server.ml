open Player

exception Client_exit

let new_player_id = ref 0

let starting = ref false

let ended = ref false

let max_players = Values.max_players

let players = Array.make max_players ((stdin,stdout),Player.default) (** stdin and stdout for default chans *)

let asteroids_ids = Array.make Values.nb_asteroids (Arena.add_object_no_id Values.asteroid_mass Values.asteroid_radius)

let real_players () = List.filter (fun ((_,_),p) -> p.ship_id <> -1) (Array.to_list players)

let objective = ref (Object.create 0 0. Values.objective_radius)

let scores () = List.map (fun ((_,_),p) -> (p.name,p.score)) (real_players ())

let asteroids_coords () = List.map (fun x -> Arena.objects.(x).coord_x,Arena.objects.(x).coord_y) (List.filter (fun x -> x <> -1) (Array.to_list asteroids_ids))

let message ?(id = (-1)) cmd =
  match id with
  |(-1) -> Array.iter (fun x -> output_string (snd (fst x)) (Command.FromServer.to_string cmd)) players
  |id -> output_string (snd (fst players.(id))) (Command.FromServer.to_string cmd)

let create_server port max_con =
  let sock = Unix.socket Unix.PF_INET Unix.SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1"
  in
    Unix.bind sock (Unix.ADDR_INET(addr, port));
    Unix.listen sock max_con ;
    sock

let refresh_id () =
  let rec loop ind =
    match (snd players.(ind)) with
    |p when p.ship_id = -1 -> new_player_id := ind; true
    |_ -> if ind < max_players then loop (ind + 1) else false
  in loop 0

let server_process sock service =
  while true do
    let (s, caller) = Unix.accept sock
    in
      (if (refresh_id ())
        then begin
          ignore(Thread.create service ((Unix.in_channel_of_descr s,Unix.out_channel_of_descr s),!new_player_id));
          if (not !starting) then starting := true
        end else message ~id:(!new_player_id) Command.FromServer.DENIED)
  done

let server_service (chans,id) =
  let inchan = fst chans
  and outchan = snd chans
  in
    try
      while true do
        match Command.FromClient.of_string (input_line inchan) with
        |Command.FromClient.CONNECT(name) ->
          begin
            players.(id) <- ((inchan,outchan), Player.create name id);
            message ~id:id (Command.FromServer.WELCOME(name,(scores ()), Player.coords (snd players.(id)), asteroids_coords ()));
            if !Values.phase == "jeu" then message ~id:id (Command.FromServer.SESSION(List.map (fun ((_,_),p) -> (p.name, Player.coords p)) (real_players ()), Object.coords !objective, asteroids_coords ()));
            message (Command.FromServer.NEWPLAYER(name))
          end
        |Command.FromClient.EXIT(name) ->
          begin
            players.(id) <- ((stdin,stdout),Player.default);
            message (Command.FromServer.PLAYERLEFT(name)); raise Client_exit
          end
        |Command.FromClient.NEWCOM(angle,thrust) -> (Arena.turn id angle; Arena.accelerate id thrust)
        |Command.FromClient.UNRECOGNIZED -> () (** ignore unrecognized command *)
      done
    with Client_exit -> ()

let game () =
  while true do
    while (not !starting) do
      Thread.delay 1.
    done;
    Values.phase := "jeu";
    Thread.delay 20.;
    message (Command.FromServer.SESSION(List.map (fun ((_,_),p) -> (p.name, Player.coords p)) (real_players ()), Object.coords !objective, asteroids_coords ()));
    while (not !ended) do
      let start = Sys.time () in
      (** check scores *)
      for i = 0 to max_players do
        if (snd players.(i)).score >= Values.max_score
          then begin
            ended := true;
            message (Command.FromServer.WINNER((scores ())))
          end
      done;
      (** check objectif *)
      let at_least_one = ref false in
      (** we give points to all players touching the objective *)
      Array.iter (fun ((_,_),p) -> if (Player.touching p !objective) then (at_least_one := true;  p.score <- p.score + 1)) players;
      if !at_least_one then objective := (Object.create 0 0. Values.objective_radius);
      (** check collisions *)
      Array.iter (fun ((_,_),p) -> Array.iter (Object.collision Arena.objects.(p.ship_id)) Arena.objects) players;
      (** move objects *)
      Arena.move_all ();
      (** sleep during 1 / server_tickrate - calcul_time *)
      let wait_time = 1. /. Values.server_tickrate -. (Sys.time () -. start) in
      (** if wait_time < 0, Values.server_tickrate is too big *)
      (if (wait_time > 0.)
        then Thread.delay (wait_time));
      (** send message TICK to everyone *)
      message (Command.FromServer.TICK(List.map (fun ((_,_),p) -> Player.vcoords p) (real_players ())))
    done;
    starting := false;
    ended := false;
    Values.phase := "attente";
  done

let _ =
  let port = int_of_string Sys.argv.(1) in
  let sock = create_server port 4 ; in
  ignore(Thread.create game ());
  server_process sock server_service
