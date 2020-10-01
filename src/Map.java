package DEstraction;
import DEstraction.*;

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.awt.image.*;


public class Map{ 
	
	private GameThread gameThread;
	private Cell mapCells [][];
	private Cell cellTypes [];
	public int cellWidth = 16;
	public int cellHeight = 16;
	public float fishSpawnChance = (float)0.01;  //percents (%)
	
	public int maxResCount = 500;
	
	public Map(GameThread gThread){
		gameThread = gThread;
		initCells();
		generateRandomeMap(60, 60);
	}
	
	public void initCells() {
		cellTypes = new Cell[] {
			new Cell(true, "grass", 0, new Color(150, 255, 150),  null),
			new Cell(true, "tree", 1, new Color(110, 90, 0),  null),
			new Cell(true, "stone", 2, new Color(150, 150, 150),  null),
			new Cell(true, "water", 3, new Color(100, 200, 255),  null),
			new Cell(true, "fish", 4, new Color(0, 255, 255),  null)
		};
	}
	
	public void loadMap() {
		//todo
	}
	
	public int getMapWidth(){
		return cellWidth * mapCells.length;
	}
	
	public int getMapHeight(){
		return cellHeight * mapCells[0].length;
	}
	
	public void processingMap(){
		int isResourcesExist = 0;
		for (int i = 0; i < mapCells.length; i++)
			for (int j = 0; j < mapCells[0].length; j++) {
				if (mapCells[i][j].cellType == 0) continue; //для травы пока что ничего не делаем
				switch (mapCells[i][j].cellType) {
					// Если больше нет дерева или камня, то превращаем клетку в траву.
					case 1:
					case 2: 
						if (mapCells[i][j].resourceCount == 0) mapCells[i][j] = new Cell(cellTypes[0]);
						else isResourcesExist ++;
						break;
					//Если поблизости с водой есть рыба, то есть шанс, что рыба перкочует в свободную клетку.
					case 3: 
						if (i > 0) if (mapCells[i-1][j].cellType == 4) 
							if (mapCells[i-1][j].resourceCount > maxResCount/2 && (int)(Math.random() * (100 / fishSpawnChance)) == 0) {
								mapCells[i][j] = new Cell(cellTypes[4]);
								mapCells[i-1][j].resourceCount = mapCells[i][j].resourceCount = (int)(mapCells[i-1][j].resourceCount/2);
								break;
							}							
						if (i < mapCells.length - 1) if (mapCells[i+1][j].cellType == 4)
							if (mapCells[i+1][j].resourceCount > maxResCount/2 && (int)(Math.random() * (100 / fishSpawnChance)) == 0) {
								mapCells[i][j] = new Cell(cellTypes[4]);
								mapCells[i+1][j].resourceCount = mapCells[i][j].resourceCount = (int)(mapCells[i+1][j].resourceCount/2);
								break;
							}	
						if (j > 0) if (mapCells[i][j-1].cellType == 4)
							if (mapCells[i][j-1].resourceCount > maxResCount/2 && (int)(Math.random() * (100 / fishSpawnChance)) == 0) {
								mapCells[i][j] = new Cell(cellTypes[4]);
								mapCells[i][j-1].resourceCount = mapCells[i][j].resourceCount = (int)(mapCells[i][j-1].resourceCount/2);
								break;
							}	
						if (j < mapCells[0].length - 1) if (mapCells[i][j+1].cellType == 4)
							if (mapCells[i][j+1].resourceCount > maxResCount/2 && (int)(Math.random() * (100 / fishSpawnChance)) == 0) {
								mapCells[i][j] = new Cell(cellTypes[4]);
								mapCells[i][j+1].resourceCount = mapCells[i][j].resourceCount = (int)(mapCells[i][j+1].resourceCount/2);
								break;
							}	
						break;
					//Если истощены запасы рыбы, то превращаем клетку в воду.
					case 4: 
						if (mapCells[i][j].resourceCount == 0) {
							mapCells[i][j] = new Cell(cellTypes[3]); 
							break;
						}
						if (mapCells[i][j].resourceCount > 2 && mapCells[i][j].resourceCount < maxResCount && (int)(Math.random() * (100 / fishSpawnChance)) == 0) {
							mapCells[i][j].resourceCount *= 2; 
							break;
						}	
				}
			}
		if (isResourcesExist == 0) gameThread.resourceEnded();
	}
	
	public int getResourceFrom(Point p, int workingSkill) {
		Cell cell = getCellFromCoord(p.x, p.y);
		int answer;
		if (cell.resourceCount < workingSkill) {
			answer = cell.resourceCount;
			cell.resourceCount = 0;
		} else {
			answer = workingSkill;
			cell.resourceCount = cell.resourceCount - workingSkill;
		}
		return answer;
	}
	
	public void generateRandomeMap(int lenghtSize, int widthSize) {
		mapCells = new Cell[widthSize][lenghtSize];
		for (int i = 0; i < widthSize; i++)
			for (int j = 0; j < lenghtSize; j++){
				mapCells[i][j] = new Cell(cellTypes[(int)(Math.random() * cellTypes.length)]);
				if ((mapCells[i][j].cellType != 0) && (mapCells[i][j].cellType != 3)) mapCells[i][j].resourceCount = (int)(Math.random() * maxResCount + 1);
			}
	}
	
	public void paintMap(Graphics2D g2d, Point offset) {
		//Graphics2D g2d = (Graphics2D) g;
		
		for (int i = 0; i < mapCells.length; i++)
			for(int j = 0; j < mapCells[0].length; j++) {
				if (mapCells[i][j].isPrimitive) {
					g2d.setColor(mapCells[i][j].cellColor);
					g2d.fillRect(i * cellWidth + offset.x, j * cellHeight + offset.y, cellWidth, cellHeight);
				}
			}
	}
	
	public boolean isCellIsRes(int xCoord, int yCoord){
		return (getTypeOfResource(xCoord, yCoord) == 0)?false: true;
	}
	
	private Cell getCellFromCoord(int xCoord, int yCoord) {
		return mapCells[(int)(xCoord/cellWidth)][(int)(yCoord/cellHeight)];
	}
	
	public int getTypeOfResource(int xCoord, int yCoord) {
		Cell cell = getCellFromCoord(xCoord, yCoord);
		switch (cell.cellType) {
			case 1: return 1; // wood
			case 2: return 2; // stone
			case 4: return 3; // fish
			default: return 0;
		}
	}
		
	public String[] getCellInfo(int xCoord, int yCoord){
		Cell cell = getCellFromCoord(xCoord, yCoord);
		String answer [] = {
			cell.cellName,
			String.valueOf(cell.resourceCount)
		};
		return answer;
	}
	
	private class Cell {
		public boolean isPrimitive = true;
		public String cellName;
		public Color cellColor;
		public BufferedImage cellPic;
		public int resourceCount = 0;
		public int cellType;
		
		Cell(boolean prim, String name, int type, Color clr, BufferedImage image) {
			isPrimitive = prim;
			cellName = name;
			cellColor = clr;
			cellPic = image;
			cellType = type;
		}
		
		Cell(Cell byCellType) {
			isPrimitive = byCellType.isPrimitive;
			cellName = byCellType.cellName;
			cellColor = byCellType.cellColor;
			cellPic = byCellType.cellPic;
			cellType = byCellType.cellType;
		}
	}
}
