package model;

import java.util.HashMap;

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

	public void move_vehicules() {
		for(Vehicule v : vehicules.values()) {
			v.move();
		}
	}

	public void move_all() {
		for(Vehicule v : vehicules.values()) {
			v.move();
		}
		for(Obstacle o : obstacles.values()) {
			o.move();
		}
	}

	public void remove_vehicule(String owner) {
		vehicules.remove(owner);
	}

	public void setObjectif(double x, double y) {
			obj.setX(x);
			obj.setY(y);
	}
}
