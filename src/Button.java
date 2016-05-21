import javax.swing.*;
import java.awt.*;

public class Button implements GameData{
	Rectangle button;
	Color color;
	boolean ifHover;
	boolean ifFill;
	boolean ifSound;
	String content;
	int counter;
	Item shopItem;
	public Button(Rectangle button, String content){
		this.button = button;
		this.color = buttonInitColor;
		this.ifHover = false;
		this.ifFill = false;
		this.ifSound = false;
		this.counter = 0;
		this.content = content;
	}	
	public void setColor(Color color){
		this.color = color;
	}
	public void setShopItem(Item item){
		this.shopItem = item;
	}
	public Item getShopItem(){
		return shopItem;
	}
	public Rectangle rect(){
		return button;
	}
	public void setRect(Rectangle rect){
		this.button = rect;
	}
	public Color getColor(){
		return color;
	}
	public void setHover(boolean value){
		this.ifHover = value;
	}
	public void setFill(boolean value){
		this.ifFill = value;
	}
	public boolean getFill(){
		return ifFill;
	}
	public boolean getHover(){
		return ifHover;
	}
	public String getContent(){
		return content;
	}
	public void setCounter(int a){
		this.counter = a;
	}
	public int getCounter(){
		return this.counter;
	}
	public boolean getSound(){
		return ifSound;
	}
	public void setSound(boolean value){
		this.ifSound = value;
	}
}