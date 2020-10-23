/**
	Copyright 2020 Dark Dead Dragon (ak LeonardoDarkVinchi)
	License by GNU GPLv3
*/

package DEstraction;
import DEstraction.*;

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.awt.image.*;

public class GameArena extends JPanel{ 
	
	private Map mainMap;
	Settlers settlers;
	Structures structures;
	
	private Point position;
	public int offsetX = 0;
	public int offsetY = 0;
	
	public int x1;
	public int x2;
	public int y1;
	public int y2;
	public boolean isRect;
	
	public int choosenSetlrs[];
	
	GameArena(Map transMap, Settlers transSettlers, Structures transStructures) {
		mainMap = transMap;
		settlers = transSettlers;
		structures = transStructures;
	}
	
	public void setPosition(Point p) {
		position = p;
	}
	
	public void setOffset(Point e) {
		offsetX = e.x - position.x;
		offsetY = e.y - position.y;
		
		checkOffset();
		//System.out.println(offsetX + " " + offsetY); //debug info
	}
	
	public void checkOffset() {
		//System.out.println("Checking offset: " + offsetX + " " + offsetY);
		if (offsetX < getWidth() - mainMap.getMapWidth()) offsetX = getWidth() - mainMap.getMapWidth();
		if (offsetY < getHeight() - mainMap.getMapHeight()) offsetY = getHeight() - mainMap.getMapHeight();
		if (offsetX > 0) offsetX = 0;
		if (offsetY > 0) offsetY = 0;
		
		// System.out.println("getHeight() - mainMap.getMapHeight(): " + (getHeight() - mainMap.getMapHeight()));
		// System.out.println("After check: " + offsetX + " " + offsetY);
	}
	
	public void setRectCoords(Point startPoint, Point endPoint) {
		if (startPoint != null && endPoint != null) {
			isRect = true;
			x1 = startPoint.x;
			x2 = endPoint.x;
			y1 = startPoint.y;
			y2 = endPoint.y;
		} else {
			isRect = false;
		}
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			
		if (getWidth() > mainMap.getMapWidth() || getHeight() > mainMap.getMapHeight()) {
			g2d.setColor(Colors.black);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
		Point offsetPoint = new Point(offsetX, offsetY);	
		mainMap.paintMap(g2d, offsetPoint);
		settlers.drawSettlers(g2d, offsetPoint);
		g2d.setColor(Colors.black);
		if (isRect) g2d.drawRect(Math.min(x1,x2), Math.min(y1,y2), Math.abs(x1-x2), Math.abs(y1-y2));
	}
	
}
