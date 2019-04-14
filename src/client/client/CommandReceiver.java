package client;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;
import java.lang.Runnable;
import model.Arena;
import view.GameWindow;

public class CommandReceiver implements Runnable {

	protected static final int PORT=45678;

	private Socket s;
	private boolean game_phase = false;
	private boolean compatibility_mode = false;
	private Arena arena;
	private ObjectMover mover;
	private GameWindow gw;
	private HashMap<String, Integer> players_scores;
	private String player;

	public CommandReceiver(Socket s, Arena arena, ObjectMover mover, GameWindow gw, HashMap<String, Integer> players_scores, String player) {
		this.s = s;
		this.arena = arena;
		this.mover = mover;
		this.gw = gw;
		this.players_scores = players_scores;
		this.player = player;
	}

	public void run() {
		try {
			BufferedReader canalLecture = new BufferedReader(new InputStreamReader(new BufferedInputStream(s.getInputStream())));
			String ligne = new String();

			while(true) {
				ligne = canalLecture.readLine();
				if (ligne == null) {
					System.out.println("Server is not responding. Closing the game.");
					gw.serverOut();
					return;
				}
				String [] arr = ligne.split("/");
				int length = arr.length;
				switch(arr[0]) {
					case "WELCOME":
						if (arr[1].equals("jeu")) {
							game_phase = true;
							gw.setGamePhase(game_phase);
						}
						setPlayers(arr[2]);
						String [] x_and_y = arr[3].split("[XY]");
						arena.setVehiculeCoord(player, Double.parseDouble(x_and_y[1]), Double.parseDouble(x_and_y[2]));
						setObstaclesCoords(arr[4]);
						System.out.println("Welcome!\nPhase: " + arr[1]);
						printScores();
						break;
					case "DENIED" :
						System.out.println("Connexion denied.");
						return;
					case "NEWPLAYER" :
						players_scores.put(arr[1], 0);
						System.out.println(arr[1] + " joined the game.");
						break;
					case "PLAYERLEFT" :
						players_scores.remove(arr[1]);
						arena.removeVehicule(arr[1]);
						System.out.println(arr[1] + " left the game.");
						break;
					case "SESSION" :
						setPlayersCoords(arr[1]);
						x_and_y = arr[2].split("[XY]");
						arena.setObjectif(Double.parseDouble(x_and_y[1]), Double.parseDouble(x_and_y[2]));
						setObstaclesCoords(arr[3]);
						game_phase = true;
						gw.setGamePhase(game_phase);
						System.out.println("Session started.");
						break;
					case "WINNER" :
						game_phase = false;
						arena.clear();
						gw.setGamePhase(game_phase);
						System.out.println("Session ended.");
						break;
					case "TICK" :
						setPlayersVcoords(arr[1]);
						if (!compatibility_mode) {
							setObstacleVcoords(arr[2]);
						}
						break;
					case "NEWOBJ" :
						setPlayers(arr[2]);
						x_and_y = arr[1].split("[XY]");
						arena.setObjectif(Double.parseDouble(x_and_y[1]), Double.parseDouble(x_and_y[2]));
						printScores();
						break;
					default :;
				}
			}
		}
		catch(IOException e) {
			System.err.println(e);
		}
	}

	public void setPlayers(String str) {
		String [] players = str.split("[|]");
		for (int i = 0; i < players.length; i++) {
			String [] name_and_score = players[i].split("[:]");
			players_scores.put(name_and_score[0], Integer.parseInt(name_and_score[1]));
		}
	}

	public void printScores() {
		System.out.println("\n######## SCORES ########");
		int line_len = 24;
		List<Map.Entry<String, Integer>> scores = players_scores
			.entrySet()
			.stream()
			.sorted(Map.Entry.comparingByValue((Integer a, Integer b) -> b - a))
			.collect(Collectors.toList());
		scores.forEach(item->{
			String name = item.getKey();
			String points = item.getValue().toString();
			int len = name.length() + points.length();
			for (int i = len; i < line_len - 4; i ++) {
				name += " ";
			}
			System.out.println("# " + name + points + " #");
		});
		System.out.println("########################\n");
	}

	public void setPlayersCoords(String str) {
		String [] players = str.split("[|]");
		for (int i = 0; i < players.length; i++) {
			String [] name_and_coord = players[i].split("[:]");
			String [] x_and_y = name_and_coord[1].split("[XY]");
			arena.setVehiculeCoord(name_and_coord[0], Double.parseDouble(x_and_y[1]), Double.parseDouble(x_and_y[2]));
		}
	}

	public void setPlayersVcoords(String str) {
		String [] players = str.split("[|]");
		for (int i = 0; i < players.length; i++) {
			String [] name_and_vcoord = players[i].split("[:]");
			String [] vcoord = name_and_vcoord[1].split("[XYVT]");
			arena.setVehiculeVcoord(name_and_vcoord[0], Double.parseDouble(vcoord[1]), Double.parseDouble(vcoord[2]), Double.parseDouble(vcoord[4]), Double.parseDouble(vcoord[6]), Double.parseDouble(vcoord[7]));
		}
	}

	public void setObstaclesCoords(String str) {
		String [] obstacles = str.split("[|]");
		for (int i = 0; i < obstacles.length; i++) {
			String [] id_and_coord = obstacles[i].split("[:]");
			if (id_and_coord.length == 1) {
				compatibility_mode = true;
        mover.compatibilityMode();
				String [] x_and_y = obstacles[i].split("[XY]");
				arena.setObstacleCoord(i, Double.parseDouble(x_and_y[1]), Double.parseDouble(x_and_y[2]));
			}
			else {
				String [] x_and_y = id_and_coord[1].split("[XY]");
				arena.setObstacleCoord(Integer.parseInt(id_and_coord[0]), Double.parseDouble(x_and_y[1]), Double.parseDouble(x_and_y[2]));
			}
		}
	}

	public void setObstacleVcoords(String str) {
		String [] obstacles = str.split("[|]");
		for (int i = 0; i < obstacles.length; i++) {
			String [] id_and_vcoord = obstacles[i].split("[:]");
			String [] vcoord = id_and_vcoord[1].split("[XYVT]");
			arena.setObstacleVcoord(Integer.parseInt(id_and_vcoord[0]), Double.parseDouble(vcoord[1]), Double.parseDouble(vcoord[2]), Double.parseDouble(vcoord[4]), Double.parseDouble(vcoord[6]), Double.parseDouble(vcoord[7]));
		}
	}
}
