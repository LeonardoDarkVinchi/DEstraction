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
		closeButton = new JButton("Ясно понятно...");
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
		helpInfo.setText("<html> Ну здарова. Раз ты сюда пришел, значит еще не вкурсах что здесь творится. <br>" 
						+"Либо это я зашел протестировать окно помощи. КРЧ.<br>"
						+"Это дерьмо еще на очень... очень... ОЧЕНЬ глубокой стадии разработки. <br>"
						+"Я хочу чтобы ты собрал всю свою волю в кулак и протестировал все дерьмо из этого дерьма.<br>"
						+"Слушай сюда: управление до боли простое: <br><br>"
						+"<em>ЛКМ - выбрать объект, юнита или клетку на карте, как повезет</b><br>"
						+"Зажав ЛКМ - можно выделить несколько юнитов<br>"
						+"ПКМ - отдать приказ юниту немедленно. Также очищает очередь задач<br>"
						+"Shift + ПКМ - добавить юниту задачу<br>"
						+"Зажав колесико можно подвигать карту<<br>"
						+"Нажав на 4-ую клавишу мыши можно заспаунить юнита</em><br><br>"
						+"Если тебе не повезло родиться с 4-ой клавиши мыши - это твои проблемы!<br>"
						+"Цель игры еще проще! Нужно всего-то собрать все ресурсы дерева и камня на карте!<br>"
						+"Для этих целей можешь спаунить юнитов, и направлять их на ресурсы."
						+"Однако не стоит забывать, что они тоже люди, и тоже устают.<br>"
						+"Поэтому следи за их показателями, отправляй ловить рыбу, чтобы пополнить запасы продовольствия.<br>"
						+"Есть еще один момент: рыба может е... размножаться, а ты нет. В смысле твои поселенцы нет.<br>"
						+"ХЗ что там на счет тебя...<br><br>"
						+"И помни, я слежу за тобой! Ты зашел сюда уже " + countOfOpening + " раз. <br>"
						+"Будь на чеку и не забудь доложить " + 
						((countOfOpening == 1)?"кучу.":
						(countOfOpening == 2)?"<s>кучу</s> о найденных багах.": "о найденных багах.")
						+"<br>Текущая сборка № " + getVersion()
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
