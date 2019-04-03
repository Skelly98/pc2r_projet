open Player

exception Client_exit

let new_player_id = ref 0

let starting = ref false

let ended = ref false

let max_players = Values.max_players

let players = Array.make max_players ((stdin,stdout),Player.default) (** stdin and stdout for default chans *)

let scores () = List.filter (fun (name,score) -> name <> "") (List.map (fun ((_,_),p) -> (p.name,p.score)) (Array.to_list players))

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
            message ~id:id (Command.FromServer.WELCOME(name,(scores ()), Player.coords (snd players.(id))));
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
    Thread.delay 20.;
    message (Command.FromServer.SESSION(List.map (fun ((_,_),p) -> (p.name, Player.coords p)) (Array.to_list players), Object.coords Arena.objects.(0)));
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
      if (Arena.objects.(0).id = -1)
        then begin
          Arena.add_object 0;
          message (Command.FromServer.NEWOBJ(Object.coords Arena.objects.(0), scores ()))
        end;
      (** check collisions *)
      (** TODO *)
      (** move objects *)
      Arena.move_all ();
      (** sleep during server_tickrate - calcul_time *)
      let calcul_time = Sys.time () -. start in
      Thread.delay (Values.server_tickrate -. calcul_time);
      (** send message TICK to everyone *)
      message (Command.FromServer.TICK(List.map (fun ((_,_),p) -> Player.vcoords p) (Array.to_list players)))
    done;
  done

let _ =
  let port = int_of_string Sys.argv.(1) in
  let sock = create_server port 4 ; in
  ignore(Thread.create game ());
  server_process sock server_service
