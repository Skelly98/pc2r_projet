package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import model.Arena;
import model.Objectif;
import model.Obstacle;
import model.Vehicule;

@SuppressWarnings("serial")
public class MyCanvas extends JPanel {

  private Arena arena;
  private int width;
  private int height;
  private String player;

  public MyCanvas(Arena arena, String player) {
    this.arena = arena;
    this.player = player;
    setBounds(0,0,1000,1000);
    setBackground(Color.BLACK);
  }

  public void drawVehicule(Graphics2D g, int[] paintData) {
    int offset = paintData[2] / 2;
    g.drawLine(paintData[0] + offset, paintData[1] + offset, paintData[0] + offset + paintData[2], paintData[1] + offset + paintData[2]);
    offset = 5;
    g.drawLine(paintData[0] + offset, paintData[1] + offset, paintData[0] + offset + paintData[2], paintData[1] + offset + paintData[2]);
    offset = 3;
    g.drawLine(paintData[0] + offset, paintData[1] + offset, paintData[0] + offset + paintData[2], paintData[1] + offset + paintData[2]);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2D = (Graphics2D) g;

    /** Objectif */
    g2D.setColor(Color.GREEN);
    int[] obj = arena.getObjectifPaintData();
    g2D.drawOval(obj[0], obj[1], obj[2], obj[2]);

    /** Obstacles */
    g2D.setColor(Color.WHITE);
    for (Obstacle o : arena.getObstacles()) {
      int[] obstacle = o.getPaintData();
      g2D.drawOval(obstacle[0], obstacle[1], obstacle[2], obstacle[2]);
    }

    /** Players */
    g2D.setColor(Color.BLUE);
    int[] player_ship = arena.getVehiculePaintData(player);
    if (player_ship != null) {
      drawVehicule(g2D, player_ship);
    }
    g2D.setColor(Color.RED);
    for (Vehicule v : arena.getVehiculesExcept(player)) {
      int[] enemy_ship = v.getPaintData();
      drawVehicule(g2D, enemy_ship);
    }
  }
}
