package ui;

import java.awt.AlphaComposite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import main.Logger;
import main.Engage;
import backend.library.MediaFile;

public class DraggableImage extends JLabel implements MouseMotionListener, MouseListener {
	
	private Image img = null;
	private MediaFile source;
	private float alpha = .5f;
	private Engage main;
	
	int prevRelativeX, prevRelativeY;

	public DraggableImage(MediaFile image, Engage odm)
	{
		main = odm;
		source = image;
		img = image.getThumbImage();
		
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(img, 0);
		
		try { mediaTracker.waitForID(0); }
		catch (InterruptedException ie) {
			System.err.println(ie);
			System.exit(1);
		}
		
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(img.getWidth(this), img.getHeight(this)));
		this.setSize(this.getPreferredSize());
	}
	
	public DraggableImage(JLabel image)
	{
		Image img = ((ImageIcon)image.getIcon()).getImage();
		
		if(img == null) { Logger.log(Logger.DEBUG,"Dragging image is null."); }
		
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(img, 0);
		
		try { mediaTracker.waitForID(0); }
		catch (InterruptedException ie) {
			System.err.println(ie);
			System.exit(1);
		}
		
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(img.getWidth(this), img.getHeight(this)));
		this.setSize(this.getPreferredSize());
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2d.drawImage(img, 0, 0, this);
	}
	
	//public ImageIcon getIcon() {
	//	return new ImageIcon(img);
	//}
	
	public int getWidth() {
		return img.getWidth(null);
	}
	
	public int getHeight() {
		return img.getHeight(null);
	}

	
	public void mouseDragged(MouseEvent evt) {
		
		//This is the location where we would like to put the object
		int newX = this.getX() + (evt.getX() - prevRelativeX); //add to the old upper left coordinate the displacement of mouse cursor
		int newY = this.getY() + (evt.getY() - prevRelativeY);
		
		//We need to be sure that neither the X nor the Y are below zero to keep the frame
		//from moving above the top or to the left of the page
		newX = Math.max(0,newX);
		newY = Math.max(0,newY);
		
		//We do not wish for the draggable frame to move so far that it exceeds the bounds of
		//the parent frame on the bottom or the right side either
		int largestX = getParent().getWidth() - this.getWidth();
		int largestY = getParent().getHeight() - this.getHeight();
		newX = Math.min(largestX, newX);
		newY = Math.min(largestY, newY);
		
		this.setLocation(newX, newY);
		
		Point componentPoint = SwingUtilities.convertPoint(
				this,
			    new Point(evt.getX(), evt.getY()),
			    main);
		
		
		try {
			if((main.getContentPane().getComponentAt(0,componentPoint.y)) != null && Class.forName("javax.swing.JTabbedPane").isInstance(main.getContentPane().getComponentAt(0,componentPoint.y))) {
				main.getCurrentStationStrip().mouseDragged(evt);
			}
		}catch(ClassNotFoundException e) {}
	
		this.getParent().repaint();
		
	}

	public void mouseMoved(MouseEvent evt) {
		prevRelativeX = evt.getX();
		prevRelativeY = evt.getY();
	}
	public void mouseEntered(MouseEvent arg0) {
		setCursor(new Cursor(Cursor.MOVE_CURSOR));
	}
	
	public void mouseReleased(MouseEvent arg0) {		
		
		Container parent = this.getParent();
		parent.remove(this);
		
		try {
		
		Logger.log(0,"DraggableImage, Release over: " + main.getContentPane().getComponentAt(arg0.getPoint()));
		if((main.getContentPane().getComponentAt(arg0.getPoint())) != null && Class.forName("javax.swing.JTabbedPane").isInstance(main.getContentPane().getComponentAt(arg0.getPoint()))) {
			
			main.getCurrentStationStrip().mouseReleased(arg0);
		}
		}
		catch(ClassNotFoundException e) {}
		
		parent.repaint();
	}
	public void mouseClicked(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	
	public MediaFile getMediaFile() {
		return source;
	}
}