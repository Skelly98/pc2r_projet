package client;

import java.lang.Runnable;
import model.Arena;

public class ObjectMover implements Runnable {

	private boolean compatibility_mode = false;
	private Arena arena;
  private double refreshTickrate;

	public ObjectMover(Arena arena, double refreshTickrate) {
		this.arena = arena;
    this.refreshTickrate = refreshTickrate;
	}

  public void compatibilityMode() {
    compatibility_mode = true;
  }

	public void run() {
		long tick_runtime = (long) ((1. / refreshTickrate) * 1000);
		while(true) {
      long startTime = System.currentTimeMillis();
      /** check collisions */
      if (compatibility_mode) {
        arena.collisionVehicules();
      }
      else {
        arena.collisionAll();
      }
      /** move objects */
      if (compatibility_mode) {
        arena.moveVehicules();
      }
      else {
        arena.moveAll();
      }
      /** sleep during 1 / refreshTickrate - calcul_time */
      long waitTime = tick_runtime - (System.currentTimeMillis() - startTime);
      /** if waitTime < 0, refreshTickrate is too big */
      try {
        if (waitTime > 0.) {
          Thread.sleep(waitTime);
        }
      } catch (InterruptedException e) {

			}
    }
	}
}
