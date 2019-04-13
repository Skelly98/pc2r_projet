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

			double xDist = this.posX - o.posX;
      double yDist = this.posY - o.posY;
			double distSquared = xDist*xDist + yDist*yDist;
			double xVelocity = o.vX - this.vX;
      double yVelocity = o.vY - this.vY;
      double dotProduct = xDist*xVelocity + yDist*yVelocity;
      //Neat vector maths, used for checking if the objects moves towards one another.
      if(dotProduct > 0){
        double collisionScale = dotProduct / distSquared;
        double xCollision = xDist * collisionScale;
        double yCollision = yDist * collisionScale;
        //The Collision vector is the speed difference projected on the Dist vector,
        //thus it is the component of the speed difference needed for the collision.
        double combinedMass = this.mass + o.mass;
        double collisionWeight1 = 2 * o.mass / combinedMass;
        double collisionWeight2 = 2 * this.mass / combinedMass;

        this.vX += collisionWeight1 * xCollision;
        this.vY += collisionWeight1 * yCollision;
        o.vX -= collisionWeight2 * xCollision;
        o.vY -= collisionWeight2 * yCollision;
      }
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
