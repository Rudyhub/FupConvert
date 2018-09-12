package com.mysterman.FupConvert;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;
public class FupMouseEvent implements MouseInputListener{
	private static FupLayout frame = null;
	private static Point origin;
	public FupMouseEvent(FupLayout jf){
		frame = jf;
		origin = new Point();
	}
	public void mousePressed(MouseEvent e){
		origin.x = e.getPoint().x;
		origin.y = e.getPoint().y;
	}
	public void mouseDragged(MouseEvent e){
		Point p = frame.getLocation();
		frame.setLocation(p.x+(e.getX()-origin.x), p.y+(e.getY()-origin.y) );
	}
	public void mouseMoved(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
}
