/**
	Copyright 2020 Dark Dead Dragon (ak LeonardoDarkVinchi)
	License by GNU GPLv3
*/

package DEstraction;
import DEstraction.*;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.event.*;

public class InfoFrame extends JFrame {

	JLabel infoLabel;
	FrameMove mainPanel;
	boolean isInside;
	boolean isAboutPerson;
			
	public InfoFrame() {
		//setSize(100, 50);
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
				
		infoLabel = new JLabel("");
		mainPanel = new FrameMove(this);
		
		CustomListener cs = new CustomListener();
		mainPanel.addMouseListener(cs);				
		mainPanel.addMouseMotionListener(cs);
				
		mainPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 0));
		mainPanel.add(infoLabel);
		add(mainPanel);
		
		setType(Type.UTILITY);
	}
	
	public void setResourceInfo(String name, String count){
		isAboutPerson = false;
		infoLabel.setText("<html>" + name + "<br>Ресурс: " + count + "</html>");
		if (!isVisible()) {
			pack();
			setVisible(true);
		}
	}
		
	public void setUnitInfo(String name, String age, String state, String workingType, String workingSkill){
		isAboutPerson = true;
		float workSk = Float.parseFloat(workingSkill);
		infoLabel.setText("<html>Name: " + name
						+"<br>Age: " + age
						+"<br>Status: " + state
						+"<br>Last type of working: " + workingType
						+"<br>Skill of working: " + (int)workSk
						+"<br>Learning: " + (int)((workSk - (int)workSk)*100) + "%"
						+"</html>");
		if (!isVisible()) {
			pack();
			setVisible(true);
		}
	}
	
	private class CustomListener implements MouseInputListener{
		
		public void mouseMoved(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			isInside = true;
		}

		public void mouseExited(MouseEvent e) {
			isInside = false;
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent e){
		}
		
		public void mouseReleased(MouseEvent e) {			
		}
	}
 }
