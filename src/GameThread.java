package DEstraction;
import DEstraction.*;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.text.*;

public class GameThread extends Thread{
	
	GameArena drawPanel;
	GameSupportPanel gameSupportPanel;
	MainFrame mainFrame;
	Map mainMap;
	InfoFrame infoWindow;
	Settlers settlers;	
	
	long gameStartTime;
	
	boolean isInside;
	byte gameStopped = 0;
	int choosenSettler = -13;
	int drawSleepTime = 17;
	int gameSleepTime = 100;
	
	Point startPoint;
	Point endPoint;	
	
	int choosenSettlers[];
	
	GameThread(MainFrame mainWindow) {		
		//game init
		mainMap = new Map(this);
		settlers = new Settlers(this);
		mainFrame = mainWindow;
		infoWindow = new InfoFrame();
		drawPanel = new GameArena(mainMap, settlers);
		gameSupportPanel = new GameSupportPanel(mainMap, settlers);
		settlers.transmitGameSupportPanel(gameSupportPanel);
		settlers.transmitMap(mainMap);
		
		//�������� �������. Java �� ����� �������� MouseInputListener ����� �������. 
		//������ ����� ���������� ��������. ��� ��� ���������� ������ ������ CustomListener
		//������ ���� �������� �� ���� ������� ���������, �� ����� ������� ������ �� ������,
		//� �������� ��� ������, ��� MouseListener � ��� MouseMotionListener.
		CustomListener cs = new CustomListener();
		drawPanel.addMouseListener(cs);				
		drawPanel.addMouseMotionListener(cs);
				
		mainFrame.add(drawPanel, mainFrame.constraintsForGamePanel);	
		mainFrame.add(gameSupportPanel, mainFrame.constraintsForGSPanel);		
		mainFrame.setVisible(true);
	}
	
	public void settlerDeathAlert(int settlerNumer){
		//System.out.println("Death Alert sitisen: " + settlerNumer);
		if (choosenSettler == settlerNumer) {
			choosenSettler = -13;
			gameSupportPanel.settlerUnchoosen();
			if (infoWindow.isVisible()) infoWindow.dispose();
		}
		if (choosenSettler > settlerNumer) choosenSettler--;
		if (settlers.getSettlersCount() == 0 && gameSupportPanel.food < 100) {
			long gameEndTime = System.currentTimeMillis();
			SimpleDateFormat formatter = new SimpleDateFormat("HH�: mm�: ss�");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			JOptionPane.showMessageDialog(null,"<html>���������...<br>������� " + formatter.format(gameEndTime - gameStartTime) + "</html>", "GameOver", JOptionPane.INFORMATION_MESSAGE);
			new Thread(()->{
				mainFrame.gameButton.doClick();
			}).start();	
		}
	}
	
	public void resourceEnded(){
		long gameEndTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("HH� mm� ss�");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		JOptionPane.showMessageDialog(null,"<html> �� ������� �� ������ � ���� ������ ��: "
										+ "<br>" + formatter.format(gameEndTime - gameStartTime)
										+"</html>"
										, "����������!!!", JOptionPane.INFORMATION_MESSAGE);
		new Thread(()->{
				mainFrame.gameButton.doClick();
			}, "GameOver").start();	
	}
	
	public void stopGame() {
		gameStopped = 4; 
		// �������� �������� ����� ������� ��������� ��� �������� ���������. 
		for (;(gameStopped & 3) != 3;) //���� ��� ������ �� �����������.
			try {
				Thread.sleep(100); } catch (Exception ex) {};
		mainFrame.getContentPane().remove(drawPanel);
		mainFrame.getContentPane().remove(gameSupportPanel);
		if (infoWindow.isVisible()) infoWindow.dispose();
		//������� �������� � ������� ������.
		//���� ��� ������ ��������� ���� �� �����, �� ��� ������������ ������, ��� ��������� ������ � ����� ��������
		//���������� �� ������ �� ��� ������� ������ �� ������. ����� GarbageCollector �� ������ ����������� �����
		//������ ��� ��� ������ �� ��� ������ ��� ��� ����� ��������.
		drawPanel = null;
		gameSupportPanel = null;
		mainMap = null;
		mainFrame = null;
		
	}
	
	public void run() {
		try {
			gameStartTime = System.currentTimeMillis();	
			new DrawThread().start();
			for (;(gameStopped & 4) != 4;){ //���� ��� ��������� �� �������� ��� ������� ������� stopGame
				long startTime= System.currentTimeMillis();	
				//��������� ������ ���������, ���� �� ������
				if (choosenSettler >= 0) {
					gameSupportPanel.settlerChoosen(choosenSettler); 
				}
				if (choosenSettlers != null) {
					gameSupportPanel.settlersChoosen(choosenSettlers.length); 
				}
				//��������� ���� ����������
				if (infoWindow.isVisible()) {
					if (isInside || infoWindow.isInside) {
						try {
							if (infoWindow.isAboutPerson) {
								String settlerInfo [] = settlers.getSettlerInfo(choosenSettler);
								infoWindow.setUnitInfo(settlerInfo[0], settlerInfo[1], settlerInfo[2], settlerInfo[3], settlerInfo[4]);
							} else {
								String cellInfo [] = mainMap.getCellInfo(infoWindow.getX() - drawPanel.getLocationOnScreen().x  - drawPanel.offsetX, 
																		infoWindow.getY() - drawPanel.getLocationOnScreen().y - drawPanel.offsetY);
								infoWindow.setResourceInfo(cellInfo[0], cellInfo[1]);
							}
						} catch (Exception ex) { infoWindow.dispose(); }
					} else infoWindow.dispose();
				}
				mainMap.processingMap();
				//System.out.print("-");
				settlers.processingSettlers();
				long passedTime = System.currentTimeMillis() - startTime;
				try { Thread.sleep((gameSleepTime > passedTime)? gameSleepTime - passedTime: 0); } catch (Exception ex) {};
			}
			gameStopped = (byte)(gameStopped | 1); //������ � ���������� ������� ������
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
			"<html>�������������� ��������������. <br>��������� �� ��������: " + ex.toString(), 
			"�� �� ��", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	private class CustomListener implements MouseInputListener{

		boolean isMiddlePressed = false;
			

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
			int mapX = e.getX() - drawPanel.offsetX;
			int mapY = e.getY() - drawPanel.offsetY;
			
			if (e.getButton() == 1 && !infoWindow.isVisible()) {
				startPoint = new Point(e.getX(), e.getY());
			}
			
			if (e.getButton() == 2) {
				isMiddlePressed = true;
				drawPanel.setPosition(new Point(mapX, mapY));
			}
		}

		public void mouseDragged(MouseEvent e){
			//System.out.println(e.getButton()); //���������� ����������
			if (startPoint != null) {
				endPoint = new Point(e.getX(), e.getY());
				drawPanel.setRectCoords(startPoint, endPoint);
			}
			
			if (isMiddlePressed) {
				System.out.println("Is middle " + isMiddlePressed);
				drawPanel.setOffset(e);
			}
		}
		
		public void mouseReleased(MouseEvent e) {			
			Point currentMousePoint = MouseInfo.getPointerInfo().getLocation();
			
			//���������� �� ���������. ������� ����� �������, ��� ��� ����� �� ��������� ������ ���������� ��������.
			//��� ������ �������� ���������� ������������ �� ���������. 
			//����� �� ������� �� ������� ��������, � ���� �� ����� �������������� �������� c �������� ������, �� ���� �� ��������.
			//������ ������� ��� ���� � ����� ������������ �������������� => ����� ������ ���� �������������.
			int mapX = e.getX() - drawPanel.offsetX;
			int mapY = e.getY() - drawPanel.offsetY;
			
			//JOptionPane.showMessageDialog(null,"Message " + e.getButton()/*+ e.getX() + " " + e.getY()*//*+ e.getSource()*/, "mouseClicked", JOptionPane.INFORMATION_MESSAGE);
			if (infoWindow.isVisible()) infoWindow.dispose();
			else {
				if (e.getButton() == 1){
					if (choosenSettler >= 0) {
						choosenSettler = -13;
						gameSupportPanel.settlerUnchoosen();
						settlers.unChooseAll();
					}
					if (choosenSettlers != null) {
						settlers.unChooseAll();
						choosenSettlers = null;
					}
					if (startPoint != null) {
						endPoint = new Point(e.getX(), e.getY());
						if (Math.abs(startPoint.x - endPoint.x) > 2 && Math.abs(startPoint.y - endPoint.y) > 2) {
							choosenSettlers = settlers.getSettlers(startPoint, endPoint);
							if (choosenSettlers != null)
								if (choosenSettlers.length == 1) {
									choosenSettler = choosenSettlers[0];
									choosenSettlers = null;
								}
						} else {
							int localChoosenSettler = settlers.isSettlerChoosen(mapX, mapY);
							if (localChoosenSettler < 0) {
								String cellInfo [] = mainMap.getCellInfo(mapX, mapY);
								infoWindow.setResourceInfo(cellInfo[0], cellInfo[1]);
								infoWindow.setLocation(currentMousePoint.x, currentMousePoint.y);
							}
							else {
								choosenSettler = localChoosenSettler;
								String settlerInfo [] = settlers.getSettlerInfo(localChoosenSettler);
								infoWindow.setUnitInfo(settlerInfo[0], settlerInfo[1], settlerInfo[2], settlerInfo[3], settlerInfo[4]);
								infoWindow.setLocation(currentMousePoint.x, currentMousePoint.y);
							}
						}
					}
					startPoint = null;
					endPoint = null;
					drawPanel.setRectCoords(startPoint, endPoint);
				}
				if (e.getButton() == 2) isMiddlePressed = false;
				if (e.getButton() == 3){
					//todo checkdestination on map
					if (choosenSettler >= 0) 
						if (e.isShiftDown()) {
							//System.out.println("next task setted");
							settlers.setNextDestination(choosenSettler, new Point(mapX, mapY), mainMap.isCellIsRes(mapX, mapY), false); 
						} else
							settlers.setDestination(choosenSettler, new Point(mapX, mapY), mainMap.isCellIsRes(mapX, mapY), false); 
					if (choosenSettlers != null)
						if (e.isShiftDown()) {
							for (int i : choosenSettlers)
								settlers.setNextDestination(i, new Point(mapX, mapY), mainMap.isCellIsRes(mapX, mapY), false);
						} else {
							for (int i : choosenSettlers)
								settlers.setDestination(i, new Point(mapX, mapY), mainMap.isCellIsRes(mapX, mapY), false);
						}
				}
				if (e.getButton() == 4){
					if (gameSupportPanel.food >= 100) {
						settlers.addNewSettler(mapX, mapY);
						gameSupportPanel.resourseChanged(3, -100);
					}
				}
			}
		}
	}

	private class DrawThread extends Thread{
		
		public void run() {
			for (;(gameStopped & 4) != 4;){ //���� ��� ��������� �� �������� ��� ������� ������� stopGame
				long startTime= System.currentTimeMillis();
				drawPanel.repaint();
				long passedTime = System.currentTimeMillis() - startTime;
				//System.out.println("passed: " + passedTime);
				try { Thread.sleep((drawSleepTime > passedTime)? drawSleepTime - passedTime: 0); } catch (Exception ex) {};
				//try { Thread.sleep(drawSleepTime); } catch (Exception ex) {};
			}
			gameStopped = (byte)(gameStopped | 2); //������ � ���������� ������� ������
		}			
	}
}
