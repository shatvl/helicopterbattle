import java.awt.image.BufferedImage;

public class HealthBonus extends Bonus {
	private int health;
	public static BufferedImage image;
	
	public HealthBonus(int xCoordinate, int yCoordinate, long secDuration, int fallingSpeed, int health) {
		super(xCoordinate, yCoordinate, secDuration, fallingSpeed, HealthBonus.image);
		this.health = health;
	}

	@Override
	public void apply(PlayerHelicopter player) {
		player.health = Math.min(health, PlayerHelicopter.healthInit);
	}
}
