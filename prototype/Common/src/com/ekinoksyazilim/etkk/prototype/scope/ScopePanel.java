package com.ekinoksyazilim.etkk.prototype.scope;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ScopePanel extends JComponent {

	private int radius;
	private int centerX;
	private int centerY;
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, getWidth(), getHeight());

		g2d.setStroke(new BasicStroke(2));
		
		g2d.setColor(new Color(255, 0, 0, 100));
		drawSector(g2d, 37, 30);
		
		g2d.setColor(new Color(255, 0, 0, 255));
		drawTick(g2d, 37, radius);
		
		g2d.setColor(new Color(0, 255, 0, 100));
		drawSector(g2d, 251, 30);
		
		g2d.setColor(new Color(0, 255, 0, 255));
		drawTick(g2d, 251, radius);
		
		g2d.setColor(Color.yellow);
		g2d.drawOval(centerX - radius,  centerY - radius, radius * 2, radius * 2);
		
		for(int i = 0; i < 360; i += 10) {
			
			if( i % 90 == 0) {

				drawTick(g2d, i, 20);
				
			} else if( i % 30 == 0) {
				
				drawTick(g2d, i, 10);
				
			} else {
				
				drawTick(g2d, i, 5);
			}
			
			drawText(g2d, i, radius + 6, i + "");
		}
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {

		centerX = width / 2;
		centerY = height / 2;
		radius = Math.min(width, height) / 2 - 30;
		
		super.setBounds(x, y, width, height);
	}
	
	private void drawTick(Graphics2D g2d, double degree, int length) {
		
		double radians = Math.toRadians(degree - 90);
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		
		Line2D line = new Line2D.Double(
							centerX + radius * cos, 
							centerY + radius * sin, 
							centerX + (radius - length) * cos, 
							centerY + (radius - length) * sin);
		
		g2d.draw(line);
	}
	
	private void drawSector(Graphics2D g2d, double degree, double span) {
		
		degree = 360 - degree + 90;
		
		g2d.fillArc(
				centerX - radius, 
				centerY - radius, 
				radius * 2, 
				radius * 2, 
				round(degree - span / 2), 
				round(span));
	}
	
	private void drawText(Graphics2D g2d, double degree, double radius, String text) {
		
		double radians = Math.toRadians(degree - 90);
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		
		double x = centerX + radius * cos; 
		double y = centerY + radius * sin;
		
		Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(text, g2d);
		
		double offsetX = (Math.abs(((degree + 90) % 360) - 180) / 180.0) * -bounds.getWidth();
		double offsetY = (1 - Math.abs(degree - 180) / 180.0) * bounds.getHeight() - 2;
		
		g2d.drawString(text, round(x + offsetX), round(y + offsetY));
	}
	
	private int round(double value) {
		
		return (int) (value > 0 ? value + 0.5 : value - 0.5);
	}
	
	public static void main(String[] args) {

		ScopePanel scope = new ScopePanel();
		scope.setBackground(Color.black);
		
		JFrame frame = new JFrame("J2D Scope Demo");
		frame.setContentPane(scope);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
