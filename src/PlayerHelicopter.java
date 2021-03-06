import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Helicopter which is managed by player.
 * 
 * @author www.gametutorial.net
 */

public class PlayerHelicopter {
    
    // Health of the helicopter.
    public static final int healthInit = 100;
    public int health;
    
    // Position of the helicopter on the screen.
    public int xCoordinate;
    public int yCoordinate;
    
    // Moving speed and also direction.
    private double movingXspeed;
    public double movingYspeed;
    private double acceleratingXspeed;
    private double acceleratingYspeed;
    private double stoppingXspeed;
    private double stoppingYspeed;
    
    // Helicopter rockets.
    private final int numberOfRocketsInit = 15;
    public int numberOfRockets;
    
    // Helicopter machinegun ammo.
    private final int numberOfAmmoInit = 500;
    public int numberOfAmmo;
    
    // Helicopter machinegun bullet params
    public final static long timeBetweenNewBullets = Framework.secInNanosec / 10;
    public static long timeOfLastCreatedBullet = 0;
    public final static int machineGunBulletSpeed = 20;
    public final static int machineGunBulletDamage = 20;
    public static BufferedImage machineGunBulletImg;
    
    // Images of helicopter and its propellers.
    public BufferedImage helicopterBodyImg;
    private BufferedImage helicopterRearPropellerAnimImg;
    
    // Animation of the helicopter propeller.
    private Animation helicopterRearPropellerAnim;
    // Offset for the propeler. We add offset to the position of the position of helicopter.
    private int offsetXRearPropeller;
    private int offsetYRearPropeller;
    
    // Offset of the helicopter rocket holder.
    private int offsetXRocketHolder;
    private int offsetYRocketHolder;
    // Position on the frame/window of the helicopter rocket holder.
    public int rocketHolderXcoordinate;
    public int rocketHolderYcoordinate;
    
    // Offset of the helicopter machine gun. We add offset to the position of the position of helicopter.
    private int offsetXMachineGun;
    private int offsetYMachineGun;
    // Position on the frame/window of the helicopter machine gun.
    public int machineGunXcoordinate;
    public int machineGunYcoordinate;
    
    // Bonuses
    public Bonus shieldBonus;
    
    
    /**
     * Creates object of player.
     * 
     * @param xCoordinate Starting x coordinate of helicopter.
     * @param yCoordinate Starting y coordinate of helicopter.
     */
    public PlayerHelicopter(int xCoordinate, int yCoordinate)
    {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
        loadContent();
        initialize();
    }
    
    
    /**
     * Set variables and objects for this class.
     */
    private void initialize()
    {
        this.health = healthInit;
        
        this.numberOfRockets = numberOfRocketsInit;
        this.numberOfAmmo = numberOfAmmoInit;
        
        this.movingXspeed = 0;
        this.movingYspeed = 0;
        this.acceleratingXspeed = 0.2;
        this.acceleratingYspeed = 0.2;
        this.stoppingXspeed = 0.1;
        this.stoppingYspeed = 0.1;
       
        this.offsetXRearPropeller = -9;
        this.offsetYRearPropeller = -5;
        
        this.offsetXRocketHolder = 138;
        this.offsetYRocketHolder = 40;
        this.rocketHolderXcoordinate = this.xCoordinate + this.offsetXRocketHolder;
        this.rocketHolderYcoordinate = this.yCoordinate + this.offsetYRocketHolder;
        
        this.offsetXMachineGun = helicopterBodyImg.getWidth() - 40;
        this.offsetYMachineGun = helicopterBodyImg.getHeight();
        this.machineGunXcoordinate = this.xCoordinate + this.offsetXMachineGun;
        this.machineGunYcoordinate = this.yCoordinate + this.offsetYMachineGun;
    }
    
    /**
     * Load files for this class.
     */
    private void loadContent()
    {
        try 
        {
            helicopterBodyImg = ImageIO.read(new File("1_helicopter_body.png"));
            helicopterRearPropellerAnimImg = ImageIO.read(new File("1_rear_propeller_anim_blur.png"));
        } 
        catch (IOException ex) {
            Logger.getLogger(PlayerHelicopter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Now that we have images of propeller animation we initialize animation object.
        helicopterRearPropellerAnim = new Animation(helicopterRearPropellerAnimImg, 54, 54, 4, 20, true, xCoordinate + offsetXRearPropeller, yCoordinate + offsetYRearPropeller, 0);
    }
    
    
    /**
     * Resets the player.
     * 
     * @param xCoordinate Starting x coordinate of helicopter.
     * @param yCoordinate Starting y coordinate of helicopter.
     */
    public void reset(int xCoordinate, int yCoordinate)
    {
        this.health = healthInit;
        
        this.numberOfRockets = numberOfRocketsInit;
        this.numberOfAmmo = numberOfAmmoInit;
        
        this.shieldBonus = null;
        
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        
        this.machineGunXcoordinate = this.xCoordinate + this.offsetXMachineGun;
        this.machineGunYcoordinate = this.yCoordinate + this.offsetYMachineGun;
        
        this.movingXspeed = 0;
        this.movingYspeed = 0;
    }
    
    
    /**
     * Checks if player is shooting. It also checks if player can 
     * shoot (time between bullets, does a player have any bullet left).
     * 
     * @param gameTime The current elapsed game time in nanoseconds.
     * @return true if player is shooting.
     */
    public boolean isShooting(long gameTime)
    {
        // Checks if left mouse button is down && if it is the time for a new bullet.
        if( Canvas.mouseButtonState(MouseEvent.BUTTON1) && 
            ((gameTime - PlayerHelicopter.timeOfLastCreatedBullet) >= PlayerHelicopter.timeBetweenNewBullets) &&
            this.numberOfAmmo > 0) 
        {
            return true;
        } else
            return false;
    }
    
    
    /**
     * Checks if player is fired a rocket. It also checks if player can 
     * fire a rocket (time between rockets, does a player have any rocket left).
     * 
     * @param gameTime The current elapsed game time in nanoseconds.
     * @return true if player is fired a rocket.
     */
    public boolean isFiredRocket(long gameTime)
    {
        // Checks if right mouse button is down && if it is the time for new rocket && if he has any rocket left.
        if( Canvas.mouseButtonState(MouseEvent.BUTTON3) && 
            ((gameTime - Rocket.timeOfLastCreatedRocket) >= Rocket.timeBetweenNewRockets) && 
            this.numberOfRockets > 0 ) 
        {
            return true;
        } else
            return false;
    }
    
    
    /**
     * Checks if player moving helicopter and sets its moving speed if player is moving.
     */
    public void isMoving()
    {
        // Moving on the x coordinate.
        if(Canvas.keyboardKeyState(KeyEvent.VK_D) || Canvas.keyboardKeyState(KeyEvent.VK_RIGHT))
        	movingXspeed += acceleratingXspeed;
        else if(Canvas.keyboardKeyState(KeyEvent.VK_A) || Canvas.keyboardKeyState(KeyEvent.VK_LEFT))
            movingXspeed -= acceleratingXspeed;
        else    // Stoping
            if(movingXspeed < 0)
                movingXspeed += stoppingXspeed;
            else if(movingXspeed > 0)
                movingXspeed -= stoppingXspeed;
        
        // Moving on the y coordinate.
        if(Canvas.keyboardKeyState(KeyEvent.VK_W) || Canvas.keyboardKeyState(KeyEvent.VK_UP))
            movingYspeed -= acceleratingYspeed;
        else if(Canvas.keyboardKeyState(KeyEvent.VK_S) || Canvas.keyboardKeyState(KeyEvent.VK_DOWN))
            movingYspeed += acceleratingYspeed;
        else    // Stoping
            if(movingYspeed < 0)
                movingYspeed += stoppingYspeed;
            else if(movingYspeed > 0)
                movingYspeed -= stoppingYspeed;
    }
    
    /**
     * Creates new bullet which moves in direction specified by unit vector
     * @param directionUnit unit vector
     * @return new bullet
     */
    public Bullet spawnMachineGunBullet(Vector2d directionUnit)
    {
    	return new Bullet(machineGunXcoordinate, machineGunYcoordinate,
    			machineGunBulletSpeed * directionUnit.x,
    			machineGunBulletSpeed * directionUnit.y,
    			machineGunBulletDamage, machineGunBulletImg);
    }
    
    
    /**
     * Updates position of helicopter, animations.
     */
    public void Update()
    {
        // Move helicopter and its propellers.
    	double xC = xCoordinate += movingXspeed;
    	if(xC < 0 || xC > Framework.frameWidth - this.helicopterBodyImg.getWidth())
    	{
    		movingXspeed = 0;
    	}
        double yC = yCoordinate += movingYspeed;
        if(yC < 0 || yC > Framework.frameHeight - this.helicopterBodyImg.getHeight())
        {
        	movingYspeed = 0;
        }
        helicopterRearPropellerAnim.changeCoordinates(xCoordinate + offsetXRearPropeller, yCoordinate + offsetYRearPropeller);
        
        // Change position of the rocket holder.
        this.rocketHolderXcoordinate = this.xCoordinate + this.offsetXRocketHolder;
        this.rocketHolderYcoordinate = this.yCoordinate + this.offsetYRocketHolder;
        
        // Move the machine gun with helicopter.
        this.machineGunXcoordinate = this.xCoordinate + this.offsetXMachineGun;
        this.machineGunYcoordinate = this.yCoordinate + this.offsetYMachineGun;
    }
    
    
    /**
     * Draws helicopter to the screen.
     * 
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(helicopterBodyImg, xCoordinate, yCoordinate, null);
        helicopterRearPropellerAnim.Draw(g2d);
    }
    
}
