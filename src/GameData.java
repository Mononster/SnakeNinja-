import javax.swing.*;
import java.awt.*;

public interface GameData{
	public static int SNAKE_INIT_SPEED = 100;
	public static int SNAKE_INIT_LIFE = 3;
	public static final int GAME_INIT_WIDTH = 760;
	public static final int GAME_INIT_HEIGHT = 760;
	public static final int SNAKE_INIT_SIZE = 20;
	public static final int WORM_INCREASE_SIZE = 200;
	public static int SNAKE_SPEED = SNAKE_INIT_SPEED;
	public static final int ITEM_INIT_SIZE = 30;
	public static int ANIMATION_DURATION = 400000;
	public static Color backgroundColor = Color.WHITE;
	public static Color buttonInitColor = Color.BLUE;
	public static Color hoverColor = Color.BLACK;
	public static Color lightBlack = new Color(230,228,228);
	public static Color lightBlue = new Color(204,229,255);
	public static Color lightRed = new Color(255,204,204);
	public static Color lightGrey = Color.decode("#E5E4E2");
	public static Color blue = new Color(0,0,204);
	public static Color lightGreen = Color.decode("#99FF00");
	public static Color lightOrange = Color.decode("#ffe4b2");
	public static Color goldColor = Color.decode("#FFD700");
	public static Font title = new Font("Comic Sans MS", Font.BOLD, GAME_INIT_WIDTH / 7);
	public static Font options = new Font("arial", Font.BOLD,35);
	public static Font splashScreen = new Font("Comic Sans MS", Font.BOLD, 35);
	public static Color blue1 = new Color(0,0,0);
	public static Color blue2 = new Color(51,0,0);
	public static Color blue3 = new Color(103,0,0);
	public static Color blue4 = new Color(154,0,0);
	public static Color blue5 = new Color(205,0,0);
	public static Color blue6 = new Color(255,0,0);
}