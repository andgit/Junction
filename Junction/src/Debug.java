import java.awt.Color;
import java.awt.Graphics2D;

public class Debug {

    public static void doDrawing(Graphics2D g2d, String message, Color color, float x, float y){

    	g2d.setPaint(color);
        g2d.drawString(message, x, y);
    }
}
