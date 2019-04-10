package client;

import java.util.HashMap;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import model.Arena;

import view.GameWindow;

public class Client {

	public static void main(String[] args) {
		double refresh_tickrate = .100;
		Arena arena = new Arena();
		HashMap<String, Integer> players_scores = new HashMap<String, Integer>();
		if(args.length < 3) {
			System.err.println("Usage : java Client <hote> <port> <player_name>");
			System.exit(1);
		}
		Socket s = null;
		try {
			s = new Socket(args[0],Integer.parseInt(args[1]));
		} catch (IOException e) {
			System.err.println("IUnknown server");
			System.exit(1);
		}
		ObjectMover mover = new ObjectMover(arena, refresh_tickrate);
		Thread window = new Thread(new GameWindow(s, arena, players_scores, args[3]));
		Thread objMover = new Thread(mover);
		Thread cmdReceiver = new Thread(new CommandReceiver(s, arena, mover, players_scores));
		cmdReceiver.start();
		objMover.start();
		window.start();

		// quit game
		try {
			window.join();
		} catch (InterruptedException ie) {
			System.err.println("Exit game");
		}
		objMover.interrupt();
		cmdReceiver.interrupt();
	}
}
