import java.awt.image.BufferedImage;

public class HealthBonus extends Bonus {
	private int health;
	public static BufferedImage image;
	
	public HealthBonus(double xCoordinate, double yCoordinate, double fallingSpeed, int health) {
		super(xCoordinate, yCoordinate, 0, fallingSpeed, 0L, HealthBonus.image);
		this.health = health;
	}

	@Override
	public void apply(PlayerHelicopter player) {
		if(player.health > 0) {
			player.health = Math.min(player.health + health, PlayerHelicopter.healthInit);
		}
	}
}
