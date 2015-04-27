import java.awt.image.BufferedImage;

public class ShieldBonus extends Bonus {
	public static BufferedImage image;
	public static BufferedImage guiIcon;

	public ShieldBonus(double xCoordinate, double yCoordinate, long secDuration, double fallingSpeed) {
		super(xCoordinate, yCoordinate, secDuration, fallingSpeed, 0, ShieldBonus.image);
	}
	
	@Override
	public void apply(PlayerHelicopter player) {
		player.shieldBonus = this;
	}
}
