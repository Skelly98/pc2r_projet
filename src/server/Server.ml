(* compilation: ocamlc -thread unix.cma threads.cma echoserver2.ml -o echoserver2 *)
exception Client_exit

let refresh_tickrate = 100.

let max_objects = 1000

let max_x = 5.

let max_y = 5.

let min_x = -5.

let min_y = -5.

let thrust_power = 0.05

let turn_speed = 5

let new_object_id = ref 0

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
      new_object_id := !new_object_id + 1
  done

let server_service (chans,id) =
  let inchan = fst chans
  and outchan = snd chans
  in
    Arena.add_object id;
    try
      while true do
        match Command.of_string (input_line inchan) with
        |EXIT -> (Arena.remove_object id; raise Client_exit)
        |CLOCK -> Arena.clock id
        |ANTICLOCK -> Arena.anticlock id
        |THRUST -> Arena.thrust id
      done
    with Client_exit -> ()

let _ =
  let port = int_of_string Sys.argv.(1)
  in
  let sock = create_server port 4 ;
  in
    server_process sock server_service
