package model;

public class Vehicule extends Object{

	public final static double turnit = Math.PI / 6;
	public final static int thrustit = 1;

	public Vehicule(double posX, double posY) {
		super(posX, posY);
		mass = 100.;
		radius = 0.1;
	}

	public synchronized void clock() {
		angle += turnit;
	}

	public synchronized void anticlock() {
		angle -= turnit;
	}

	public synchronized void thrust() {
		vX += thrustit + Math.cos(angle);
		vY += thrustit + Math.sin(angle);
	}
}
