package model;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Arena {

	public static final double half_width = 5.;
	public static final double half_length = 5.;
	private HashMap<String, Vehicule> vehicules;
	private HashMap<Integer, Obstacle> obstacles;
	private Objectif obj;


	public Arena() {
		this.vehicules = new HashMap<String, Vehicule>();
		this.obstacles = new HashMap<Integer, Obstacle>();
		// outside
		obj = new Objectif(2 * half_width, 2* half_length, 0.1);
	}

	public void setVehiculeCoord(String owner, double x, double y) {
		if (vehicules.containsKey(owner)) {
			vehicules.get(owner).setCoord(x, y);
		}
		else {
			vehicules.put(owner, new Vehicule(x, y));
		}
	}

	public void setVehiculeVcoord(String owner, double x, double y, double vx, double vy, double angle) {
		vehicules.get(owner).setVcoord(x, y, vx, vy, angle);
	}

	public void setObstacleCoord(int id, double x, double y) {
		if (obstacles.containsKey(id)) {
			obstacles.get(id).setCoord(x, y);
		}
		else {
			obstacles.put(id, new Obstacle(x, y));
		}
	}

	public void setObstacleVcoord(int id, double x, double y, double vx, double vy, double angle) {
		obstacles.get(id).setVcoord(x, y, vx, vy, angle);
	}

	public void moveVehicules() {
		for (Vehicule v : vehicules.values()) {
			v.move();
		}
	}

	public void moveAll() {
		for (Vehicule v : vehicules.values()) {
			v.move();
		}
		for (Obstacle o : obstacles.values()) {
			o.move();
		}
	}

	public void collisionAll() {
		List<Obstacle> done = new ArrayList<Obstacle>();
		for (Obstacle o : obstacles.values()) {
			for (Vehicule v : vehicules.values()) {
				if (o.touching(v)) {
					o.collision(v);
				}
			}
			for (Obstacle obs : obstacles.values()) {
				if (!o.equals(obs) && ! done.contains(obs) && o.touching(obs)) {
					o.collision(obs);
				}
			}
			done.add(o);
		}
	}

	public void collisionVehicules() {
		for (Vehicule v : vehicules.values()) {
			for (Obstacle o : obstacles.values()) {
				if (v.touching(o)) {
					v.collision(o);
				}
			}
		}
	}

	public void removeVehicule(String owner) {
		vehicules.remove(owner);
	}

	public void setObjectif(double x, double y) {
			obj.setX(x);
			obj.setY(y);
	}
}
