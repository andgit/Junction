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
	private long generateNewCarTimer;
	private final int generatingCarDelta = 2000;
	private int turnedCarsOnLeftCounter = 0;//co 3 auto ma skrecac w lewo-to jest counter
	private int turnedCarsOnRightCounter = 0;//co 3 auto ma skrecac w prawo-to jest counter
	private int yOfNewCar = 650;

	int x=500,y=650, w=20, h=30;
	boolean rot = false;
	boolean allowedToChangeTheLane = true;
	
	private void init() {
		
		firstTrafficLights = new TrafficLights(TrafficLights.TrafficLightsColors.GREEN, true, false, 600, 595);
		cars = new ArrayList<Car>();
		cars.add(new Car(500, 100, 20, 30, 4, 6, Color.RED, firstTrafficLights, true, false, false));
		cars.add(new Car(500, 200, 20, 30, 4, 6, Color.YELLOW, firstTrafficLights,true, false, false));
		cars.add(new Car(500, 300, 20, 30, 6, 6, Color.GREEN, firstTrafficLights, true, false, false));
		cars.add(new Car(500, 400, 20, 30, 4, 6, Color.BLACK, firstTrafficLights, true, false, false));
		cars.add(new Car(500, 500, 20, 30, 6, 6, Color.PINK, firstTrafficLights, true, false, false));
		cars.add(new Car(500, 600, 20, 30, 5, 6, Color.ORANGE, firstTrafficLights, true, false, false));
		//cars.add(new Car(500, 400, 20, 30, 4, 6, Color.GREEN, firstTrafficLights));
		//cars.add(new Car(500, 500, 20, 30, 5, 6, Color.BLACK, firstTrafficLights));
		cars.add(new Car(470, 300, 20, 30, 3, 6, Color.RED, firstTrafficLights, false, false, false));
		cars.add(new Car(470, 600, 20, 30, 6, 6, Color.PINK, firstTrafficLights, false, false, false));
		generateNewCarTimer = System.currentTimeMillis();
		System.currentTimeMillis();
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
	
		handleCarList();
		
		for(Car c: cars) {
			
			if(c.sideLane) {
				if((c.x<550 || c.isRotateOnSideLane) && c.allowedToChangeTheLane) {
					++c.x;
				}
				if( (c.y>530 || (c.x>=670 && c.y>490)) && c.allowedToChangeTheLane) {
					--c.y;
				} else if(c.isRotateOnSideLane == false) {
					c.isRotateOnSideLane=true;
					int t=c.width;
					c.width=c.height;
					c.height=t;
				} else {
					for(Car insideCar: cars) {//czy nie bedzie kolizji jak auto wjezdza na pas
						if(insideCar.sideLane==false) {
							if(insideCar.y() < 510 && insideCar.y() > 490) {
							
								if(x > (insideCar.x()-50) || x < (insideCar.x()+50)) {
								
									c.allowedToChangeTheLane = true;
									//c.sideLane = false;
									c.goRight = true;
								}
							}
						}
					}
				}
			}
		}
		
		if((x<550 || rot) && allowedToChangeTheLane) {
			++x;
		}
		if( (y>530 || (x>=670 && y>490)) && allowedToChangeTheLane) {
			--y;
		} else if(rot == false) {
			rot=true;
			int t=w;
			w=h;
			h=t;
		} else {
			for(Car c: cars) {
				if(c.y() < 510 && c.y() > 490) {
				
					if(x > (c.x()-50) || x < (c.x()+50)) {
					
						allowedToChangeTheLane = true;
					}
				}
			}
		}
		//rotate rect test
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setPaint(new Color(150, 150, 0));
        g2d.fillRect(x, y, w, h);
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
					
					//if(cars.get(i).x() == cars.get(j).x()) {//trzeba sie jeszcze zastano
					if(cars.get(i).goLeft == false && cars.get(i).goRight == false) {
						
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
					//}
					} else {
						//nie jestem pewien czy to jest ok-chyba tak
						if(cars.get(i).x() <= cars.get(j).x()) {
							
							if(cars.get(i).collision(cars.get(j))) {
							
								if(Math.abs((cars.get(i).x() - cars.get(j).x())) <= cars.get(i).width()) {
									
									//int shift = Math.abs((cars.get(i).y() - cars.get(j).y()));
									cars.get(j).x(cars.get(j).x() + 20);
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
		
		for(int i=0;i<cars.size();++i) {//usuwanie aut z listy jak wyjdzie poza obszar
			
			if(cars.get(i).y() <= 10 || cars.get(i).x() >= 750 || cars.get(i).x() <= 10) {
				
				cars.remove(i);
			}
		}
		//dodawanie nowego auta
		boolean timerAllowedToGenerateNewCarOnRightLane = false;
		boolean timerAllowedToGenerateNewCarOnLeftLane = false;
		if(System.currentTimeMillis() - this.generateNewCarTimer > generatingCarDelta) {
			
			this.generateNewCarTimer = System.currentTimeMillis();
			timerAllowedToGenerateNewCarOnRightLane = true;
			timerAllowedToGenerateNewCarOnLeftLane = true;
		}
		if(timerAllowedToGenerateNewCarOnRightLane) {
			
			boolean noCarsInAreaOfCreatingNewCarOnRightLane = true;
			boolean noCarsInAreaOfCreatingNewCarOnLeftLane = true;
			for(int i=0;i<cars.size();++i) {
				
				if(cars.get(i).lane == true) {//sprawdzam prawy pas
					
					if(cars.get(i).y() < 700 && cars.get(i).y() > 500) {//czy sa jakies auta w miejscu gdzie maja powstac nowe
						
						noCarsInAreaOfCreatingNewCarOnRightLane = false;//wchodze wiec sa auta
						break;
					}
				}
			}
			for(int i=0;i<cars.size();++i) {
				
				if(cars.get(i).lane == false) {//sprawdzam lewy pas
					
					if(cars.get(i).y() < 700 && cars.get(i).y() > 500) {//czy sa jakies auta w miejscu gdzie maja powstac nowe
						
						noCarsInAreaOfCreatingNewCarOnLeftLane = false;//wchodze wiec sa auta
						break;
					}
				}
			}
			if(noCarsInAreaOfCreatingNewCarOnRightLane) {
				
				timerAllowedToGenerateNewCarOnRightLane = false;
				noCarsInAreaOfCreatingNewCarOnRightLane = true;
				//this.generateNewCarTimer = System.currentTimeMillis();//jeszcze przemyslec
				int randSpeed = GlobalVariables.randInt(3, 6);
				Color randColor = GlobalVariables.randColor();
				if(turnedCarsOnRightCounter==1) {
					
					++turnedCarsOnRightCounter;
					cars.add(new Car(500, yOfNewCar, 20, 30, randSpeed, 6, randColor, firstTrafficLights, true, true, false));
				} else if(turnedCarsOnRightCounter==2) {
					
					turnedCarsOnRightCounter = 0;
					cars.add(new Car(500, yOfNewCar, 20, 30, randSpeed, 6, randColor, firstTrafficLights, true, true, true));
				} else {
					
					++turnedCarsOnRightCounter;
					cars.add(new Car(500, yOfNewCar, 20, 30, randSpeed, 6, randColor, firstTrafficLights, true, false, false));
				}
			}
			if(noCarsInAreaOfCreatingNewCarOnLeftLane) {
				
				timerAllowedToGenerateNewCarOnLeftLane = false;
				noCarsInAreaOfCreatingNewCarOnLeftLane = true;
				int randSpeed = GlobalVariables.randInt(3, 6);
				Color randColor = GlobalVariables.randColor();
				if(turnedCarsOnLeftCounter==3) {
					
					turnedCarsOnLeftCounter=0;
					cars.add(new Car(470, yOfNewCar, 20, 30, randSpeed, 6, randColor, firstTrafficLights, false, true, false));
				} else {
					
					++turnedCarsOnLeftCounter;
					cars.add(new Car(470, yOfNewCar, 20, 30, randSpeed, 6, randColor, firstTrafficLights, false, false, false));
				}
				
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