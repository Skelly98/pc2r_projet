package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import model.Arena;

import view.GameWindow;

public class Client {

	private double refresh_tickrate;

	public static void main(String[] args) {
		int PORT=45678;
		GameWindow gw;
		Arena arena = new Arena();
		if(args.length != 1) {
			System.err.println("Usage : java Client <hote>");
			System.exit(1);
		}
		Socket s = null;
		try {
			s = new Socket(args[0],PORT);
		} catch (IOException e) {
			System.err.println("IUnknown server");
			System.exit(1);
		}
		Thread cmdReceiver = new Thread(new CommandReceiver(s, arena));
		cmdReceiver.start();
	}
}
