import java.applet.AudioClip;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 */

public class Game {
    
	private Clip fireGun;
	private Clip fireRocket;
	private Clip helicopter;
	private Clip expl;
    // Use this to generate a random number.
    private Random random;
    
    // We will use this for seting mouse position.
    private Robot robot;
    
    // Player - helicopter that is managed by player.
    private PlayerHelicopter player;
    
    // Enemy helicopters.
    private ArrayList<EnemyHelicopter> enemyHelicopterList;
    private ArrayList<Stone> stonesList;
    private ArrayList<StoneSmoke> stonesSmokeList;
    // Explosions
    private ArrayList<Animation> explosionsList;
    private BufferedImage explosionAnimImg;
    
    // List of all the machine gun bullets.
    private ArrayList<Bullet> playerBulletsList;
    
    private ArrayList<Bullet> bossBulletsList;
    
    // List of all the bonuses
    private ArrayList<Bonus> bonusList;
    // List of all the rockets.
    private ArrayList<Rocket> rocketsList;
    // List of all the rockets smoke.
    private ArrayList<RocketSmoke> rocketSmokeList;
    // Animation when player fires a bullet
    private ArrayList<Animation> bulletFire;
    // Image for the sky color.
    private BufferedImage skyColorImg;
    
    // Images for white spot on the sky.
    private BufferedImage cloudLayer1Img;
    private BufferedImage cloudLayer2Img;
    // Images for mountains and ground.
    private BufferedImage mountainsImg;
    private BufferedImage groundImg;
    
    // Objects of moving images.
    private MovingBackground cloudLayer1Moving;
    private MovingBackground cloudLayer2Moving;
    private MovingBackground mountainsMoving;
    private MovingBackground groundMoving;
    
    private BufferedImage shotImg;
    // Image of mouse cursor.
    private BufferedImage mouseCursorImg;
    
    // Font that we will use to write statistic to the screen.
    private Font font;
    
    private int level;
    
    // Statistics (destroyed enemies, run away enemies)
    private int runAwayEnemies;
    private int destroyedEnemies;
    
    private boolean bossFight;
    private int numOfEnemiesForBoss;
    private Boss boss;
    

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
        random = new Random();
        
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        player = new PlayerHelicopter(Framework.frameWidth / 4, Framework.frameHeight / 4);
        
        enemyHelicopterList = new ArrayList<EnemyHelicopter>();
        
        explosionsList = new ArrayList<Animation>();
        
        bonusList = new ArrayList<Bonus>();
        playerBulletsList = new ArrayList<Bullet>();
        bossBulletsList = new ArrayList<Bullet>();
        rocketsList = new ArrayList<Rocket>();
        bulletFire = new ArrayList<Animation>();
        rocketSmokeList = new ArrayList<RocketSmoke>();
        stonesList = new ArrayList<Stone>();
        stonesSmokeList = new ArrayList<StoneSmoke>();
        // Moving images.
        cloudLayer1Moving = new MovingBackground();
        cloudLayer2Moving = new MovingBackground();
        mountainsMoving = new MovingBackground();
        groundMoving = new MovingBackground();
        
        font = new Font("monospaced", Font.BOLD, 18);
        
        runAwayEnemies = 0;
        destroyedEnemies = 0;
        numOfEnemiesForBoss = 7;
        level = 1;
    }
    
    /**
     * Load game files (images).
     */
    private void LoadContent()
    {
        try 
        {
        	try {
				fireGun = AudioSystem.getClip();
				fireRocket = AudioSystem.getClip();
				helicopter = AudioSystem.getClip();
				AudioInputStream inputStreamGun = AudioSystem.getAudioInputStream(new File("GUN.wav"));
				AudioInputStream inputStreamRocket = AudioSystem.getAudioInputStream(new File("rocket.wav"));
				AudioInputStream inputStreamHelicopter = AudioSystem.getAudioInputStream(new File("ain23.wav"));
				expl = AudioSystem.getClip();
				AudioInputStream inputStreamExpl = AudioSystem.getAudioInputStream(new File("expl.wav"));
				expl.open(inputStreamExpl);
				helicopter.open(inputStreamHelicopter);
				fireGun.open(inputStreamGun);
				fireRocket.open(inputStreamRocket);
				
			} catch (LineUnavailableException e) {
			} catch (UnsupportedAudioFileException e) {
			}
        	
            // Images of environment
            //URL skyColorImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/sky_color.jpg");
            skyColorImg = ImageIO.read(new File("1_sky.png"));
            //URL cloudLayer1ImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/cloud_layer_1.png");
            cloudLayer1Img = ImageIO.read(new File("cloud_layer_1.png"));
            //URL cloudLayer2ImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/cloud_layer_2.png");
            cloudLayer2Img = ImageIO.read(new File("cloud_layer_2.png"));
            //URL mountainsImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/mountains.png");
            mountainsImg = ImageIO.read(new File("mountains.png"));
            //URL groundImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/ground.png");
            groundImg = ImageIO.read(new File("ground.png"));
            
            // Load images for player helicopter
            PlayerHelicopter.machineGunBulletImg = ImageIO.read(new File("bullet.png"));
            
            // Load images for Boss
            Boss.helicopter1Img = ImageIO.read(new File("boss_1.png"));
            Boss.helicopter2Img = ImageIO.read(new File("boss_2.png"));
            Boss.bulletImg = ImageIO.read(new File("boss_bullet.png"));
            
            // Load bonus images
            HealthBonus.image = ImageIO.read(new File("health_shield.png"));
            BulletBonus.image = ImageIO.read(new File("bullet_shield.png"));
            RocketBonus.image = ImageIO.read(new File("rocket_shield.png"));
            ShieldBonus.image = ImageIO.read(new File("shield.png"));
            ShieldBonus.guiIcon = ImageIO.read(new File("shield_small.png"));
            
            // Load images for enemy helicopter
           // URL helicopterBodyImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/2_helicopter_body.png");
            EnemyHelicopter.helicopterBodyImg = ImageIO.read(new File("2_helicopter_body.png"));
            EnemyHelicopter.helicopterRearPropellerAnimImg = ImageIO.read(new File("2_rear_propeller_anim.png"));
            Stone.stoneImg = ImageIO.read(new File("kometa.png"));
            StoneSmoke.smokeImg = ImageIO.read(new File("kometa_anim.png"));
            // Images of rocket and its smoke.
            //URL rocketImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/rocket.png");
            Rocket.rocketImg = ImageIO.read(new File("rocket.png"));
           // URL rocketSmokeImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/rocket_smoke.png");
            RocketSmoke.smokeImg = ImageIO.read(new File("rocket_smoke.png"));
            
            shotImg = ImageIO.read(new File("shot.png"));
            
            explosionAnimImg = ImageIO.read(new File("explosion_anim.png"));
            
            // Image of mouse cursor.
           // URL mouseCursorImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/mouse_cursor.png");
            mouseCursorImg = ImageIO.read(new File("mouse_cursor.png"));            
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Now that we have images we initialize moving images.
        cloudLayer1Moving.Initialize(cloudLayer1Img, -6, 0);
        cloudLayer2Moving.Initialize(cloudLayer2Img, -2, 0);
        mountainsMoving.Initialize(mountainsImg, -1, Framework.frameHeight - groundImg.getHeight() - mountainsImg.getHeight() + 40);
        groundMoving.Initialize(groundImg, -1.2, Framework.frameHeight - groundImg.getHeight());
    }
     
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        player.Reset(Framework.frameWidth / 4, Framework.frameHeight / 4);
        
        EnemyHelicopter.restartEnemy();
        Stone.restartStone();
        PlayerHelicopter.timeOfLastCreatedBullet = 0;
        Rocket.timeOfLastCreatedRocket = 0;
        
        bossFight = false;
        
        // Empty all the lists.
 
        bonusList.clear();
        stonesSmokeList.clear();
        enemyHelicopterList.clear();
        playerBulletsList.clear();
        bossBulletsList.clear();
        rocketsList.clear();
        rocketSmokeList.clear();
        explosionsList.clear();
        stonesList.clear();
        // Statistics
        runAwayEnemies = 0;
        destroyedEnemies = 0;
        level = 1;
    }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime The elapsed game time in nanoseconds.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        /* Player */
        // When player is destroyed and all explosions are finished showing we change game status.
        if( !isPlayerAlive() && explosionsList.isEmpty() ){
            Framework.gameState = Framework.GameState.GAMEOVER;
            return; // If player is destroyed, we don't need to do thing below.
        }
        // When a player is out of rockets and machine gun bullets, and all lists 
        // of bullets, rockets and explosions are empty(end showing) we finish the game.
        if(player.numberOfAmmo <= 0 && 
           player.numberOfRockets <= 0 && 
           playerBulletsList.isEmpty() && 
           rocketsList.isEmpty() && 
           explosionsList.isEmpty())
        {
            Framework.gameState = Framework.GameState.GAMEOVER;
            return;
        }
        // If player is alive we update him.
        if(isPlayerAlive()){
        	helicopter.loop(1);
            isPlayerShooting(gameTime, mousePosition);
            didPlayerFiredRocket(gameTime);
            player.isMoving();
            player.Update();
        }
        
        /* Mouse */
        limitMousePosition(mousePosition);
        
        /* Bullets */
        updateBullets();
        
        /* Bonuses */
        updateBonuses(gameTime);
        
        /* Rockets */
        updateRockets(gameTime); // It also checks for collisions (if any of the rockets hit any of the enemy helicopter).
        updateRocketSmoke(gameTime);
        
        /* Enemies */
        if(bossFight && boss.rageMode) {
	        updateStones(gameTime);
	        updateStonesSmoke(gameTime);
        }
        
        createEnemyHelicopter(gameTime);
        updateEnemies(gameTime);
        
        /* Explosions */
        updateExplosions();
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition, long gameTime)
    {
        // Image for background sky color.
        g2d.drawImage(skyColorImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        // Moving images.
        mountainsMoving.Draw(g2d);
        groundMoving.Draw(g2d);
        cloudLayer2Moving.Draw(g2d);
        
        if(isPlayerAlive())
            player.Draw(g2d);
        
        // Draws all the enemies.
        for(int i = 0; i < enemyHelicopterList.size(); i++)
        {
            enemyHelicopterList.get(i).Draw(g2d);
        }
        if(bossFight) {
        	boss.draw(g2d);
	        for(int i = 0; i < stonesList.size(); i++)
	        {
	        	stonesList.get(i).Draw(g2d);
	        }
	        for(int i = 0; i < bossBulletsList.size(); i++)
	        {
	            bossBulletsList.get(i).Draw(g2d);
	        }
        }
        // Draws all the bullets. 
        for(int i = 0; i < playerBulletsList.size(); i++)
        {
            playerBulletsList.get(i).Draw(g2d);
           
        }
        
        // Draws bullet fire animation
        for(int i = 0; i < bulletFire.size(); i++)
        {
        	Animation an = bulletFire.get(i);
        	if(an.active) {
        		an.changeCoordinates(player.xCoordinate + player.helicopterBodyImg.getWidth() - 61,
        							 	 player.yCoordinate + player.helicopterBodyImg.getHeight() - 40);
        		an.Draw(g2d);
        	} else {
        		bulletFire.remove(i--);
        	}
        }
        
        // Draws all the rockets. 
        for(int i = 0; i < rocketsList.size(); i++)
        {
            rocketsList.get(i).Draw(g2d);
        }
        // Draws smoke of all the rockets.
        for(int i = 0; i < rocketSmokeList.size(); i++)
        {
            rocketSmokeList.get(i).Draw(g2d);
        }
        for(int i = 0; i < stonesSmokeList.size(); i++)
        {
        	stonesSmokeList.get(i).Draw(g2d);
        }
        // Draw all explosions.
        for(int i = 0; i < explosionsList.size(); i++)
        {
        	expl.start();	
            explosionsList.get(i).Draw(g2d);
        }
        
        // Draw all bonuses
        for(Bonus bonus : bonusList) {
        	bonus.draw(g2d);
        }
        
        // Draw statistics
        g2d.setFont(font);
        g2d.setColor(Color.RED);
      
        if(bossFight) {
        	g2d.drawString("HP: " + boss.health, (int)boss.xCoordinate, (int)boss.yCoordinate + 20);
        }
        g2d.drawString("HP: " + player.health, player.xCoordinate, player.yCoordinate - 5);
        if(player.shieldBonus != null) {
        	g2d.drawImage(ShieldBonus.guiIcon,
        			player.xCoordinate - ShieldBonus.guiIcon.getWidth() - 5,
        			player.yCoordinate - 50,
        			ShieldBonus.guiIcon.getWidth(),
        			ShieldBonus.guiIcon.getHeight(),
        			null);
        	g2d.drawString(formatTime(player.shieldBonus.secDuration * Framework.secInNanosec
        			- gameTime + player.shieldBonus.consumeTime),
        			player.xCoordinate,
        			player.yCoordinate - 25);
        	
        }
        for(EnemyHelicopter eh : enemyHelicopterList) {
        	g2d.drawString("HP: " + eh.health, eh.xCoordinate, eh.yCoordinate - 5);
        }
        
        g2d.drawString(formatTime(gameTime), Framework.frameWidth/2 - 45, 21);
        g2d.drawString("DESTROYED: " + destroyedEnemies, 10, 21);
        g2d.drawString("RUNAWAY: "   + runAwayEnemies,   10, 41);
        g2d.drawString("ROCKETS: "   + player.numberOfRockets, 10, 61);
        g2d.drawString("AMMO: "      + player.numberOfAmmo, 10, 81);
        g2d.drawString("LEVEL: "     + level, 10, 101);
        
        // Moving images. We draw this cloud in front of the helicopter.
        cloudLayer1Moving.Draw(g2d);
        
        // Mouse cursor
        if(isPlayerAlive())
            drawRotatedMouseCursor(g2d, mousePosition);
    }
    
    /**
     * Draws some game statistics when game is over.
     * 
     * @param g2d Graphics2D
     * @param gameTime Elapsed game time.
     */
    public void DrawStatistic(Graphics2D g2d, long gameTime){
        g2d.drawString("Time: " + formatTime(gameTime),                Framework.frameWidth/2 - 50, Framework.frameHeight/3 + 80);
        g2d.drawString("Rockets left: "      + player.numberOfRockets, Framework.frameWidth/2 - 55, Framework.frameHeight/3 + 105);
        g2d.drawString("Ammo left: "         + player.numberOfAmmo,    Framework.frameWidth/2 - 55, Framework.frameHeight/3 + 125);
        g2d.drawString("Destroyed enemies: " + destroyedEnemies,       Framework.frameWidth/2 - 65, Framework.frameHeight/3 + 150);
        g2d.drawString("Runaway enemies: "   + runAwayEnemies,         Framework.frameWidth/2 - 65, Framework.frameHeight/3 + 170);
        g2d.setFont(font);
        g2d.drawString("Statistics: ",                                 Framework.frameWidth/2 - 75, Framework.frameHeight/3 + 60);
    }
    
    /**
     * Draws rotated mouse cursor.
     * It rotates the cursor image on the basis of the player helicopter machine gun.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Position of the mouse.
     */
    private void drawRotatedMouseCursor(Graphics2D g2d, Point mousePosition)
    {
        double RIGHT_ANGLE_RADIANS = Math.PI / 2;
        
        // Positon of the player helicopter machine gun.
        int pivotX = player.machineGunXcoordinate;
        int pivotY = player.machineGunYcoordinate;
        
        int a = pivotX - mousePosition.x;
        int b = pivotY - mousePosition.y;
        double ab = (double)a / (double)b;
        double alfaAngleRadians = Math.atan(ab);

        if(mousePosition.y < pivotY) // Above the helicopter.
            alfaAngleRadians = RIGHT_ANGLE_RADIANS - alfaAngleRadians - RIGHT_ANGLE_RADIANS*2;
        else if (mousePosition.y > pivotY) // Under the helicopter.
            alfaAngleRadians = RIGHT_ANGLE_RADIANS - alfaAngleRadians;
        else
            alfaAngleRadians = 0;

        AffineTransform origXform = g2d.getTransform();
        AffineTransform newXform = (AffineTransform)(origXform.clone());

        newXform.rotate(alfaAngleRadians, mousePosition.x, mousePosition.y);
        g2d.setTransform(newXform);
        
        g2d.drawImage(mouseCursorImg, mousePosition.x, mousePosition.y - mouseCursorImg.getHeight()/2, null); // We substract half of the cursor image so that will be drawn in center of the y mouse coordinate.
        
        g2d.setTransform(origXform);
    }
    
    /**
     * Format given time into 00:00 format.
     * 
     * @param time Time that is in nanoseconds.
     * @return Time in 00:00 format.
     */
    private static String formatTime(long time){
            // Given time in seconds.
            int sec = (int)(time / Framework.milisecInNanosec / 1000);

            // Given time in minutes and seconds.
            int min = sec / 60;
            sec = sec - (min * 60);

            String minString, secString;

            if(min <= 9)
                minString = "0" + Integer.toString(min);
            else
                minString = "" + Integer.toString(min);

            if(sec <= 9)
                secString = "0" + Integer.toString(sec);
            else
                secString = "" + Integer.toString(sec);

            return minString + ":" + secString;
    }
    
    
    
    
    
    /*
     * 
     * Methods for updating the game. 
     * 
     */
    
     
    /**
     * Check if player is alive. If not, set game over status.
     * 
     * @return True if player is alive, false otherwise.
     */
    private boolean isPlayerAlive()
    {
        return player.health > 0;
    }
    
    /**
     * Convert mouse position to unit vector
     * @param mousePosition
     * @return unit vector
     */
    private static Vector2d mousePositionToVector(int xOrigin, int yOrigin, Point mousePosition)
    {
        Vector2d direction = new Vector2d(mousePosition.x - xOrigin, mousePosition.y - yOrigin);
        return direction.multiply(1.0 / direction.length());
    }
    
    /**
     * Checks if the player is shooting with the machine gun and creates bullets if he shooting.
     * 
     * @param gameTime Game time.
     */
    private void isPlayerShooting(long gameTime, Point mousePosition)
    {
        if(player.isShooting(gameTime))
        {
            PlayerHelicopter.timeOfLastCreatedBullet = gameTime;
            player.numberOfAmmo--;
            
            Bullet b = player.spawnMachineGunBullet(mousePositionToVector(player.machineGunXcoordinate, player.machineGunYcoordinate, mousePosition));
            fireGun.start();
            if(!fireGun.isActive()){
            	fireGun.setFramePosition(0);
            }
            bulletFire.add(new Animation(shotImg, 40, 50, 5, 40, false, 0, 0, 0));
            playerBulletsList.add(b);
        }
    }
    
    /**
     * Checks if the player is fired the rocket and creates it if he did.
     * It also checks if player can fire the rocket.
     * 
     * @param gameTime Game time.
     */
    private void didPlayerFiredRocket(long gameTime)
    {
        if(player.isFiredRocket(gameTime))
        {
        	if(fireRocket.isRunning()){
        		fireRocket.stop();
        		fireRocket.setFramePosition(0);
        	} else {
        		fireRocket.setFramePosition(0);
        	}
            Rocket.timeOfLastCreatedRocket = gameTime;
            player.numberOfRockets--;
            fireRocket.start();
            Rocket r = new Rocket();
            r.Initialize(player.rocketHolderXcoordinate, player.rocketHolderYcoordinate);
            rocketsList.add(r);
        }
    }
    
    /**
     * Creates a new enemy if it's time.
     * 
     * @param gameTime Game time.
     */ 
    private void createEnemyHelicopter(long gameTime)
    {
    	if(bossFight) {
    		return;
    	}
    	if(destroyedEnemies == level * numOfEnemiesForBoss) {
    		bossFight = true;
    		BufferedImage bossImg = random.nextInt() % 2 == 0 ? Boss.helicopter1Img : Boss.helicopter2Img;
    		boss = new Boss(level * Boss.initHealth, Framework.frameWidth,
    				Framework.frameHeight / 2 - bossImg.getHeight() / 2,
    				bossImg);
    		enemyHelicopterList.clear();
    		EnemyHelicopter.spawnEnemies = false;
    	} else if(EnemyHelicopter.spawnEnemies && gameTime - EnemyHelicopter.timeOfLastCreatedEnemy >= EnemyHelicopter.timeBetweenNewEnemies) {
            EnemyHelicopter eh = new EnemyHelicopter();
            int xCoordinate = Framework.frameWidth;
            int yCoordinate = random.nextInt(Framework.frameHeight - EnemyHelicopter.helicopterBodyImg.getHeight());
            eh.Initialize(xCoordinate, yCoordinate);
            // Add created enemy to the list of enemies.
            enemyHelicopterList.add(eh);
            
            // Speed up enemy speed and appearence.
            EnemyHelicopter.speedUp();
            
            // Sets new time for last created enemy.
            EnemyHelicopter.timeOfLastCreatedEnemy = gameTime;
        }
    }
    
    /**
     * Checks if player crashed with enemy, ends the game if he did
     * @param playerRectangle
     * @param enemyRectangle
     * @return
     */
    private boolean isPlayerCrashed(Rectangle playerRectangle, Rectangle enemyRectangle) {
        if(playerRectangle.intersects(enemyRectangle)){
        	if(player.shieldBonus == null) {
	            player.health = 0;
	            helicopter.stop();
	            
	            // Add explosion of player helicopter.
	            for(int exNum = 0; exNum < 3; exNum++){
	                Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, player.xCoordinate + exNum*60,
	                		player.yCoordinate - random.nextInt(100), exNum * 200 +random.nextInt(100));
	                explosionsList.add(expAnim);
	                expl.setFramePosition(0);
	            }
        	}
            // Add explosion of enemy helicopter.
            for(int exNum = 0; exNum < 3; exNum++){
                Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, enemyRectangle.x + exNum*60,
                		enemyRectangle.y - random.nextInt(100), exNum * 200 +random.nextInt(100));
                explosionsList.add(expAnim);
            }
            
            return true;
        }
        return false;
    }
   
    /**
     * Updates all enemies.
     * Move the helicopter and checks if he left the screen.
     * Updates helicopter animations.
     * Checks if enemy was destroyed.
     * Checks if any enemy collision with player.
     */
    private void updateEnemies(long gameTime)
    {
    	if(bossFight) {
    		// update boss position
    		boss.updateVelocity(playerBulletsList, rocketsList);
    		boss.update();
    		
    		// create bullets!
    		if(gameTime - boss.lastBulletSpawnTime >= Boss.timeBetweenBullets) {
    			boss.lastBulletSpawnTime = gameTime;
    			bossBulletsList.add(boss.spawnBullet(player.xCoordinate, player.yCoordinate));
    		}
    		
    		if(player.shieldBonus == null &&
    		   isPlayerCrashed(new Rectangle(player.xCoordinate, player.yCoordinate, player.helicopterBodyImg.getWidth(), player.helicopterBodyImg.getHeight()),
    				   new Rectangle((int)boss.xCoordinate, (int)boss.yCoordinate, boss.helicopterImg.getWidth(), boss.helicopterImg.getHeight()))) {
    			bossFight = false;
    		} else if(boss.health <= 0) {
    			++level;
    			bossFight = false;
    			
    			// boss explosion
                Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, (int)boss.xCoordinate, (int)boss.yCoordinate - explosionAnimImg.getHeight()/3, 0);
                explosionsList.add(expAnim);
                expl.setFramePosition(0);
                
                // we may spawn enemies again
                EnemyHelicopter.spawnEnemies = true;
                
                // clear stones if any
                stonesList.clear();
                stonesSmokeList.clear();
                
                bossBulletsList.clear();
    		}
    	} else {
	        for(int i = 0; i < enemyHelicopterList.size(); i++)
	        {
	            EnemyHelicopter eh = enemyHelicopterList.get(i);
	            
	            eh.Update();
	            
	            // Is chrashed with player?
	            if(isPlayerCrashed(new Rectangle(player.xCoordinate, player.yCoordinate, player.helicopterBodyImg.getWidth(), player.helicopterBodyImg.getHeight()),
	            		 new Rectangle(eh.xCoordinate, eh.yCoordinate, EnemyHelicopter.helicopterBodyImg.getWidth(), EnemyHelicopter.helicopterBodyImg.getHeight()))) {
	                // Remove helicopter from the list.
	                enemyHelicopterList.remove(i--);
	                ++destroyedEnemies;
	            	break;
	            }
	            
	            // Check health.
	            if(eh.health <= 0){
	                // Add explosion of helicopter.
	                Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, eh.xCoordinate, eh.yCoordinate - explosionAnimImg.getHeight()/3, 0); // Substring 1/3 explosion image height (explosionAnimImg.getHeight()/3) so that explosion is drawn more at the center of the helicopter.
	                explosionsList.add(expAnim);
	                expl.setFramePosition(0);
	                // Increase the destroyed enemies counter.
	                destroyedEnemies++;
	                
	                // Remove helicopter from the list.
	                enemyHelicopterList.remove(i--);
	                
	                // Helicopter was destroyed so we can move to next helicopter.
	                continue;
	            }
	            
	            // If the current enemy is left the screen we remove him from the list and update the runAwayEnemies variable.
	            if(eh.isLeftScreen())
	            {
	                enemyHelicopterList.remove(i--);
	                runAwayEnemies++;
	            }
	        }
    	}
    }
    
    /**
     * Update bonuses
     * Spawn new if it's time
     * Consume if collected
     */
    private void updateBonuses(long gameTime) {
    	// Generate new bonuses
    	if(random.nextInt() % 500 == 0) {
    		double speed = 1.8 + random.nextDouble() * 2.7001;
    		switch(random.nextInt() % 4) {
    		case 0:
    			bonusList.add(new HealthBonus(random.nextInt(Framework.frameWidth - HealthBonus.image.getWidth()),
    					-HealthBonus.image.getHeight(), speed, 20));
    			break;
    		case 1:
    			bonusList.add(new BulletBonus(random.nextInt(Framework.frameWidth - BulletBonus.image.getWidth()),
    					-BulletBonus.image.getHeight(), speed, 100));
    			break;
    		case 2:
    			bonusList.add(new RocketBonus(random.nextInt(Framework.frameWidth - RocketBonus.image.getWidth()),
    					-RocketBonus.image.getHeight(), speed, 5));
    			break;
    		case 3:
    			bonusList.add(new ShieldBonus(random.nextInt(Framework.frameWidth - ShieldBonus.image.getWidth()),
    					-ShieldBonus.image.getHeight(), 10, speed));
    			break;
    		}
    	}
    	
    	// Update bonuses
    	Rectangle playerRect = new Rectangle(player.xCoordinate, player.yCoordinate,
    			player.helicopterBodyImg.getWidth(), player.helicopterBodyImg.getHeight());
    	for(int i = 0; i < bonusList.size(); ++i) {
    		Bonus bonus = bonusList.get(i);
    		
    		bonus.update();
    		
    		Rectangle bonusRect = new Rectangle((int)bonus.xCoordinate, (int)bonus.yCoordinate,
    				bonus.image.getWidth(), bonus.image.getHeight());
    		
    		if(playerRect.intersects(bonusRect)) {
    			bonus.apply(player);
    			bonus.consumeTime = gameTime;
    			bonusList.remove(i--);
    		} else if(bonus.isLeftScreen()){
    			bonusList.remove(i--);
    		}
    	}
    	
    	// Update player bonuses
    	if(player.shieldBonus != null && 
    	   gameTime - player.shieldBonus.consumeTime >
    	   player.shieldBonus.secDuration * Framework.secInNanosec) {
    		player.shieldBonus = null;
    	}
    }
    
    /**
     * Update bullets. 
     * It moves bullets.
     * Checks if the bullet is left the screen.
     * Checks if any bullets is hit any enemy.
     */
    private void updateBullets()
    {
        for(int i = 0; i < playerBulletsList.size(); i++)
        {
            Bullet bullet = playerBulletsList.get(i);
            
            // Move the bullet.
            bullet.Update();
            
            // Is left the screen?
            if(bullet.isItLeftScreen()){
                playerBulletsList.remove(i);
                // Bullet have left the screen so we removed it from the list and now we can continue to the next bullet.
                continue;
            }
            
            // Did hit any enemy?
            // Rectangle of the bullet image.
            Rectangle bulletRectangle = new Rectangle((int)bullet.xCoordinate, (int)bullet.yCoordinate,
            		bullet.bulletImg.getWidth(),
            		bullet.bulletImg.getHeight());
            // Go trough all enemies.
            for(int j = 0; j < enemyHelicopterList.size(); j++)
            {
                EnemyHelicopter eh = enemyHelicopterList.get(j);

                // Current enemy rectangle.
                Rectangle enemyRectangel = new Rectangle(eh.xCoordinate, eh.yCoordinate, EnemyHelicopter.helicopterBodyImg.getWidth(), EnemyHelicopter.helicopterBodyImg.getHeight());

                // Is current bullet over current enemy?
                if(bulletRectangle.intersects(enemyRectangel))
                {
                    // Bullet hit the enemy so we reduce his health.
                    eh.health -= bullet.damagePoints;
                    
                    // Bullet was also destroyed so we remove it.
                    playerBulletsList.remove(i);
                    
                    // That bullet hit enemy so we don't need to check other enemies.
                    break;
                }
            }
            // Check if boss is hit
            if(bossFight && !boss.invincible) {
            	Rectangle bossRect = new Rectangle((int)boss.xCoordinate, (int)boss.yCoordinate, boss.helicopterImg.getWidth(), boss.helicopterImg.getHeight());
            	if(bulletRectangle.intersects(bossRect)) {
            		boss.health -= bullet.damagePoints;
            		playerBulletsList.remove(i);
            	}
            }
        }
        for(int i = 0; i < bossBulletsList.size(); ++i) {
        	Bullet bullet = bossBulletsList.get(i);
            
            // Move the bullet.
            bullet.Update();
            
            // Is left the screen?
            if(bullet.isItLeftScreen()) {
                bossBulletsList.remove(i--);
                // Bullet have left the screen so we removed it from the list and now we can continue to the next bullet.
                continue;
            }
            
            Rectangle bulletRectangle = new Rectangle((int)bullet.xCoordinate, (int)bullet.yCoordinate,
            				  bullet.bulletImg.getWidth(),
            				  bullet.bulletImg.getHeight()),
            		  playerRectangle = new Rectangle(player.xCoordinate, player.yCoordinate, player.helicopterBodyImg.getWidth(),
            				  player.helicopterBodyImg.getHeight());
            if(bulletRectangle.intersects(playerRectangle)) {
            	if(player.shieldBonus == null) {
            		player.health -= bullet.damagePoints;
            	}
            	bossBulletsList.remove(i--);
            }
        }
    }

    /**
     * Update rockets. 
     * It moves rocket and add smoke behind it.
     * Checks if the rocket is left the screen.
     * Checks if any rocket is hit any enemy.
     * 
     * @param gameTime Game time.
     */
    
    private void updateRockets(long gameTime)
    {
        for(int i = 0; i < rocketsList.size(); i++)
        {
            Rocket rocket = rocketsList.get(i);
            
            // Moves the rocket.
            rocket.Update();
            
            // Checks if it is left the screen.
            if(rocket.isItLeftScreen())
            {
                rocketsList.remove(i);
                // Rocket left the screen so we removed it from the list and now we can continue to the next rocket.
                continue;
            }
            
            // Creates a rocket smoke.
            RocketSmoke rs = new RocketSmoke();
            int xCoordinate = rocket.xCoordinate - RocketSmoke.smokeImg.getWidth(); // Subtract the size of the rocket smoke image (rocketSmokeImg.getWidth()) so that smoke isn't drawn under/behind the image of rocket.
            int yCoordinte = rocket.yCoordinate - 5 + random.nextInt(6); // Subtract 5 so that smoke will be at the middle of the rocket on y coordinate. We rendomly add a number between 0 and 6 so that the smoke line isn't straight line.
            rs.Initialize(xCoordinate, yCoordinte, gameTime, rocket.currentSmokeLifeTime);
            rocketSmokeList.add(rs);
            
            // Because the rocket is fast we get empty space between smokes so we need to add more smoke. 
            // The higher is the speed of rockets, the bigger are empty spaces.
            int smokePositionX = 5 + random.nextInt(8); // We will draw this smoke a little bit ahead of the one we draw before.
            rs = new RocketSmoke();
            xCoordinate = rocket.xCoordinate - RocketSmoke.smokeImg.getWidth() + smokePositionX; // Here we need to add so that the smoke will not be on the same x coordinate as previous smoke. First we need to add 5 because we add random number from 0 to 8 and if the random number is 0 it would be on the same coordinate as smoke before.
            yCoordinte = rocket.yCoordinate - 5 + random.nextInt(6); // Subtract 5 so that smoke will be at the middle of the rocket on y coordinate. We rendomly add a number between 0 and 6 so that the smoke line isn't straight line.
            rs.Initialize(xCoordinate, yCoordinte, gameTime, rocket.currentSmokeLifeTime);
            rocketSmokeList.add(rs);
            
            // Increase the life time for the next piece of rocket smoke.
            rocket.currentSmokeLifeTime *= 1.02;
            
            // Checks if current rocket hit any enemy.
            if( checkIfRocketHitEnemy(rocket) ){
                // Rocket was also destroyed so we remove it.
            	expl.setFramePosition(0);
                rocketsList.remove(i);
            }
        }
    }
    private void updateStones(long gameTime)
    {
    	if(gameTime - Stone.timeOfLastCreatedEnemy >= Stone.timeBetweenNewStones)
    	{
    		Stone st = new Stone();
    		int yCoordinate = 0;
    		int xCoordinate = random.nextInt(Framework.frameWidth - Stone.stoneImg.getWidth());
    		st.Initialize(xCoordinate, yCoordinate);
    		stonesList.add(st);
    		Stone.speedUp();
    		Stone.timeOfLastCreatedEnemy = gameTime;
    	}
    	
    	for(int i = 0; i < stonesList.size(); i++)
    	{
    		Stone stone = stonesList.get(i);
    		stone.Update();
    		if(stone.isBottomScreen())
    		{
    			stonesList.remove(i);
    			continue;
    		}
    		Rectangle stoneRectangle = new Rectangle(stone.xCoordinate, stone.yCoordinate, Stone.stoneImg.getWidth(), Stone.stoneImg.getHeight());
    		Rectangle playerRect = new Rectangle(player.xCoordinate, player.yCoordinate, player.helicopterBodyImg.getWidth(), player.helicopterBodyImg.getHeight());
    		if(player.shieldBonus == null && playerRect.intersects(stoneRectangle)){
                player.health = 0;
                helicopter.stop();
                stonesList.remove(i);
                for(int exNum = 0; exNum < 3; exNum++){
                    Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, player.xCoordinate + exNum*60, player.yCoordinate - random.nextInt(100), exNum * 200 +random.nextInt(100));
                    explosionsList.add(expAnim);
                    expl.setFramePosition(0);
                }
                // Because player crashed with enemy the game will be over so we don't need to check other enemies.
                break;
            }
    		StoneSmoke ss = new StoneSmoke();
    		int yCoordinate = stone.yCoordinate + 10 - StoneSmoke.smokeImg.getHeight();
    		int xCoordinate = stone.xCoordinate + 6 + random.nextInt(3);
    		ss.Initialize(xCoordinate, yCoordinate, gameTime, stone.currentSmokeLifeTime);
    		stonesSmokeList.add(ss);
    		
    		stone.currentSmokeLifeTime *= 1.001;
   
    	}
    }
    /**
     * Checks if the given rocket is hit any of enemy helicopters.
     * 
     * @param rocket Rocket to check.
     * @return True if it hit any of enemy helicopters, false otherwise.
     */
    private boolean checkIfRocketHitEnemy(Rocket rocket)
    {
        boolean didItHitEnemy = false;
        
        // Current rocket rectangle. // I inserted number 2 instead of rocketImg.getWidth() because I wanted that rocket 
        // is over helicopter when collision is detected, because actual image of helicopter isn't a rectangle shape. (We could calculate/make 3 areas where helicopter can be hit and checks these areas, but this is easier.)
        Rectangle rocketRectangle = new Rectangle(rocket.xCoordinate, rocket.yCoordinate, 2, Rocket.rocketImg.getHeight());
        
        // Go trough all enemies.
        for(int j = 0; j < enemyHelicopterList.size(); j++)
        {
            EnemyHelicopter eh = enemyHelicopterList.get(j);

            // Current enemy rectangle.
            Rectangle enemyRectangel = new Rectangle(eh.xCoordinate, eh.yCoordinate, EnemyHelicopter.helicopterBodyImg.getWidth(), EnemyHelicopter.helicopterBodyImg.getHeight());

            // Is current rocket over current enemy?
            if(rocketRectangle.intersects(enemyRectangel))
            {
                didItHitEnemy = true;
                
                // Rocket hit the enemy so we reduce his health.
                eh.health -= Rocket.damagePower;
                
                // Rocket hit enemy so we don't need to check other enemies.
                break;
            }
        }
        // Check boss
        if(bossFight && !boss.invincible) {
        	Rectangle bossRect = new Rectangle((int)boss.xCoordinate, (int)boss.yCoordinate, boss.helicopterImg.getWidth(), boss.helicopterImg.getHeight());
        	if(rocketRectangle.intersects(bossRect)) {
        		didItHitEnemy = true;
        		boss.health -= Rocket.damagePower;
        	}
        }
        
        return didItHitEnemy;
    }
    
    /**
     * Updates smoke of all the rockets.
     * If the life time of the smoke is over then we delete it from list.
     * It also changes a transparency of a smoke image, so that smoke slowly disappear.
     * 
     * @param gameTime Game time.
     */
    private void updateRocketSmoke(long gameTime)
    {
        for(int i = 0; i < rocketSmokeList.size(); i++)
        {
            RocketSmoke rs = rocketSmokeList.get(i);
            
            // Is it time to remove the smoke.
            if(rs.didSmokeDisappear(gameTime))
                rocketSmokeList.remove(i);
            
            // Set new transparency of rocket smoke image.
            rs.updateTransparency(gameTime);
        }
    }
    
    private void updateStonesSmoke(long gameTime)
    {
    	for(int i = 0; i < stonesSmokeList.size(); i++)
    	{
    		StoneSmoke ss = stonesSmokeList.get(i);
    		if(ss.didSmokeDisappear(gameTime))
    			stonesSmokeList.remove(i);
    		ss.updateTransparency(gameTime);
    	}    		
    }
    /**
     * Updates all the animations of an explosion and remove the animation when is over.
     */
    private void updateExplosions()
    {
        for(int i = 0; i < explosionsList.size(); i++)
        {
            // If the animation is over we remove it from the list.
            if(!explosionsList.get(i).active)
                explosionsList.remove(i);
        }
    }
    
    /**
     * It limits the distance of the mouse from the player.
     * 
     * @param mousePosition Position of the mouse.
     */
    private void limitMousePosition(Point mousePosition)
    {
        // Max distance from the player on y coordinate above player helicopter.
        int maxYcoordinateDistanceFromPlayer_top = 30;
        // Max distance from the player on y coordinate under player helicopter.
        int maxYcoordinateDistanceFromPlayer_bottom = 120;
        
        // Mouse cursor will always be the same distance from the player helicopter machine gun on the x coordinate.
        int mouseXcoordinate = player.machineGunXcoordinate + 250;
        
        // Here we will limit the distance of mouse cursor on the y coordinate.
        int mouseYcoordinate = mousePosition.y;
        if(mousePosition.y < player.machineGunYcoordinate){ // Above the helicopter machine gun.
            if(mousePosition.y < player.machineGunYcoordinate - maxYcoordinateDistanceFromPlayer_top)
                mouseYcoordinate = player.machineGunYcoordinate - maxYcoordinateDistanceFromPlayer_top;
        } else { // Under the helicopter.
            if(mousePosition.y > player.machineGunYcoordinate + maxYcoordinateDistanceFromPlayer_bottom)
                mouseYcoordinate = player.machineGunYcoordinate + maxYcoordinateDistanceFromPlayer_bottom;
        }
        
        // We move mouse on y coordinate with helicopter. That makes shooting easier.
        mouseYcoordinate += player.movingYspeed;
        
        // Move the mouse.
        robot.mouseMove(mouseXcoordinate, mouseYcoordinate);
    }
}
