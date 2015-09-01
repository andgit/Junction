import java.awt.Color;
import java.awt.Image;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;

public class GlobalVariables {

	public static boolean IS_PAUSED = false;
	public static Image BACKGROUND_IMAGE = new ImageIcon("junction.jpg").getImage();
	public static final int SCREEN_WIDTH = BACKGROUND_IMAGE.getWidth(null);
	public static final int SCREEN_HEIGHT = BACKGROUND_IMAGE.getHeight(null);
	public static final int TIMER_DELAY = 30;
	public static final int TRAFFIC_LIGHT_RADIUS = 10;
	public static final int GREEN_ARROW_WIDTH = 10;
	public static final int GREEN_ARROW_HEIGHT = 5;
	public static boolean ENABLE_DEBUG = true;	// enable debug text bounding rects
	
	public static Color randColor() {
		
		int randColor = ThreadLocalRandom.current().nextInt(0, 5);
		Color randedColor = Color.ORANGE;
		switch(randColor) {
		
		case 0:
			randedColor = Color.RED;
			break;
		case 1:
			randedColor = Color.ORANGE;
			break;
		case 2:
			randedColor = Color.BLUE;
			break;
		case 3:
			randedColor = Color.YELLOW;
			break;
		case 4:
			randedColor = Color.GREEN;
			break;
			default:
				randedColor = Color.BLACK;
		}
		
		return randedColor;
	}
	
	public static int randInt(int origin, int bound) {
		
		return ThreadLocalRandom.current().nextInt(origin, bound);
	}
}
