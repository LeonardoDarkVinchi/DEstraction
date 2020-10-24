/**
	Copyright 2020 Dark Dead Dragon (ak LeonardoDarkVinchi)
	License by GNU GPLv3
*/

package DEstraction;
import DEstraction.Animation;

import java.awt.*;
import java.util.*;
import java.awt.geom.*;


public class Structures{ 
	
	Structure structs[];
	Structure structTypes[];
	Map mainMap;
	
	public Structures(Map transMap){
		mainMap = transMap;
		structs = new Structure[0];
		structTypes = new Structure[] {
			new Structure(true, "home", 0, 2, 2, new Color(75, 50, 0)),
			new Structure(true, "bridge", 1, 1, 1, new Color(100, 75, 0))
		};
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
		//gameSupportPanel.structureCountChanged();
		//gameThread.settlerDeathAlert(structureNumber);
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
		int x;
		int y;
		int width;
		int height;
		boolean isPrimitive;
		int structureType;
		String name;
		boolean isSelected = false;
		Color structColor;
		
		Structure(boolean isPrim, String structName, int structType, int widthInCells, int heightInCells, Color clr) {
			isPrimitive = isPrim;
			name = structName;
			structureType = structType;
			structColor = clr;
			width = widthInCells * mainMap.cellWidth;
			height = heightInCells * mainMap.cellHeight;
		}
		
		Structure(Structure byStructureType, Point pos) {
			isPrimitive = byStructureType.isPrimitive;
			name = byStructureType.name;
			structColor = byStructureType.structColor;
			// cellPic = byStructureType.cellPic;
			structureType = byStructureType.structureType;
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
