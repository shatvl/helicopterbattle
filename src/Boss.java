import java.util.*;
import java.awt.*;
import java.awt.image.*;

public class Boss {
	public int health;
	public boolean rageMode;
	public double xCoordinate;
	public double yCoordinate;
	private double xVelocity;
	private double yVelocity;
	private int accelTime;
	private int totalAccelTime;
	private int curAccel;
	private boolean isMoving;
	private Random random;
	static public BufferedImage helicopterImg;
	static final double accel = 1.0;
	static final int initHealth = 1000;
	static final int rageHealth = 100;
	
	public Boss(int health, double xCoordinate, double yCoordinate) {
		this.health = health;
		this.rageMode = false;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.xVelocity = -1.0;
		this.yVelocity = 0.0;
		this.isMoving = true;
		this.random = new Random();
		this.accelTime = 0;
		this.totalAccelTime = 0;
		this.curAccel = 1;
	}
	
	public void updateVelocity(ArrayList<Bullet> playerBullets, ArrayList<Rocket> playerRockets) {
		if(!isMoving) {
			if(playerBullets.size() == 0 && playerRockets.size() == 0) {
			}
		}
	}
	
	public void update() {
		if(health <= rageHealth) {
			rageMode = true;
		}
		if(xCoordinate > Framework.frameWidth - Boss.helicopterImg.getWidth()) {
			xCoordinate += xVelocity;
		} else {
			xVelocity = 0.0;
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
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(helicopterImg, (int)xCoordinate, (int)yCoordinate, null);
	}
}
