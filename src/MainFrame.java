/**
	Copyright 2020 Dark Dead Dragon (ak LeonardoDarkVinchi)
	License by GNU GPLv3
*/

package DEstraction;
import DEstraction.*;

import javax.imageio.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.event.*;


public class MainFrame extends JFrame {
	public int menuModeWidth = 100;
	public int menuModeHeight = 160;
	public int gameModeWidth = 740;
	public int gameModeHeight = 640;
	private GameThread game;
	private MainFrame thisFrame;
	JButton gameButton;
	JButton optionsButton;
	JButton closeButton;
	JButton helpButton;
	GridBagLayout gameGridLayout;
	GridBagConstraints constraintsForGamePanel; 
	GridBagConstraints constraintsForMenuPanel; 
	GridBagConstraints constraintsForGSPanel; 
	FrameMove menuPanel;
	private HelpFrame helpWindow;
	public OptionsFrame optionsWindow;
	
	public MainFrame() {
		super("DEstraction");
		setResizable(false);
		setDefaultCloseOperation(3);
		setUndecorated(true);
		setSize(menuModeWidth, menuModeHeight);
		Point p = DisplayParams.getCenterMainScreen();
		setLocation(p.x, p.y);
		
		gameButton = new JButton("Старт");
		optionsButton = new JButton("Опции");
		helpButton = new JButton("Помощь");
		closeButton = new JButton("Выход");
		menuPanel = new FrameMove(this);
		
		helpWindow = new HelpFrame();
		optionsWindow = new OptionsFrame(this);
		thisFrame = this;
		
		menuPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 0));
		menuPanel.add(new JLabel("DEstraction"));
		menuPanel.add(gameButton);
		menuPanel.add(optionsButton);
		menuPanel.add(helpButton);
		menuPanel.add(closeButton);
		add(menuPanel);
		
		setConsttraints();
		setActionListeners();
		setCustomCursor(true);		
		
		setVisible(true);
	}
 
	private void setConsttraints() {	
		constraintsForMenuPanel = new GridBagConstraints();
		constraintsForMenuPanel.gridx = 0;
		constraintsForMenuPanel.gridwidth = 1; 
		constraintsForMenuPanel.gridy = 0; 
		constraintsForMenuPanel.gridheight = 1; 
		constraintsForMenuPanel.fill = GridBagConstraints.BOTH;
		constraintsForMenuPanel.weightx = 1.0;
		constraintsForMenuPanel.weighty = 1.0;
		
		constraintsForGamePanel = new GridBagConstraints();		
		constraintsForGamePanel.gridx = 1;
		constraintsForGamePanel.gridwidth = 1; 
		constraintsForGamePanel.gridheight = 3; 
		constraintsForGamePanel.gridy = 0; 
		constraintsForGamePanel.fill = GridBagConstraints.BOTH;
		constraintsForGamePanel.weightx = 1.0;
		constraintsForGamePanel.weighty = 1.0;
		
		constraintsForGSPanel = new GridBagConstraints();		
		constraintsForGSPanel.gridx = 0;
		constraintsForGSPanel.gridwidth = 1; 
		constraintsForGSPanel.gridheight = 2; 
		constraintsForGSPanel.gridy = 1; 
		constraintsForGSPanel.fill = GridBagConstraints.BOTH;
		constraintsForGSPanel.weightx = 1.0;
		constraintsForGSPanel.weighty = 1.0;
	}
	
	private void setActionListeners() {
		gameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				if (game == null)				
					try {
						game = new GameThread(thisFrame);
						changeWindow();
						game.start();
						setVisible(true);
						gameButton.setText("Стоп");
					}
					catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				else
					try {
						changeWindow();
						game.stopGame();
						game = null;
						setVisible(true);
						gameButton.setText("Старт");
					}
					catch (Exception ex) {}
			}
		});
		
		optionsButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				optionsWindow.setVisible(true);
			}
		});
		
		helpButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				helpWindow.increaseCount();	
				if (helpWindow.isVisible()) helpWindow.dispose();
				if (helpWindow.countOfOpening == 5) 
					JOptionPane.showMessageDialog(null,"Пять нажатий. Хорошо, хорошо. Тестируй дальше.", "Моё поздравление!", JOptionPane.INFORMATION_MESSAGE);
				if (helpWindow.countOfOpening == 10) 
					JOptionPane.showMessageDialog(null,"Слушай, помимо кнопки помощи в игре есть еще куда понажимать!", "Моё увлажнение!", JOptionPane.QUESTION_MESSAGE);
				if (helpWindow.countOfOpening >= 20 && helpWindow.countOfOpening < 25) 
					JOptionPane.showMessageDialog(null,"Слушай, если ты и дальше продолжишь насиловать кнопку помощи, она сломается!", "Моё предупреждение!", JOptionPane.WARNING_MESSAGE);
				if (helpWindow.countOfOpening == 25) {
					JOptionPane.showMessageDialog(null,"Все, ты ее сломал! С тебя 100$ на лечение!", "Моё осуждение!", JOptionPane.ERROR_MESSAGE );
				}
				if (helpWindow.countOfOpening < 25) helpWindow.setVisible(true);
				if (helpWindow.countOfOpening == 30) {
					thisFrame.setVisible(false);
					JOptionPane.showMessageDialog(null,"Ты ждал чуда? Зря зря. Теперь ты сломал игру.", "Моё недоумение!", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		});
		
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				try {
					if (game != null) game.stopGame();
					System.exit(0);
				}
				catch (Exception ex) {}
			}
		});
	}
	
	private void setCustomCursor(boolean isOn) {
		if (isOn) {
			Image myImage = null;
			try {
				myImage = ImageIO.read(getClass().getResource("img/cursor.gif"));
			} catch (Exception ex) {};
			if (myImage != null) {
				setCursor(Toolkit.getDefaultToolkit().createCustomCursor(myImage, new Point(0,0),""));
			}
		} else {
			setCursor(Cursor.getDefaultCursor());
		}
	}
	
	public void paramsChanged() {
		gameModeWidth = optionsWindow.windowWidth.getValue() + menuModeWidth;
		gameModeHeight = optionsWindow.windowHeight.getValue();
		setAlwaysOnTop(optionsWindow.onTopCheckBox.isSelected());
		setCustomCursor(optionsWindow.customCursorCheckBox.isSelected());
		if (game != null) {
			if (optionsWindow.fullSreenCheckBox.isSelected()) {
				Rectangle display = DisplayParams.getDefaultScreenBounds();
				gameModeWidth = display.width;
				gameModeHeight = display.height;
				setLocation(display.x, display.y);
				menuPanel.setMovable(false);
			} else {
				menuPanel.setMovable(true);
			}
			if (optionsWindow.fullMapSizeCheckBox.isSelected()) {
				gameModeWidth = game.mainMap.getMapWidth() + menuModeWidth;
				gameModeHeight = game.mainMap.getMapHeight();
			}
			setSize(gameModeWidth, gameModeHeight);
			setGridBagLayout();  
			game.drawPanel.setSize(gameModeWidth - menuModeWidth, gameModeHeight);
			game.drawPanel.checkOffset();
			game.gameSleepTime = (int)(100 / optionsWindow.gameSpeed);
			game.drawSleepTime = (int)(1000/ optionsWindow.frameRate);
		}
	}
	
	public void changeWindow() {
		dispose();
		if (getWidth() == menuModeWidth) 
			changeWindowToGameMode();
		else changeWindowToMenuMode();
	}
	
	private void changeWindowToMenuMode() {
		getContentPane().remove(menuPanel);
		setLayout(new GridLayout(0, 1, 0, 0));
		add(menuPanel);
		menuPanel.setMovable(true);
		setSize(menuModeWidth, menuModeHeight);
	}
	
	private void changeWindowToGameMode() {
		getContentPane().remove(menuPanel);
		gameGridLayout = new GridBagLayout();
		setLayout(gameGridLayout);
		if (optionsWindow.fullSreenCheckBox.isSelected()) {
			Rectangle display = DisplayParams.getDefaultScreenBounds();
			gameModeWidth = display.width;
			gameModeHeight = display.height;
			setLocation(display.x, display.y);
			menuPanel.setMovable(false);
		}
		if (optionsWindow.fullMapSizeCheckBox.isSelected()) {
			gameModeWidth = game.mainMap.getMapWidth() + menuModeWidth;
			gameModeHeight = game.mainMap.getMapHeight();
		}
		setSize(gameModeWidth, gameModeHeight);
		
		add(menuPanel, constraintsForMenuPanel);
		add(game.drawPanel, constraintsForGamePanel);	
		add(game.gameSupportPanel, constraintsForGSPanel);	
		
		setGridBagLayout();
	}
	
	private void setGridBagLayout() {
		gameGridLayout.columnWidths = new int[2];
		gameGridLayout.columnWidths[0] = menuModeWidth;
		gameGridLayout.columnWidths[1] = gameModeWidth - menuModeWidth;
		
		gameGridLayout.rowHeights = new int[3];
		gameGridLayout.rowHeights[0] = menuModeHeight;
		for (int i = 1; i < gameGridLayout.rowHeights.length; i++) {
			gameGridLayout.rowHeights[i] = (gameModeHeight - menuModeHeight)/(gameGridLayout.rowHeights.length - 1); 
		};
	}
}
