package pl.com.mat.letterrecognizer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.*;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	
	private String letter = "";
	private Graphics2D graph;
	private BufferedImage img;
	
	private int pX, pY;

	public DrawPanel() {
		super();
		img = new BufferedImage(262, 399, BufferedImage.TYPE_INT_RGB);
		graph = img.createGraphics();
		graph.setColor(Color.WHITE);
		graph.fillRect(0,0,262,399);
		graph.setColor(Color.BLACK);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(img, 0, 0, null);
		g2d.setFont(new Font("Verdana", Font.BOLD, 300));
		g2d.drawString(letter, 15, 310);
	}
	
	public void clearImage() {
		letter = "";
		graph.setColor(Color.WHITE);
		graph.fillRect(0,0,img.getWidth(),img.getHeight());
		graph.setColor(Color.BLACK);
		repaint();
	}
	
	public void drawPoint(int x, int y) {
		pX = x;
		pY = y;
		graph.fillRect(x, y, 1, 1);
		repaint();
	}
	
	public void draw(int x, int y) {
		graph.drawLine(pX, pY, x, y);
		repaint();
		pX = x;
		pY = y;
	}
	
	public void drawLetter(String let) {
		letter = let;
		repaint();
	}
	
	public BufferedImage getImage() {
		return img;
	}
}
