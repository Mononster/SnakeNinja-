import javax.swing.*;
import java.awt.*;

public class Game implements Runnable,GameData{
	public static int fps = 0;
	public static double dt;
	private Thread gameThread;
	private GameBoard gameBoard;
	public static JFrame gameFrame;
	public static int stopWatchCounter = 60;
	public static int comboCounter = 0;
	public static int optimalFPS = 65;


	public void play(){
		gameFrame = new JFrame("Snake!!!");
		gameFrame.setSize(800,800);
		gameFrame.setResizable(false);
		gameBoard = new GameBoard();
		gameFrame.setContentPane(gameBoard);
		gameFrame.setVisible(true);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		gameFrame.setLocation(dimension.width/2 - GAME_INIT_WIDTH / 2, dimension.height/2 - GAME_INIT_HEIGHT);

		gameThread = new Thread(this);
		gameThread.start();
		
	}

	public void run(){
		long timer = System.currentTimeMillis();
		long lastTime = System.nanoTime();
		long optimalTime = 1000000000 / optimalFPS ;
		int frames = 0;
		

		while (true){
			long now = System.nanoTime();
			long updateLength = now - lastTime;
			lastTime = now;
			frames++;
			
			if (System.currentTimeMillis() - timer >= 1000){
				timer = timer + 1000;
				if (!GameBoard.isGamePaused){
					stopWatchCounter--;
					comboCounter++;
				}
				fps = frames;
				dt = 1 / (float)fps;
				//System.out.println("FPS: " + fps + "dt: " + dt);
				frames = 0;
			}
			if (!GameBoard.isGamePaused){
				gameBoard.run();
			}
			gameBoard.drawGame();
			try{
				long time = (lastTime - System.nanoTime() + optimalTime)/1000000;
				if (time < 0){
					time = optimalTime / 1000000;
				}
				Thread.sleep(time);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	public static void main(String args[]){
		if (args.length != 0){
			optimalFPS = Integer.parseInt(args[0]);
		}
		Game game = new Game();
		game.play();
	}
}