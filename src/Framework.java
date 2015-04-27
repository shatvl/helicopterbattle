import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

public class Framework extends Canvas {
    
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;
    
    /**
     * Sleep time between frames in milliseconds
     */
    public static final long msecBetweenFrames = 10L;
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 60;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    
    /**
     * Possible states of the game
     */
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}
    /**
     * Current state of the game
     */
    public static GameState gameState;    
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;
    
    // The actual game
    private Game game;
    
    
    private Font font;
    
    // Images for menu.
    private BufferedImage gameTitleImg;
    private BufferedImage menuBorderImg;
    private BufferedImage skyColorImg;
    private BufferedImage cloudLayer1Img;
    private BufferedImage cloudLayer2Img;
    private BufferedImage gameIsLoading;
    private BufferedImage gameLoad;

    public Framework ()
    {
        super();
        
        gameState = GameState.VISUALIZING;
        
        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                gameLoop();
            }
        };
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
     */
    private void initialize()
    {
        font = new Font("monospaced", Font.BOLD, 28);
    }
    
    /**
     * Load files (images).
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void loadContent()
    {
        try 
        {
            menuBorderImg = ImageIO.read(new File("menu_border.png"));
            skyColorImg = ImageIO.read(new File("1_sky.png"));
            gameTitleImg = ImageIO.read(new File("helicopter_battle_title.png"));
            cloudLayer1Img = ImageIO.read(new File("cloud_layer_1.png"));
            cloudLayer2Img = ImageIO.read(new File("cloud_layer_2.png"));
            gameIsLoading = ImageIO.read(new File("loading_picture.jpg"));
            gameLoad = ImageIO.read(new File("game_is_loading.png"));
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    
    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void gameLoop()
    {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    
                    lastTime = System.nanoTime();
                    
                    game.updateGame(gameTime, mousePosition());
                    
                    //lastTime = System.nanoTime();
                break;
                case GAMEOVER:
                    //...
                break;
                case MAIN_MENU:
                    //...
                break;
                case OPTIONS:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case STARTING:
                    // Sets variables and objects.
                    initialize();
                    // Load files - images, sounds, ...
                    loadContent();
                    
                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                    
                break;
                case VISUALIZING:
                    // On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and at last 798px). 
                    // So we wait one second for the window/frame to be set to its correct size. Just in case we
                    // also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
                    // so that we although get approximately size.
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }
            
            // Repaint the screen.
            repaint();
            
            // Here we calculate the time that defines for how long we should put thread to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            
            try {
				Thread.sleep(Framework.msecBetweenFrames);
			} catch (InterruptedException e) {
			}
        }
    }
    
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void draw(Graphics2D g2d)
    {
    	long drawStart;
        switch (gameState)
        {
            case PLAYING:
            	drawStart = System.nanoTime();
                game.draw(g2d, mousePosition(), gameTime);
                g2d.drawString("FPS: "       + secInNanosec / (System.nanoTime() - drawStart), 10, 121);
            break;
            case GAMEOVER:
                drawMenuBackground(g2d);
                g2d.setColor(Color.black);
                g2d.drawString("Press ENTER to restart or ESC to exit.", frameWidth/2 - 113, frameHeight/4 + 30);
                game.drawStatistic(g2d, gameTime);
                g2d.setFont(font);
                g2d.drawString("GAME OVER", frameWidth/2 - 90, frameHeight/4);
            break;
            case MAIN_MENU:
                drawMenuBackground(g2d);
                g2d.drawImage(gameTitleImg, frameWidth/2 - gameTitleImg.getWidth()/2, frameHeight/4, null);
                g2d.setColor(Color.black);
                g2d.drawString("Use w, a, d or arrow keys to move the helicopter.", frameWidth / 2 - 117, frameHeight / 2 - 30);
                g2d.drawString("Use left mouse button to fire bullets and right mouse button to fire rockets.", frameWidth / 2 - 180, frameHeight / 2);
                g2d.drawString("Press any key to start the game or ESC to exit.", frameWidth / 2 - 114, frameHeight / 2 + 30);
            break;
            case OPTIONS:
                //...
            break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawImage(gameIsLoading, 0, 0, frameWidth, frameHeight, null);
                g2d.drawImage(gameLoad, frameWidth - 450, frameHeight - 400, null);            
            break;
        }
    }
    
    
    /**
     * Starts new game.
     */
    private void newGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game();
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.restartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
    }
    
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
        
        switch(gameState)
        {
            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
            break;
            case MAIN_MENU:
            	if(e.getKeyCode() == KeyEvent.VK_ENTER)
            		newGame();
            break;
        }
    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }
    
    private void drawMenuBackground(Graphics2D g2d){
        g2d.drawImage(skyColorImg,    0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(menuBorderImg,  0, 0, Framework.frameWidth, Framework.frameHeight, null);

    }
}
