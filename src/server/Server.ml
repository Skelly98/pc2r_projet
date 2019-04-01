(* compilation: ocamlc -thread unix.cma threads.cma echoserver2.ml -o echoserver2 *)
exception Client_exit

let new_object_id = ref 0

let starting = ref false

let ended = ref false

let max_players = Values.max_players

let players = Array.make max_players ((stdin,stdout),Player.fake)

let create_server port max_con =
  let sock = Unix.socket Unix.PF_INET Unix.SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1"
  in
    Unix.bind sock (Unix.ADDR_INET(addr, port));
    Unix.listen sock max_con ;
    sock

let server_process sock service =
  while true do
    let (s, caller) = Unix.accept sock
    in
      ignore(Thread.create service ((Unix.in_channel_of_descr s,Unix.out_channel_of_descr s),!new_object_id));
      if (not !starting) then starting := true;
      new_object_id := !new_object_id + 1
  done

let message ?(id = (-1)) cmd =
  match id with
  |(-1) -> Array.iter (fun x -> output_string (snd (fst x)) (Command.FromServer.to_string cmd)) players
  |id -> output_string (snd (fst players.(id))) (Command.FromServer.to_string cmd)

let server_service (chans,id) =
  let inchan = fst chans
  and outchan = snd chans
  in
    try
      while true do
        match Command.FromClient.of_string (input_line inchan) with
        |Command.FromClient.CONNECT(name) -> players.(id) <- ((inchan,outchan), Player.create name id)
        |Command.FromClient.EXIT(name) -> (players.(id) <- ((stdin,stdout),Player.fake); message (Command.FromServer.PLAYERLEFT(name)); raise Client_exit)
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
    while (not !ended) do
      let start = Sys.time () in
      (** check scores *)
      for i = 0 to max_players do
        if (snd players.(i)).score >= Values.max_score then ended := true
      done;
      (** move objects *)
      Arena.move_all ();
      (** sleep during TICK - calcul_time *)
      let calcul_time = Sys.time () -. start in
      Thread.delay (Values.server_tickrate -. calcul_time);
      (** send message TICK to everyone *)
      message (Command.FromServer.TICK(List.map (fun ((_,_),p) -> Player.vcoord p) (Array.to_list players)))
    done;
  done

let server_game () =
  ignore(Thread.create game ()) 

let _ =
  let port = int_of_string Sys.argv.(1) in
  let sock = create_server port 4 ; in
  server_game ();
  server_process sock server_service
