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
public class Panel extends JPanel {

  private Arena arena;
  private String player;

  public Panel(Arena arena, String player) {
    this.arena = arena;
    this.player = player;
    setBackground(Color.BLACK);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2D = (Graphics2D) g;

    /** Objectif */
    g2D.setColor(Color.GREEN);
    int[] obj = arena.getObjectifPaintData();
    g2D.fillOval(obj[0], obj[1], obj[2], obj[2]);

    /** Obstacles */
    g2D.setColor(Color.WHITE);
    for (Obstacle o : arena.getObstacles()) {
      int[][] obstacle = o.getPaintDataPolygon();
      g2D.drawPolygon(obstacle[0], obstacle[1], 12);
    }

    /** Players */
    g2D.setColor(Color.BLUE);
    int[][] player_ship = arena.getVehiculePaintData(player);
    if (player_ship != null) {
      g2D.fillPolygon(player_ship[0], player_ship[1], 4);
    }
    g2D.setColor(Color.RED);
    for (Vehicule v : arena.getVehiculesExcept(player)) {
      int[][] enemy_ship = v.getPaintDataPolygon();
      g2D.fillPolygon(enemy_ship[0], enemy_ship[1], 4);
    }
  }
}
