import java.awt.Graphics2D;
import java.awt.image.BufferedImage;



public class Stone {
	
	private static final long timeBetweenNewStonesInit = Framework.secInNanosec*2;
	public static long timeBetweenNewStones = timeBetweenNewStonesInit;
	public static long timeOfLastCreatedEnemy = 0;
	public long currentSmokeLifeTime;
	
	public int xCoordinate;
	public int yCoordinate;
	
	private static final double movingYspeedInit = 4;
	private static double movingYspeed = movingYspeedInit;
	
	public static BufferedImage stoneImg;
	public static Animation stoneAnim;
	
	private static int offsetXanim = 9;
	private static int offsetYanim = 25;
	
	public void Initialize(int xCoordinate, int yCoordinate){
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		
		this.currentSmokeLifeTime = Framework.secInNanosec / 2;
		Stone.movingYspeed = 4;
	}
	
	public static void restartStone(){
		Stone.timeBetweenNewStones = timeBetweenNewStonesInit;
		Stone.timeOfLastCreatedEnemy = 0;
		Stone.movingYspeed = movingYspeedInit;
	}
	
	public boolean isBottomScreen(){
		return yCoordinate > Framework.frameHeight + stoneImg.getHeight();
	}
	
	public void Update(){
		yCoordinate += movingYspeed;
	
	}
	
	public static void speedUp(){
		if(Stone.timeBetweenNewStones > Framework.secInNanosec / 2)
			Stone.timeBetweenNewStones -= Framework.secInNanosec / 100;
		Stone.movingYspeed -= 0.25;
	}
	
	public void Draw(Graphics2D g2d)
	{
		g2d.drawImage(stoneImg, xCoordinate, yCoordinate, null);
	}
}
