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

public class PersonalMenu extends JFrame {

	GameThread gameThread;
	JLabel infoLabel;
	JCheckBox autoWork;
	JPanel mainPanel;
			
	public PersonalMenu(GameThread mainThread) {
		//setSize(100, 50);
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
		
		gameThread = mainThread;
		infoLabel = new JLabel("");
		mainPanel = new JPanel();
		autoWork = new JCheckBox("Автоуправление");
				
		autoWork.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				gameThread.settlerAutoWorkChanged(autoWork.isSelected());
			}
		});
		
		mainPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 0));
		mainPanel.add(infoLabel);
		mainPanel.add(autoWork);
		add(mainPanel);
		
		setType(Type.UTILITY);
	}
			
	public void setUnitInfo(String name, String autoWorkState){
		infoLabel.setText("<html>Name: " + name
						+"</html>");
		autoWork.setSelected(Boolean.parseBoolean(autoWorkState));
		if (!isVisible()) {
			pack();
			setVisible(true);
		}
	}
 }
