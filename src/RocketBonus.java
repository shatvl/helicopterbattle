import java.awt.image.BufferedImage;

public class RocketBonus extends Bonus {
	int rockets;
	public static BufferedImage image;
	
	public RocketBonus(double xCoordinate, double yCoordinate, double fallingSpeed, int rockets) {
		super(xCoordinate, yCoordinate, 0, fallingSpeed, 0L, RocketBonus.image);
		this.rockets = rockets;
	}

	@Override
	public void apply(PlayerHelicopter player) {
		player.numberOfRockets += rockets;
	}
}
