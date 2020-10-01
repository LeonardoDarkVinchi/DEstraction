package DEstraction;

import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import javax.swing.*;


public class FrameMove extends JPanel {

    private Point position;
	protected final JFrame parent;
	
	//public FrameMove(final AppWindow newParent){	
	public FrameMove(final JFrame newParent){	
		this.parent = newParent;
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				position = e.getPoint();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				grabbedMove(e);
			}
		});
	}
		
	private void grabbedMove(MouseEvent e){
		int thisX = parent.getLocation().x;
		int thisY = parent.getLocation().y;
		int X = thisX + e.getX() - position.x;
		int Y = thisY + e.getY() - position.y;
		parent.setLocation(X, Y);
	}
}