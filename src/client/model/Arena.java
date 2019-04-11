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

	public synchronized void setVehiculeCoord(String owner, double x, double y) {
		if (vehicules.containsKey(owner)) {
			vehicules.get(owner).setCoord(x, y);
		}
		else {
			vehicules.put(owner, new Vehicule(x, y));
		}
	}

	public synchronized void setVehiculeVcoord(String owner, double x, double y, double vx, double vy, double angle) {
		if (!vehicules.containsKey(owner)) {
			vehicules.put(owner, new Vehicule(x, y));
		}
		vehicules.get(owner).setVcoord(x, y, vx, vy, angle);
	}

	public synchronized void setObstacleCoord(int id, double x, double y) {
		if (obstacles.containsKey(id)) {
			obstacles.get(id).setCoord(x, y);
		}
		else {
			obstacles.put(id, new Obstacle(x, y));
		}
	}

	public synchronized void setObstacleVcoord(int id, double x, double y, double vx, double vy, double angle) {
		if (!obstacles.containsKey(id)) {
			obstacles.put(id, new Obstacle(x, y));
		}
			obstacles.get(id).setVcoord(x, y, vx, vy, angle);
	}

	public synchronized void moveVehicules() {
		for (Vehicule v : vehicules.values()) {
			v.move();
		}
	}

	public synchronized void moveAll() {
		for (Vehicule v : vehicules.values()) {
			v.move();
		}
		for (Obstacle o : obstacles.values()) {
			o.move();
		}
	}

	public synchronized void collisionAll() {
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

	public synchronized void collisionVehicules() {
		for (Vehicule v : vehicules.values()) {
			for (Obstacle o : obstacles.values()) {
				if (v.touching(o)) {
					v.collision(o);
				}
			}
		}
	}

	public synchronized void removeVehicule(String owner) {
		vehicules.remove(owner);
	}

	public synchronized void setObjectif(double x, double y) {
			obj.setX(x);
			obj.setY(y);
	}

	public synchronized int[] getObjectifPaintData() {
		return obj.getPaintData();
	}

	public synchronized List<Vehicule> getVehiculesExcept(String name) {
		if (vehicules.containsKey(name)) {
			List<Vehicule> res = new ArrayList<Vehicule>(vehicules.values());
			res.remove(vehicules.get(name));
			return res;
		}
		return new ArrayList<Vehicule>(vehicules.values());
	}

	public synchronized int[] getVehiculePaintData(String name) {
		if(vehicules.containsKey(name)) {
			return vehicules.get(name).getPaintData();
		}
		return null;
	}

	public synchronized List<Obstacle> getObstacles() {
		return new ArrayList<Obstacle>(obstacles.values());
	}
}
