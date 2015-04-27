import java.awt.image.BufferedImage;
import java.awt.*;

public abstract class Bonus {
	// Duration of bonus in seconds
	public long secDuration;
	// Falling speed in pixels per frame
	public int fallingSpeed;
	// Bonus image
	public BufferedImage image;
	
	public int xCoordinate;
	public int yCoordinate;
	
	protected Bonus(int xCoordinate, int yCoordinate, long secDuration, int fallingSpeed,
			BufferedImage image) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.secDuration = secDuration;
		this.fallingSpeed = fallingSpeed;
		this.image = image;
	}
	
	/**
	 * update position
	 */
	public void update() {
		yCoordinate += fallingSpeed;
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(image, xCoordinate, yCoordinate, null);
	}
	
	public boolean isLeftScreen() {
		return yCoordinate > Framework.frameHeight;
	}
	
	/**
	 * apply bonus effect to player
	 * @param player
	 */
	public abstract void apply(PlayerHelicopter player);
}
