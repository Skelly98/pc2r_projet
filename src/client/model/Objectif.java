package model;

public class Objectif {

	private double x;
	private double y;
	private double radius;

	public Objectif(double x, double y, double radius) {
		this.x=x;
		this.y=y;
		this.radius = radius;
	}

	public synchronized double getX() {
		return x;
	}

	public synchronized double getY() {
		return y;
	}

	public synchronized void setX(double x) {
		this.x = x;
	}

	public synchronized void setY(double y) {
		this.y = y;
	}

	public synchronized double getRadius() {
		return radius;
	}

	public synchronized int[] getPaintData() {
		int [] data = new int[3];
		data[0] = (int) (x - radius + Arena.half_width);
		data[1] = (int) (y - radius + Arena.half_height);
		data[2] = (int) (2 * radius);
		return data;
	}
}
