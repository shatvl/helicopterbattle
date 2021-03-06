import java.awt.image.BufferedImage;


public class BulletBonus extends Bonus {
	private int bullets;
	public static BufferedImage image;
	
	public BulletBonus(double xCoordinate, double yCoordinate, double fallingSpeed, int bullets) {
		super(xCoordinate, yCoordinate, 0, fallingSpeed, 0L, BulletBonus.image);
		this.bullets = bullets;
	}

	@Override
	public void apply(PlayerHelicopter player) {
		player.numberOfAmmo += bullets;
	}
}
