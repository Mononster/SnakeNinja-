import java.awt.Rectangle;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Item implements GameData{
	private int posX;
	private int posY;
	private int size;
	private int animatingTime;
	protected Image img;
	protected String type;
	protected Color color;
	protected int timeCounter;
	protected int cost;
	protected int maxExistTime;
	protected String description;
	protected boolean ifDraw;

	public Item(int posX, int posY, Image img){
		this.posX = posX;
		this.posY = posY;
		this.ifDraw = true;
		this.img = img;
		this.timeCounter = 0;
		this.maxExistTime = 2000;
		size = ITEM_INIT_SIZE;
		type = "normal";
		color = blue;
		animatingTime = 0;
	}
	public int getPosX(){
		return posX;
	}
	public int getPosY(){
		return posY;
	}
	public int getSize(){
		return size;
	}
	public void setSize(int size){
		this.size = size;
	}
	public String getType(){
		return type;
	}
	public Image getImage(){
		return img;
	}
	public void setImage(Image img){
		this.img = img;
	}
	public Rectangle rect(){
		return new Rectangle(posX,posY,size,size);
	}
	public void effect(Snake snake){
	}
	public Color getColor(){
		return color;
	}	
	public int getCost(){
		return cost;
	}
	public void setIfDraw(boolean draw){
		this.ifDraw = draw;
	}
	public boolean getIfDraw(){
		return this.ifDraw;
	}

	public String getDescription(){
		return description;
	}
	public int getAnimating(){
		return animatingTime;
	}
	public int getMaxExistTime(){
		return maxExistTime;
	}
	public void incrementTimeCounter(){
		this.timeCounter++;
	}
	public void incrementAnimating(){
		this.animatingTime++;
	}
	public boolean checkTimeCounter(){
		if (this.type == "normal"){
			return false;
		}
		if (this.timeCounter >= maxExistTime){
			return true;
		}
		return false;
	}
	public boolean checkIfAnimate(int lowerBound, int upperBound){
		if (this.type == "normal"){
			return false;
		}
		if (lowerBound <= this.maxExistTime - this.timeCounter && 
		 	this.maxExistTime - this.timeCounter<= upperBound){
			return true;
		}
		return false;
	}
}

class Worm extends Item{
	public Worm(int posX, int posY, Image img){
		super(posX,posY,img);
		type = "worm";
		maxExistTime = 3000;
	}
	public void effect(Snake snake){
		GameBoard.baseSize = WORM_INCREASE_SIZE;
	}
}
class KillWorm extends Item{
	public KillWorm(int posX, int posY, Image img){
		super(posX,posY,img);
		type = "RAID!";
		Random rand = new Random();
		this.cost = rand.nextInt(50) + 100;
		description = "Haha, let's kill all the worms in next level :>!!";
	}	
	public void effect(Snake snake){
		 GameBoard.ifGenerateWorm = false;
		 GameBoard.score -= cost;
	}
}
class Goose extends Item{
	public Goose(int posX, int posY, Image img){
		super(posX,posY,img);
		type = "Goose!";
		Random rand = new Random();
		this.cost = rand.nextInt(1) + 1;
		description = "When you saw a goose, you gotta run faster bruh!! RIP!!";
	}
	public void effect(Snake snake){
		GameBoard.ifMaxSpeed = true;
	}
}
class Heart extends Item{
	public Heart(int posX, int posY, Image img){
		super(posX,posY,img);
		type = "Healthier!";
		Random rand = new Random();
		this.cost = rand.nextInt(50) + 50;
		description = "Your HP will + 1 since on :)!!";
	}
	public void effect(Snake snake){
		 GameBoard.life += 1;
		 GameBoard.score -= cost;
	}
}

class MoneyBagShop extends Item{
	public MoneyBagShop(int posX, int posY, Image img){
		super(posX,posY,img);
		type = "MoneyBag!";
		Random rand = new Random();
		this.cost = rand.nextInt(50) + 150;
		description = "There is a chance you will see some big money bags in next level! :)!!";
	}
	public void effect(Snake snake){
		 GameBoard.ifGenerateMB = true;
		 GameBoard.score -= cost;
	}
}

class MoneyBag extends Item{
	public MoneyBag(int posX, int posY, Image img){
		super(posX,posY,img);
		type = "MoneyBag!";
		maxExistTime = 3000;
	}
	public void effect(Snake snake){
		 GameBoard.score += 150;
	}
}

class ScorePotion extends Item {
	int scoreIncrese = 30;
	public ScorePotion(int posX, int posY, Image img){
		super(posX,posY,img);
		type = "score";
		color = lightOrange;
		maxExistTime = 2000;
		if (GameBoard.arcadeMode){
			scoreIncrese = 20;
		}
	}	
	public void effect(Snake snake){
		GameBoard.score += scoreIncrese;
		GameBoard.comboValue += scoreIncrese;
	}
}	
class SmallerPotion extends Item {
	int removeSize = 5;
	public SmallerPotion(int posX, int posY, Image img){
		super(posX,posY,img);
		type = "smaller";
		color = lightGreen;
		maxExistTime = 1000;
	}
	public void effect(Snake snake){
		snake.removeBody(removeSize);
	}
}
class RestorePotion extends Item {
	public RestorePotion(int posX, int posY, Image img){
		super(posX,posY,img);
		type = "restore";
		maxExistTime = 1000;
	}
	public void effect(Snake snake){
		snake.restoreBody();
	}
}