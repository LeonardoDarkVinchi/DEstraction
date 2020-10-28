/**
	Copyright 2020 Dark Dead Dragon (ak LeonardoDarkVinchi)
	License by GNU GPLv3
*/

package DEstraction;
import DEstraction.Animation;

import java.awt.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;


public class Structures{ 
	
	Structure structs[];
	Structure structTypes[];
	Structure allowForBuild[];
	
	Map mainMap;
	GameThread gameThread;
	
	public Structures(Map transMap, GameThread transParent){
		mainMap = transMap;
		gameThread = transParent;
		structs = new Structure[0];
		structTypes = new Structure[] {
			new Structure(true, "Хижина", 0, 2, 2, new Color(75, 50, 0), new int[] {400, 100, 0}),
			new Structure(true, "Мост", 1, 1, 1, new Color(100, 75, 0), new int[] {100, 0, 0})
		};
		allowForBuild = new Structure[] {
			structTypes[0],
			structTypes[1]
		};
	}

	public JPanel madePanelWithBuildButtons() {
		JPanel buildPanel = new JPanel();
		buildPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 0));
		for (Structure struct : allowForBuild) {
			JButton buildButton = new JButton(struct.name);
			buildButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
					// planNewStructure(struct.structureType);
					gameThread.startToPlanBuiling(struct.structureType);
				}
			});
			buildButton.setToolTipText("<html>" + ((struct.resNeed[0] > 0)?"Дерева " + struct.resNeed[0]:"")
			+ ((struct.resNeed[1] > 0)?"<br> Камня " + struct.resNeed[1]:"")
			+ ((struct.resNeed[2] > 0)?"<br> Еды " + struct.resNeed[2]:"")
			+ "</html>");
			buildPanel.add(buildButton);
		}
		return buildPanel;
	}

	// public void planNewStructure(int structureType) {
		// gameThread.startToPlanBuiling(structureType);
		// //System.out.println("Выбрано для строительства: " + structTypes[structureType].name); //отладочная информация
	// }
	
	public Rectangle checkPlan(int structureType, Point mousePoint) {
		int x = mousePoint.x - (mousePoint.x % mainMap.cellWidth);
		int y = mousePoint.y - (mousePoint.y % mainMap.cellHeight);
		int width = structTypes[structureType].width - 1;
		int height = structTypes[structureType].height - 1;
		
		return new Rectangle(x, y, width, height);
	}
	
	public boolean checkPlan(Rectangle rect, int structType) {
		int x1 = (int)(rect.x / mainMap.cellWidth);
		int y1 = (int)(rect.y / mainMap.cellHeight);

		return Map.isSecondInFirst(mainMap, structTypes[structType].underStructure, x1, y1);
	}

	public void addNewStructure(int x, int y, int structureType){
		structs = Arrays.copyOf(structs, structs.length + 1);
		structs[structs.length - 1] = new Structure(structTypes[structureType],new Point(x, y));
		// gameSupportPanel.settlersCountChanged();
	}
	
	public void deleteStructure(int structureNumber){
		for (int i = structureNumber; i < structs.length - 1; i++){
			structs[i] = structs[i+1];
		}
		structs = Arrays.copyOf(structs, structs.length - 1);
	}
	
	public void drawStructures(Graphics2D g2d, Point offset){
		for (Structure str : structs)
			str.drawStructure(g2d, offset);
	}
	
	
	private class Structure {
		int hp;
		int buildReady;
		Animation work;
		Animation stay;
		int resources[];
		int resNeed[];
		int x;
		int y;
		int width;
		int height;
		boolean isPrimitive;
		int structureType;
		String name;
		boolean isSelected = false;
		Color structColor;
		Map underStructure;
		
		Structure(boolean isPrim, String structName, int structType, int widthInCells, int heightInCells, Color clr, int[] res) {
			isPrimitive = isPrim;
			name = structName;
			structureType = structType;
			structColor = clr;
			width = widthInCells * mainMap.cellWidth;
			height = heightInCells * mainMap.cellHeight;
			underStructure = Map.makeBuildMap(structureType);
			resNeed = res;
		}
				
		Structure(Structure byStructureType, Point pos) {
			isPrimitive = byStructureType.isPrimitive;
			name = byStructureType.name;
			structColor = byStructureType.structColor;
			structureType = byStructureType.structureType;
			x = pos.x;
			y = pos.y;
			width = byStructureType.width;
			height = byStructureType.height;
			buildReady = 0;
		}
		
		public void drawStructure(Graphics2D g2d, Point offset){
			if (isSelected) {
				g2d.setColor(Colors.black);
				g2d.draw(new Ellipse2D.Float((x - (int)(width/2) + offset.x), (y + offset.y), 
													(width*2), height));
			}
			if (isPrimitive) {
				g2d.setColor(structColor);
				g2d.fillRect(x + offset.x, y + offset.y, width, height);
			}
			else {
				
			}
		}
	}
}
