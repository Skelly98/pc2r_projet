package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;

public class FromServer extends Thread {
	DataInputStream inchan;
	GameWindow gw;
	Client c;
		
	public FromServer(DataInputStream inchan, GameWindow gw, Client c) {
		this.inchan = inchan;
		this.gw = gw;
		this.c = c;
	}

	@Override 
	public void run() {
		try {
			while(true) {
				String command = inchan.readLine();
				String [] arr = command.split("/");
				int length = arr.length;
				switch(arr[0]) {
					case "WELCOME": 
						if(length != 4) System.out.println("Format incorrect ! --> WELCOME/phase/scores/coord/");
						else {
							System.out.println("Bienvenue !\nPhase de la session : "+ arr[1]+ "\n Score: " + arr[2] + "\n Coordonnées de l'objectif : "+arr[3]); 	
						}
						break;
					case "DENIED" : 
						if(length != 1) System.out.println("Format incorrect ! --> DENIED/");
						else {
							System.out.println("Connexion refusée");
						}
						break;
					case "NEWPLAYER" : 
						if(length != 2) System.out.println("Format incorrect ! --> NEWPLAYER/user");
						else{
							System.out.println("Le joueur "+ arr[1] +" s'est connecté");
						}
						break;
						
					case "PLAYERLEFT" : 
						if(length != 2) System.out.println("Format incorrect ! --> PLAYERLEFT/user");
						else {
							System.out.println("Le joueur "+ arr[1] +" s'est déconnecté");
						}
						break;
					case "SESSION" : 
						if(length != 3) System.out.println("Format incorrect ! --> SESSION/coords/coords");
						else {
							System.out.println("Session commencée.\nCoordonnées des véhicules : "+arr[1]+"\nCoordonnées de l'objectif : "+arr[2] );
						}
						break;
					case "WINNER" :
						if(length != 2) System.out.println("Format incorrect ! --> WINNER/coords");
						else {
							System.out.println("Session terminée.\nScores finaux : "+arr[1] );
						}
						break;
					case "TICK" : 
						if(length != 2) System.out.println("Format incorrect ! --> TICK/coords");
						else {
							System.out.println("Propagation des nouvelles coordonnées : "+arr[1] );
						}
						break;
					case "NEWOBJ" :
						if(length != 3) System.out.println("Format incorrect ! --> NEWOBJ/coords/score");
						else {
							System.out.println("Nouvel objectif : "+arr[1]+"\n Scores : "+arr[2] );
						}
						break;
					default : System.out.println("Commande  inexistante.");
				}
					
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
