package model;

import java.util.Random;

public abstract class Object {

	protected int id;
	protected double posX;
	protected double posY;
	protected double radius;
	protected double half_width, half_length;
	protected double vX;
	protected double vY;
	protected double angle;
	protected double mass;
	public static double turnit;
	public static double thrustit;
	
	
	public synchronized void init(int id,double mass, double radius, double turnit, double thrustit,
			double half_width, double half_length) {
		this.id = id;
		this.turnit = turnit;
		this.thrustit = thrustit;
		this.half_width = half_width;
		this.half_length = half_length;
		Random r = new Random();
		this.posX = (half_width) + (2*half_width - half_width) * r.nextDouble();
		this.posY = (half_length) + (2*half_length - half_length) * r.nextDouble();
		this.angle = 0;
		this.vX = 0;
		this.vY = 0;
		this.mass = mass;
		this.radius = radius;

	}
 
	public synchronized void delete() {
		this.id = -1;
	}
	
	public synchronized void move() {
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
	
	public synchronized void clock() {
		angle = angle - turnit;
	}
	
	public synchronized void anticlock() {
		angle = angle - turnit;
	}
	
	public void thrust() {
		vX = vX + turnit + Math.cos(angle);
		vX = vY + turnit + Math.sin(angle);
	}
	
	public boolean touching(Object o) {
		return (this.id != o.id) && ((this.posX - o.posX) *2 + (this.posY - o.posY)*2 <= (this.radius + o.radius) *2); 
	}

	public synchronized void collision(Object o) {
		
		//def des constantes
		double direction1 = Math.acos(this.vX + o.vX);
		double direction2 = Math.acos(this.vY + o.vY);
		double v1 = Math.sqrt(this.vX *2 + this.vY *2);
		double v2 = Math.sqrt(o.vX *2 + o.vY *2);
		double m1_m2_div_m1_m2 = (this.mass - o.mass) / (this.mass + o.mass);
        double m2_m1_div_m2_m1 = (o.mass - this.mass) / (o.mass + this.mass);
        double _2_m2_div_m1_m2 = (2. * o.mass ) / (this.mass + o.mass);
        double _2_m1_div_m1_m2 = (2. * this.mass ) / (this.mass + o.mass);
        
        //calcul
        double new_direction_1 = Math.atan (m1_m2_div_m1_m2 * (Math.tan(direction1))
          + (_2_m2_div_m1_m2 * v2 * (Math.sin(direction2)) / (v1 * (Math.cos(direction1)))));
        double new_direction_2 = Math.atan (m2_m1_div_m2_m1 * (Math.tan(direction2))
          + (_2_m1_div_m1_m2 * v1 * (Math.sin(direction1))) / (v2 * (Math.cos(direction2))));
        double new_speed_1 = Math.sqrt ((m1_m2_div_m1_m2 * v1 * (Math.sin(direction1)) + _2_m2_div_m1_m2 * v2 * (Math.sin(direction2)))* 2 + (v1 * (Math.cos(direction1)))*2);
        double new_speed_2 = Math.sqrt ((m2_m1_div_m2_m1 * v2 * (Math.sin(direction2)) + _2_m1_div_m1_m2 * v1 * (Math.sin (direction1)))* 2 + (v2 * (Math.cos(direction2))*2));

        //affectation des nouvelles vitesses
        this.vX =  new_speed_1 * (Math.cos (new_direction_1));
        this.vY =  new_speed_1 * (Math.sin (new_direction_1));
        o.vX = new_speed_2 * (Math.cos(new_direction_2));
        o.vY= new_speed_2 * (Math.sin(new_direction_2));
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
