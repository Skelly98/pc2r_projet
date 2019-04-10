package client;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.lang.Runnable;
import model.Arena;

public class CommandReceiver implements Runnable {

	protected static final int PORT=45678;

	private Socket s;
	private boolean game_phase = false;
	private boolean compatibility_mode = false;
	private Arena arena;
	private ObjectMover mover;
	private HashMap<String, Integer> players_scores;

	public CommandReceiver(Socket s, Arena arena, ObjectMover mover, HashMap<String, Integer> players_scores) {
		this.s = s;
		this.arena = arena;
		this.mover = mover;
		this.players_scores = players_scores; new HashMap<String, Integer>();
	}

	public void run() {
		try {
			DataInputStream canalLecture = new DataInputStream(s.getInputStream());
			DataInputStream console = new DataInputStream(s.getInputStream());
			System.out.println("Connexion etablie : "+ s.getInetAddress()+" port : "+ s.getPort());

			String ligne = new String();
			char c;

			while(true) {
				while((c=(char) System.in.read())!= '\n') {
					ligne = ligne+c;
				}

				ligne = canalLecture.readLine();
				String [] arr = ligne.split("/");
				int length = arr.length;
				switch(arr[0]) {
					case "WELCOME":
						if(length < 5) System.out.println("Format incorrect ! --> WELCOME/phase/scores/coord/coords");
						else {
							if (arr[1].equals("jeu")) {
								game_phase = true;
							}
							setPlayers(arr[2]);
							String [] x_and_y = arr[3].split("[XY]+");
							arena.setObjectif(Double.parseDouble(x_and_y[0]), Double.parseDouble(x_and_y[1]));
							setObstaclesCoords(arr[4]);
							System.out.println("Bienvenue !\nPhase de la session : "+ arr[1]+ "\n Score: " + arr[2] + "\n Coordonnées de l'objectif : "+arr[3]);
						}
						break;
					case "DENIED" :
						if(length < 1) System.out.println("Format incorrect ! --> DENIED/");
						else {
							System.out.println("Connexion refusée");
							return;
						}
						break;
					case "NEWPLAYER" :
						if(length < 2) System.out.println("Format incorrect ! --> NEWPLAYER/user");
						else{
							players_scores.put(arr[1], 0);
							System.out.println("Le joueur "+ arr[1] +" s'est connecté");
						}
						break;

					case "PLAYERLEFT" :
						if(length < 2) System.out.println("Format incorrect ! --> PLAYERLEFT/user");
						else {
							players_scores.remove(arr[1]);
							System.out.println("Le joueur "+ arr[1] +" s'est déconnecté");
						}
						break;
					case "SESSION" :
						if(length < 5) System.out.println("Format incorrect ! --> SESSION/coords/coord/ocoords");
						else {
							setPlayersCoords(arr[1]);
							String [] x_and_y = arr[2].split("[XY]+");
							arena.setObjectif(Double.parseDouble(x_and_y[0]), Double.parseDouble(x_and_y[1]));
							setObstaclesCoords(arr[3]);
							game_phase = true;
							System.out.println("Session commencée.\nCoordonnées des véhicules : "+arr[1]+"\nCoordonnées de l'objectif : "+arr[2] );
						}
						break;
					case "WINNER" :
						if(length < 2) System.out.println("Format incorrect ! --> WINNER/coords");
						else {
							game_phase = false;
							System.out.println("Session terminée.\nScores finaux : "+arr[1] );
						}
						break;
					case "TICK" :
						if(length < 2) System.out.println("Format incorrect ! --> TICK/vcoords");
						else {
							setPlayersVcoords(arr[1]);
							if (!compatibility_mode) {
								setObstacleVcoords(arr[2]);
							}
							System.out.println("Propagation des nouvelles coordonnées : "+arr[1] );
						}
						break;
					case "NEWOBJ" :
						if(length < 3) System.out.println("Format incorrect ! --> NEWOBJ/coord/score");
						else {
							setPlayers(arr[2]);
							String [] x_and_y = arr[1].split("[XY]+");
							arena.setObjectif(Double.parseDouble(x_and_y[0]), Double.parseDouble(x_and_y[1]));
							System.out.println("Nouvel objectif : "+arr[1]+"\n Scores : "+arr[2]);
						}
						break;
					default : System.out.println("Commande  inexistante.");
				}
			}
		}
		catch(IOException e) {
			System.err.println(e);
		}
	}

	public void setPlayers(String str) {
		String [] players = str.split("|");
		for (int i = 0; i < players.length; i++) {
			String [] name_and_coord = players[i].split(":");
			players_scores.put(name_and_coord[0], Integer.parseInt(name_and_coord[1]));
		}
	}

	public void setPlayersCoords(String str) {
		String [] players = str.split("|");
		for (int i = 0; i < players.length; i++) {
			String [] name_and_coord = players[i].split(":");
			String [] x_and_y = name_and_coord[1].split("[XY]+");
			arena.setVehiculeCoord(name_and_coord[0], Double.parseDouble(x_and_y[0]), Double.parseDouble(x_and_y[1]));
		}
	}

	public void setPlayersVcoords(String str) {
		String [] players = str.split("|");
		for (int i = 0; i < players.length; i++) {
			String [] name_and_vcoord = players[i].split(":");
			String [] vcoord = name_and_vcoord[1].split("[XYVA]+");
			arena.setVehiculeVcoord(name_and_vcoord[0], Double.parseDouble(vcoord[0]), Double.parseDouble(vcoord[1]), Double.parseDouble(vcoord[2]), Double.parseDouble(vcoord[3]), Double.parseDouble(vcoord[4]));
		}
	}

	public void setObstaclesCoords(String str) {
		String [] obstacles = str.split("|");
		for (int i = 0; i < obstacles.length; i++) {
			String [] id_and_coord = obstacles[i].split(":");
			if (id_and_coord.length == 0) {
				boolean compatibility_mode = true;
                mover.compatibilityMode();
				String [] x_and_y = obstacles[i].split("[XY]+");
				arena.setObstacleCoord(i, Double.parseDouble(x_and_y[0]), Double.parseDouble(x_and_y[1]));
			}
			String [] x_and_y = id_and_coord[1].split("[XY]+");
			arena.setObstacleCoord(Integer.parseInt(id_and_coord[0]), Double.parseDouble(x_and_y[0]), Double.parseDouble(x_and_y[1]));
		}
	}

	public void setObstacleVcoords(String str) {
		String [] obstacles = str.split("|");
		for (int i = 0; i < obstacles.length; i++) {
			String [] id_and_vcoord = obstacles[i].split(":");
			String [] vcoord = id_and_vcoord[1].split("[XYVA]+");
			arena.setObstacleVcoord(Integer.parseInt(id_and_vcoord[0]), Double.parseDouble(vcoord[0]), Double.parseDouble(vcoord[1]), Double.parseDouble(vcoord[2]), Double.parseDouble(vcoord[3]), Double.parseDouble(vcoord[4]));
		}
	}
}
