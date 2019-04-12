package model;

public abstract class Object {

	private static int cpt = 0;
	public final int id;
	protected double posX;
	protected double posY;
	protected double radius;
	protected double vX;
	protected double vY;
	protected double angle;
	protected double mass;

	public Object(double posX, double posY) {
		id = ++cpt;
		this.posX = posX;
		this.posY = posY;
		this.angle = - Math.PI / 2;
		this.vX = 0.;
		this.vY = 0.;
	}

	public synchronized void move() {
		double calcul_posX = posX + vX;
		double calcul_posY= posY + vY;

		if(calcul_posX >= -Arena.half_width && calcul_posX < Arena.half_width) {
			posX = calcul_posX;
		}
		else if(calcul_posX < -Arena.half_width) {
			posX = calcul_posX + 2 * Arena.half_width;
		}
		else {
			posX = calcul_posX - 2 * Arena.half_width;
		}

		if(calcul_posY >= -Arena.half_height && calcul_posY < Arena.half_height) {
			posY = calcul_posY;
		}
		else if(calcul_posY < - Arena.half_height) {
			posY = calcul_posY + 2 * Arena.half_height;
		}
		else {
			posY = calcul_posY - 2 * Arena.half_height;
		}
	}

	public synchronized boolean touching(Object o) {
		synchronized (o) {
			return Math.sqrt(Math.pow(o.posX - this.posX, 2.) + Math.pow(o.posY - this.posY, 2.)) <= this.radius + o.radius;
		}
	}

	public synchronized void collision(Object o) {
		synchronized (o) {

			//def des constantes
			double direction1 = Math.acos(this.vX + o.vX);
			double direction2 = Math.acos(this.vY + o.vY);
			double v1 = Math.sqrt(Math.pow(this.vX, 2.) + Math.pow(this.vY, 2.));
			double v2 = Math.sqrt(Math.pow(o.vX, 2.) + Math.pow(o.vY, 2.));
			double m1_m2_div_m1_m2 = (this.mass - o.mass) / (this.mass + o.mass);
			double m2_m1_div_m2_m1 = (o.mass - this.mass) / (o.mass + this.mass);
			double _2_m2_div_m1_m2 = (2. * o.mass ) / (this.mass + o.mass);
			double _2_m1_div_m1_m2 = (2. * this.mass ) / (this.mass + o.mass);

			//empeche div par 0
			v1 = (v1 == 0. ? 0.000001 : v1);
			v2 = (v2 == 0. ? 0.000001 : v2);

			//calcul
			double new_direction_1 = Math.atan (m1_m2_div_m1_m2 * (Math.tan(direction1))
			+ (_2_m2_div_m1_m2 * v2 * (Math.sin(direction2)) / (v1 * (Math.cos(direction1)))));
			double new_direction_2 = Math.atan (m2_m1_div_m2_m1 * (Math.tan(direction2))
			+ (_2_m1_div_m1_m2 * v1 * (Math.sin(direction1))) / (v2 * (Math.cos(direction2))));
			double new_speed_1 = Math.sqrt (Math.pow((m1_m2_div_m1_m2 * v1 * (Math.sin(direction1)) + _2_m2_div_m1_m2 * v2 * Math.sin(direction2)), 2.) + Math.pow(v1 * (Math.cos(direction1)), 2.));
			double new_speed_2 = Math.sqrt (Math.pow((m2_m1_div_m2_m1 * v2 * (Math.sin(direction2)) + _2_m1_div_m1_m2 * v2 * Math.sin(direction1)), 2.) + Math.pow(v2 * (Math.cos(direction2)), 2.));

			//affectation des nouvelles vitesses
			this.vX =  new_speed_1 * (Math.cos (new_direction_1));
			this.vY =  new_speed_1 * (Math.sin (new_direction_1));
			o.vX = new_speed_2 * (Math.cos(new_direction_2));
	    o.vY= new_speed_2 * (Math.sin(new_direction_2));
		}
	}

	public synchronized void collision_comp(Object o) {
		synchronized (o) {
			this.vX *= -1;
			this.vY *= -1;
		}
	}

	public synchronized void setCoord(double posX, double posY) {
		this.posX = posX;
		this.posY = posY;
	}

	public synchronized void setVcoord(double posX, double posY, double vX, double vY, double angle) {
		this.posX = posX;
		this.posY = posY;
		this.vX = vX;
		this.vY = vY;
		this.angle = angle;
	}

	public synchronized boolean equals(java.lang.Object o) {
		if (!(o instanceof Object)) {
			return false;
		}
		return this.id == ((Object) o).id;
	}

	public synchronized int[] getPaintData() {
		int [] data = new int[3];
		data[0] = (int) (posX - radius + Arena.half_width);
		data[1] = (int) (posY - radius + Arena.half_height);
		data[2] = (int) (2 * radius);
		return data;
	}
}
