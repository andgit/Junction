import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class TrafficLights {

	public Pair positions[]; 
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
	private long redGreenLightsLightingTime;
	private long yellowLightLightingTime;
	private long elapsedPausedTime;
	
	public TrafficLights(TrafficLightsColors currentColor, boolean isAvailableGreenArrow, boolean isEnableGreenArrow, Pair positions[], long redGreenLightsLightingTime, long yellowLightLightingTime) {
		
		this.currentColor = currentColor;
		this.previousColor = currentColor;
		this.greenArrow = new GreenArrow(isAvailableGreenArrow, isEnableGreenArrow, positions[3].left, positions[3].right);
		this.positions = positions;
		this.redGreenLightsLightingTime = redGreenLightsLightingTime;
		this.yellowLightLightingTime = yellowLightLightingTime;
		
		init();
	}

    public void actionPerformed() {
    	
    	if(GlobalVariables.IS_PAUSED == false) {
    		
    		updateLightsTimersAndColors();
    	}
    }
    
    public void paintComponent(Graphics g) {
    	
    	//final int x = 585;
    	//int y = 590;
    	int currentPosition = 0;
    	String message = null;
    	
    	switch(currentColor) {
    	
	    	case RED:
	    		g.setColor(Color.RED);
	    		//y = 570;
	    		currentPosition = 0;
	    		message = "RED";
	    		break;
	    	case YELLOW:
	    		g.setColor(Color.YELLOW);
	    		//y = 580;
	    		currentPosition = 1;
	    		message = "YELLOW";
	    		break;
	    	case GREEN:
	    		g.setColor(Color.GREEN);
	    		message = "GREEN";
	    		currentPosition = 2;
	    		break;
	    		
			default:
				g.setColor(Color.GREEN);
				//y = 590;
				currentPosition = 2;
				break;
    	}
		
    	if(GlobalVariables.ENABLE_DEBUG)
		{
	        Debug.doDrawing((Graphics2D) g, message, g.getColor(), positions[0].left, positions[0].right);
	        
	        String greenArrowMessage = "";
	        if(greenArrow.isAvailable) {
	        	
	        	if(greenArrow.isEnable) {
	        		
	        		greenArrowMessage = "enable";
	        	} else {
	        		
	        		greenArrowMessage = "disable";
	        	}
	        } else {
	        	
	        	greenArrowMessage = "notAvailable";
	        }
	        Debug.doDrawing((Graphics2D) g, greenArrowMessage, g.getColor(), positions[3].left + 15, positions[3].right + 5);
		}
    	
		g.fillOval(positions[currentPosition].left, positions[currentPosition].right, GlobalVariables.TRAFFIC_LIGHT_RADIUS, GlobalVariables.TRAFFIC_LIGHT_RADIUS);
		
		greenArrow.paintComponent(g);
    }

	public void init() {
		
		lightsTimer = System.currentTimeMillis();
		elapsedPausedTime = 0;
	}
	
	private void updateLightsTimersAndColors() {
		
		if(greenArrow.isAvailable) {//if current color is red green arrow is enable 
		
			if(this.currentColor == TrafficLightsColors.RED) {
				
				greenArrow.isEnable = true;
			} else {
				
				greenArrow.isEnable = false;
			}
		}
		
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
	
	public boolean isGreenArrowAvailable() {
		
		return this.greenArrow.isAvailable;
	}
	public boolean isGreenArrowEnable() {
		
		return this.greenArrow.isEnable;
	}
}
