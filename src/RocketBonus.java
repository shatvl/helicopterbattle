import java.awt.image.BufferedImage;

public class RocketBonus extends Bonus {
	int rockets;
	public static BufferedImage image;
	
	public RocketBonus(int xCoordinate, int yCoordinate, long secDuration, int fallingSpeed, int rockets) {
		super(xCoordinate, yCoordinate, secDuration, fallingSpeed, RocketBonus.image);
		this.rockets = rockets;
	}

	@Override
	public void apply(PlayerHelicopter player) {
		player.numberOfRockets += rockets;
	}
}
