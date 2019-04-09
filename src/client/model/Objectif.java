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

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getRadius() {
		return radius;
	}
}
