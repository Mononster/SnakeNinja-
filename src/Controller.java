import java.awt.event.*;
import java.io.*;

public class Controller implements MouseListener,KeyListener,GameData, MouseMotionListener{
	GameBoard game;
	Snake snake;
	Snake animateSnake;
	Boolean moveMouse = false;
	int signSpeed = SNAKE_INIT_SPEED;
	int gameWidth = GameBoard.gameWidth;
	int gameHeight = GameBoard.gameHeight;
	Boolean[] controlKey = {false,false};
	public Controller(GameBoard game, Snake snake, Snake animateSnake){
		this.game = game;
		this.snake = snake;
		this.animateSnake = animateSnake;
	}
	public void mouseEntered(MouseEvent e){
	}
	public void mouseClicked(MouseEvent e){
	}
	public void mouseExited(MouseEvent e){

	}
	public void mouseMoved(MouseEvent arg0) {
        int x = arg0.getX();
        int y = arg0.getY();
      //  System.out.println(x + " "+ y);
        game.detectSpeed(x,y);
        game.detectHover(x,y);
    }
    public void mouseDragged(MouseEvent arg0) {}
	public void mousePressed(MouseEvent e){
		int mx = e.getX();
		int my = e.getY();
		if ((GameBoard.printStart || GameBoard.printSetting 
			|| GameBoard.isGameOver || GameBoard.printHelp || GameBoard.printLevelWinScreen
			|| GameBoard.printFinalWinScreen || (GameBoard.showInstuction && 
			(GameBoard.arcadeMode || GameBoard.adventureMode))) && !game.getButtonFill(2)){
			game.playMusic("mouseClick.wav");
		}
		if (GameBoard.isGameOver == true && GameBoard.printStart == false){
			if (my >= gameHeight/2 + 50 && my <= gameHeight/2 + 50 + gameHeight/ 14){
				if (mx >= 215 && mx <= gameWidth / 4 + gameWidth / 7 + 15){
					GameBoard.ifKeepBool = true;
					game.initGame();
					if (GameBoard.arcadeMode){
						GameBoard.speedLevel = 9;
						GameBoard.infoCounter = 0;
					} 
					GameBoard.keepSpeed = true;
					GameBoard.printStart = false;
					GameBoard.printPlayScene = true;
				}
				if (mx >= 430 && mx <= 550){
					game.initGame();
					GameBoard.keepSpeed = false;
					
					GameBoard.printStart = true;
					GameBoard.printPlayScene = false;
				}
			}
			return;
		}
		if (GameBoard.printStart == true && GameBoard.isGameOver == false ){
			// menu start button
			if (GameBoard.freeStyleMode){
				if (my >=  6 * gameHeight /7 && my <=  6 * gameHeight /7 + GameBoard.gameHeight/14){
					if (mx >= 80 && mx <= 420){
						GameBoard.printStart = false;
						GameBoard.printPlayScene = true;
					}
				}
			}
			if (my >= 590 && my <= 650){
				if (mx >= 100 && mx <= 160){
					GameBoard.adventureMode = true;
					GameBoard.printStart = false;
					GameBoard.speedLevel = 6;
					GameBoard.infoCounter = 0;
					if (game.getButtonFill(3)){
						//dont show instruction.
						GameBoard.printPlayScene = true;
					}else{
						GameBoard.showInstuction = true;
					}	
				}
				if (mx >= 410 && mx <= 470){
					GameBoard.freeStyleMode = true;
					GameBoard.infoCounter = 150;
				}
				if (mx >= 260 && mx <= 320){
					GameBoard.arcadeMode = true;
					Game.stopWatchCounter = 60;
					Game.comboCounter = 0;
					GameBoard.printStart = false;
					GameBoard.speedLevel = 9;
					if (game.getButtonFill(3)){
						//dont show instruction.
						GameBoard.printPlayScene = true;
					}else{
						GameBoard.printPlayScene = false;
						GameBoard.showInstuction = true;
					}
					GameBoard.infoCounter = 0;
					return;
				}
			}
		}

		//Button settingButton = new Button(new Rectangle(550, 620, 150,50),"Setting");
		if (GameBoard.printStart && !GameBoard.printSetting){
			if (my >= 600 && my <= 650 && mx >= 630 & mx <= 780){
				GameBoard.printSetting = true;
				GameBoard.printStart = false;
				moveMouse = true;
			}
		}
		if (GameBoard.printStart && !GameBoard.printHelp){
			if (my >= 650 && my <= 700 && mx >= 680 && mx <= 770){
				GameBoard.printStart = false;
				GameBoard.printHelp = true;
				moveMouse = true;
			}
		}
		if (GameBoard.printHelp){
			if (my >= 600 && my <= 650 && mx >= 300 && mx <= 500){
				GameBoard.printHelp = false;
				GameBoard.printStart = true;
				moveMouse = true;
			}
		}
		if (GameBoard.showInstuction && (GameBoard.arcadeMode || GameBoard.adventureMode)
			){
			if (my >= 600 && my <= 650 && mx >= 300 && mx <= 500){
				GameBoard.showInstuction = false;
				GameBoard.printPlayScene = true;
				Game.stopWatchCounter = 60;
			}
			if(my >= 683 && my <= 703 && mx >= 270 && mx <= 290){
				if (GameBoard.showButton.getFill()){
					GameBoard.showButton.setFill(false);
					//if the check box is alreay being checked.
					game.setButtonFill(3,false);
				}
				else{
					GameBoard.showButton.setFill(true);
					game.setButtonFill(3,true);
				}
				try {
					game.saveSetting();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		if (GameBoard.printLevelWinScreen){
			if (my >= 600 && my <= 650 && mx >= 300 && mx <= 500){
				game.resetGame();
				GameBoard.printLevelWinScreen = false;
				GameBoard.printPlayScene = true;	
			}
			for (Button temp: GameBoard.shopButtons){
				if (temp.getContent() == "Next Level!!") continue;
				Item shopItem = temp.getShopItem();
				if (shopItem.getIfDraw() && my >= temp.rect().y && my <= temp.rect().y + temp.rect().height &&
					mx >= temp.rect().x && mx <= temp.rect().x + temp.rect().width 
					&& GameBoard.score >= shopItem.getCost()){
					shopItem.effect(snake);
					shopItem.setIfDraw(false);
				}
			}
		}
		if (GameBoard.printFinalWinScreen){
			if (my >= 650 && my <= 720 && mx >= 600 && mx<= 720){
				game.initGame();
				GameBoard.printStart = true;
				GameBoard.printFinalWinScreen = false;
			}
		}
		if (GameBoard.printSetting){
			if (my >= 600 && my <= 650 && mx >= 350 && mx <= 400){
				GameBoard.printSetting = false;
				GameBoard.printStart = true;
				moveMouse = true;
			}
			if (my >= 265 && my <= 305 && mx>= 80 && mx<=120){
				if (game.getButtonFill(1)){
					//if the check box is alreay being checked.
					game.setButtonFill(1,false);
					GameBoard.restartBgm = true;
				}
				else{
					game.setButtonFill(1,true);
				}
				try {
					game.saveSetting();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			if (mx >= 80 && mx <= 120 && my >= 365 && my <= 405){
				if (game.getButtonFill(2)){
					//if the check box is alreay being checked.
					game.setButtonFill(2,false);
				}
				else{
					game.setButtonFill(2,true);
				}
				try {
					game.saveSetting();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			if (mx >= 80 && mx <= 120 && my >= 165 && my <= 205){
				if (game.getButtonFill(3)){
					game.setButtonFill(3,false);
					GameBoard.showInstuction = true;
					GameBoard.showButton.setFill(false);
				}
				else{
					game.setButtonFill(3,true);
					GameBoard.showInstuction = false;
				}
				try {
					game.saveSetting();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
				
		}
		if(moveMouse){
       		game.detectHover(0,0);
		}
	}
	public void mouseReleased(MouseEvent e){

	}

	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		int direction = snake.getDirection();
		int size = snake.getBody().size();
		Snake temp = snake;
		if (key == KeyEvent.VK_P && !GameBoard.isGameOver && !GameBoard.printStart){
			Boolean ifPaused = game.getIsGamePaused();
			//System.out.println(ifPaused);
			if (ifPaused){
				game.setGamePaused(false);
			}
			else{
				game.setGamePaused(true);
			}
		}
		if (key == KeyEvent.VK_R && !GameBoard.isGameOver && !GameBoard.printStart){
			game.resetGame();
		}

		if (!GameBoard.isGameOver && !game.getIsGamePaused()){
			if (!GameBoard.freeStyleMode && GameBoard.infoCounter <= 100) return;
			if (snake.getDirection() == 0){
				//case where snake can move to any directions.
				switch(key){
					case(KeyEvent.VK_LEFT):
					temp.setSpeed(-signSpeed);
					temp.setDirection(1);
					break;
					case(KeyEvent.VK_RIGHT):
					temp.setSpeed(signSpeed);
					temp.setDirection(1);
					break;
					case(KeyEvent.VK_UP):
					temp.setSpeed(-signSpeed);
					temp.setDirection(-1);
					break;
					case(KeyEvent.VK_DOWN):
					temp.setSpeed(signSpeed);
					temp.setDirection(-1);
					break;
					//if snake is at initial state;
				}
			}
			else{
				if (direction == 1){
					//is snake already is moving left/right.
					switch(key){
						case(KeyEvent.VK_UP):
						temp.setSpeed(-signSpeed);
						temp.setDirection(-1);
						break;
						case(KeyEvent.VK_DOWN):
						temp.setSpeed(signSpeed);
						temp.setDirection(-1);
						break;
					}
				}
				if (direction == -1){
					//if snake is moving up/down, then we can only move left/right.
					switch(key){
						case(KeyEvent.VK_LEFT):
						temp.setSpeed(-signSpeed);
						temp.setDirection(1);
						break;
						case(KeyEvent.VK_RIGHT):
						temp.setSpeed(signSpeed);
						temp.setDirection(1);
						break;
					}
				}
			}
		}
	}
	public void keyReleased(KeyEvent e){

	}
	public void keyTyped(KeyEvent e){

	}
	public void setSnake(Snake snake){
		this.snake = snake;
	}
	public void setAnimateSnake(Snake snake){
		this.animateSnake = snake;
	}
}