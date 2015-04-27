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
	private double yAccel;
	private int accelTime;
	private int totalAccelTime;
	private boolean isMoving;
	public boolean invincible;
	private Random random;
	
    private double offsetXMachineGun;
    private double offsetYMachineGun;
    
    public double machineGunXcoordinate;
    public double machineGunYcoordinate;
    
    long lastBulletSpawnTime = 0;
    
    public BufferedImage helicopterImg;
    
	static public BufferedImage helicopter1Img;
	static public BufferedImage helicopter2Img;
	static public BufferedImage bulletImg;
	static final int initHealth = 2000;
	static final long timeBetweenBullets = Framework.secInNanosec / 2;
	static final int bulletSpeed = 10;
	static final int bulletDamage = 40;
	static final int rageHealth = initHealth / 2;
	
	public Boss(int health, double xCoordinate, double yCoordinate, BufferedImage helicopterImg) {
		this.health = health;
		this.rageMode = false;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.xVelocity = -1.0;
		this.yVelocity = 0.0;
		this.yAccel = 0.0;
		this.isMoving = true;
		this.invincible = true;
		this.random = new Random();
		this.helicopterImg = helicopterImg;
		this.accelTime = (int)Math.ceil(0.5 * helicopterImg.getWidth() / Math.abs(xVelocity));
		this.totalAccelTime = 0;
		
		this.offsetXMachineGun = helicopterImg.getWidth() - 40;
        this.offsetYMachineGun = helicopterImg.getHeight();
        this.machineGunXcoordinate = this.xCoordinate + this.offsetXMachineGun;
        this.machineGunYcoordinate = this.yCoordinate + this.offsetYMachineGun;
	}
	
	private void calculateAccel(double yPos) {
		yVelocity = 0.0;
		yAccel = (yPos - yCoordinate) / (accelTime * accelTime);
	}
	
	public Bullet spawnBullet(double x, double y) {
		Vector2d direction = new Vector2d(x - machineGunXcoordinate, y - machineGunYcoordinate);
		Vector2d velocity = direction.multiply(bulletSpeed / direction.length());
		return new Bullet(machineGunXcoordinate, machineGunYcoordinate, velocity.x, velocity.y, bulletDamage, Boss.bulletImg);
	}
	
	public void updateVelocity(ArrayList<Bullet> playerBullets, ArrayList<Rocket> playerRockets) {
		if(!isMoving) {
			//if(playerBullets.size() == 0 && playerRockets.size() == 0) {
				accelTime = 50 + random.nextInt(20);
				calculateAccel(random.nextDouble() * (Framework.frameHeight - helicopterImg.getHeight()));
			//} else {
				
			//}
			isMoving = true;
		}
	}
	
	public void update() {
		if(health <= rageHealth) {
			rageMode = true;
		}
		if(xCoordinate > Framework.frameWidth - helicopterImg.getWidth() - 50) {
			xCoordinate += xVelocity;
			machineGunXcoordinate = xCoordinate + offsetXMachineGun;
		} else {
			invincible = false;
		}
	    if(totalAccelTime == accelTime) {
			yAccel *= -1.0;
		}
		if(totalAccelTime == 2*accelTime) {
			isMoving = false;
			yAccel *= -1.0;
			totalAccelTime = 0;
		} else {
			yCoordinate += yVelocity + 0.5*yAccel;
			machineGunYcoordinate = yCoordinate + offsetYMachineGun;
			yVelocity += yAccel;
			++totalAccelTime;
		}
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(helicopterImg, (int)xCoordinate, (int)yCoordinate, null);
	}
}
