package edu.mst.distopsysproj.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.mst.distopsysproj.person.Location;

public class BridgeCrossingFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final int UPDATE_INTERVAL = 5;
	
	private HashMap<String, Circle> circles;
	private PaintPanel paintPanel;

	public BridgeCrossingFrame(String[] circleIds) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
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
				Thread.sleep(80*UPDATE_INTERVAL);
			}catch (Exception ex) {}
		}
	}
	
	private void setToLeft(Circle circle) {
		for (int i = circle.getX(); i >= 0; i--){
			circle.setX(i);
			paintPanel.repaint();
			try { 
				Thread.sleep(UPDATE_INTERVAL);
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
				Thread.sleep(UPDATE_INTERVAL);
			}catch (Exception ex) {}	
		}
	}
	
	private void setToRight(Circle circle) {
		for (int i = circle.getX(); i <= 550; i++){
			circle.setX(i);
			paintPanel.repaint();
			try { 
				Thread.sleep(UPDATE_INTERVAL);
			}catch (Exception ex) {}			
		}
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
