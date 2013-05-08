package edu.mst.distopsysproj.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mst.distopsysproj.person.Location;

public class BridgeCrossingFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, Circle> circles;
	private PaintPanel paintPanel;
	
	static final int SPEED_MIN = 0;
	static final int SPEED_MAX = 30;
	static final int SPEED_INIT = 25;
	
	private int speed = SPEED_MAX - SPEED_INIT;

	public BridgeCrossingFrame(String[] circleIds) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 420);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Ricart-Agrawala's Protocol Test");
		
		circles = new HashMap<String, Circle>();
		int lengthOverTwo = circleIds.length/2;
		for (int i = 0; i < circleIds.length; i++) {			
			Circle circle = new Circle(circleIds[i]);
			if(i < lengthOverTwo){
				circle.setColor(Color.blue);
				circle.setX(0);
				circle.setPosition(Location.A);
			}else{
				circle.setColor(Color.red);
				circle.setX(550);
				circle.setPosition(Location.B);
			}
			
			circle.setY(100 + i*50);
			circles.put(circleIds[i], circle);
		}
		
		paintPanel = new PaintPanel();
		add(BorderLayout.CENTER, paintPanel);
		
		JPanel speedPanel = new JPanel(new BorderLayout(0, 5));
		
		JLabel speedLabel = new JLabel("Speed");
		speedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		speedPanel.add(BorderLayout.NORTH, speedLabel);
		
		JSlider speedSlider = new JSlider(JSlider.HORIZONTAL,
				SPEED_MIN, SPEED_MAX, SPEED_INIT);
		speedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int speed = (int) source.getValue();
					setSpeed(SPEED_MAX - speed);
					System.out.println("Speed set to " + speed);
				}
			}
		});
		
		speedSlider.setMajorTickSpacing(10);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedPanel.add(BorderLayout.SOUTH, speedSlider);
		
		add(BorderLayout.NORTH, speedPanel);
		setVisible(true);
	}
	
	public void setCircleToPosition(String id, Location location) {
		Circle circle = circles.get(id);
		if(circle.getPosition() != location){			
			if(location == Location.A) setToLeft(circle);
			else if(location == Location.BRIDGE) setToBridge(circle);
			else setToRight(circle);
			
			circle.setPosition(location);

			try { 
				Thread.sleep(80*speed);
			}catch (Exception ex) {}
		}
	}
	
	private void setToLeft(Circle circle) {
		for (int i = circle.getX(); i >= 0; i--){
			circle.setX(i);
			paintPanel.repaint();
			try { 
				Thread.sleep(speed);
			}catch (Exception ex) {}			
		}
	}
	
	private void setToBridge(Circle circle) {
		int x = circle.getX();
		while(x != 260){
			if(x > 260) circle.setX(--x); // if the circle is on the right, move it left
			else circle.setX(++x); // if the circle is on the left, move it right
			x = circle.getX();
			
			paintPanel.repaint();
			try { 
				Thread.sleep(speed);
			}catch (Exception ex) {}	
		}
	}
	
	private void setToRight(Circle circle) {
		for (int i = circle.getX(); i <= 550; i++){
			circle.setX(i);
			paintPanel.repaint();
			try { 
				Thread.sleep(speed);
			}catch (Exception ex) {}			
		}
	}
	
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	
	private class PaintPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		// called by the system when necessary to repaint()
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// clear the screen
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			// draw the circles
			for (Circle circle : circles.values()) {
				g.setColor(circle.getColor());
				g.fillOval(circle.getX(), circle.getY(), 40, 40);
			}
		}
	}

}
