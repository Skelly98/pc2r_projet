package model;

import java.util.List;

public class Arena {
	
	private int center_X;
	private int center_Y;
	private int half_width;
	private int half_length;
	private List <Vehicule> list_vehicule;
	
	
	public Arena(int center_X, int center_Y, int half_width, int half_length, List<Vehicule> list_vehicule) {
		this.center_X = center_X;
		this.center_Y = center_Y;
		this.half_width = half_width;
		this.half_length = half_length;
		this.list_vehicule = list_vehicule;
	}
	
	public void move_all() {
		for(Vehicule v : list_vehicule) {
			v.move();
		}
	}

	public void remove_vehicule(int id) {
		list_vehicule.get(id).delete();
	}
	
}
