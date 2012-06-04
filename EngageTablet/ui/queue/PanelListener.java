package ui.queue;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.DraggableImage;
import backend.library.MediaFile;

public class PanelListener implements MouseListener {

	private JPanel thePanel;
	private MediaStrip theStrip;
	private DraggableImage dragImage;
	
	public PanelListener(JPanel panel, MediaStrip strip) {
	
		thePanel = panel;
		theStrip = strip;
		
	}

	public void mouseReleased(MouseEvent e) {
		dragImage = null;
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		
		dragImage = null;
		
		if((thePanel.getComponentAt(e.getX(),e.getY())) instanceof JLabel) {
			MediaFile image = (MediaFile)theStrip.getLabelsToMediaFiles().get(thePanel.getComponentAt(e.getX(),e.getY()));
			dragImage = new DraggableImage(image,theStrip.getMain());
		}
	
	}
	public void mouseExited(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
}