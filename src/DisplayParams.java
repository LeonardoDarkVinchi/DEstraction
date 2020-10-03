/**
	Copyright 2020 Dark Dead Dragon (ak LeonardoDarkVinchi)
	License by GNU GPLv3
*/

package DEstraction;

import java.io.*;
import java.util.Locale;
import java.awt.*;
import java.awt.geom.*;

public class DisplayParams{ 


	public DisplayParams(){

	}


	public static Rectangle getMaximumScreenBounds() {
		int minx=0, miny=0, maxx=0, maxy=0;
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for(GraphicsDevice device : environment.getScreenDevices()){
			Rectangle bounds = device.getDefaultConfiguration().getBounds();
			minx = Math.min(minx, bounds.x);
			miny = Math.min(miny, bounds.y);
			maxx = Math.max(maxx,  bounds.x+bounds.width);
			maxy = Math.max(maxy, bounds.y+bounds.height);
		}
		return new Rectangle(minx, miny, maxx-minx, maxy-miny);
	}

	public static Rectangle getDefaultScreenBounds() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return gd.getDefaultConfiguration().getBounds();
	}

	public static Point getCenterMainScreen(){
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) dimension.getWidth() / 2;
		int y = (int) dimension.getHeight() / 2;
		return new Point(x, y);
	}
	
	
	public static Rectangle getToolsArea(){
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Rectangle bounds = gd.getDefaultConfiguration().getBounds();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());

		Rectangle safeBounds = new Rectangle(bounds);
		safeBounds.x += insets.left;
		safeBounds.y += insets.top;
		safeBounds.width -= (insets.left + insets.right);
		safeBounds.height -= (insets.top + insets.bottom);
		
		Area area = new Area(bounds);
		area.subtract(new Area(safeBounds));
		return area.getBounds();
	}
}