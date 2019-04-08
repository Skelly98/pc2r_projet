package view;

import javax.swing.JFrame;

import model.Arena;

public class GameWindow extends JFrame{
	
	private Arena arene;
	
	public static void main(String[] args) {
		new GameWindow();
	}

	public GameWindow() {
		this.setName("Arenes Vectorielles");
		this.setVisible(true);
	}
}
