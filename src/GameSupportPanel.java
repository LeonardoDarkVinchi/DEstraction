/**
	Copyright 2020 Dark Dead Dragon (ak LeonardoDarkVinchi)
	License by GNU GPLv3
*/

package DEstraction;
import DEstraction.*;

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.awt.image.*;

public class GameSupportPanel extends JPanel{ 
	
	private Map mainMap;
	Settlers settlers;
	
	int wood = 0;
	int stone = 0;
	int food = 100;
	
	int choosenSettler; 
	
	JLabel resourcesInfo;
	JLabel currentSettlersCount;
	JLabel settlerInfo;
	JButton killSettler;
	
	public GameSupportPanel(Map transMap, Settlers transSettlers) {
		mainMap = transMap;
		settlers = transSettlers;
		
		resourcesInfo = new JLabel("");
		currentSettlersCount = new JLabel("");
		settlerInfo = new JLabel("");
		killSettler = new JButton("�� ��������!");
		killSettler.setMargin(new Insets(0, 0, 0, 0));
		
		settlersCountChanged();
		resourseChanged(0, 0);		
		
		setLayout(new java.awt.GridLayout(0, 1, 0, 0));
		
		add(resourcesInfo);
		add(currentSettlersCount);
		add(settlerInfo);
		add(killSettler);
		
		killSettler.setVisible(false);
		killSettler.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				settlers.deleteSettler(choosenSettler);
				killSettler.setText("�������");
			}
		});
	}
	
	public void resourseChanged(int typeOfResource, int amount){
		switch (typeOfResource) {
			case 1: wood +=	amount; break;
			case 2: stone += amount; break;
			case 3: food += amount; break;
			default: break;			
		}
		resourcesInfo.setText("<html>�����: " + wood
							+"<br>�����: "+ stone
							+"<br>���: "+ food
							+"</html>");
	}
	
	public void settlerChoosen(int settlerNumber){
		choosenSettler = settlerNumber;
		String stlrInfo[] = settlers.getSettlerFullInfo(choosenSettler);
		settlerInfo.setText("<html>���: " + stlrInfo[0]
							+"<br>�������: "+ stlrInfo[1]
							+"<br>��������: "+ stlrInfo[2]
							+"<br>C������: "+ stlrInfo[3]
							+"<br>��������: "+ stlrInfo[4]
							+"<br>������: "+ stlrInfo[5]
							+"</html>");
		killSettler.setVisible(true);
	}
	
	public void settlersChoosen(int settlersNumber){
		settlerInfo.setText("<html>�������: " + settlersNumber + "</html>");
	}
	
	public void settlerUnchoosen(){
		settlerInfo.setText("");
		killSettler.setVisible(false);
	}
	
	public void settlersCountChanged(){
		int mans = settlers.getMansCount();
		currentSettlersCount.setText("<html>���������: " + settlers.getSettlersCount()
									+"<br>�/�: "+ mans + " / " + (settlers.getSettlersCount() - mans)
									+"</html>");
	}
}
