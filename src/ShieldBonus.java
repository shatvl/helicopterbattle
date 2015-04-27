import java.awt.image.BufferedImage;

public class ShieldBonus extends Bonus {
	public static BufferedImage image;

	public ShieldBonus(int xCoordinate, int yCoordinate, long secDuration, int fallingSpeed) {
		super(xCoordinate, yCoordinate, secDuration, fallingSpeed, ShieldBonus.image);
	}
	
	@Override
	public void apply(PlayerHelicopter player) {
		player.shieldBonus = this;
	}
}
