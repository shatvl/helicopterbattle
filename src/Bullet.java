import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Arbitrary bullet.
 * 
 * @author www.gametutorial.net
 */

public class Bullet {    
    // Damage that a bullet does to enemies
    public int damagePoints;
    
    // Position of the bullet on the screen. Must be of type double because movingXspeed and movingYspeed will not be a whole number.
    public double xCoordinate;
    public double yCoordinate;
    
    // Bullet velocity
    private double xVelocity;
    private double yVelocity;
    
    public BufferedImage bulletImg;
    
    /**
     * Creates arbitrary bullet.
     * 
     * @param xCoordinate From which x coordinate was bullet fired?
     * @param yCoordinate From which y coordinate was bullet fired?
     * @param xVelocity x component of velocity vector
     * @param yVelocity y component of velocity vector
     * @param damagePoint how much damage bullet deals
     * @param bullet bullet image
     */
    public Bullet(double xCoordinate, double yCoordinate, double xVelocity, double yVelocity, int damagePoints, BufferedImage bullet)
    {
    	this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.damagePoints = damagePoints;
        this.bulletImg = bullet;
    }
    
    /**
     * Checks if the bullet is left the screen.
     * 
     * @return true if the bullet left the screen, false otherwise.
     */
    public boolean isItLeftScreen()
    {
        return !(xCoordinate > 0 && xCoordinate < Framework.frameWidth &&
                yCoordinate > 0 && yCoordinate < Framework.frameHeight  );
    }
    
    
    /**
     * Moves the bullet.
     */
    public void update()
    {
        xCoordinate += xVelocity;
        yCoordinate += yVelocity;
    }
    
    
    /**
     * Draws the bullet to the screen.
     * 
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(bulletImg, (int)xCoordinate, (int)yCoordinate, null);
    }
}
