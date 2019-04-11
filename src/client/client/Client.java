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
		ObjectMover mover = null;
		Thread window = null;
		Thread objMover = null;
		Thread cmdReceiver = null;
		try {
			s = new Socket(args[0],Integer.parseInt(args[1]));
			mover = new ObjectMover(arena, refresh_tickrate);
			window = new Thread(new GameWindow(s, arena, players_scores, args[2], refresh_tickrate));
			objMover = new Thread(mover);
			cmdReceiver = new Thread(new CommandReceiver(s, arena, mover, players_scores));
		} catch (IOException e) {
			System.err.println("Unknown server");
			System.exit(1);
		}
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
