package DEstraction;
import DEstraction.*;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.util.jar.*;


public class HelpFrame extends JFrame {
		
	JLabel helpInfo;
	JButton closeButton;
	FrameMove mainPanel;
	HelpFrame thisFrame = this;
	public static int countOfOpening = 0;
	
	public HelpFrame() {
		setSize(500, 500);
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
		Point p = DisplayParams.getCenterMainScreen();
		setLocation(p.x - (int)(getWidth()/2), p.y - (int)(getHeight()/2));
		
		helpInfo = new JLabel("");
		closeButton = new JButton("���� �������...");
		mainPanel = new FrameMove(this);

		addComponents();
		
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
				thisFrame.dispose();
			}
		});
		
		setType(Type.UTILITY);
	}
	
	private void addComponents() {
		GridBagLayout gbl = new GridBagLayout();
		mainPanel.setLayout(gbl);
		
		GridBagConstraints con = new GridBagConstraints();
		con.gridx = 0;
		con.gridwidth = 1; 
		con.gridy = 0; 
		con.gridheight = 1; 
		con.fill = GridBagConstraints.BOTH;
		con.weightx = 1.0;
		con.weighty = 1.0;
		
		mainPanel.add(helpInfo, con);
		
		con.gridy = 1;

		mainPanel.add(closeButton, con);
			
		if (gbl.rowHeights == null) gbl.rowHeights = new int[2];
		gbl.rowHeights[0] = (int)(getHeight() * 9 / 10);
		gbl.rowHeights[1] = (int)(getHeight() * 1 / 10);
		
		add(mainPanel);
	}
	
	public void increaseCount() {
		countOfOpening++;
		setHelpText();
	}
	
	private void setHelpText() {
		helpInfo.setText("<html> �� �������. ��� �� ���� ������, ������ ��� �� ������� ��� ����� ��������. <br>" 
						+"���� ��� � ����� �������������� ���� ������. ���.<br>"
						+"��� ������ ��� �� �����... �����... ����� �������� ������ ����������. <br>"
						+"� ���� ����� �� ������ ��� ���� ���� � ����� � ������������� ��� ������ �� ����� ������.<br>"
						+"������ ����: ���������� �� ���� �������: <br><br>"
						+"<em>��� - ������� ������, ����� ��� ������ �� �����, ��� �������</b><br>"
						+"����� ��� - ����� �������� ��������� ������<br>"
						+"��� - ������ ������ ����� ����������. ����� ������� ������� �����<br>"
						+"Shift + ��� - �������� ����� ������<br>"
						+"����� �������� ����� ��������� �����<<br>"
						+"����� �� 4-�� ������� ���� ����� ���������� �����</em><br><br>"
						+"���� ���� �� ������� �������� � 4-�� ������� ���� - ��� ���� ��������!<br>"
						+"���� ���� ��� �����! ����� �����-�� ������� ��� ������� ������ � ����� �� �����!<br>"
						+"��� ���� ����� ������ �������� ������, � ���������� �� �� �������."
						+"������ �� ����� ��������, ��� ��� ���� ����, � ���� ������.<br>"
						+"������� ����� �� �� ������������, ��������� ������ ����, ����� ��������� ������ ��������������.<br>"
						+"���� ��� ���� ������: ���� ����� �... ������������, � �� ���. � ������ ���� ��������� ���.<br>"
						+"�� ��� ��� �� ���� ����...<br><br>"
						+"� �����, � ����� �� �����! �� ����� ���� ��� " + countOfOpening + " ���. <br>"
						+"���� �� ���� � �� ������ �������� " + 
						((countOfOpening == 1)?"����.":
						(countOfOpening == 2)?"<s>����</s> � ��������� �����.": "� ��������� �����.")
						+"<br>������� ������ � " + getVersion()
						+"</html>");
	}
	
	   
	private String getVersion() {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
			Manifest mf = new Manifest(is);
			String version = mf.getMainAttributes().getValue("Program-Version");
			if(version != null) {
				return version;
			} else return "";
		} catch (Exception ex) {
			return "";
		}
	}
 }
