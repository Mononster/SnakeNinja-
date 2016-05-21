import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.awt.event.*;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.lang.Math;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;


public class GameBoard extends JPanel implements ActionListener,GameData{
	Controller c;
	Snake snake;
	public static Snake animateSnake;

	public static int movementCounter = 0;
	public static int speedLevel = 6;
	public static int infoCounter = 0;
	int counter = 0;
	int drawCounter = 0;
	int timeCounter = 0;
	int excuteCounter = 0;
	int levelCounter = 1;
	int shopItemCounter = 0;
	int goal = 0;
	int itemCounter = 0;
	public static int life = SNAKE_INIT_LIFE;
	

	boolean itemCollision;
	
	boolean ifInvoker = true;
	boolean ifPlayBgm = false;
	boolean ifRestartTime = false;
	public static boolean keepSpeed = false;
	public static boolean ifMaxSpeed = false;
	public static boolean ifGenerateMB = false;
	public static boolean ifGenerateWorm = true;
	public static boolean isGamePaused = false;
	public static boolean ifPlaySoundEffect = true;
	public static boolean printSelectScreen = false;
	public static boolean printStart = true;
	public static boolean isGameOver = false;
	public static boolean printSetting = false;
	public static boolean printHelp = false;
	public static boolean restartBgm = false;
	public static boolean printPlayScene = false;
	public static boolean printLevelWinScreen = false;
	public static boolean printFinalWinScreen = false;
	public static boolean ifKeepBool = false;
	public static boolean freeStyleMode = false;
	public static boolean arcadeMode = false;
	public static boolean adventureMode = false;
	public static boolean showInstuction = false;

	private ArrayList <Item> items;
	private ArrayList <Item> shop;
	private ArrayList <Clip> clips;
	private ArrayList <Image> frogs;
	private ArrayList <Image> itemImages;
	private ArrayList <Image> modeImages;
	private ArrayList <Image> shopImages;
	private Image cursorImg;
	private Image lifeImg;
	private Image gear;


	public static int score = 0;
	public static int comboValue = 0;
	public static int comboLevel = 0;
	public static int increaseSize = 0;
	public static int gameWidth = GAME_INIT_WIDTH;
	public static int gameHeight = GAME_INIT_HEIGHT;
	public static int baseSize = 40;

	Random random;
	ArrayList<Button> menuButtons;
	ArrayList<Button> playButtons;
	ArrayList<Button> settingButtons;
	public static ArrayList<Button> shopButtons;

	public static Button showButton = new Button(new Rectangle(270,683,20,20),"");
	Button settingButton = new Button(new Rectangle(630, 600, 150,50),"Setting");
	Button helpButton = new Button(new Rectangle(680, 650, 120, 50), "Help");

	Button adventureButton = new Button(new Rectangle(100,590,60,60), "");
	Button freeStyleButton = new Button(new Rectangle(410,590,60,60), "");
	Button arcadeButton = new Button(new Rectangle(260,590,60,60), "");

	Button gotButton = new Button(new Rectangle(300,600,180,50),"GOT IT!!");
	
	Button replayButton = new Button(new Rectangle(215, gameHeight/2 + 50, gameWidth/7 + 20, gameHeight/ 14),"Again");
	Button menuButton = new Button(new Rectangle(430, gameHeight / 2 + 50, gameWidth/7, gameHeight / 14),"Menu");

	Button levelButton = new Button(new Rectangle(300,600,180,50),"Next Level!!");
	Button backButton = new Button(new Rectangle(600,650,120,70),"Menu");
	ArrayList<Color> bgColors;

	AudioInputStream bgm;
	Clip bgmClip;
	Clip eat;

	public GameBoard(){
		super();
		menuButtons = new ArrayList<Button> ();
		playButtons = new ArrayList<Button> ();
		settingButtons = new ArrayList<Button> ();
		clips = new ArrayList<Clip>();
		createBGColor();
		try{
			bgm = AudioSystem.getAudioInputStream(new File("./game/sound/bgm2.wav").getAbsoluteFile());
			bgmClip = AudioSystem.getClip();
		}catch(Exception e){
			e.printStackTrace();
		}
		playButtons.add(replayButton);
		playButtons.add(menuButton);
		createSpeedButton();
		createSettingButton();

		menuButtons.add(settingButton);
		menuButtons.add(helpButton);

		menuButtons.add(adventureButton);
		menuButtons.add(freeStyleButton);
		menuButtons.add(arcadeButton);

		c = new Controller(this,snake,animateSnake);
		addKeyListener(c);
		addMouseListener(c);
		addMouseMotionListener(c);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		try{
			loadSetting();
		}catch (IOException ioe){
			ioe.printStackTrace();
		}
		initGame();
		loadImage();
		loadMusic();
		if (!getButtonFill(1)){
			playBgm();
			ifPlayBgm = true;
		}
		makeCursor();
		repaint();
	}

	public void initGame(){
		Game.gameFrame.getContentPane().setBackground(backgroundColor);
		animateSnake = new Snake();
		makeAnimateSnake();
		random = new Random();
		movementCounter = 0;
		levelCounter = 1;
		comboLevel = 0;
		comboValue = 0;
		score = 0;
		life = SNAKE_INIT_LIFE;
		ifRestartTime = true;
		if (keepSpeed){
			speedLevel = 6;
		}
		if (!ifKeepBool){
			freeStyleMode = false;
			arcadeMode = false;
			adventureMode = false;
		}
		ifKeepBool = false;
		ifGenerateWorm = true;
		ifGenerateMB = false;
		ifMaxSpeed = false;
		resetGame();
	}
	public void resetGame(){
		snake = new Snake();
		items = new ArrayList<Item>();
		shop = new ArrayList<Item>();
		shopButtons = new ArrayList<Button> ();
		shopButtons.add(levelButton);
		shopItemCounter = random.nextInt(3) + 1;
		isGamePaused = false;
		isGameOver = false;
		counter = 0;
		movementCounter = 0;
		c.setSnake(snake);
		c.setAnimateSnake(animateSnake);
		Game.comboCounter = 0;
		if (ifRestartTime){
			Game.stopWatchCounter = 60;
		}
		generateGoal(levelCounter);
		itemCounter = 0;
		checkMusicClose();
	}
	public void createBGColor(){
		bgColors = new ArrayList<Color> ();
		// bgColors.add(Color.BLACK);
		bgColors.add(blue1);
		bgColors.add(blue2);
		bgColors.add(blue3);
		bgColors.add(blue4);
		bgColors.add(blue5);
		bgColors.add(blue6);
	}
	public void playBgm(){

		try{
			bgm = AudioSystem.getAudioInputStream(new File("./game/sound/bgm2.wav").getAbsoluteFile());
			bgmClip = AudioSystem.getClip();
			bgmClip.open(bgm);
			bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void loadMusic(){
		AudioInputStream eatStream;
		try{
			eatStream = AudioSystem.getAudioInputStream(new File("./game/sound/eat.wav").getAbsoluteFile());
			eat = AudioSystem.getClip();
			eat.open(eatStream);
		}catch(Exception e){

		}
	}
	public void playMusic(String name){
		AudioInputStream audio;
		Clip clip;
		try{
			audio = AudioSystem.getAudioInputStream(new File("./game/sound/" + name).getAbsoluteFile());
			if(name == "eat.wav"){
				eat = AudioSystem.getClip();
				eat.open(audio);
				eat.start();
			}
			else{
				clip = AudioSystem.getClip();
				clips.add(clip);
				clip.open(audio);
				clip.start();
			}
			
			//clip.loop(Clip.LOOP_CONTINUOUSLY);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public synchronized void checkMusicClose(){
		//System.out.println(clips.size());
		for (int i = 0; i < clips.size();i++){
			Clip tempClip = clips.get(i);
			if (tempClip.isRunning()){
				continue;
			} 
			else{
				tempClip.close();
				clips.remove(tempClip);
			}
		}

	}
	public void createSpeedButton(){
		for (int i = 0; i < 10; i++){
			Button newButton = new Button(new Rectangle(
				80 + i * 35, 6 * gameHeight /7,25, gameHeight/ 14),new Integer(i).toString());
			menuButtons.add(newButton);
		}
	}
	public void createSettingButton(){
		Button okButton = new Button(new Rectangle(350,600,50,50),"ok");
		settingButtons.add(okButton);
		Button bgmButton = new Button(new Rectangle(80,265,40,40),"");
		settingButtons.add(bgmButton);
		Button effectButton = new Button(new Rectangle(80,365,40,40),"");
		settingButtons.add(effectButton);
		Button freeButton = new Button(new Rectangle(80,165,40,40), "");
		settingButtons.add(freeButton);
	}
	public void makeCursor(){
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		cursorImg = new ImageIcon("./game/image/snake.png").getImage();
		Image image1 = cursorImg.getScaledInstance(25,25, Image.SCALE_DEFAULT);
		Cursor c = toolkit.createCustomCursor(image1,new Point(this.getX(),this.getY()),"img");
		this.setCursor(c);
	}
	public void setCursorInvisible(){
		BufferedImage cursorImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		Cursor invisCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "invis cursor");
		this.setCursor(invisCursor);
	}
	public void loadImage(){
		frogs = new ArrayList<Image>();
		itemImages = new ArrayList<Image>();
		shopImages = new ArrayList<Image>();
		modeImages = new ArrayList<Image>();
		for (int i = 10; i < 200; i = i + 20){
			String imgTag = new Integer(i).toString();
			Image temp = new ImageIcon("./game/image/" + i + ".gif").getImage();
			frogs.add(temp);
			if (i == 190){
				frogs.add(temp.getScaledInstance(40,40,Image.SCALE_DEFAULT));
			}
		}
		for (int i = 1; i < 6; i++){
			Image temp = new ImageIcon("./game/image/frog" + i + ".png").getImage();
			Image temp2 = temp.getScaledInstance(30,30, Image.SCALE_DEFAULT);
			if (i == 1 || i == 3 || i == 4){
				Image temp3 = temp.getScaledInstance(60,60, Image.SCALE_DEFAULT);
				modeImages.add(temp3);
			}
			itemImages.add(temp2);
		}	
		for (int i = 1; i < 5; i++){
			Image temp = new ImageIcon("./game/image/shopItem" + i + ".png").getImage();
			Image temp2 = temp.getScaledInstance(120,120, Image.SCALE_DEFAULT);
			shopImages.add(temp2);
		}
		Image temp = new ImageIcon("./game/image/worm.png").getImage();
		Image temp2 = temp.getScaledInstance(30,30, Image.SCALE_DEFAULT);
		lifeImg = new ImageIcon("./game/image/heart.png").getImage();
		lifeImg = lifeImg.getScaledInstance(30,30,Image.SCALE_DEFAULT);
		itemImages.add(temp2);

		for (int i = 1; i < 4; i++){
			Image lightFrog = new ImageIcon("./game/image/frog" + i + "Lighter.png").getImage();
			Image temp3 = lightFrog.getScaledInstance(60,60,Image.SCALE_DEFAULT);
			modeImages.add(temp3);
		}
		gear = new ImageIcon("./game/image/gear.jpeg").getImage();
		gear = gear.getScaledInstance(50,50,Image.SCALE_DEFAULT);
	}
	public boolean checkWatch(){
		if (Game.stopWatchCounter == -1){
			return true;
		}
		return false;
	}
	public void generateGoal(int level){
		switch(level){
			case(1): goal = 150; break;
			case(2): goal = 350; break;
			case(3): goal = 700; break;
			case(4): goal = 1000; break;
		}
	}
	public void generateSpeedAndWatch(){
		if (adventureMode){
			switch(levelCounter){
				// case (0) : 
				// speedLevel = 6;
				// Game.stopWatchCounter = 40;
				// break;
				case (1) : 
				speedLevel = 6; 
				Game.stopWatchCounter = 40;
				break;
				case (2) : 
				speedLevel = 6; 
				Game.stopWatchCounter = 40;
				break;
				case (3) : 
				speedLevel = 7;
				Game.stopWatchCounter = 40;
				break;
				case (4) : 
				speedLevel = 8;
				Game.stopWatchCounter = 40; 
				break;
			}
			if (ifMaxSpeed){
				speedLevel = 9;
			}
		}
	}

	public boolean checkIfWin(int level){
		if (score >= goal){
			levelCounter++;
			if (levelCounter == 5){
				printFinalWinScreen = true;
				printLevelWinScreen = false;
				return true;
			}
			printLevelWinScreen = true;
			infoCounter = 0;
			generateGoal(levelCounter);
			ifGenerateWorm = true;
			ifGenerateMB = false;
			ifMaxSpeed = false;
			if (!getButtonFill(2)){
				playMusic("win.wav");
			}
			return true;
		}
		else{
			return false;
		}
	}
	public void generateItem(int level){
		while (true){
			boolean successRandom = true;
			int randPosX = random.nextInt(gameWidth - 60) + 20;
			int randPosY = random.nextInt(gameHeight - 60) + 20;
			Rectangle middle = new Rectangle(400,400,20,20);
			if (middle.intersects(new Rectangle(randPosX,randPosY,20,20))){
				successRandom = false;
				continue;
			}
			for (Rectangle temp: snake.getBody()){
				if (temp.intersects(new Rectangle(randPosX,randPosY, 40,40))){
					successRandom = false;
					break;
				}
			}
			for (Item temp2: items){
				if (temp2.rect().intersects(new Rectangle(randPosX,randPosY, (int)temp2.getSize(),(int)temp2.getSize()))){
					successRandom = false;
					break;
				}
			}
			if (successRandom){
				Item newItem = new Item(randPosX,randPosY,itemImages.get(0));
				// if (comboLevel >= 5){
				// 	newItem.setImage(frogs.get(10));
				// }
				int rand = random.nextInt(20);
				int rand2 = random.nextInt(50);
				
				if (level == 1){
					if (rand == 1) newItem = new SmallerPotion(randPosX,randPosY,itemImages.get(1));
					if (rand == 4) {
						if (!ifGenerateWorm) return;
						newItem = new Worm(randPosX,randPosY,itemImages.get(5));
					}
					if (rand == 2 || rand == 3) newItem = new ScorePotion(randPosX,randPosY, itemImages.get(2));			
				}
				if (level == 2){
					if (rand == 1) newItem = new SmallerPotion(randPosX,randPosY,itemImages.get(1));
					if (rand == 2 || rand == 3) newItem = new ScorePotion(randPosX,randPosY, itemImages.get(2));
					if (rand == 4 || rand == 5){
						newItem = new Worm(randPosX,randPosY,itemImages.get(5));
						if (!ifGenerateWorm){
							newItem = new Item(randPosX,randPosY,itemImages.get(0));
						}
					} 
				}
				if (level == 3){
					if (rand == 1) newItem = new SmallerPotion(randPosX,randPosY,itemImages.get(1));
					if (rand == 2 || rand == 3) newItem = new ScorePotion(randPosX,randPosY, itemImages.get(2));
					if (rand == 4 || rand == 5 || rand == 6) {
						newItem = new Worm(randPosX,randPosY,itemImages.get(5));
						if (!ifGenerateWorm){
							newItem = new Item(randPosX,randPosY,itemImages.get(0));
						}
						
					}
				}
				if (level == 4){
					if (rand == 1) newItem = new SmallerPotion(randPosX,randPosY,itemImages.get(1));
					if (rand == 2) newItem = new ScorePotion(randPosX,randPosY, itemImages.get(2));
					if (rand == 4 || rand == 5 || rand == 6) {
						
						newItem = new Worm(randPosX,randPosY,itemImages.get(5));
						if (!ifGenerateWorm){
							newItem = new Item(randPosX,randPosY,itemImages.get(0));
						}

					}
				}
				if (ifGenerateMB){
					int rand3 = random.nextInt(40);
					if (rand3 == 1){
						newItem = new MoneyBag(randPosX,randPosY,itemImages.get(4));
					} 
				}
				if (rand2 == 1) newItem = new RestorePotion(randPosX,randPosY,itemImages.get(3));
				items.add(newItem);
				break;
			}
		}
	}
	public void generateShop(int level, int index){
		int posx = 0;
		if (index == 0){
			posx = 50;
		}else if (index == 1){
			posx = 300;
		}else{
			posx = 570;
		}
		Item temp = new KillWorm(posx, 300, shopImages.get(index));
		Button itemButton = new Button(new Rectangle(posx - 5,295, 130,130), "");
		boolean successRandom = false;

		outerloop:
		while(!successRandom){
			//System.out.println("here");
			int rand = random.nextInt(4) + 1;
			switch(rand){
				case(1) : temp = new KillWorm(posx, 300, shopImages.get(0)); break;
				case(2) : temp = new Heart(posx, 300, shopImages.get(2)); break;
				case(3) : temp = new MoneyBagShop(posx, 300, shopImages.get(1)); break;
				case(4) : temp = new Goose(posx, 300, shopImages.get(3)); break;
			}
			for (Item shopTemp: shop){
				if (temp.getType() == shopTemp.getType()){
					continue outerloop;
				}
			}
			successRandom = true;
		}
		if (successRandom){
			shop.add(temp);
			itemButton.setShopItem(temp);
			shopButtons.add(itemButton);

		}
	}
	public void checkItemCollision(){
		Rectangle snakeFront = snake.getBody().get(0);
		for (int i = 0; i < items.size(); i++){
			Item temp = items.get(i);
			if (temp.rect().intersects(snakeFront)){
				// if snake eat an item.
				//System.out.println(snake.getBody().size());
				temp.effect(snake);
				items.remove(temp);
				itemCollision = true;
				if (!getButtonFill(2) && !eat.isRunning()){
					playMusic("eat.wav");
				}
			}
		}
	}
	public boolean checkWallCollision(){
		Snake operateSnake = snake;
		if (printStart){
			operateSnake = animateSnake;
		}
		Rectangle snakeFront = operateSnake.getBody().get(0);
		if (snakeFront.getX() <= 0 || snakeFront.getY() <= 0 ||
			snakeFront.getX() + snakeFront.getWidth() >= gameWidth ||
			snakeFront.getY() + snakeFront.getHeight() >= gameHeight){
			return true;
		}
		return false;
	}
	public void goThroughWall(int direction, Rectangle temp, Rectangle olderFirst){
		if (olderFirst.getX() <= 0){
			// //System.out.println("here");
			if (olderFirst.getY() + olderFirst.getHeight() >= 800){
				moveSnake(direction,temp,olderFirst,-800,0);
			}
			else{
				moveSnake(direction,temp,olderFirst,0,800);
			}
		}
		else if (olderFirst.getY() <= 0){
			if (olderFirst.getX() + olderFirst.getWidth() >= 800){
				moveSnake(direction,temp,olderFirst,0,-800);
			}
			else{
				moveSnake(direction,temp,olderFirst,800,0);
			}
		}
		else if (olderFirst.getX() + olderFirst.getWidth() >= 800){
			moveSnake(direction,temp,olderFirst,0,-800);
		}
		else if (olderFirst.getY() + olderFirst.getHeight() >= 800){
			moveSnake(direction,temp,olderFirst,-800,0);
		}
		else{
			moveSnake(direction,temp,olderFirst,0,0);
		}
	}
	public boolean checkHeadCollision(){
		Rectangle snakeFront = snake.getBody().get(0);
		int insideX = (int)snakeFront.getX() + 3;
		int insideY = (int)snakeFront.getY() - 3;
		int size = (int)snakeFront.getWidth() - 6;
		Rectangle insideFront = new Rectangle(insideX,insideY,size,size);
		//System.out.println(snake.getBody().size());
		int collision = 0;
		if (speedLevel == 9) collision = 20;
		for (int i = 40 + collision ; i < snake.getBody().size(); i++){
			Rectangle temp = snake.getBody().get(i);
			if (insideFront.intersects(temp)){
				return true;
			}
		}
		return false;
	}
	public void hoverHelper(int a){
		menuButtons.get(a).setHover(true);	
		//System.out.println(menuButtons.get(12).getContent());
		for (int i = 12; i < menuButtons.size(); i++){
			if (i == a) continue;
			menuButtons.get(i).setHover(false);
		}	
		if (menuButtons.get(a).getSound() == false 
			&& menuButtons.get(a).getCounter() == 0){
			menuButtons.get(a).setSound(true);
			for (int i = 12; i < menuButtons.size(); i++){
				if (i == a) continue;
				menuButtons.get(i).setSound(false);
				menuButtons.get(i).setCounter(0);
			}
		}
	}
	public void detectHover(int mx, int my){
		if (printStart){
			if (my >= 6 * gameHeight / 7 && my < 6 * gameHeight / 7 + 25
				&& mx >= 80 && mx <= 420 && freeStyleMode){
				for (int i = 0; i < 10; i ++){
					if (mx >= 80 + i * 35 && mx <= 105 + i*35){
						if (!menuButtons.get(i).getSound() && !menuButtons.get(i).getHover()){
							menuButtons.get(i).setSound(true);
						}
						menuButtons.get(i).setHover(true);
					}
					else{
						menuButtons.get(i).setHover(false);
						menuButtons.get(i).setSound(false);
					}
				}
			}
			else if (my >= 590 && my <= 650 && mx >= 100 && mx <= 160){
				hoverHelper(12);
			}
			else if (my >= 590 && my <= 650 && mx >= 260 && mx <= 320){
				hoverHelper(14);
			}
			else if (my >= 590 && my <= 650 && mx >= 410 && mx <= 470){
				hoverHelper(13);
			}
			else if (my >= 600 && my <= 650 && mx >= 630 && mx <= 780){
				menuButtons.get(10).setHover(true);	
				menuButtons.get(11).setHover(false);	
				if (menuButtons.get(10).getSound() == false 
					&& menuButtons.get(10).getCounter() == 0){
					menuButtons.get(10).setSound(true);

					menuButtons.get(11).setSound(false);
					menuButtons.get(11).setCounter(0);
				}
			}
			else if(my >= 650 && my <= 700 && mx >= 680 && mx <= 770){
				menuButtons.get(11).setHover(true);	
				menuButtons.get(10).setHover(false);	
				//System.out.println(menuButtons.get(11).getHover());
				if (menuButtons.get(11).getSound() == false 
					&& menuButtons.get(11).getCounter() == 0){
					menuButtons.get(11).setSound(true);
					menuButtons.get(10).setSound(false);
					menuButtons.get(10).setCounter(0);
				}
			}
			else{
				for (Button temp: menuButtons){
					temp.setHover(false);
					temp.setSound(false);
				}
				menuButtons.get(10).setCounter(0);
				menuButtons.get(11).setCounter(0);
			}

		}
		else if (printSetting){
			for (Button temp: settingButtons){
				int x = temp.rect().x;
				int y = temp.rect().y;
				if (my >= y && my <= y + temp.rect().height && mx >= x && mx <= x+ temp.rect().width){
					if (temp.getSound() == false && !temp.getHover()){
						temp.setSound(true);
					}
					temp.setHover(true);
				}
				else{
					temp.setHover(false);
					temp.setSound(false);
				}
			}

		}
		else if(printHelp || (showInstuction && (arcadeMode || adventureMode))){
			if (my >= 600 && my <= 650 && mx >= 300 && mx <= 500){
				if (gotButton.getSound() == false && !gotButton.getHover()){
					gotButton.setSound(true);
				}
				gotButton.setHover(true);
			}
			else{
				gotButton.setHover(false);
				gotButton.setSound(false);
			}
			if (showInstuction){
				if (my >= 683 && my <= 703 && mx >= 270 && mx <= 290){
					if (showButton.getSound() == false && !showButton.getHover()){
						showButton.setSound(true);
					}
					showButton.setHover(true);
				}
				else{
					showButton.setHover(false);
					showButton.setSound(false);
				}
			}
		}
		else if(printLevelWinScreen){
			for (Button temp: shopButtons){
				int x = temp.rect().x;
				int y = temp.rect().y;
				if (my >= y && my <= y + temp.rect().height && mx >= x && mx <= x+ temp.rect().width){
					if (temp.getSound() == false && !temp.getHover()){
						temp.setSound(true);
					}
					temp.setHover(true);
				}
				else{
					temp.setHover(false);
					temp.setSound(false);
				}
			}
		}
		else if(printFinalWinScreen){
			//Button backButton = new Button(new Rectangle(600,650,120,70),"Menu");
			if (my >= 650 && my <= 720 && mx >= 600 && mx<= 720){
				if (backButton.getSound() == false && !backButton.getHover()){
					backButton.setSound(true);
				}
				backButton.setHover(true);
			}
			else{
				backButton.setHover(false);
				backButton.setSound(false);
			}
		}
		else{
			// if GAME OVER.
			for (Button temp: playButtons){
				int x = temp.rect().x;
				int y = temp.rect().y;
				if (my >= y && my <= y + temp.rect().height && mx >= x && mx <= x+ temp.rect().width){
					if (temp.getSound() == false && !temp.getHover()){
						temp.setSound(true);
					}
					temp.setHover(true);
				}
				else{
					temp.setHover(false);
					temp.setSound(false);
				}
			}
		}
	}
	public void detectSpeed(int mx, int my){
		int oldSpeed = speedLevel;
		if (printStart && freeStyleMode){
			if (my >= 6 * gameHeight /7 && my <= 6 * gameHeight /7 + gameHeight/14){
				for (int i = 0; i < 10; i ++){
					if (mx >= 80 + i * 35 && mx <= 105 + i*35){
						speedLevel = i;
					}
				}
			}
			else{
				for (Button temp : menuButtons){
					temp.setHover(false);
				}
			}
			if (oldSpeed != speedLevel){
				movementCounter = 0;
				animateSnake = new Snake();
				makeAnimateSnake();
			}
		}
	}
	public void save(String fileName) throws IOException{
		File f = new File(fileName);
		File f2 = new File("temp.txt");
		boolean bool = f2.createNewFile();
		FileWriter fw = new FileWriter(f2.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		if (f.exists()){
			FileReader fileScanner = new FileReader(f);
			BufferedReader rw = new BufferedReader(fileScanner);
			int highScore = Integer.parseInt(rw.readLine());;
			if (highScore > score){
				bw.close();
				f2.delete();
				return;
			}else{
				bw.write(new Integer(score).toString());
			}
			rw.close();
		}
		else{
			//System.out.println(new Integer(score).toString());
			bw.write(new Integer(score).toString());
		}
		bw.close();
		f.delete();
		f2.renameTo(f);
		//f.delete();
	}
	public int load(String fileName) throws IOException{
		File f = new File(fileName);
		int readScore = 0;
		//System.out.println(f.exists());
		if (f.exists()){
			FileReader fileScanner = new FileReader(f);
			BufferedReader br = new BufferedReader(fileScanner);
			String line = br.readLine();
			readScore = Integer.parseInt(line);
		}
		return readScore;
	}
	public void saveSetting() throws IOException{
		File f = new File("SettingConfig.txt");
		File f2 = new File("temp2.txt");
		boolean bool = f2.createNewFile();
		FileWriter fw = new FileWriter(f2.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = 1; i < 4; i++){
			int num = 0;
			if (getButtonFill(i)) num = 1;
			bw.write(new Integer(num).toString());
			bw.newLine();
		}
		bw.close();
		f.delete();
		f2.renameTo(f);
	}
	public void loadSetting() throws IOException{
		File f = new File("SettingConfig.txt");
		if (f.exists()){
			FileReader fileScanner = new FileReader(f);
			BufferedReader rw = new BufferedReader(fileScanner);
			int bgmSetting = Integer.parseInt(rw.readLine());
			int effectSetting = Integer.parseInt(rw.readLine());
			int ifFreeStyleMode = Integer.parseInt(rw.readLine());
			if (ifFreeStyleMode == 1){
				setButtonFill(3,true);
				showInstuction = false;
			}else{
				setButtonFill(3, false);
				showInstuction = true;
			}
			if (bgmSetting == 1){
				setButtonFill(1, true);
			}
			else{
				setButtonFill(1, false);
			}
			if (effectSetting == 1){
				setButtonFill(2, true);
			}
			else{
				setButtonFill(2, false);
			}
		}
	}
	public void paintSetting(Graphics2D g2){
		g2.setColor(Color.RED);
		Font settings = new Font("Comic Sans MS", Font.BOLD, 40);
		g2.setFont(settings);
		g2.drawString("Disable Background Music.", 150, 300);
		g2.drawString("Disable Sound Effect.", 150, 400);
		g2.drawString("Do not Show instruction.", 150, 200);
		for (Button temp : settingButtons){
			if (temp.getHover()){
				g2.setColor(Color.BLUE);
				if (temp.getSound() && !getButtonFill(2)){
					playMusic("button1.wav");
					temp.setSound(false);
				}
			}
				//g2.draw(temp.rect());
			else{
				g2.setColor(lightBlue);
			}

			if (temp.getContent() == ""){
				g2.draw(temp.rect());
			}
			if (temp.getFill()){
				g2.setColor(Color.BLACK);
				g2.fillRect(temp.rect().x,temp.rect().y, temp.rect().width,temp.rect().height);
			}
			
			g2.drawString(temp.getContent(), temp.rect().x + 5, temp.rect().y + gameHeight/21);
		}
	}
	public void paintPlay(Graphics2D g2){
		Game.gameFrame.getContentPane().setBackground(Color.WHITE);
		Font infoFont = new Font("Comic Sans MS",Font.BOLD, 80);
		g2.setFont(new Font("Comic Sans MS", Font.PLAIN, gameWidth/38));    
		g2.setColor(Color.BLACK);
		g2.drawString("FPS   : "+ Game.fps, gameWidth/76, gameHeight / 38);
		g2.drawString("Score: "+ score, gameWidth/76, gameHeight / 18);
		g2.drawImage(gear,750,730,null);
		if (infoCounter >= 0 && infoCounter <= 100){
			g2.setFont(infoFont);
			g2.setColor(Color.ORANGE);
			if (infoCounter <= 50){
				String seconds = "60S!!";
				if (adventureMode){
					// switch(levelCounter){
					// 	case(1):seconds ="40S!!";break;
					// 	case(2):seconds ="35S!!";break;
					// 	case(3):seconds ="30S!!";break;
					// 	case(4):seconds ="25S!!";break;
					// }
					seconds = "40S!!";
				}
				g2.drawString(seconds,300,350);
			}
			if (infoCounter > 50){
				g2.drawString("Go Go Go!",215,350);
			}
			infoCounter++;
			generateSpeedAndWatch();
		}else{
			
			g2.setColor(snake.getColor());
			if (isGameOver){
				g2.setColor(lightBlack);
			}
			g2.setColor(Color.BLACK);
			if (!isGameOver){
				if (!freeStyleMode){
					String a = "";
					g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));    
					g2.drawString("Time: ", 640, 30);
					g2.setColor(Color.BLACK);
					if (Game.stopWatchCounter <= 10){
						g2.setColor(Color.RED);
					}
					g2.drawString(a + Game.stopWatchCounter, 730, 30);
					g2.setColor(Color.BLACK);
					if (adventureMode){
						g2.drawString("Goal:", 640, 60);
						g2.setColor(goldColor);
						g2.drawString(a + goal, 730, 60);
						g2.setFont(new Font("Comic Sans MS", Font.PLAIN, gameWidth/38));   
						g2.setColor(Color.BLACK); 
						g2.drawString("Level : " + levelCounter, gameWidth/76,65);
						for (int i = 0; i < life; i++){
							g2.drawImage(lifeImg, 20 + i * 28, 730, null);
						}
					}
					if (arcadeMode){
						Font comboFont = new Font("Comic Sans MS", Font.BOLD, 80);
						String drawContent = "";
						String drawValue = "";
						switch(comboLevel){
							// case(0):
							// drawContent = "Test";
							// drawValue = "Frogs + 5!"; 
							// break;
							case(1):
							drawContent = "Good!";
							drawValue = "Frogs + 8!"; 
							break;
							case(2):
							drawContent = "Nice!!";
							drawValue = "Frogs + 12!";
							break;
							case(3):
							drawContent = "Keep Up!!!";
							drawValue = "Frogs + 16!";
							break;
							case(4):
							drawContent = "Excellent!!!!";
							drawValue = "Frogs + 20!";
							break;
							case(5):
							drawContent = "OMG!!!!!";
							drawValue = "Frogs + 30";
							break;
						}
						// g2.drawString("Combo: " + comboLevel, 670, gameHeight / 18);
						// g2.drawString("ComboV: " + comboValue, 670, 65);
						g2.setFont(comboFont);
						g2.setColor(lightOrange);
						g2.drawString(drawContent, 200, 300);
						g2.drawString(drawValue, 200, 400);

					}
				}
				g2.setColor(snake.getColor());
				for (int i = 0; i < snake.getBody().size(); i++){
					Rectangle temp = snake.getBody().get(i);
					g2.fillRect((int)temp.getX(),(int)temp.getY(),(int)temp.getWidth(),(int)temp.getHeight());
				}
				g2.setColor(Color.BLUE);
				for (int i = 0; i < items.size(); i++){
					Item temp = items.get(i);
					int animateTime = 2 * temp.getMaxExistTime() / 4;
					if (temp.checkIfAnimate(0,animateTime)){
						temp.incrementAnimating();
					}

					if (temp.getAnimating() % 20 != 0 && temp.getAnimating() % 20 != 1
						&& temp.getAnimating() % 20 != 2 && temp.checkIfAnimate(animateTime/3, animateTime)){
						g2.drawImage(temp.getImage(),temp.getPosX(), temp.getPosY(),null);
					}

					if (temp.getAnimating() % 5 == 0 && temp.checkIfAnimate(0, animateTime/3)){
						g2.drawImage(temp.getImage(),temp.getPosX(), temp.getPosY(),null);
					}
					if (!temp.checkIfAnimate(0,animateTime)){
						g2.drawImage(temp.getImage(),temp.getPosX(), temp.getPosY(),null);
					}
					//g2.fillRect(temp.getPosX(),temp.getPosY(),temp.getSize(),temp.getSize());
				}
			}
		}
	}
	public void paintHelp(Graphics2D g2){
		g2.setColor(Color.BLUE);
		Font settings = new Font("Comic Sans MS", Font.BOLD, 40);
		g2.setFont(settings);
		g2.drawString("Control: ", 100, 110);
		Font instruction = new Font("Comic Sans MS", Font.BOLD, 25);
		g2.setFont(instruction);
		g2.setColor(Color.RED);
		g2.drawString("Using arrow keys to control the movement of snake!", 130, 170);
		g2.drawString("Press P to pause the game!.", 130, 210);
		g2.drawString("Press R to restart the level!.", 130, 250);
		g2.drawString("Good luck! Have fun!!!.", 130, 290);
		g2.setColor(Color.BLUE);
		g2.setFont(settings);
		g2.drawString("Item Representations: ", 100, 360);
		g2.drawImage(itemImages.get(0),120,380,null);
		g2.setFont(instruction);
		g2.setColor(Color.GREEN);
		g2.drawString(": Regular frog!! Eat to score and increase body!", 155, 405 );
		g2.drawImage(itemImages.get(2),120,420,null);
		g2.setColor(goldColor);
		g2.drawString(": Gold frog!! Eat to get 30 extra points!", 155, 445);
		g2.setColor(lightGreen);
		g2.drawImage(itemImages.get(1),120, 460, null);
		g2.drawString(": Magic frog!! Eat to make body shorter!", 155, 485);
		g2.setColor(Color.RED);
		g2.drawImage(itemImages.get(3), 120, 500, null);
		g2.drawString(": Delicious Crab!! Restore whole BODY!", 155, 525);
		g2.drawImage(itemImages.get(5), 120,540,null);
		g2.setColor(Color.decode("#800080"));
		g2.drawString(": Worm!! :>", 155, 565);

		if (gotButton.getHover()){
			g2.setColor(Color.BLUE);
			if (gotButton.getSound() && !getButtonFill(2)){
				playMusic("button1.wav");
				gotButton.setSound(false);
			}
		}
		else{
			g2.setColor(lightBlue);
		}
		g2.setFont(settings);
		g2.drawString(gotButton.getContent(), gotButton.rect().x + 5, gotButton.rect().y + 40);
		//g2.draw(gotButton.rect());
	}
	public void paintInstruction(Graphics2D g2){
		Font settings = new Font("Comic Sans MS", Font.BOLD, 40);
		Font instruction = new Font("Comic Sans MS", Font.BOLD, 25);
		Font showFont = new Font("Comic Sans MS", Font.PLAIN, 20);
		Font enableFont = new Font("Comic Sans MS", Font.PLAIN, 10);
		g2.setColor(Color.BLUE);
		g2.setFont(settings);
		g2.drawString("Control: ", 80, 110);
		g2.setColor(Color.RED);
		g2.setFont(instruction);
		g2.drawString("Using arrow keys to control the movement of snake!", 110, 170);
		g2.drawString("Press P to pause the game!.", 110, 210);

		if (gotButton.getHover()){
			g2.setColor(Color.BLUE);
			if (gotButton.getSound() && !getButtonFill(2)){
				playMusic("button1.wav");
				gotButton.setSound(false);
			}
		}
		else{
			g2.setColor(lightBlue);
		}
		g2.setFont(settings);
		g2.drawString(gotButton.getContent(), gotButton.rect().x + 5, gotButton.rect().y + 40);

		if (showButton.getHover()){
			g2.setColor(Color.BLUE);
			if (showButton.getSound() && !getButtonFill(2)){
				playMusic("button1.wav");
				showButton.setSound(false);
			}
		}
		else{
			g2.setColor(lightBlue);
		}
		if (showButton.getFill()){
			g2.setColor(Color.BLACK);
			g2.fillRect(showButton.rect().x,showButton.rect().y, showButton.rect().width,showButton.rect().height);
		}

		g2.setFont(showFont);
		g2.draw(showButton.rect());
		g2.setColor(Color.BLACK);
		g2.drawString("Don't show this again!", 300,700);
		g2.setFont(enableFont);
		g2.drawString("You can enable this in setting.", 370, 720);
		g2.setColor(Color.BLUE);
		g2.setFont(settings);
		if (arcadeMode){
			g2.drawString("Arcade Feature: ", 80, 280);
			g2.setColor(Color.RED);
			g2.setFont(instruction);
			g2.drawString("You have 60 seconds in this mode!",110, 340);
			g2.drawString("Try to eat as many frogs as you can!!", 110, 380);
			g2.setColor(goldColor);
			g2.drawString("If you eat enough frogs in time,", 110,420);
			g2.drawString("you can get extra bonus!!", 110, 460);
			g2.setColor(Color.RED);
			g2.drawString("There is no wall collision in Arcade mode!", 110, 500);
		}
		if (adventureMode){
			g2.drawString("Adventure Feature: ", 80, 280);
			g2.setColor(Color.RED);
			g2.setFont(instruction);
			g2.drawString("You have 60 seconds and 4 lives in this mode!",110, 340);
			g2.drawString("There is a desired score for each level!!", 110, 380);
			g2.drawString("You need to reach the goal in time!", 110,420);
			g2.drawString("You can buy items after you reach the goal!!", 110,460);
			g2.drawString("Wall Collision is enable!!", 110, 500);
		}
	}
	public void paintStart(Graphics2D g2){
		//draw the animated colorful frog with related to the speedLevel of the game.
		for (int i = 0; i< 10; i++){
			if (speedLevel == i){
				g2.drawImage(frogs.get(i),50,40,null);
			}
		}
		//draw all the text in the starting scene.
		Font menu = new Font("Comic Sans MS", Font.BOLD, 130);
		Font resize = new Font("Comic Sans MS", Font.BOLD, gameHeight/21);
		Font about = new Font("Comic sans MS", Font.BOLD, gameHeight/40);
		Font about2 = new Font("Comic sans MS", Font.BOLD, gameHeight/60);
		Font speedString = new Font("Comic sans MS", Font.BOLD, 30);
		g2.setFont(about);
		g2.drawString("Designed by Marvin Zhan", gameWidth/2 + 130, 720);
		g2.setFont(about2);
		g2.drawString("               University of Waterloo", gameWidth/2 + 150, 740);

		if (freeStyleMode){
			g2.setFont(speedString);
			g2.drawString("Speed: ", 50, 640);
			g2.setFont(about);
			g2.drawString("(Lazy -> Energetic)", 160,640);
		}

		g2.setFont(menu);
		g2.setColor(Color.BLACK);

		//print animateSnake body.
		//System.out.println("AnimateBodySize = " + animateSnake.getBody().size());
		

		if (Game.optimalFPS >= 18){
			int maxTime = 500 - (int)(11000 * Game.dt) + (10 - speedLevel) * 100;
			snakeAnimation(speedLevel);
			movementCounter++;
		//if reach a cycle, count start from 0.
			if (movementCounter >= maxTime || (excuteCounter == 0)){
				movementCounter = 0;
				animateSnake = new Snake();
				makeAnimateSnake();
			}
			for (int i = 0; i < animateSnake.getBody().size(); i++){
				Rectangle temp = animateSnake.getBody().get(i);
				g2.fillRect((int)temp.getX(),(int)temp.getY(),(int)temp.getWidth(),(int)temp.getHeight());
				//System.out.println("temp.x == " + temp.getX());
			}
		}

		//draw the big snake title.
		g2.setColor(Color.BLUE);
		g2.drawString("Snake!", 200, gameHeight/6);
	 	g2.setFont(resize);
	 	//draw all the menu buttons.
	 	//NOTICE: menuButtons[0] - menuButtons[9] are all the speed buttons.
	 	//menuButton[10] is the setting button, and menuButton[11] is the help button.
		for (int i = 0; i < menuButtons.size(); i++){
			if (i >= 0 && i <= 9 && !freeStyleMode) continue;
			//if 0 <= i <= 9 and we are not playing freeSytle Mode, we dont print the buttons.
			Button temp = menuButtons.get(i);
			//g2.draw(temp.rect());

			if (temp.getHover()){
				g2.setColor(Color.BLUE);
				if (temp.getSound() && !getButtonFill(2)){
					playMusic("button1.wav");
					temp.setSound(false);
					temp.setCounter(temp.getCounter()+ 1);
				}
				if (i >= 12 && i <= 14 && !freeStyleMode){
					if (i == 12){
						g2.drawImage(modeImages.get(1), 100, 590, null);
					}
					if (i == 13){
						g2.drawImage(modeImages.get(0), 410, 590, null);
					}
					if (i == 14){
						g2.drawImage(modeImages.get(2), 260,590, null);
					}
					continue;
				}
			}
			else{
				g2.setColor(lightBlue);
				if (i >= 12 && i <= 14 && !freeStyleMode){
					if (i == 12){
						g2.drawImage(modeImages.get(3), 100, 590, null);
					}
					if (i == 13){
						g2.drawImage(modeImages.get(4), 410, 590, null);
					}
					if (i == 14){
						g2.drawImage(modeImages.get(5), 260, 590, null);
					}
					continue;
				}
				
			}
			g2.drawString(temp.getContent(), temp.rect().x + 5, temp.rect().y + gameHeight/21);
			//g2.draw(temp.rect());
		}
		//draw mode string

		
		if (!freeStyleMode){
			g2.setColor(Color.red);	
			g2.drawString("Mission", 65, 685);
			g2.drawString("Arcade", 230, 685);
			g2.drawString("Classic", 380, 685);
		}
		//draw the images in front to each mode.
	}
	public void paintWinScreen(Graphics2D g2){
		Font settings = new Font("Comic Sans MS", Font.BOLD, 40);
		Font words = new Font("Comic Sans MS", Font.BOLD, 20);
		g2.setFont(settings);
		g2.setColor(Color.RED);
		g2.drawString("Congratulations!",50, 50);
		g2.setColor(Color.BLUE);
		g2.drawString("You have reached the desire goal!!", 50, 100);
		g2.drawString("Your Score: " + score, 50, 150);
		g2.setColor(Color.RED);
		g2.drawString("Shopping Center: ", 50, 210);
		g2.setColor(Color.BLUE);
		g2.setFont(words);
		g2.drawString("Click on the item that you wanna buy.", 50,250);
		for (Button temp : shopButtons){
			if (temp.getHover()){
				g2.setColor(Color.BLUE);
				if (temp.getContent() == "" && !temp.getShopItem().getIfDraw()) continue;
				if (temp.getSound() && !getButtonFill(2)){
					playMusic("button1.wav");
					temp.setSound(false);
				}
				if (temp.getContent() == "" && temp.getShopItem().getIfDraw()){
					Stroke oldStroke = g2.getStroke();
					g2.setStroke(new BasicStroke(2));
					g2.draw(temp.rect());
					g2.setStroke(oldStroke);
					g2.setFont(words);
					g2.drawString(temp.getShopItem().getDescription(), 40, 500);
				}
			}
				//g2.draw(temp.rect());
			else{
				g2.setColor(lightBlue);
			}
			g2.setFont(settings);
			g2.drawString(temp.getContent(), temp.rect().x + 5, temp.rect().y + 40);
		}

		g2.setColor(Color.RED);
		g2.setFont(words);
		g2.drawString("Cost: ", 10,450);
		g2.drawString("Type: ", 10,280);
		for (int i = 0; i < shop.size();i++){
			int posX = shop.get(i).getPosX() + 35;
			if (i == 1 || i == 2){
				posX = shop.get(i).getPosX() + 25;
			}
			if (shop.get(i).getIfDraw()){
				g2.drawString(shop.get(i).getType(),posX,shop.get(i).getPosY() - 20);
				g2.drawImage(shop.get(i).getImage(),shop.get(i).getPosX(),shop.get(i).getPosY(),null);
				g2.drawString(shop.get(i).getCost() + " Points",shop.get(i).getPosX() + 25,shop.get(i).getPosY() + 150);
			}
		}
	}
	public void paintGameOverScene(Graphics2D g2){
		Game.gameFrame.getContentPane().setBackground(backgroundColor);
		int bestScore = score;
		try {
			if (arcadeMode){
				save("arcadeScore.txt");
				bestScore = load("arcadeScore.txt");
			}
			if (freeStyleMode){
				save("classicScore.txt");
				bestScore = load("classicScore.txt");
			}
			if (adventureMode){
				save("adventureScore.txt");
				bestScore = load("adventureScore.txt");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		Font gameOver = new Font("Comic Sans MS",Font.BOLD,170);
		Font info = new Font("Comic Sans MS",Font.BOLD,50);
		g2.setFont(gameOver);
		g2.setColor(Color.BLUE);
		g2.drawString("GAME" , gameWidth/2-250, gameHeight/2 - 200);
		g2.drawString("OVER!" , gameWidth/2-250, gameHeight/2 - 50);
		g2.setFont(info);
		for (Button temp : playButtons){
			if (temp.getHover()){
				g2.setColor(Color.BLUE);
				if (temp.getSound() && !getButtonFill(2)){
					playMusic("button1.wav");
					temp.setSound(false);
				}
			}
			else{
				g2.setColor(lightBlue);
			}
			g2.drawString(temp.getContent(), temp.rect().x + 5, temp.rect().y + gameHeight/21);
		}
		g2.setColor(Color.BLUE);
		g2.drawString("Best  : " + bestScore, gameWidth/2-150, gameHeight/2 + 200);
		g2.drawString("Score : " + score , gameWidth/2-150, gameHeight/2 + 300);
	}
	public void paintFinalScene(Graphics2D g2){
		Font settings = new Font("Comic Sans MS", Font.BOLD, 60);
		Font about = new Font("Comic Sans MS", Font.BOLD, 35);
		Font about2 = new Font("Comic sans MS", Font.BOLD, 20);
		Font about3 = new Font("Comic sans MS", Font.BOLD, 15);
		Font buttonFont = new Font("Comic Sans MS", Font.BOLD, 45);
		g2.setFont(settings);
		g2.setColor(Color.RED);
		g2.drawString("Congratulations!",180,100);
		g2.setFont(about);
		g2.setColor(goldColor);
		g2.drawString("You have passed all the levels!!!!",140, 300);
		g2.setColor(Color.RED);
		g2.drawString("Your Score: " + score, 260, 350);
		g2.setColor(goldColor);
		g2.drawString("Hope you had a great time with the game!!", 50, 400);
		g2.drawImage(frogs.get(5),330,130,null);
		g2.setColor(Color.BLACK);
		g2.setFont(about2);

		g2.drawString("Designed by Marvin Zhan.",270, 460);
		g2.setFont(about3);
		g2.drawString(" April 1st - June 1st, 2016.",290, 490);
		g2.drawString("University of Waterloo.", 320,520);
		if (backButton.getHover()){
			g2.setColor(Color.BLUE);
			if (backButton.getSound() && !getButtonFill(2)){
				playMusic("button1.wav");
				backButton.setSound(false);
			}
		}
		else{
			g2.setColor(lightBlue);
		}
		g2.setFont(buttonFont);
		//g2.draw(backButton.rect());
		g2.drawString(backButton.getContent(), backButton.rect().x + 5, backButton.rect().y + 45);
	}
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		gameWidth = getWidth();
		gameHeight = getHeight();
		
		if (!ifPlayBgm){
			bgmClip.stop();
		}
		if (restartBgm){
			playBgm();
			restartBgm = false;
		}
		if (printStart){
			paintStart(g2);
		}
		if (printHelp){
			paintHelp(g2);
		}
		if (printSetting){
			paintSetting(g2);
		}
		if (showInstuction && (adventureMode || arcadeMode)){
			paintInstruction(g2);
		}
		if (getButtonFill(1)){
			ifPlayBgm = false;
		}
		else{
			ifPlayBgm = true;
		}
		if (isGamePaused){
			g.setFont(options);
			g.drawString("Game Paused!", GameBoard.gameWidth/2-125, GameBoard.gameHeight/2);
		}
		if (printPlayScene){
			paintPlay(g2);
			itemCounter++;
		}
		if (printPlayScene && !isGameOver){
			setCursorInvisible();
		}else{
			makeCursor();
		}
		if (printLevelWinScreen){
			paintWinScreen(g2);
		}
		if (isGameOver){
			paintGameOverScene(g2);
		}
		if (printFinalWinScreen){
			paintFinalScene(g2);
		}
		excuteCounter++;
	}
	public void actionPerformed(ActionEvent e){
		update();
		repaint();
	}
	public void run(){
		for (int i = 0; i < speedLevel + 1; i++){
			update();
		}	
	}
	public void drawGame(){
		repaint();
	}
	public void comboCheck(){
		int desireScore = 0;
		switch(comboLevel){
			case(0): desireScore = 15; break;
			case(1): desireScore = 20; break;
			case(2): desireScore = 20; break;
			case(3): desireScore = 25; break;
			case(4): desireScore = 25; break;
			case(5): desireScore = 30; break;
		}
		snake.setColor(bgColors.get(comboLevel));
		if (Game.comboCounter == 5){
			if (comboValue >= desireScore){
				if (comboLevel <= 4){
					if (!getButtonFill(2)){
						playMusic(comboLevel+".wav");
					}
					comboLevel++;
				}
			}
			else{
				comboLevel = 0;
			}
			comboValue = 0;
			Game.comboCounter = 0;
		}
		if (comboValue >= desireScore ){
			if (comboLevel <= 4){
				comboLevel++;
				comboValue = 0;
				Game.comboCounter = 0;
			}
			if (comboLevel == 5){
				comboValue = 0;
				Game.comboCounter = 0;
			}
			if (!getButtonFill(2)){
				playMusic(comboLevel+".wav");
			}
		}	
		if (comboLevel >= 5){
			for (Item temp: items){
				if (temp.getType() == "normal"){
					temp.setImage(frogs.get(10));
					temp.setSize(50);
				}
			}
		}
		if (comboLevel < 5){
			for (Item temp: items){
				if (temp.getType() == "normal"){
					temp.setImage(itemImages.get(0));
					temp.setSize(ITEM_INIT_SIZE);
				}
			}
		}
	}
	public void update(){
		
		Snake operateSnake = snake;
		int frogValue = 5;
		int wormValue = 1;
		if (printStart){
			if (Game.optimalFPS <= 18) return;
			operateSnake = animateSnake;
		}else{
			operateSnake = snake;
		}
		increaseSize = baseSize - (int)(820 * Game.dt);
		//System.out.println(increaseSize);
		int direction = operateSnake.getDirection();
		Rectangle olderFirst = operateSnake.getBody().get(0);
		Rectangle temp = olderFirst;
		if (printPlayScene && !isGameOver && !printFinalWinScreen){
			if ((checkWallCollision() && !arcadeMode) || 
				(snake.getBody().size() > 60 && checkHeadCollision())){
				if (life == 0 || freeStyleMode || arcadeMode){
					if (!getButtonFill(2)){
						playMusic("lose.wav");
					}
					isGameOver = true;
					operateSnake.setSpeed(0);
					repaint();
					return;
				}
				life--;
				ifRestartTime = false;
				resetGame();
				if (!getButtonFill(2)){
					playMusic("die.wav");
				}
				return;
			}
			if (checkWatch() && !freeStyleMode){
				if (adventureMode && checkIfWin(levelCounter)){
					printPlayScene = false;
					ifRestartTime = true;
					return;
				}
				if (!getButtonFill(2)){
					playMusic("lose.wav");
				}
				isGameOver = true;
				operateSnake.setSpeed(0);
				repaint();
				return;
			}
			if (arcadeMode){
				comboCheck();
				goThroughWall(direction,temp,olderFirst);
			}
			else{
				moveSnake(direction,temp,olderFirst,0,0);
			}
			
			int regularFrogSize = 0;
			//now start generate items part.
			//Arcade Mode: always keep at least 5 regular frogs.
			//			   2 more frogs will be added depend on the time.

			for (Item itemTemp: items){
				if (itemTemp.getType() == "normal"){
					regularFrogSize++;
				}
			}
			if (freeStyleMode || adventureMode){
				if (regularFrogSize < 2 && items.size() < 4){
					generateItem(levelCounter);
				}
				if (itemCounter >= 200 && items.size() < 4){
					generateItem(levelCounter);
					itemCounter = 0;
				}
			}

			if (arcadeMode){
				if (regularFrogSize < 5 && items.size() < 7){
					generateItem(2);
				}
				if (itemCounter >= 200 && items.size() < 7){
					generateItem(2);
					itemCounter = 0;
				}
			}

			//add a new rectangle to the front of the snake body.
			//now we need to remove the last rectangle.
			checkItemCollision();
			if (!itemCollision){
				operateSnake.getBody().remove(operateSnake.getBody().size() - 1);
			}
			else{
				counter++;
				//System.out.println(counter);
				//System.out.println(Game.dt);
				if (counter >= baseSize - (int)(820 * Game.dt)){
					switch(comboLevel){
						case(0): frogValue = 5; break;
						case(1): frogValue = 8; break;
						case(2): frogValue = 12; break;
						case(3): frogValue = 16; break;
						case(4): frogValue = 20; break;
						case(5): frogValue = 30; wormValue = 5; break;
					}
					if (baseSize == WORM_INCREASE_SIZE){
						score = score + wormValue;
						comboValue = comboValue + 1;
					}
					else{
						score = score + frogValue;
						comboValue = comboValue + 5;
					}
					itemCollision = false;
					counter = 0;
					baseSize = 40;
				}
			}
			for (Iterator<Item> it = items.iterator(); it.hasNext();){
				Item temp1 = it.next();
				temp1.incrementTimeCounter();
				if (temp1.checkTimeCounter()){
					it.remove();
				}
			}
		}
		if (printLevelWinScreen){
			for (int i = 0; i < shopItemCounter; i++){
				generateShop(levelCounter,i);
			}
			shopItemCounter = -1;
		}
		if (printStart){
			//System.out.println(direction);
			moveSnake(direction,temp,olderFirst,0,0);
			Rectangle toRemove = operateSnake.getBody().get(operateSnake.getBody().size() - 1);
			//System.out.println("toRemove.x == " + toRemove.getX());
			operateSnake.getBody().remove(toRemove);
		}
	}
	public void moveSnake(int direction, Rectangle temp, Rectangle olderFirst,int upDownWall,int leftRightWall){
		Snake operateSnake = snake;
		if (printStart){
			operateSnake = animateSnake;
		}else{
			operateSnake = snake;
		}
		if (direction == 1){
			//move left or right.
			int newPosX = (int)olderFirst.getX() + (int)(operateSnake.getSpeed() * Game.dt) + leftRightWall;
			//System.out.println(newPosX + " " + (int)olderFirst.getY());
			if (olderFirst.y <= 800 && olderFirst.y >= 780){
				olderFirst.y = 0;
			}
			if (olderFirst.y < 0 && olderFirst.y >= -20){
				olderFirst.y = 0;
			}
			temp = new Rectangle(newPosX, (int)olderFirst.getY(),
				(int)olderFirst.getWidth(), (int)olderFirst.getHeight());
		}
		if (direction == -1){
			//System.out.println(newPosY);
			int newPosY = (int)olderFirst.getY() + (int)(operateSnake.getSpeed() * Game.dt) + upDownWall;
			if (olderFirst.x <= 800 && olderFirst.x >= 780){
				olderFirst.x = 0;
			}
			if (olderFirst.x <= 0 && olderFirst.x >= -20){
				olderFirst.x = 0;
			}
			temp = new Rectangle((int)olderFirst.getX(), newPosY, 
				(int)olderFirst.getWidth(), (int)olderFirst.getHeight());
		}
		
		operateSnake.getBody().add(0,temp);
		//System.out.println("operateSnake.size() == " + operateSnake.getBody().get(0).getX() );
	}
	public void snakeAnimation(int speedLevel){
		int animationTime = 500 - (int)(11000 * Game.dt) + (10 - speedLevel) * 100;
		//System.out.println(animationTime);
		int dire = 0;
		int speed = SNAKE_SPEED;
		if (movementCounter >= 0 && movementCounter <= 4 * animationTime / 30){
			dire = 1;
			// move right.
		}
		else if (movementCounter > 4 * animationTime / 30 && movementCounter <= 5 * animationTime / 30){
			dire = -1;
			speed = -speed;
			// move up.
		}
		else if (movementCounter > 5 * animationTime / 30 && movementCounter <= 9 * animationTime / 30){
			dire = 1;
			speed = -speed;
			// move left
		}
		else if (movementCounter > 9 * animationTime / 30 && movementCounter <= 10 * animationTime / 30){
			dire = -1;
			speed = -speed;
			// move up
		}
		else if (movementCounter > 10 * animationTime / 30 && movementCounter <= 14 * animationTime / 30){
			dire = 1;
			//move right;
		}
		else if (movementCounter > 14 * animationTime / 30 && movementCounter <= 15 * animationTime /30){
			dire = -1;
			speed = -speed;
			//move up;
		}
		else if (movementCounter > 15 * animationTime / 30 && movementCounter <= 19 * animationTime / 30){
			dire = 1;
			speed = -speed;
			//move left;
		}
		else if (movementCounter > 19 * animationTime / 30 && movementCounter <= 20 * animationTime / 30){
			dire = -1;
			//move down;
		}
		else if (movementCounter > 20 * animationTime / 30 && movementCounter <= 24 * animationTime / 30){
			dire = 1;
			//move right;
		}
		else if (movementCounter > 24 * animationTime / 30 && movementCounter <= 25 * animationTime / 30){
			dire = -1;
			//move down;
		}
		else if (movementCounter > 25 * animationTime / 30 && movementCounter <= animationTime){
			dire = 1;
			speed = -speed;
			//move left
		}
		animateSnake.setDirection(dire);
		animateSnake.setSpeed(speed);
		//System.out.println(dire + " " + speed + " " + movementCounter + " " + Game.dt );
	}
	public void makeAnimateSnake(){
		int size = animateSnake.getSize();
		int targetSize = (int)(2100 * Game.dt);
		if (Game.dt == 0){
			targetSize = 20;
		}
		//System.out.println(Game.dt);
		animateSnake.getBody().remove(0);
		for (int i = 0; i < 130 - targetSize ; i++){
			Rectangle component = new Rectangle(70 - i * size, 580, 
				size, size);
			animateSnake.addBody(component);
		}
		animateSnake.setDirection(1);
		animateSnake.setSpeed(SNAKE_SPEED);
	}
	public void setGamePaused(boolean value){
    	isGamePaused = value;
    }
	public boolean getIsGamePaused(){
    	return isGamePaused;
    }
    public void setButtonFill(int i, boolean value){
    	settingButtons.get(i).setFill(value);
    }
    public boolean getButtonFill(int i){
    	return settingButtons.get(i).getFill();
    }
}