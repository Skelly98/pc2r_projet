package view;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
	private MyCanvas m;
	private Double refresh_tickrate;

	public GameWindow(Socket s, Arena arena, HashMap<String, Integer> players_scores, String player, double refresh_tickrate) throws IOException {
		this.s = s;
		this.arena = arena;
		this.players_scores = players_scores;
		this.player = player;
		this.refresh_tickrate = refresh_tickrate;
		canalEcriture = new PrintStream(s.getOutputStream());
		m = new MyCanvas(arena, player);
	}

	private void send(String msg) {
		canalEcriture.println(msg);
	}

	public void run() {
		/** window definition */
		setName("Arenes Vectorielles");
		setSize(1000, 1000);
		add(m);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		int delay = (int) ((1. / refresh_tickrate) * 1000); //milliseconds
  	ActionListener taskPerformer = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
          m.repaint();
					repaint();
      }
  	};
  	new Timer(delay, taskPerformer).start();

		send("CONNECT/" + player);
	}
}
