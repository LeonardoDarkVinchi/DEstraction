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
	Structures structures;
	
	int wood = 0;
	int stone = 0;
	int food = 100;
	
	int choosenSettler = -13; 
	int choosenStructure;
	
	JLabel resourcesInfo;
	JLabel currentSettlersCount;
	JPanel thirdPanel;
	JPanel structureButtonsPanel;
	JLabel settlerInfo;
	JButton specialButton;
	
	public GameSupportPanel(Map transMap, Settlers transSettlers, Structures transStructures) {
		mainMap = transMap;
		settlers = transSettlers;
		structures = transStructures;
		
		resourcesInfo = new JLabel("");
		currentSettlersCount = new JLabel("");
		thirdPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(thirdPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		structureButtonsPanel = structures.madePanelWithBuildButtons();
		settlerInfo = new JLabel("");
		specialButton = new JButton("���������");
		specialButton.setMargin(new Insets(0, 0, 0, 0));
		
		settlersCountChanged();
		resourseChanged(0, 0);		
		
		setLayout(new java.awt.GridLayout(0, 1, 0, 0));
		
		add(resourcesInfo);
		add(currentSettlersCount);
		add(scrollPane);
		add(specialButton);
		
		specialButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				if (choosenSettler != -13)
					settlers.deleteSettler(choosenSettler);
				else {
					clearThirdPanel();
					thirdPanel.add(structureButtonsPanel);
					thirdPanel.repaint();
				}
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
	
	public void settlersCountChanged(){
		int mans = settlers.getMansCount();
		currentSettlersCount.setText("<html>���������: " + settlers.getSettlersCount()
									+"<br>�/�: "+ mans + " / " + (settlers.getSettlersCount() - mans)
									+"</html>");
	}
	
	public void settlerChoosen(int settlerNumber){
		clearThirdPanel();
		thirdPanel.add(settlerInfo);
		
		choosenSettler = settlerNumber;
		String stlrInfo[] = settlers.getSettlerFullInfo(choosenSettler);
		settlerInfo.setText("<html>���: " + stlrInfo[0]
							+"<br>�������: "+ stlrInfo[1]
							+"<br>��������: "+ stlrInfo[2]
							+"<br>C������: "+ stlrInfo[3]
							+"<br>��������: "+ stlrInfo[4]
							+"<br>������: "+ stlrInfo[5]
							+"</html>");
		
		specialButton.setText("�������");
	}
	
	private void clearThirdPanel() {
		thirdPanel.removeAll();
		thirdPanel.revalidate();
	}
	
	public void settlersChoosen(int settlersNumber){
		clearThirdPanel();
		thirdPanel.add(settlerInfo);
		thirdPanel.repaint();
		settlerUnchoosen();
		settlerInfo.setText("<html>�������: " + settlersNumber + "</html>");
		
	}
	
	public void settlerUnchoosen(){
		choosenSettler = -13;
		settlerInfo.setText("");
		specialButton.setText("���������");
	}
}
