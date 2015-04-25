import java.util.*;
import java.awt.*;
import java.awt.image.*;

public class Boss {
	private int health;
	public double xCoordinate;
	public double yCoordinate;
	private double xVelocity;
	private double yVelocity;
	private int accelTime;
	private int totalAccelTime;
	private int curAccel;
	private boolean isMoving;
	private BufferedImage helicopterImg;
	static final double accel = 1.0;
	static final int initHealth = 1000;
	
	public Boss(int health, double xCoordinate, double yCoordinate) {
		this.health = health;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.isMoving = false;
		this.accelTime = 0;
		this.totalAccelTime = 0;
		this.curAccel = 1;
	}
	
	public void updateVelocity(ArrayList<Bullet> playerBullets, ArrayList<Rocket> playerRockets) {
		if(!isMoving) {
		}
	}
	
	public void update() {
		if(totalAccelTime == accelTime) {
			curAccel *= -1;
		}
		if(totalAccelTime == 2*accelTime) {
			isMoving = false;
			curAccel *= -1;
			totalAccelTime = 0;
		} else {
			yCoordinate += (totalAccelTime > 0 ? (totalAccelTime - 1)*curAccel*accel : 0) + 0.5*curAccel*accel;
			++totalAccelTime;
		}
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(helicopterImg, (int)xCoordinate, (int)yCoordinate, null);
	}
}
