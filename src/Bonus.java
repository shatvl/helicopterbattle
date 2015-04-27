import java.awt.image.BufferedImage;
import java.awt.*;

public abstract class Bonus {
	// Duration of bonus in seconds
	public long secDuration;
	// When item was consumed
	public long consumeTime;
	// Falling speed in pixels per frame
	public double fallingSpeed;
	// Bonus image
	public BufferedImage image;
	
	public double xCoordinate;
	public double yCoordinate;
	
	protected Bonus(double xCoordinate, double yCoordinate, long secDuration, double fallingSpeed,
			long consumeTime, BufferedImage image) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.secDuration = secDuration;
		this.fallingSpeed = fallingSpeed;
		this.consumeTime = consumeTime;
		this.image = image;
	}
	
	/**
	 * update position
	 */
	public void update() {
		yCoordinate += fallingSpeed;
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(image, (int)xCoordinate, (int)yCoordinate, null);
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
