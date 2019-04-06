package model;

public class Vehicule {
	
	private int id;
	private double posX;
	private double posY;
	private double angle;
	private double vX;
	private double vY;
	private double mass;
	private double radius;
	private double half_width, half_length;
	private double turnit;
	private double thrustit;
	
	
	public Vehicule(int id, double posX, double posY, double angle, double vX,
			double vY, double mass, double radius, double turnit, double thrustit,
			double half_width, double half_length) {
		this.id = id;
		this.posX = posX;
		this.posY = posY;
		this.angle = angle;
		this.vX = vX;
		this.vY = vY;
		this.mass = mass;
		this.radius = radius;
		this.turnit = turnit;
		this.thrustit = thrustit;
		this.half_width = half_width;
		this.half_length = half_length;
	}
 
	public void delete() {
		this.id = -1;
	}
	
	public void move() {
		if(this.id != -1) {
			double calcul_posX = posX + vX;
			double calcul_posY= posY + vY;
			
			if(calcul_posX >= -half_width && calcul_posX< half_width) {
				posX = calcul_posX;
			}
			else if(calcul_posX < -half_width) {
				posX = calcul_posX+ 2*half_width;
			}
			else {
				posX = calcul_posX + 2*(-half_width); 
			}
			
			if(calcul_posY >= -half_length && calcul_posX< half_length) {
				posY = calcul_posY;
			}
			else if(calcul_posX < -half_length) {
				posX = calcul_posX+ 2*half_length;
			}
			else {
				posX = calcul_posX + 2*(-half_length); 
			}
		}
	}
	
	public void clock() {
		angle = angle - turnit;
	}
	
	public void anticlock() {
		angle = angle - turnit;
	}
	
	public void thrust() {
		vX = vX + turnit + Math.cos(angle);
		vX = vY + turnit + Math.sin(angle);
	}

	public void collision(Vehicule v) {
		
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void setvX(double vX) {
		this.vX = vX;
	}

	public void setvY(double vY) {
		this.vY = vY;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	
	
}
