package DEstraction;
import DEstraction.*;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;


public class MainFrame extends JFrame {
	private int menuModeWidth = 100;
	private int menuModeHeight = 160;
	private int gameModeWidth = 740;
	private int gameModeHeight = 640;
	private GameThread game;
	private MainFrame thisFrame;
	JButton gameButton;
	JButton optionsButton;
	JButton closeButton;
	JButton helpButton;
	GridBagConstraints constraintsForGamePanel; 
	GridBagConstraints constraintsForMenuPanel; 
	GridBagConstraints constraintsForGSPanel; 
	FrameMove menuPanel;
	private HelpFrame helpWindow;
	
	public MainFrame() {
		super("DEstraction");
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
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
		
		setVisible(true);
	}
 
	private void setConsttraints(){	
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
	
	private void setActionListeners(){
		gameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				if (game == null)				
					try {
						changeWindow();
						game = new GameThread(thisFrame);
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
				JOptionPane.showMessageDialog(null,"<html>Тут у нас куча опций.<br>Может быть будут когда-нибудь.</html>", "Это была кнопка остановки игры.", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		helpButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				helpWindow.increaseCount();	
				if (helpWindow.isVisible()) helpWindow.dispose();
				if (helpWindow.countOfOpening == 5) 
					JOptionPane.showMessageDialog(null,"Пять нажатий. Хорошо, хорошо. Тестируй дальше.", "Моё поздравление!", JOptionPane.INFORMATION_MESSAGE);
				if (helpWindow.countOfOpening == 10) 
					JOptionPane.showMessageDialog(null,"Так, я сейчас в непонятках. Ты тестируешь или ты на столько тупой, что не можешь запомнить самые базовые клавиши?", "Моё увлажнение!", JOptionPane.QUESTION_MESSAGE);
				if (helpWindow.countOfOpening >= 20 && helpWindow.countOfOpening < 30) 
					JOptionPane.showMessageDialog(null,"Слушай, если ты и дальше продолжишь насиловать кнопку помощи, она сломается!", "Моё предупреждение!", JOptionPane.WARNING_MESSAGE);
				if (helpWindow.countOfOpening == 30) {
					JOptionPane.showMessageDialog(null,"Все, ты ее сломал. Не забудь доложить кучу!", "Моё осуждение!", JOptionPane.ERROR_MESSAGE );
				}
				if (helpWindow.countOfOpening < 30) helpWindow.setVisible(true);
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
	
	public void changeWindow() {
		dispose();
		if (getWidth() == menuModeWidth) 
			changeWindowToGameMode();
		else changeWindowToMenuMode();
		//setVisible(true);
	}
	
	private void changeWindowToMenuMode() {
		getContentPane().remove(menuPanel);
		setLayout(new GridLayout(0, 1, 0, 0));
		add(menuPanel);
		setSize(menuModeWidth, menuModeHeight);
	}
	
	private void changeWindowToGameMode() {
		getContentPane().remove(menuPanel);
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		setSize(gameModeWidth, gameModeHeight);
		add(menuPanel, constraintsForMenuPanel);
				
		gbl.columnWidths = new int[2];
		gbl.columnWidths[0] = menuModeWidth;
		gbl.columnWidths[1] = gameModeWidth - menuModeWidth;
		// for (int i = 1; i < gbl.columnWidths.length; i++) {
			// gbl.columnWidths[i] = (gameModeWidth - menuModeWidth)/gbl.columnWidths.length; 
		// };
		
		gbl.rowHeights = new int[3];
		gbl.rowHeights[0] = menuModeHeight;
		for (int i = 1; i < gbl.rowHeights.length; i++) {
			gbl.rowHeights[i] = (gameModeHeight - menuModeHeight)/(gbl.rowHeights.length - 1); 
		};
	}
}
