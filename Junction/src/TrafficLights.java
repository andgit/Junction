import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class TrafficLights {

	public enum TrafficLightsColors {RED, YELLOW, GREEN;}
	
	private class GreenArrow {
		
		private boolean isAvailable = false;
		private boolean isEnable = false;
		private final int x, y;
		
		public GreenArrow(boolean availableGreenArrow, boolean isEnableGreenArrow, int x, int y) {

			this.isAvailable = availableGreenArrow;
			this.isEnable = isEnableGreenArrow;
			this.x = x;
			this.y = y;
		}
		
		public void paintComponent(Graphics g) {
			
			if(this.isAvailable && this.isEnable) {
				
				g.setColor(Color.GREEN);
				g.fillRect(this.x, this.y, GlobalVariables.GREEN_ARROW_WIDTH, GlobalVariables.GREEN_ARROW_HEIGHT);
			}
		}
	}
	
	private TrafficLightsColors currentColor;
	private TrafficLightsColors previousColor; // red or green after yellow light
	private GreenArrow greenArrow;
	private long lightsTimer;
	private long redGreenLightsLightingTime = 2000;
	private long yellowLightLightingTime = 1000;
	private long elapsedPausedTime;
	
	public TrafficLights(TrafficLightsColors currentColor, boolean isAvailableGreenArrow, boolean isEnableGreenArrow, int greenArrowX, int greenArrowY) {
		
		this.currentColor = currentColor;
		this.previousColor = currentColor;
		this.greenArrow = new GreenArrow(isAvailableGreenArrow, isEnableGreenArrow, greenArrowX, greenArrowY);
		init();
	}

    public void actionPerformed() {
    	
    	if(GlobalVariables.IS_PAUSED == false) {
    		
    		updateLightsTimersAndColors();
    	}
    }
    
    public void paintComponent(Graphics g) {
    	
    	final int x = 585;
    	int y = 590;
    	String message = null;
    	
    	switch(currentColor) {
    	
	    	case RED:
	    		g.setColor(Color.RED);
	    		y = 570;
	    		message = "RED";
	    		break;
	    	case YELLOW:
	    		g.setColor(Color.YELLOW);
	    		y = 580;
	    		message = "YELLOW";
	    		break;
	    	case GREEN:
	    		g.setColor(Color.GREEN);
	    		message = "GREEN";
	    		break;
	    		
			default:
				g.setColor(Color.GREEN);
				y = 590;
				break;
    	}
		
    	if(GlobalVariables.ENABLE_DEBUG)
		{
	        Debug.doDrawing((Graphics2D) g, message, g.getColor(), 600.f, 580.f);
		}
    	
		g.fillOval(x, y, GlobalVariables.TRAFFIC_LIGHT_RADIUS, GlobalVariables.TRAFFIC_LIGHT_RADIUS);
		
		greenArrow.paintComponent(g);
    }

	public void init() {
		
		lightsTimer = System.currentTimeMillis();
		elapsedPausedTime = 0;
	}
	
	private void updateLightsTimersAndColors() {
		
		if(this.currentColor == TrafficLightsColors.GREEN || this.currentColor == TrafficLightsColors.RED) {
			
			if(System.currentTimeMillis() - this.lightsTimer >= this.redGreenLightsLightingTime) {
				
				this.previousColor = this.currentColor;
				this.currentColor = TrafficLightsColors.YELLOW;
				this.lightsTimer = System.currentTimeMillis();
			}
		} else if(this.currentColor == TrafficLightsColors.YELLOW) {
			
			if(System.currentTimeMillis() - this.lightsTimer >= this.yellowLightLightingTime) {
				
				if(this.previousColor == TrafficLightsColors.RED) {
					
					this.currentColor = TrafficLightsColors.GREEN;
				} else {
					
					this.currentColor = TrafficLightsColors.RED;
				}
				lightsTimer = System.currentTimeMillis();
			}
		}
	}
	
	public void pause() {	//Test it!
		
		if(elapsedPausedTime == 0) {
			
			elapsedPausedTime = System.currentTimeMillis();
		} else {
			
			lightsTimer -= elapsedPausedTime;
			elapsedPausedTime = 0;
		}
	}
	
	TrafficLightsColors currentColor() {
		
		return this.currentColor;
	}
}
