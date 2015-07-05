import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.graphics.GRoundRect;
import acm.program.GraphicsProgram;

public class GameGrid extends GraphicsProgram {
	// size of screen
	public static final int APPLICATION_WIDTH = 500;
	public static final int APPLICATION_HEIGHT = 500;

	private static int boxsize = 25;
	private static int width = 20;
	private static int height = 20;
	static int[][] grid = new int[width][height];

	private static int offsetX = 2;
	private static int offsetY = 2;

	private static GImage pacman;

	private int foodScore = 2410;

	private int pacX;
	private int pacY;

	private int ghostX;
	private int ghostY;

	public static Color c;

	private Ghost ghost1;
	private Ghost ghost2;
	private Ghost ghost3;
	private Ghost ghost4;

	private double timer = 0;
	public static char[][] char2D = new char[width][height];
	public static ArrayList<GOval> foodList = new ArrayList<GOval>();
	public static ArrayList<Ghost> ghostList = new ArrayList<Ghost>();

	public static void main(String[] args) {
		new GameGrid().start(args);
		//System.out.println(alist.size());

	}

	public void run() {
		addKeyListeners();
		addMouseListeners();

		File gridFile = new File("largerboard.txt");
		parseFile(gridFile);

		// create a 10x10 grid of rectangles
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				GRect myRect = new GRect(boxsize * i, boxsize * j, boxsize,
						boxsize);
				add(myRect);
				// myRect.setFilled(true);
				// myRect.setColor(Color.BLUE);

			}
		}

		/*	for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				grid[i][j] = (int) (2 * Math.random()); // or grid[i][j] =
				// rg.nextInt(0, 1)
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		} */

		// add food to board in black places
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (char2D[i][j] == '0') {
					c = Color.BLACK;
					GOval foodPiece = new GOval(10 + 25 * i, 10 + 25 * j, 5, 5);
					add(foodPiece);
					foodPiece.setFilled(true);
					foodPiece.setColor(Color.YELLOW);
					foodList.add(foodPiece);
				} else {
					c = Color.BLUE;
				}
				// draw boxes
				GRect myRect = new GRect(boxsize * i, boxsize * j, boxsize,
						boxsize);
				add(myRect);
				myRect.sendBackward();
				myRect.setFilled(true);
				myRect.setColor(c);
			}
		}

		//		GOval pacman = new GOval(5, 5, 15, 15);
		//		add(pacman);
		//		pacman.setFilled(true);
		//		pacman.setColor(Color.YELLOW);

		// create and add pacman to top left of screen

		pacman = new GImage("pacmanR.png", offsetX, offsetY);
		add(pacman);
		pacman.scale(0.04);

		addGhosts();

		// score label
		GLabel scoreLabel = new GLabel("Score: " + foodScore, 31, 44);
		scoreLabel.setColor(Color.RED);
		add(scoreLabel);
		scoreLabel.sendToFront();
		Font f = new Font("Arial", 1, 18);
		scoreLabel.setFont(f);

		// game loop (for eating food and modifying screen)
		while(true) {
			//ghost1 moves once a second, and follows pacman
			if(timer % 500 == 0) {
				for(int i = 0; i < ghostList.size(); i++) {
					if(ghostList.get(i).getGhostX() < pacX && isCollidable(ghostX + 1, ghostY)) {
						ghostList.get(i).move("right");
					} else if(ghostList.get(i).getGhostX() > pacX && isCollidable(ghostX - 1, ghostY)) {
						ghostList.get(i).move("left");
					} else if(ghostList.get(i).getGhostY() < pacY && isCollidable(ghostX, ghostY + 1)) {
						ghostList.get(i).move("down");
					} else if(ghostList.get(i).getGhostY() > pacY && isCollidable(ghostX, ghostY - 1)) {
						ghostList.get(i).move("up");
					} 
				}
			}
			//update score label
			scoreLabel.setLabel("Score: " + foodScore);
			//checking food collision
			for(int i = 0; i < foodList.size(); i++) {
				GOval foodPiece = foodList.get(i);
				if(isColliding(pacman, foodPiece)) {
					remove(foodPiece);
					foodList.remove(i);
					foodScore += 10;	
				}

			}

			if(foodScore == 2420) {
				Font r = new Font("Arial", 1, 30);
				
				GRect cover = new GRect(0, 0, APPLICATION_WIDTH, APPLICATION_HEIGHT);
				cover.setFilled(true);
				cover.setColor(Color.BLACK);
				add(cover);
				cover.sendToFront();

				GLabel finalScore = new GLabel("YOU WIN!", 175, 200);
				finalScore.setColor(Color.WHITE);
				add(finalScore);
				finalScore.sendToFront();
				finalScore.setFont(r);

				GLabel playAgain = new GLabel("Would you like to play again?", 50, 250);
				playAgain.setColor(Color.WHITE);
				add(playAgain);
				playAgain.sendToFront();
				playAgain.setFont(r);

				GRoundRect playButton = new GRoundRect(175, 275, 150, 75);
				playButton.setFilled(true);
				playButton.setColor(Color.WHITE);
				add(playButton);
				playButton.sendToFront();
				
				GLabel play = new GLabel("Play Again", 200, 320);
				play.setColor(Color.BLACK);
				add(play);
				play.sendToFront();
				Font p = new Font("Arial", 1, 20);
				play.setFont(p);

				break;
			}

			for(int i = 0; i < ghostList.size(); i++) {
				Ghost ghost1 = ghostList.get(i);
				if(isColliding(pacman, ghost1)) {
					remove(pacman);
					//				GRect cover = new GRect(0, 0, APPLICATION_WIDTH, APPLICATION_HEIGHT);
					//				cover.setFilled(true);
					//				cover.setColor(Color.BLACK);
					//				add(cover);
					//				cover.sendToFront();

					GLabel youLose = new GLabel("YOU LOSE!", 175, 200);
					youLose.setColor(Color.WHITE);
					add(youLose);
					youLose.sendToFront();
					Font y = new Font("Arial", 1, 30);
					youLose.setFont(y);

					break;
				}
			}

			ghostX = ghost1.getGhostX();
			ghostY = ghost1.getGhostY(); 
			pacX = (int) (pacman.getX() / boxsize);
			pacY = (int) (pacman.getY() / boxsize);

			pause(20);
			timer += 20;
		}
	}

	public void addGhosts() {
		ghost1 = new Ghost("ghostlb.png", 1, 251, boxsize);
		add(ghost1);
		ghost1.scale(0.04);
		ghostList.add(ghost1);

		ghost2 = new Ghost("ghostp.png", 1, 500 - 25, boxsize);
		add(ghost2);
		ghost2.scale(0.04);
		ghostList.add(ghost2);

		ghost3 = new Ghost("ghostr.png", 500 - 25, 500 - 25, boxsize);
		add(ghost3);
		ghost3.scale(0.04);
		ghostList.add(ghost3);

		ghost4 = new Ghost("ghosto.png", 500 - 25, 1, boxsize);
		add(ghost4);
		ghost4.scale(0.07);
		ghostList.add(ghost4);

	}

	public boolean isColliding(GObject object1, GObject object2) {
		if(object1.getBounds().intersects(object2.getBounds())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCollidable(int x, int y) {
		if(x >= 0 && x < 20 && y >= 0 && y < 20) {
			if(char2D[x][y] == '1') {
				return false;
			} else {
				return true;
			} 
		} else {
			return false;
		}
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT  && isCollidable(pacX + 1, pacY)) {
			remove(pacman);
			pacman = new GImage("pacmanR.png", pacman.getX(), pacman.getY());
			add(pacman);
			pacman.scale(0.04);
			pacman.move(boxsize, 0);
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT && isCollidable(pacX - 1, pacY)) {
			remove(pacman);
			pacman = new GImage("pacmanL.png", pacman.getX(), pacman.getY());
			add(pacman);
			pacman.scale(0.04);
			pacman.move(-boxsize, 0);
		} else if(e.getKeyCode() == KeyEvent.VK_UP && isCollidable(pacX, pacY - 1)) {
			remove(pacman);
			pacman = new GImage("pacmanU.png", pacman.getX(), pacman.getY());
			add(pacman);
			pacman.scale(0.04);
			pacman.move(0, -boxsize);
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN && isCollidable(pacX, pacY + 1)) {
			remove(pacman);
			pacman = new GImage("pacmanD.png", pacman.getX(), pacman.getY());
			add(pacman);
			pacman.scale(0.04);
			pacman.move(0, boxsize);
		}
	}

	public void parseFile(File gridFile) {
		try {
			Scanner sc = new Scanner(gridFile);
			int rowNum = 0;
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				System.out.println(line);
				char[] sepLine = line.toCharArray();
				for(int i = 0; i < sepLine.length; i++) {
					char2D[i][rowNum] = sepLine[i];
				}
				rowNum++;
			}
		}
		catch(IOException e) {
			System.out.println("File not Found.");
		}
	}

}