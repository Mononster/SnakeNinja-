import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.Color;

public class Snake implements GameData{
	private int speed;
	private int direction;
	private Color snakeColor;
	//moving direction of the snake 1 for left/right, -1 for up/down.
	//0 for initial.
	private ArrayList <Rectangle> snakeBody;

	// snake is made of a list of squares.
	private int squareSize;
	
	public Snake(){
		int posX = 400;
		int posY = 400;
		speed = SNAKE_INIT_SPEED;
		squareSize = SNAKE_INIT_SIZE;
		snakeColor = new Color(0,0,0);
		direction = 0; 
		snakeBody = new ArrayList<Rectangle>();
		snakeBody.add(new Rectangle(posX, posY,squareSize, squareSize));
		// since the at the start the snake can move to any directions.
	}
	public ArrayList<Rectangle> getBody(){
		return snakeBody;
	}
	public void addBody(Rectangle temp){
		snakeBody.add(temp);
	}
	public void removeBody(int size){
		int numToRemove = size * GameBoard.increaseSize;
		if (snakeBody.size() > numToRemove + GameBoard.increaseSize * 2){
			for (int i = 0; i < numToRemove; i++){
				snakeBody.remove(snakeBody.get(snakeBody.size() - 1));
			}
		}
		else{
			snakeBody.remove(snakeBody.get(snakeBody.size() - 1));
		}
	}
	public void restoreBody(){
		for (int i = 0 ; i < (snakeBody.size() - 2) * GameBoard.increaseSize ; i++){
			snakeBody.remove(snakeBody.get(snakeBody.size() - 1));
		}
	}
	public void setColor(Color toset){
		this.snakeColor = toset;
	}
	public Color getColor(){
		return snakeColor;
	}
	public int getSpeed(){
		return speed;
	}
	public int getDirection(){
		return direction;
	}
	public void setSpeed(int speed){
		this.speed = speed;
	}
	public void setDirection(int dir){
		this.direction = dir;
	}
	public int getSize(){
		return squareSize;
	}
}