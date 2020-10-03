/**
	Copyright 2020 Dark Dead Dragon (ak LeonardoDarkVinchi)
	License by GNU GPLv3
*/

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
	MainFrame mainWindow;
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
	
	GameThread(MainFrame mainFrame) {		
		//game init
		mainMap = new Map(this);
		settlers = new Settlers(this);
		mainWindow = mainFrame;
		infoWindow = new InfoFrame();
		drawPanel = new GameArena(mainMap, settlers);
		gameSupportPanel = new GameSupportPanel(mainMap, settlers);
		settlers.transmitGameSupportPanel(gameSupportPanel);
		settlers.transmitMap(mainMap);
		
		gameSleepTime = (int)(100 / mainFrame.optionsWindow.gameSpeed);
		drawSleepTime = (int)(1000/ mainFrame.optionsWindow.frameRate);
		
		//Забавная история. Java не может добавить MouseInputListener сразу целиком. 
		//Только таким раздельным способом. Так как переменные внутри класса CustomListener
		//должны быть доступны из всех методов одинаково, то нужно создать объект из класса,
		//и передать его дважды, как MouseListener и как MouseMotionListener.
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
			SimpleDateFormat formatter = new SimpleDateFormat("HHч: mmм: ssс");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			JOptionPane.showMessageDialog(null,"<html>ПОТРАЧЕНО...<br>впустую " + formatter.format(gameEndTime - gameStartTime) + "</html>", "GameOver", JOptionPane.INFORMATION_MESSAGE);
			new Thread(()->{
				mainWindow.gameButton.doClick();
			}).start();	
		}
	}
	
	public void resourceEnded(){
		long gameEndTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("HHч mmм ssс");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		JOptionPane.showMessageDialog(null,"<html> Вы собрали всё дерево и весь камень за: "
										+ "<br>" + formatter.format(gameEndTime - gameStartTime)
										+"</html>"
										, "Поздравляю!!!", JOptionPane.INFORMATION_MESSAGE);
		gameStopped = (byte)(gameStopped | 4);
		new Thread(()->{
				mainWindow.gameButton.doClick();
			}, "GameOver").start();	
	}
	
	public void stopGame() {
		gameStopped = (byte)(gameStopped | 4); 
		// бинарные операции имеют меньший приоритет чем операции сравнения. 
		for (;(gameStopped & 3) != 3;) //пока оба потока не остановятся.
			try {
				Thread.sleep(100); } catch (Exception ex) {};
		mainWindow.getContentPane().remove(drawPanel);
		mainWindow.getContentPane().remove(gameSupportPanel);
		if (infoWindow.isVisible()) infoWindow.dispose();
		//Решение проблемы с утечкой памяти.
		//Если два класса ссылаются друг на друга, то для освобождения памяти, при окончании работы с этими классами
		//необходимо из одного из них удалить ссылку на другой. Иначе GarbageCollector не станет освобождать место
		//потому что оба класса по его мнению все еще будут работать.
		drawPanel = null;
		gameSupportPanel = null;
		mainMap = null;
		mainWindow = null;
		
	}
	
	public void run() {
		try {
			gameStartTime = System.currentTimeMillis();	
			new DrawThread().start();
			for (;(gameStopped & 4) != 4;){ //пока бит остановки не появится при запуске функции stopGame
				long startTime= System.currentTimeMillis();	
				//обновляем панель персонажа, если он выбран
				if (choosenSettler >= 0) {
					gameSupportPanel.settlerChoosen(choosenSettler); 
				}
				if (choosenSettlers != null) {
					gameSupportPanel.settlersChoosen(choosenSettlers.length); 
				}
				//обновляем окно информации
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
			gameStopped = (byte)(gameStopped | 1); //сигнал о завершении первого потока
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
			"<html>Непредвиденные обстоятельства. <br>Сообщение от спонсора: " + ex.toString(), 
			"Ай яй яй", JOptionPane.ERROR_MESSAGE);
			
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
			//System.out.println(e.getButton()); //отладочная информация
			if (startPoint != null) {
				endPoint = new Point(e.getX(), e.getY());
				drawPanel.setRectCoords(startPoint, endPoint);
			}
			
			if (isMiddlePressed) {
				//System.out.println("Is middle " + isMiddlePressed);
				drawPanel.setOffset(new Point(e.getX(), e.getY()));
			}
		}
		
		public void mouseReleased(MouseEvent e) {			
			Point currentMousePoint = MouseInfo.getPointerInfo().getLocation();
			
			//Координаты со смещением. Опытным путем выяснил, что при вводе из координат должны вычитаться смещения.
			//При выводе наоборот координаты складываются со смещением. 
			//Опять же зависит от функции смещения, и если бы здесь использовалось смещение c обратным знаком, то было бы наоборот.
			//Просто запомни что ввод и вывод диаметрально противоположны => знаки должны быть инвертированы.
			int mapX = e.getX() - drawPanel.offsetX;
			int mapY = e.getY() - drawPanel.offsetY;
			
			isMiddlePressed = false;
			//JOptionPane.showMessageDialog(null,"Message " + e.getButton()/*+ e.getX() + " " + e.getY()*//*+ e.getSource()*/, "mouseClicked", JOptionPane.INFORMATION_MESSAGE);
			if (infoWindow.isVisible()) infoWindow.dispose();
			else {
				if (e.getButton() == 1){
					if (startPoint != null) {
						startPoint = new Point(startPoint.x - drawPanel.offsetX, startPoint.y - drawPanel.offsetY);
						endPoint = new Point(mapX, mapY);
						if (!e.isShiftDown()) {
							gameSupportPanel.settlerUnchoosen();
							if (choosenSettler >= 0) {
								settlers.unChooseOne(choosenSettler);
								choosenSettler = -13;
							}
							if (choosenSettlers != null) {
								settlers.unChooseAll();
								choosenSettlers = null;
							}
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
								} else {
									choosenSettler = localChoosenSettler;
									String settlerInfo [] = settlers.getSettlerInfo(localChoosenSettler);
									infoWindow.setUnitInfo(settlerInfo[0], settlerInfo[1], settlerInfo[2], settlerInfo[3], settlerInfo[4]);
									infoWindow.setLocation(currentMousePoint.x, currentMousePoint.y);
								}
							}
						} else { //isShiftDown
							if (Math.abs(startPoint.x - endPoint.x) > 2 && Math.abs(startPoint.y - endPoint.y) > 2) {
								int [] newSettlersChoosen = settlers.getSettlers(startPoint, endPoint);
								//если в выделение хоть кто-то попал, то 
								if (newSettlersChoosen != null) {								
									if (choosenSettlers != null) {
										//Сравниваем массивы
										for (int i = 0; i < newSettlersChoosen.length; i++)
											for (int j = 0; j < choosenSettlers.length; j++)
												if (newSettlersChoosen[i] == choosenSettlers[j]) {
													for(int k = i; k < newSettlersChoosen.length - 1; k++)
														newSettlersChoosen[k] = newSettlersChoosen[k+1];
													newSettlersChoosen = Arrays.copyOf(newSettlersChoosen, newSettlersChoosen.length - 1);
													i--;
													break;
												}
										choosenSettlers = Arrays.copyOf(choosenSettlers, choosenSettlers.length + newSettlersChoosen.length);
										System.arraycopy(newSettlersChoosen, 0, choosenSettlers, choosenSettlers.length - newSettlersChoosen.length, newSettlersChoosen.length);
									} else { //Если никого раньше не было выбрано, выбираем тех что есть.
										if (choosenSettler < 0)
											choosenSettlers = newSettlersChoosen;
										else { //Если же был всего один, тогда запихиваем его в конец. Порядок номеров не важен.
											for (int i = 0; i < newSettlersChoosen.length; i++)
												if (newSettlersChoosen[i] == choosenSettler) {
													for(int k = i; k < newSettlersChoosen.length - 1; k++)
														newSettlersChoosen[k] = newSettlersChoosen[k+1];
													newSettlersChoosen = Arrays.copyOf(newSettlersChoosen, newSettlersChoosen.length - 1);
												}
											gameSupportPanel.settlerUnchoosen();
											choosenSettlers = Arrays.copyOf(newSettlersChoosen, newSettlersChoosen.length + 1);
											choosenSettlers[choosenSettlers.length - 1] = choosenSettler;
											choosenSettler = -13;
										}
									}
									//И в конце проверяем, что если в рамке всего один додик, то выбираем его как одного.
									if (choosenSettlers.length == 1) {
											choosenSettler = choosenSettlers[0];
											choosenSettlers = null;
										}
								} //если никто не попал вообще насрать.
							} else { 
								int localChoosenSettler = settlers.isSettlerChoosen(mapX, mapY);
								//Если же кликом попали по юниту
								if (localChoosenSettler >= 0) {
									//Если был один, то либо добавляем нового, либо если клик был по уже выбранному, отменяем выделение
									if (choosenSettler >= 0 && choosenSettlers == null) {
										gameSupportPanel.settlerUnchoosen();
										if (localChoosenSettler != choosenSettler) {
											choosenSettlers = new int[] {choosenSettler, localChoosenSettler};
										} else {
											settlers.unChooseOne(choosenSettler);
										}
										choosenSettler = -13;
									//Если был не один, то просто добавлем к массиву	
									} else if (choosenSettler < 0 && choosenSettlers != null) {
										//проверяем, был ли он уже в списке
										int i = 0;
										for (; i < choosenSettlers.length; i++)
												//Если встретился, то удаляем.
												if (choosenSettlers[i] == localChoosenSettler) {
													settlers.unChooseOne(localChoosenSettler);
													for(int k = i; k < choosenSettlers.length - 1; k++)
														choosenSettlers[k] = choosenSettlers[k+1];
													choosenSettlers = Arrays.copyOf(choosenSettlers, choosenSettlers.length - 1);
													i--;
													break; //дальше искать смысла нет.
												}
										//Если цикл прерван, то i не достигент длины массива, значит был встречен повтор.
										if (i < choosenSettlers.length) {
											if (choosenSettlers.length == 1) {
												choosenSettler = choosenSettlers[0];
												choosenSettlers = null;
											}
										} else { 
											choosenSettlers = Arrays.copyOf(choosenSettlers, choosenSettlers.length + 1);
											choosenSettlers[choosenSettlers.length - 1] = localChoosenSettler;
										}
									//Если вообще никого не было, записываем его как одного выбранного.
									} else if (choosenSettler < 0 && choosenSettlers == null) {
										choosenSettler = localChoosenSettler;
									} else System.out.println("Error! We have array of choosens and a choosen one!");
								} 
							}
						} //else isShiftDown
					}
					startPoint = null;
					drawPanel.setRectCoords(null, null);
				}
				if (e.getButton() == 3){
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
			for (;(gameStopped & 4) != 4;){ //пока бит остановки не появится при запуске функции stopGame
				long startTime= System.currentTimeMillis();
				drawPanel.repaint();
				long passedTime = System.currentTimeMillis() - startTime;
				//System.out.println("passed: " + passedTime);
				try { Thread.sleep((drawSleepTime > passedTime)? drawSleepTime - passedTime: 0); } catch (Exception ex) {};
				//try { Thread.sleep(drawSleepTime); } catch (Exception ex) {};
			}
			gameStopped = (byte)(gameStopped | 2); //сигнал о завершении второго потока
		}			
	}
}
