package view;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import model.Arena;
import model.Vehicule;

@SuppressWarnings("serial")
public class GameWindow extends JFrame implements Runnable {

	private Socket s;
	private Arena arena;
	private HashMap<String, Integer> players_scores;
	private String player;
	private PrintStream canalEcriture;
	private Panel p;
	private Double refresh_tickrate;
	private int thrust = 0;
	private int clock = 0;

	public GameWindow(Socket s, Arena arena, HashMap<String, Integer> players_scores, String player, double refresh_tickrate) throws IOException {
		this.s = s;
		this.arena = arena;
		this.players_scores = players_scores;
		this.player = player;
		this.refresh_tickrate = refresh_tickrate;
		canalEcriture = new PrintStream(s.getOutputStream());
		p = new Panel(arena, player, (int) Arena.half_width * 2, (int) Arena.half_height * 2);
	}

	private void send(String msg) {
		canalEcriture.println(msg);
	}

	public void run() {
		/** window definition */
		setName("Arenes Vectorielles");
		setSize((int) Arena.half_width * 2, (int) Arena.half_height * 2);
		add(p);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		/** refresh window and send commands with refresh_tickrate */
		int delay = (int) ((1. / refresh_tickrate) * 1000); //milliseconds
  	ActionListener taskPerformer = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        p.repaint();
				repaint();
				if (thrust != 0 || clock != 0) {
					send("NEWCOM/A" + (double) - clock * Vehicule.turnit + "T" + thrust * Vehicule.thrustit + "\n");
					thrust = 0;
					clock = 0;
				}
      }
  	};
  	new Timer(delay, taskPerformer).start();

		/** Key bindings */
		p.getInputMap().put(KeyStroke.getKeyStroke("UP"), "thrust");
		p.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "clock");
		p.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "anticlock");

		Action actionThrust = new AbstractAction() {
      public void actionPerformed(ActionEvent evt) {
				Vehicule ship = arena.getPlayer(player);
					if (ship != null) {
	          ship.thrust();
						thrust++;
				}
      }
  	};

		Action actionClock = new AbstractAction() {
      public void actionPerformed(ActionEvent evt) {
				Vehicule ship = arena.getPlayer(player);
					if (ship != null) {
	          ship.clock();
						clock++;
				}
      }
  	};

		Action actionAnticlock = new AbstractAction() {
      public void actionPerformed(ActionEvent evt) {
				Vehicule ship = arena.getPlayer(player);
					if (ship != null) {
	          ship.anticlock();
						clock--;
				}
      }
  	};

		p.getActionMap().put("thrust", actionThrust);
		p.getActionMap().put("clock", actionClock);
		p.getActionMap().put("anticlock", actionAnticlock);

		/** Connect to the server */
		send("CONNECT/" + player);
	}
}
