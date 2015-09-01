import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


public class Main extends JComponent {

	ArrayList<Car> cars;
	private TrafficLights firstTrafficLights;
	private static final long serialVersionUID = 1L;
	int tempCounter = 0;
	private long generateNewCarTimer = 0;
	private final int generatingCarDelta = 2000;

	private void init() {
		
		firstTrafficLights = new TrafficLights(TrafficLights.TrafficLightsColors.GREEN, true, false, 600, 595);
		cars = new ArrayList<Car>();
		cars.add(new Car(500, 100, 20, 30, 2, 6, Color.RED, firstTrafficLights));
		cars.add(new Car(500, 200, 20, 30, 2, 6, Color.YELLOW, firstTrafficLights));
		cars.add(new Car(500, 300, 20, 30, 3, 6, Color.GREEN, firstTrafficLights));
		cars.add(new Car(500, 400, 20, 30, 3, 6, Color.BLACK, firstTrafficLights));
		cars.add(new Car(500, 500, 20, 30, 3, 6, Color.PINK, firstTrafficLights));
		cars.add(new Car(500, 600, 20, 30, 3, 6, Color.ORANGE, firstTrafficLights));
		//cars.add(new Car(500, 400, 20, 30, 4, 6, Color.GREEN, firstTrafficLights));
		//cars.add(new Car(500, 500, 20, 30, 5, 6, Color.BLACK, firstTrafficLights));
		//cars.add(new Car(470, 600, 20, 30, 6, 6, Color.RED, firstTrafficLights));
		//cars.add(new Car(470, 500, 20, 30, 4, 6, Color.PINK, firstTrafficLights));
		generateNewCarTimer = System.currentTimeMillis();
	}
	
	public Main() {
		
		init();
		
		new Timer(GlobalVariables.TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actEvt) {
				
				for(Car c: cars) {
					
					c.actionPerformed();
				}
				handleCollision();
				firstTrafficLights.actionPerformed();
				repaint();
			}
		}).start();
	}

	@Override
	public Dimension getPreferredSize() {
		
		return new Dimension(GlobalVariables.SCREEN_WIDTH, GlobalVariables.SCREEN_HEIGHT);
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(GlobalVariables.BACKGROUND_IMAGE, 0, 0, null);
		for(Car c: cars) {
			
			c.paintComponent(g);
		}
		firstTrafficLights.paintComponent(g);
		
		if(GlobalVariables.ENABLE_DEBUG)
		{
	        String message = "H=" + Integer.toString(GlobalVariables.SCREEN_WIDTH) + " W=" + Integer.toString(GlobalVariables.SCREEN_HEIGHT);
	        Debug.doDrawing((Graphics2D) g, message, Color.RED, 20.f, 30.f);
	        
	        float y = 50.f;
	        for(Car c: cars) {
	        	
		        message = "x=" + Integer.toString(c.x()) + " y=" + Integer.toString(c.y());
		        Debug.doDrawing((Graphics2D) g, message, Color.RED, 20.f, y);
		        y += 20;
	        }

	        Debug.doDrawing((Graphics2D) g, "" + tempCounter, Color.RED, 20.f, 90.f);
	        //message = "PAUSED=" + GlobalVariables.IS_PAUSED;
	        //Debug.doDrawing((Graphics2D) g, message, Color.RED, 20.f, 70.f);
		}
		//todo
		for(int i=0;i<cars.size();++i) {
			
			if(cars.get(i).y() <= 10) {
				
				int s = cars.get(i).speed();
				int ms = cars.get(i).maxSpeed();
				Color c = cars.get(i).color();
				cars.remove(i);
				//cars.add(new Car(500, 100, 20, 600, s, ms, c, firstTrafficLights));
			}
		}
	
		handleCarList();
	}

	private static void createAndShowGui() {
		
		Main mainPanel = new Main();

		JFrame frame = new JFrame("Junction");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	protected void handleCollision() {
		
		int carsSize = cars.size();
		if(carsSize > 1) {
			
			for(int i=0;i<carsSize;++i) {
				
				for(int j=i+1;j<carsSize;++j) {
					
					if(cars.get(i).x() == cars.get(j).x()) {//trzeba sie jeszcze zastano
						
						if(cars.get(i).y() <= cars.get(j).y()) {

							if(cars.get(i).collision(cars.get(j))) {
								
								if(Math.abs((cars.get(i).y() - cars.get(j).y())) <= cars.get(i).height()) {
									
									//int shift = Math.abs((cars.get(i).y() - cars.get(j).y()));
									cars.get(j).y(cars.get(j).y() + /*cars.get(i).height()*/ 20);
									++tempCounter;
									//cars.get(i).slowDown();
								}
								
								if(cars.get(i).stop()) {
									
									cars.get(j).stop(true);
								}
								if(cars.get(j).speed() > cars.get(i).speed()) {
									
									cars.get(j).speed(cars.get(i).speed());
								}
							} else {
								
								//cars.get(i).accelarate();		//it cause bug
							}
						}
					}
				}
			}
		}
	}
	
	protected void handleCarList() {
		
		if(this.generateNewCarTimer > generatingCarDelta) {
			
			this.generateNewCarTimer = System.currentTimeMillis();
			boolean genaretaNewCar = false;
			for(int i=0;i<cars.size();++i) {
				
				if(cars.get(i).x() == 500 && cars.get(i).y() < 700 && cars.get(i).y() > 500) {
					
					genaretaNewCar = true;
				}
			}
			if(genaretaNewCar == false) {
				
				int randSpeed = GlobalVariables.randInt(3, 6);
				Color randColor = GlobalVariables.randColor();
				cars.add(new Car(500, 600, 20, 30, randSpeed, 6, randColor, firstTrafficLights));
			}
		}
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				
				createAndShowGui();
			}
		});
	}
}