import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


public class Main extends JComponent {

	private Car firstCar;
	private TrafficLights firstTrafficLights;
	private static final long serialVersionUID = 1L;

	private void init() {
		
		firstTrafficLights = new TrafficLights(TrafficLights.TrafficLightsColors.GREEN, true, false, 600, 595);
		firstCar = new Car(500, 600, 20, 30, 2, firstTrafficLights);
	}
	
	public Main() {
		
		init();
		
		new Timer(GlobalVariables.TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actEvt) {
				
				firstCar.actionPerformed();
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
		firstCar.paintComponent(g);
		firstTrafficLights.paintComponent(g);
		
		if(GlobalVariables.ENABLE_DEBUG)
		{
	        String message = "H=" + Integer.toString(GlobalVariables.SCREEN_WIDTH) + " W=" + Integer.toString(GlobalVariables.SCREEN_HEIGHT);
	        Debug.doDrawing((Graphics2D) g, message, Color.RED, 20.f, 30.f);
	        message = "x=" + Integer.toString(firstCar.x()) + " y=" + Integer.toString(firstCar.y());
	        Debug.doDrawing((Graphics2D) g, message, Color.RED, 20.f, 50.f);
	        message = "PAUSED=" + GlobalVariables.IS_PAUSED;
	        Debug.doDrawing((Graphics2D) g, message, Color.RED, 20.f, 70.f);
		}
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

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				
				createAndShowGui();
			}
		});
	}
}