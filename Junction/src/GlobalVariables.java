import java.awt.Image;

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
}
