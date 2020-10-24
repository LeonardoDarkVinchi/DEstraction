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
import java.util.*;

public class OptionsFrame extends JFrame {
	
	MainFrame mainWindow;
	
	FrameMove mainPanel;	
	JButton okButton;
	JButton cancelButton;
	JTabbedPane tabbedPane;
	
	JLabel graphicOptions;
	JSlider windowWidth;
	JSlider windowHeight;
	JCheckBox onTopCheckBox;
	JCheckBox fullSreenCheckBox;
	JCheckBox fullMapSizeCheckBox;
	
	JLabel audioOptions;
	
	JLabel gameplayOptions;
	JSlider frameRateSlider;
	JSlider gameSpeedSlider;
	JCheckBox customCursorCheckBox;
	
	float gameSpeed = (float)1.0;
	int maxGameSpeed = 5;
	int frameRate = 60;
			
	public OptionsFrame(MainFrame parent) {
		setSize(500, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
		mainWindow = parent;
		
		mainPanel = new FrameMove(this);
		cancelButton = new JButton("Отмена");
		okButton = new JButton("Принять");
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT,
                                               JTabbedPane.SCROLL_TAB_LAYOUT);
											   
		addComponentsToPanel();
		addComponentsToTabs();
		add(mainPanel);
		
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				gameSpeed = (float)(gameSpeedSlider.getValue() / 2.0);
				frameRate = (frameRateSlider.getValue() > 0)?frameRateSlider.getValue():1;
				// try {
					// framePateSpinner.commitEdit();
				// } catch (Exception e) {}
				// frameRate = (Integer)framePateSpinner.getValue();
				mainWindow.paramsChanged();
				dispose();
			}
		});
		
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				dispose();
			}
		});
		
		
		setType(Type.UTILITY);
	}
	
	private void addComponentsToPanel() {	
		int buttonWidth = 100;
		int buttonHeight = 30;
	
		GridBagLayout gbl = new GridBagLayout();
		mainPanel.setLayout(gbl);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridwidth = 3; 
		constraints.gridy = 0; 
		constraints.gridheight = 1; 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		mainPanel.add(tabbedPane, constraints);
		
		constraints.gridx = 1;
		constraints.gridwidth = 1; 
		constraints.gridy = 1; 
		mainPanel.add(okButton, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 1; 
		mainPanel.add(cancelButton, constraints);
		
		gbl.columnWidths = new int[3];
		gbl.columnWidths[0] = getWidth() - buttonWidth * 2;
		gbl.columnWidths[1] =  gbl.columnWidths[2] = buttonWidth;
		
		gbl.rowHeights = new int[2];
		gbl.rowHeights[0] = getHeight() - buttonHeight;
		gbl.rowHeights[1] = buttonHeight;
	}
	
	private void addComponentsToTabs() {
		int maxScreenWidth = DisplayParams.getDefaultScreenBounds().width;
		int maxScreenHeigth = DisplayParams.getDefaultScreenBounds().height;
		FrameMove graphicPanel = new FrameMove(this);
		graphicOptions = new JLabel("Графические опции");
		graphicPanel.setLayout(new java.awt.GridLayout(0, 2, 0, 0));
		graphicPanel.add(graphicOptions);
		graphicPanel.add(new JLabel(""));
		graphicPanel.add(new Label("Ширина игрового поля"));
		windowWidth = new JSlider(320, maxScreenWidth, mainWindow.gameModeWidth - mainWindow.menuModeWidth);
		windowWidth.setMajorTickSpacing((int)((maxScreenWidth - 640) / 4));
        windowWidth.setMinorTickSpacing(10);
		windowWidth.setSnapToTicks(true);
		windowWidth.setPaintTicks(true);
        windowWidth.setPaintLabels(true);
		graphicPanel.add(windowWidth);
		graphicPanel.add(new Label("Высота игрового поля"));
		windowHeight = new JSlider(320, maxScreenHeigth, mainWindow.gameModeHeight);
		windowHeight.setMajorTickSpacing((int)((maxScreenHeigth - 640) / 4));
        windowHeight.setMinorTickSpacing(10);
		windowHeight.setSnapToTicks(true);
		windowHeight.setPaintTicks(true);
        windowHeight.setPaintLabels(true);
		graphicPanel.add(windowHeight);
		onTopCheckBox = new JCheckBox("Окно игры поверх других окон");
		graphicPanel.add(onTopCheckBox);
		
		fullSreenCheckBox = new JCheckBox("Окно на весь экран");
		graphicPanel.add(fullSreenCheckBox);
		fullSreenCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (fullSreenCheckBox.isSelected()) {
					onTopCheckBox.setSelected(true);
					fullMapSizeCheckBox.setSelected(false);
				}
				fullMapSizeCheckBox.setEnabled(!fullSreenCheckBox.isSelected());
				onTopCheckBox.setEnabled(!fullSreenCheckBox.isSelected());
				windowHeight.setEnabled(!fullSreenCheckBox.isSelected());
				windowWidth.setEnabled(!fullSreenCheckBox.isSelected());
			}
		});
		
		fullMapSizeCheckBox = new JCheckBox("Окно по размеру карты");
		graphicPanel.add(fullMapSizeCheckBox);
		fullMapSizeCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (fullMapSizeCheckBox.isSelected()) {
					fullSreenCheckBox.setSelected(false);
				}
				fullSreenCheckBox.setEnabled(!fullMapSizeCheckBox.isSelected());
				windowHeight.setEnabled(!fullMapSizeCheckBox.isSelected());
				windowWidth.setEnabled(!fullMapSizeCheckBox.isSelected());
			}
		});
		
		tabbedPane.addTab("Графика", null, graphicPanel, "Графические настройки");
		
		FrameMove audioPanel = new FrameMove(this);
		audioOptions = new JLabel("Аудио опции");
		audioPanel.setLayout(new java.awt.GridLayout(0, 2, 0, 0));
		audioPanel.add(audioOptions);
		audioPanel.add(new JLabel(""));
		tabbedPane.addTab("Аудио", null, audioPanel, "Аудио настройки");
		
		FrameMove gameplayPanel = new FrameMove(this);
		gameplayOptions = new JLabel("Геймплейные опции");
		gameplayPanel.setLayout(new java.awt.GridLayout(0, 2));
		gameplayPanel.add(gameplayOptions);
		gameplayPanel.add(new JLabel(""));
		gameplayPanel.add(new JLabel("Частота кадров"));
		frameRateSlider = new JSlider(0, 120, frameRate);
		frameRateSlider.setMajorTickSpacing(30);
		frameRateSlider.setSnapToTicks(true);
		frameRateSlider.setPaintTicks(true);
		frameRateSlider.setPaintLabels(true);
		gameplayPanel.add(frameRateSlider);
		gameplayPanel.add(new JLabel("Скорость игры"));
		gameSpeedSlider = new JSlider(1, maxGameSpeed * 2, (int)(gameSpeed * 2));
		gameSpeedSlider.setMajorTickSpacing(1);
		gameSpeedSlider.setSnapToTicks(true);
		Dictionary<Integer, JLabel> specialLabels = new Hashtable<>();
		  for (int i = 1; i <= maxGameSpeed * 2; i ++) {
			 specialLabels.put(i, new JLabel(String.format("%1.1f", i / 2.0)));
		  }
		gameSpeedSlider.setLabelTable(specialLabels);
		gameSpeedSlider.setPaintTicks(true);
		gameSpeedSlider.setPaintLabels(true);
		gameplayPanel.add(gameSpeedSlider);
		customCursorCheckBox = new JCheckBox("Кастомный курсор");
		customCursorCheckBox.setSelected(true);
		gameplayPanel.add(customCursorCheckBox);
				
		tabbedPane.addTab("Игровые", gameplayPanel);
	}
	
 }
