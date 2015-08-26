import java.awt.Color;
import java.awt.Graphics;


public class Car {

	private int x, y;
	private int width, height;
	private int boundingHeight;
	private int speed;
	private TrafficLights assignedTrafficLights;
	private boolean stop;		// car stop
	
    public Car(int x, int y, int width, int height, int speed, TrafficLights assignedTrafficLights) {
        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.boundingHeight = 2 * this.height;
        this.speed = speed;
        this.assignedTrafficLights = assignedTrafficLights;
        this.stop = false;
    }

    public void actionPerformed() {
    	
    	if(GlobalVariables.IS_PAUSED == false) {
    		
    		if(this.stop == false) {
    			
    			y -= speed;
    		}
	    	
	    	if(y <= 0 || y >= GlobalVariables.SCREEN_WIDTH) {
	    		
	    		y = 600;
	    	} else if (x <= 0 || x >= GlobalVariables.SCREEN_HEIGHT) {
	    		
	    		x = 500;
	    	} else if(assignedTrafficLights.currentColor() == TrafficLights.TrafficLightsColors.RED) {
	    		
	    		if(y < 330 && y > 310) {//car`ll stop before red light  
	    			
	    			this.stop = true;
	    		}
	    	} else {
	    		
	    		this.stop = false;
	    	}
    	}
    }
    
    public void paintComponent(Graphics g) {
    	
		g.setColor(Color.red);
		g.fillRect(x, y, width, height);
		
		if(GlobalVariables.ENABLE_DEBUG)
		{
			g.setColor(Color.blue);
			g.drawRect(x, y-this.height, width, height+this.boundingHeight);
			
			g.fillOval(500, 310, 20, 20);
			g.setColor(Color.blue);
			g.fillOval(500, 330, 20, 20);
		}
    }
    
    /*public boolean isHit(float x, float y) {
        return getBounds2D().contains(x, y);
    }*/

    public void x(int x) {
        
        this.x = x;
    }

    public void y(int y) {
        
        this.y = y;
    }
    
    public int x() {
    	
    	return x;
    }

    public int y() {
    	
    	return y;
    }
    
    public int width() {
    	
    	return this.width;
    }
    
    public int height() {
    	
    	return this.height;
    }
}
