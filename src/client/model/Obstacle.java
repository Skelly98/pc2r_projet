package model;

import java.util.Random;

public class Obstacle extends Object{
	private double radius_polygon[];
	private double spin_speed;

	public Obstacle(double posX, double posY) {
		super(posX, posY);
		Random rand = new Random();
		mass = 1000.;
		radius = 50.;
		spin_speed = 100. + 50 * rand.nextDouble();
		spin_speed = rand.nextBoolean() ? spin_speed : - spin_speed;
		radius_polygon = new double[12];
		for (int i = 0; i < 12; i++) {
			radius_polygon[i] = (0.8 + rand.nextDouble() / 5.) * radius;
		}
	}

	@Override
	public synchronized void move() {
		super.move();
		angle+= Math.PI / spin_speed;
	}

	@Override
	public synchronized void setVcoord(double posX, double posY, double vX, double vY, double angle) {
		this.posX = posX;
		this.posY = posY;
		this.vX = vX;
		this.vY = vY;
		//ignore angle
	}

	public synchronized int[][] getPaintDataPolygon() {
		int [][] data = new int[2][12];
		// x1
		data[0][0] = (int) (posX + radius_polygon[0] * Math.cos(angle) + Arena.half_width);
		// x2
		data[0][1] = (int) (posX + radius_polygon[1] * Math.cos(angle + Math.PI / 6.) + Arena.half_width);
		// x3
		data[0][2] = (int) (posX + radius_polygon[2] * Math.cos(angle + Math.PI / 3.) + Arena.half_width);
		// x4
		data[0][3] = (int) (posX + radius_polygon[3] * Math.cos(angle + Math.PI / 2.) + Arena.half_width);
		// x5
		data[0][4] = (int) (posX + radius_polygon[4] * Math.cos(angle + 2. * Math.PI / 3.) + Arena.half_width);
		// x6
		data[0][5] = (int) (posX + radius_polygon[5] * Math.cos(angle + 5. * Math.PI / 6.) + Arena.half_width);
		// x7
		data[0][6] = (int) (posX + radius_polygon[6] * Math.cos(angle + Math.PI) + Arena.half_width);
		// x8
		data[0][7] = (int) (posX + radius_polygon[7] * Math.cos(angle + 7. * Math.PI / 6.) + Arena.half_width);
		// x9
		data[0][8] = (int) (posX + radius_polygon[8] * Math.cos(angle + 4. * Math.PI / 3.) + Arena.half_width);
		// x10
		data[0][9] = (int) (posX + radius_polygon[9] * Math.cos(angle + 3. * Math.PI / 2.) + Arena.half_width);
		// x11
		data[0][10] = (int) (posX + radius_polygon[10] * Math.cos(angle + 5. * Math.PI / 3.) + Arena.half_width);
		// x12
		data[0][11] = (int) (posX + radius_polygon[11] * Math.cos(angle + 11. * Math.PI / 6.) + Arena.half_width);

		// y1
		data[1][0] = (int) (posY + radius_polygon[0] * Math.sin(angle) + Arena.half_width);
		// y2
		data[1][1] = (int) (posY + radius_polygon[1] * Math.sin(angle + Math.PI / 6.) + Arena.half_width);
		// y3
		data[1][2] = (int) (posY + radius_polygon[2] * Math.sin(angle + Math.PI / 3.) + Arena.half_width);
		// y4
		data[1][3] = (int) (posY + radius_polygon[3] * Math.sin(angle + Math.PI / 2.) + Arena.half_width);
		// y5
		data[1][4] = (int) (posY + radius_polygon[4] * Math.sin(angle + 2. * Math.PI / 3.) + Arena.half_width);
		// y6
		data[1][5] = (int) (posY + radius_polygon[5] * Math.sin(angle + 5. * Math.PI / 6.) + Arena.half_width);
		// y7
		data[1][6] = (int) (posY + radius_polygon[6] * Math.sin(angle + Math.PI) + Arena.half_width);
		// y8
		data[1][7] = (int) (posY + radius_polygon[7] * Math.sin(angle + 7. * Math.PI / 6.) + Arena.half_width);
		// y9
		data[1][8] = (int) (posY + radius_polygon[8] * Math.sin(angle + 4. * Math.PI / 3.) + Arena.half_width);
		// y10
		data[1][9] = (int) (posY + radius_polygon[9] * Math.sin(angle + 3. * Math.PI / 2.) + Arena.half_width);
		// y11
		data[1][10] = (int) (posY + radius_polygon[10] * Math.sin(angle + 5. * Math.PI / 3.) + Arena.half_width);
		// y12
		data[1][11] = (int) (posY + radius_polygon[11] * Math.sin(angle + 11. * Math.PI / 6.) + Arena.half_width);
		return data;
	}
}
