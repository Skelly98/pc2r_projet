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

	public GameWindow(Socket s, Arena arena, HashMap<String, Integer> players_scores, String player) {
		this.s = s;
		this.arena = arena;
		this.players_scores = players_scores;
		this.player = player;
	}

	public void run() {
		try {
			PrintStream canalEcriture = new PrintStream(s.getOutputStream());
			
			/** window definition */
			setName("Arenes Vectorielles");
			setVisible(true);

			while (true) {

			}
		}
		catch(IOException e) {
			System.err.println(e);
		}
	}
}
