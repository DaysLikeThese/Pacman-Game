
import acm.graphics.GImage;
//import acm.graphics.GOval;


public class Ghost extends GImage /*GOval*/{
	private int ghostX;
	private int ghostY;
	private int boxSize;


	public Ghost(String name, double x, double y,/* int size,*/ int boxSize) {
		/*super(x, y, size, size);*/
		super(name, x, y);
		ghostX = (int) (x / boxSize);
		ghostY = (int) (y / boxSize);
		this.boxSize = boxSize;
	}

	public int getGhostX() {
		return ghostX;
	}

	public int getGhostY() {
		return ghostY;
	}

	public void move(String direction) {
		//switch case = bunch of ifs at once
		switch(direction) {
		case "right":
			move(boxSize, 0);
			ghostX++;
			break;
		case "left":
			move(-boxSize, 0);
			ghostX--;
			break;
		case "up":
			move(0, -boxSize);
			ghostY--;
			break;
		case "down":
			move(0, boxSize);
			ghostY++;
			break;
		default:
			move(0, 0);
			break;
		}
	}

	public void moveAwayFromWall(String wallLocation) {
		if(wallLocation == "left") {
			move(boxSize, 0);
		}
	}
}