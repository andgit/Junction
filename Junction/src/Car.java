import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;


public class Car {

	private int x, y;
	private int width, height;
	private int boundingWidth;
	private int boundingHeight;
	private int boundingDimensions[];
	private boolean isCollision;
	private int speed;
	private final int maxSpeed; 
	private Color color;
	private TrafficLights assignedTrafficLights;
	private boolean stop;		// car stop
	private long speedTimer;
	public boolean goLeft = false;
	public boolean goRight = false;
	public boolean lane; //pas 0-lewy, 1-prawy-wtedy wiem w ktora strone moze skrecac
	public boolean changeDirection;//czy auto ma potem skrecic
	
	public Car(int x, int y, int width, int height, int speed, int maxSpeed, Color color, TrafficLights assignedTrafficLights, boolean lane,  boolean changeDirectionCar) {
        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.boundingWidth = 2 * this.width;
        this.boundingHeight = 2 * this.height;
        this.boundingDimensions = new int[4];
        this.boundingDimensions[0] = x;
        this.boundingDimensions[1] = y-(this.boundingHeight/4);
        this.boundingDimensions[2] = width;
        this.boundingDimensions[3] = height+this.boundingHeight;
        this.isCollision = false;
        this.speed = speed;
        this.maxSpeed = maxSpeed;
        this.color = color;
        this.assignedTrafficLights = assignedTrafficLights;
        this.stop = false;
        this.speedTimer = System.currentTimeMillis();
        this.lane = lane;
        this.changeDirection = changeDirectionCar;
        System.currentTimeMillis();
    }

    public void actionPerformed() {
    	
    	if(GlobalVariables.IS_PAUSED == false) {
    		
    		go();
	    	
	    	/*if(y <= 0 || y >= GlobalVariables.SCREEN_WIDTH) {
	    		
	    		y = 600;
	    	} else if (x <= 0 || x >= GlobalVariables.SCREEN_HEIGHT) {
	    		
	    		x = 500;
	    	} else*/ if(assignedTrafficLights.currentColor() == TrafficLights.TrafficLightsColors.RED) {
	    		
	    		if(y < 330 && y > 310) {//car`ll stop before red light  
	    			
	    			this.stop = true;
	    		}
	    	} else {
	    		
	    		this.stop = false;
	    	}
	    	
	    	updateSpeed();//prawdopodobnie to moze zostac-nie psuje:)
    	}
    }

	private void go() {
		
		if(this.stop == false) {
			
			if(goRight) {
				
				x += speed;
			} else if(goLeft) {
				
				x -= speed;
			} else {
				
				y -= speed;
			}
		}
	}
    
    public void paintComponent(Graphics g) {
    	
		/*g.setColor(color);
		g.fillRect(x, y, width, height);
		updateBoundingDimensions();*/
		
        Graphics2D g2 = (Graphics2D) g;
        //angle += 0.001;
        //g2.rotate(angle, rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);
        g2.setColor(color);
        
        if(y>480 && y<490 && changeDirection && lane) {//czy auto ma skrecic w prawo-musi byc na prawym pasie
        	//g2.rotate(Math.toRadians(90));
        	if(goRight ==false) {
        		
        		goRight = true;
	        	int temp=width;
	        	width = height;
	        	height = temp;
	        	x = x + 20;//po to zeby nie bylo kolizji z tym co jedzie do gory-to nie jest uniwersalne
        	}
        	//this.width = 50;
        	//this.height = 100;
        }
        
        if(y > 240 && y < 250 && changeDirection && lane ==false) {//czy auto ma skrecic w lewo-musi byc na lewym pasie
        	if(goLeft ==false) {
        		
        		goLeft = true;
	        	int temp=width;
	        	width = height;
	        	height = temp;
	        	x = x - 20;//po to zeby nie bylo kolizji z tym co jedzie do gory-to nie jest uniwersalne
        	}
        }
        
        g2.fillRect(x, y, width, height);
        updateBoundingDimensions();
        
        Debug.doDrawing((Graphics2D) g, " x= " + x + " y= " + y + " cdirec= " + changeDirection, Color.RED, x, y);
        //g2.dispose();
		
		if(GlobalVariables.ENABLE_DEBUG)
		{
			if(this.isCollision) {
				
				g.setColor(Color.RED);
			} else {
				
				g.setColor(Color.BLUE);
			}
			//g.drawRect(x, y-(this.boundingHeight/2), width, height+this.boundingHeight);
			g.drawRect(this.boundingDimensions[0], this.boundingDimensions[1], this.boundingDimensions[2], this.boundingDimensions[3]);
			
			g.fillOval(500, 250, 20, 20);
			g.setColor(Color.blue);
			g.fillOval(500, 330, 20, 20);
		}
    }
    
	protected Rectangle getBounds() {
		
		return new Rectangle(this.boundingDimensions[0], this.boundingDimensions[1], this.boundingDimensions[2], this.boundingDimensions[3]);
	}
    
	public boolean collision(Car car) {
		
		return this.isCollision = getBounds().intersects(car.getBounds());
	}
	
	public void accelarate() {
		
		if(this.speed < this.maxSpeed) {
			
			this.speed += 1;
		}
	}
	
	public void slowDown() {
		
		if(this.speed > 1) {
			
			this.speed -= 1;
		}
	}
	
	private void updateSpeed() {
		
		if(System.currentTimeMillis() - this.speedTimer > 4000 && this.isCollision == false) {
			
			this.speed = GlobalVariables.randInt(1, this.maxSpeed);
		}
	}
	
    public void speed(int speed) {
    	
    	this.speed = speed;
    }
    
    public int speed() {
    	
    	return this.speed;
    }
	
    private void updateBoundingDimensions() {
    	
    	if(goLeft || goRight) {
    		
            this.boundingDimensions[0] = x-(this.boundingWidth/2);
            this.boundingDimensions[1] = y;
            this.boundingDimensions[2] = width+this.boundingWidth;
            this.boundingDimensions[3] = height;   

    	} else {
    		
            this.boundingDimensions[0] = x;
            this.boundingDimensions[1] = y-(this.boundingHeight/2);
            this.boundingDimensions[2] = width;
            this.boundingDimensions[3] = height+this.boundingHeight; 		
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
    
    public void stop(boolean stop) {
    	
    	this.stop = stop;
    }
    
    public boolean stop() {
    	
    	return this.stop;
    }
    
    public Color color() {
    	
    	return this.color;
    }
    
    public int maxSpeed() {
    	
    	return this.maxSpeed;
    }
}
