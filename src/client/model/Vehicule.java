package model;

public class Vehicule extends Object{

	public final static double turnit = Math.PI / 24;
	public final static int thrustit = 1;

	public Vehicule(double posX, double posY) {
		super(posX, posY);
		mass = 100.;
		radius = 10.;
	}

	public synchronized void clock() {
		angle -= turnit;
	}

	public synchronized void anticlock() {
		angle += turnit;
	}

	public synchronized void thrust() {
		vX += thrustit * Math.cos(angle);
		vY += thrustit * Math.sin(angle);
	}


	public synchronized int[][] getPaintDataPolygon() {
		int [][] data = new int[2][4];
		// x1
		data[0][0] = (int) (posX + radius * Math.cos(angle + 2. * Math.PI / 3) + Arena.half_width);
		// x2
		data[0][1] = (int) (posX + radius * Math.cos(angle) + Arena.half_width);
		// x3
		data[0][2] = (int) (posX + radius * Math.cos(angle - 2. * Math.PI / 3) + Arena.half_width);
		// x4
		data[0][3] = (int) (posX + Arena.half_width);
		// y1
		data[1][0] = (int) (posY + radius * Math.sin(angle + 2. * Math.PI / 3) + Arena.half_height);
		// y2
		data[1][1] = (int) (posY + radius * Math.sin(angle) + Arena.half_height);
		// y3
		data[1][2] = (int) (posY + radius * Math.sin(angle - 2. * Math.PI / 3) + Arena.half_height);
		// y4
		data[1][3] = (int) (posY + Arena.half_height);
		return data;
	}
}
