package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
	
	protected static final int PORT=45678;
	
	private final double turnit = 1.0;
	private final  double thrustit = 1.0;
	private double refresh_tickrate;
	
	
	
	public static void main(String[] args) {
		Socket s = null;
		if(args.length != 1) {
			System.err.println("Usage : java Client <hote>");
			System.exit(1);
		}
		try {
			s = new Socket(args[0],PORT);
			DataInputStream canalLecture = new DataInputStream(s.getInputStream());
			DataInputStream console = new DataInputStream(s.getInputStream());
			PrintStream canalEcriture = new PrintStream(s.getOutputStream());
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
			System.err.println(e);
		}
	}

}