package DEstraction;
import DEstraction.Animation;

import java.awt.*;
import java.util.*;
import java.awt.geom.*;

public class Settlers{

	private Settler settler[];
	private GameSupportPanel gameSupportPanel;
	private GameThread gameThread;
	private Map mainMap;
	
	public int maxMovingSpeed = 4;
	public int maxFood = 100;
	public int maxRest = 100;
	// private int lastDeletedSettler = -13;
	
	public Settlers(GameThread parent){
		gameThread = parent;
		settler = new Settler[0];
	}

	public void transmitGameSupportPanel(GameSupportPanel gsp){
		gameSupportPanel = gsp;
	}
	
	public void transmitMap(Map map){
		mainMap = map;
	}

	//----------------------------------------------------------

	public void addNewSettler(int x, int y){
		settler = Arrays.copyOf(settler, settler.length + 1);
		settler[settler.length - 1] = new Settler(x, y).createRandom();
		gameSupportPanel.settlersCountChanged();
	}
	
	public void deleteSettler(int settlerNumber){
		for (int i = settlerNumber; i < settler.length - 1; i++){
			settler[i] = settler[i+1];
		}
		settler = Arrays.copyOf(settler, settler.length - 1);
		gameSupportPanel.settlersCountChanged();
		gameThread.settlerDeathAlert(settlerNumber);
	}
	
	// public boolean checkIsSettlerAlive(int settlerNumber){
		// if (settlerNumber != lastDeletedSettler) return true;
		// else {
			// lastDeletedSettler = -13;
			// return false;
		// }
	// }
	//-------------------------------------------------------

	public void drawSettlers(Graphics2D g2d, Point offset){
		for (Settler setlr : settler)
			setlr.drawSettler(g2d, offset);
	}
	
	public int getSettlersCount(){
		return settler.length;
	}
	
	public int getMansCount(){
		int mansCount = 0;
		for (Settler stlr : settler) if (stlr.sex == "male") mansCount++;
		return mansCount;
	}
	
	public String[] getSettlerFullInfo(int i){
		String answer [] = {
			settler[i].name,
			String.valueOf(settler[i].age),
			String.valueOf(settler[i].hp),
			String.valueOf((int)(settler[i].food * 100 / maxFood)) + "%",
			String.valueOf((int)(settler[i].rest * 100 / maxRest)) + "%",
			((settler[i].currentStatus == 1)?"Chilling":
			(settler[i].currentStatus == 2)?"Walking":
			(settler[i].currentStatus == 3)?"Working":
						"Attacking")		
		};
		return answer;
	}
	
	public String[] getSettlerInfo(int i){
		String answer [] = {
			settler[i].name,
			String.valueOf(settler[i].age),
			((settler[i].currentStatus == 1)?"Chilling":
			(settler[i].currentStatus == 2)?"Walking":
			(settler[i].currentStatus == 3)?"Working":
						"Attacking"),
			((settler[i].resourceType == 1)?"Wood":
			(settler[i].resourceType == 2)?"Stone":
			(settler[i].resourceType == 3)?"Fish":
						"Nothing"),
			String.valueOf(settler[i].workingSkills[settler[i].resourceType])
		};
		return answer;
	}
	
	public void processingSettlers() {
		int i = -1;
		Queue<Integer> settlersToDelete = new PriorityQueue<Integer>();
		
		for (Settler stlr : settler) {
			//����� �������� ���������, ����� ��� ���� ����� ����� ��� ����!
			i++;
			//������ ���� �������� ��������� ���������, �������� �� �� �������� � �.�.
			//��������� �������, ����� HP ������� �� ����.
			if (stlr.hp <= 0) {
				settlersToDelete.offer(i); // �������� � ������� �� ��������
				//deleteSettler(i);
				//i--;
				//���� ��������� ����, ������ �� ������ ������� �� ������.
				continue;
			}
			// ���-�� ��� � 10 ������ ��������� ����� ������ 1 ������� �������.
			// ���� 1%, �� �� ������� �������� 10 �����, ��� � ������ ��������� � ����� 10% �� �������
			if ((int)(Math.random() * 100) == 0) stlr.rest--;
			//���� ����� ��� ������ ��������, �������� ������� ��� �� ������ ����
			if (stlr.food < maxFood/2 && stlr.currentStatus == 1 && gameSupportPanel.food > 0) {
				if (maxFood - stlr.food >  gameSupportPanel.food) {
					stlr.food = gameSupportPanel.food;
					gameSupportPanel.resourseChanged(3, -gameSupportPanel.food);
				}
				else {
					gameSupportPanel.resourseChanged(3, -(maxFood - stlr.food));
					stlr.food = maxFood;
				}
				//���� ��������� ��������� ������ ���� � ���� ���, ������ �� �������� ���� ���.
				continue;
			}	
			//��������� ���������� ���� ����� �������, ����� ���������� ����� �������� �� ����� ������
			if (stlr.rest < maxRest && stlr.currentStatus == 1) {
				if (stlr.food > 0) stlr.food -= 1;
				//��� ����� ��������, ���� ����� ��� �� ����.
				else stlr.hp -= 1;
				stlr.rest += 2;
				//���� ��������� "��������" � ���� ���, �� ������ ������ �� ��� �� ������ ������.
				continue;
			}	
			//��������� ���������� ���� ����� �������, ����� ���������� ����� �������� �� ����� ������
			if (stlr.hp < stlr.maxHP && stlr.currentStatus == 1 && stlr.food > 0) {
				stlr.food -= 1;
				stlr.hp += 1;
				//���� ��������� "��������" � ���� ���, �� ������ ������ �� ��� �� ������ ������.
				continue;
			}					
			//��������� ������ �� ����� ������ � ������
			if (stlr.currentStatus != 1 && stlr.rest > 0 && (int)(Math.random() * 2) == 0) {
				if (stlr.currentStatus == 2) stlr.rest -= 1;
				if (stlr.currentStatus == 3) stlr.rest -= (2 + (int)(stlr.workingSkills[stlr.resourceType]/2));
				if (stlr.currentStatus == 4) stlr.rest -= 3;
				if (stlr.rest < 0) stlr.rest = 0;
			}
			//��������� ��������� ����� ���� ������� ��������� ���� �� ����
			if ((stlr.currentStatus != 1 || stlr.currentStatus != 4) && stlr.rest == 0) {
				stlr.previousTask = stlr.currentTask;
				stlr.currentTask = null;
				// if (stlr.destination == null) {
					// stlr.previousDestination = new Point(stlr.x, stlr.y));
					// stlr.previousDestinationState = stlr.currentStatus;
				// }
				// if (stlr.destination != null) {
					// stlr.previousDestination.push(stlr.destination);
					// stlr.previousDestinationState.push(stlr.destinationState);
					// stlr.destination = null;
				// }
				stlr.currentStatus = 1;
				continue;
			}
			//��������� ������������ � ������, ����� ������� ��������� ������������ �� ���������
			if (stlr.currentStatus == 1 && stlr.rest >= maxRest && stlr.previousTask != null) {
				stlr.currentTask = stlr.previousTask;
				stlr.previousTask = null;
				//stlr.setDestination(stlr.previousDestination.pop(), stlr.previousDestinationState.pop());
			}			
			//���� ���� ����� ����������, �� ��������� ���� ����.
			//if (stlr.destination != null) {
			if (stlr.currentTask != null) {
				if (stlr.currentTask.destination.x != stlr.x || stlr.currentTask.destination.y != stlr.y) {
					if (stlr.currentStatus != 2) stlr.currentStatus = 2;
					stlr.moveToPoint();
					//���������� �����, ����� �� ���� ������ �������� �� ���� ���.
					continue;
				}
				//���� ��� ������, ��������� ����������� ��������
				else {
					stlr.currentStatus = stlr.currentTask.state;
					if (stlr.currentStatus == 1) stlr.currentTask = null;
				}
			}
			//��������� ��������, ���� � ���� ��������� ������
			if (stlr.currentStatus == 3) {
				//���� �� ���� ����� ��� ���� �������, �� ��������
				if (mainMap.isCellIsRes(stlr.x, stlr.y)) {
					stlr.resourceType = mainMap.getTypeOfResource(stlr.x, stlr.y);
					gameSupportPanel.resourseChanged(stlr.resourceType,
													mainMap.getResourceFrom(new Point(stlr.x, stlr.y),
																			(int)stlr.workingSkills[stlr.resourceType]));
					//��������� �������. ������ ��� ��� ������, � 10^workingSkill
					if (stlr.workingSkills[stlr.resourceType] < 10) 
						stlr.workingSkills[stlr.resourceType] += (float)(1.0 / (int)(Math.pow(10, (int)(stlr.workingSkills[stlr.resourceType]))));
					//System.out.print((float)(1.0 / (int)(Math.pow(10, (int)(stlr.workingSkills[stlr.resourceType])))));
					continue;
				} else {
					stlr.currentStatus = 1;
					stlr.currentTask = null;
				}
			}
			//����� � ������� ���������, ��������� ��������� ������ ��������������� ���
			if (stlr.currentTask == null && stlr.nexTasks.peek() != null) {
				//System.out.println("New task finded");
				stlr.currentTask = stlr.nexTasks.poll();
			}			
		}
		int offset = 0; //����� ������������ ��������������� �������.
		// ���� ������� �� �����, �������.
		for (;settlersToDelete.peek() != null;) {
			deleteSettler(settlersToDelete.poll() - offset);
			offset++; //����� �������� �������� ������.
		}
	}
	
	public void setDestination(int choosenSettler, Point p, boolean isRes, boolean isEnemy) {
		settler[choosenSettler].setDestination(p, isRes, isEnemy);
	}
	
	public void setNextDestination(int choosenSettler, Point p, boolean isRes, boolean isEnemy) {
		settler[choosenSettler].setNextTask(p, isRes, isEnemy);
	}
	
	public void unChooseAll() {
		for (Settler stlr : settler) {
			stlr.isSelected = false;
		}
	}
	
	public int isSettlerChoosen(int x, int y){
		int choosenSettler = -13;
		for (int i = 0; i < settler.length; i++) 
			if ((settler[i].x - settler[i].width/2 <= x && x <= settler[i].x + settler[i].width/2)
			&& (settler[i].y - settler[i].height/2 <= y && y <= settler[i].y + settler[i].height/2)) {
				choosenSettler = i;
				settler[i].isSelected = true;
				break;
			}
		return choosenSettler;
	}
	
	public int[] getSettlers(Point startPoint, Point endPoint) {
		int x1 = Math.min(startPoint.x,endPoint.x);
		int y1 = Math.min(startPoint.y,endPoint.y);
		int x2 = Math.max(startPoint.x,endPoint.x);
		int y2 = Math.max(startPoint.y,endPoint.y);
		
		int settlersID[] = new int [settler.length];
		int i = 0;
		int j = 0;
		for (Settler stlr : settler) {
			if (x1 <= stlr.x && stlr.x <= x2 && y1 <= stlr.y && stlr.y <= y2) {
				stlr.isSelected = true;
				settlersID[j] = i;
				j++;
			}
			i++;
		}
		if (j > 0) {
			if (j < i) settlersID = Arrays.copyOf(settlersID, j);
			return settlersID;
		} else 
			return null;
	}
	

	private class Settler{ 
		
		public String name;
		public String sex;
		public int age;
		public int resourceCount;
		public int resourceType = 0;
		public int x;
		public int y;
		public int maxHP;
		public int hp;
		public int food = maxFood;
		public int rest = maxRest;
		public int width;
		public int height;
		public float workingSkills[];
		private boolean isPrimitive = true;
		private Animation walk;
		private Animation work;
		private Animation attack;
		private Animation stay;
		private int lookAt;	
		public int currentStatus = 1;
		private Color settlerColor;	
		private Task currentTask;
		private Task previousTask;
		private LinkedList<Task> nexTasks;
		private boolean isSelected = false;
		
		private int movingSpeed;		
		
		public Settler(int xCoord, int yCoord){
			x = xCoord;
			y = yCoord;
			nexTasks = new LinkedList<Task>();
			//������ ������: ��������, ������ ������, �����, ����
			workingSkills = new float[4];
			workingSkills[0] = 0;
			workingSkills[1] = 0;
			workingSkills[2] = 0;
			workingSkills[3] = 0;
			
			walk = new Animation();
			work = new Animation();
			attack = new Animation();
			stay = new Animation();
		}
		
		public void drawSettler(Graphics2D g2d, Point offset){
			//Graphics2D g2d = (Graphics2D) g;
			if (isSelected) {
				g2d.setColor(new Color(0, 0, 0));
				g2d.draw(new Ellipse2D.Float((x - (width) + offset.x), (y + offset.y), 
													(width*2), height));
			}
			if (isPrimitive) {
				g2d.setColor(settlerColor);
				g2d.fillRect(x - (int)(width/2) + offset.x, y - (int)(height/2) + offset.y, width, height);
			}
			else {
				
			}
		}
		
		public void setNextTask(Point dest, boolean isRes, boolean isEnemy) {
			int destinationState = 1;
			if (isRes) {
				destinationState = 3;
			}
			else {
				if (isEnemy){
				destinationState = 4;
				}
			}
			nexTasks.offer(new Task(dest, destinationState));
		}
		
		public void setDestination(Point p, boolean isRes, boolean isEnemy) {
			int destinationState = 1;
			currentStatus = 2;
			if (previousTask != null) {
				//previousDestination.pop();
				previousTask = null;
			}
			if (isRes) {
				destinationState = 3;
			}
			else {
				if (isEnemy){
				destinationState = 4;
				}
			}
			currentTask = new Task(p, destinationState);
		}
		
		// public void setDestination(Point p, int pointState) {
			// destination = p;
			// destinationState = pointState;
		// }
		
		public void moveToPoint() {
			int movingSpeed = maxMovingSpeed;
			if (rest < 50) movingSpeed = (int)(movingSpeed/2);
			int taskX = currentTask.destination.x;
			int taskY = currentTask.destination.y;
			//todo make it with collision
			x = (Math.abs(x - taskX) > movingSpeed)?((x < taskX)? x + movingSpeed : x - movingSpeed) : taskX;
			y = (Math.abs(y - taskY) > movingSpeed)?((y < taskY)? y + movingSpeed : y - movingSpeed) : taskY;
		}
		
		public void changeSettlerStatus(int status){
			//status == 1 - stay
			//status == 2 - walk
			//status == 3 - work
			//status == 4 - attack
			currentStatus = status;
		}
		
		public Settler createRandom(){
			name = ((int)(Math.random() * 100) == 0)?"������":"�� ������";
			sex = ((int)(Math.random() * 2) == 1)?"male":"female";
			age = (int)(Math.random() * 60);
			maxHP = (int)(Math.random() * 101);
			hp = maxHP;
			if (isPrimitive) {
				settlerColor = (sex == "male")?new Color(0, 0, 200): new Color(255, 120, 200);
				width = 8;
				height = 12;
			}
			return this;
		}   
		
		public Settler loadSettler(String name){
			//todo load all stats from file
			loadAnimations(name);
			return this;
		}
		
		private void loadAnimations(String name){
			walk.load(name);
			work.load(name);
			attack.load(name);
			stay.load(name);
		}
		
		private class Task {
			public Point destination;
			public int state;
			
			public Task(Point p, int s) {
				destination = p;
				state = s;
			}
		}
	}
}





