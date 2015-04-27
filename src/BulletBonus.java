import java.awt.image.BufferedImage;


public class BulletBonus extends Bonus {
	private int bullets;
	public static BufferedImage image;
	
	public BulletBonus(int xCoordinate, int yCoordinate, long secDuration, int fallingSpeed) {
		super(xCoordinate, yCoordinate, secDuration, fallingSpeed, BulletBonus.image);
	}

	@Override
	public void apply(PlayerHelicopter player) {
		player.numberOfAmmo += bullets;
	}
}
