package view;

import javax.swing.JFrame;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import model.Arena;

@SuppressWarnings("serial")
public class GameWindow extends JFrame implements Runnable {

	private Socket s;
	private Arena arena;
	private HashMap<String, Integer> players_scores;
	private String player;
	private PrintStream canalEcriture;

	public GameWindow(Socket s, Arena arena, HashMap<String, Integer> players_scores, String player) {
		this.s = s;
		this.arena = arena;
		this.players_scores = players_scores;
		this.player = player;
		try {
			canalEcriture = new PrintStream(s.getOutputStream());
		}
		catch(IOException e) {
			System.err.println(e);
		}
	}

	private void send(String msg) {
		canalEcriture.println(msg);
	}

	public void run() {
		/** window definition */
		setName("Arenes Vectorielles");
		setVisible(true);

		send("CONNECT/" + player);
		while (true) {

		}
	}
}
