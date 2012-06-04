package ui.medialibraryview;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

import main.Constants;
import ui.DraggableImage;
import backend.library.MediaFile;
import backend.library.ThumbLabel;

/**
 * This class represents an individual "cell" in the Media Library. The term cell is used
 * in the spreadsheet sense. The actual image and decoration rendering is done in ThumbLabel,
 * with LibraryCell extending this to add drag and drop functionality.
 * 
 * @author A. Tzou
 */
class LibraryCell extends ThumbLabel implements MouseListener, MouseMotionListener
{
	//handle to the parent media library 
	private final StationMediaLibrary library;
	//flag indicating whether this cell is highlighted
	private boolean isHighlighted = false;
	//flag indicating whether or not this cell is CURRENTLY being dragged
	private boolean dragging = false;
	
	//used to slightly delay dragging so a click vs. a drag can be differentiated
	private long lastObservedPress = 0;
	
	/**
	 * This is a delay in milliseconds before a drag will take place. This is used to prevent
	 * accidental drags.
	 */
	public static final int DRAG_DELAY = 175;
	
	/**
	 * A LibraryCell is not added to the MediaLibrary pane directly, but rather a logical
	 * row or column (presently MediaColumn). The cell represents the individual media element
	 * users can interact with by clicking to make big, or dragging to the stage
	 * 
	 * @param library handle to the container Media Library
	 * @param mf encapsulation of all file information including a visual representation
	 * @param handle to the parent media library
	 */
	public LibraryCell(StationMediaLibrary library, MediaFile mf, StationMediaLibrary parentLibrary)
	{
		super(mf, false);
		this.library = library;
		
		//register listeners
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		this.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		this.setSelected(false);
		this.repaint();
	}
	
	/**
	 * Toggles whether or not the cell is selected
	 * @param b boolean indicating whether or not cell is highlighted
	 */
	public void setHighlighted(boolean b)
	{
		this.isHighlighted = b;
		this.setSelected(b);
		this.repaint();
	}
	
	public void mousePressed(MouseEvent e) 
	{
		//record the latest press to compute if drag delay is sufficient (in mouseDragged)
		this.lastObservedPress = e.getWhen();
	}
	
	public void mouseDragged(MouseEvent e) 
	{	
		//only allow dragging if a certain delay has been reached since the last observed press
		if ( (e.getWhen() - this.lastObservedPress) < LibraryCell.DRAG_DELAY)
			return;
		
		if (dragging == false)
		{
			//we are currently dragging
			dragging = true;
			//in the highest level window, spawn a semi-transparent version of the cell for dragging
			//note that this must be in the highest level window since we want dragging over all interface components
			this.library.handleToMain.spawnDraggableImage(mFile, SwingUtilities.convertPoint(this, e.getX() - (this.getIcon().getIconWidth() / 2), e.getY() - (this.getIcon().getIconHeight() / 2), this.library.handleToMain));
		}
		
		/* Here is a little bit of swing hacking: we must continuously redirect drag events within this cell to
		 * the ghostImage in the highest level window. A local to global coordinate space conversion needs to
		 * take place, which convertPoint handles. 
		 */
		DraggableImage target = this.library.handleToMain.getGhostImage();

   	    Point componentPoint = SwingUtilities.convertPoint(
			    this,
			    new Point(e.getX() - (this.getIcon().getIconWidth() / 2), e.getY() - (this.getIcon().getIconHeight() / 2)),
			    target);
		 
   	     //dispatch the event to the ghost image in main
		 target.dispatchEvent(new MouseEvent(target,
			    e.getID(),
			    e.getWhen(),
			    e.getModifiers(),
			    componentPoint.x,
			    componentPoint.y,
			    e.getClickCount(),
			    e.isPopupTrigger()));
	}

	public void mouseReleased(MouseEvent e) 
	{
		//reset the interval counter for presses
		this.lastObservedPress = 0;
		
		//follow the same scheme as above to redirect a mouseReleased to main window
		DraggableImage target = this.library.handleToMain.getGhostImage();
		 
		 if (target != null && dragging)
		 {
			 Point componentPoint = SwingUtilities.convertPoint(
					    this,
					    new Point(e.getX(), e.getY()),
					    this.library.handleToMain);
			 
			 target.dispatchEvent(new MouseEvent(target,
					    e.getID(),
					    e.getWhen(),
					    e.getModifiers(),
					    componentPoint.x,
					    componentPoint.y,
					    e.getClickCount(),
					    e.isPopupTrigger()));
		 }
		 
		 dragging = false;
	}
	
	public void mouseClicked(MouseEvent arg0) 
	{
		
		if (this.library.lastSelectedCell != null)
		{
			//handle the special case where the last selected cell is the one just clicked
			if (this.library.lastSelectedCell == this)
			{
				//deselect the cell and clear the big screen
				this.isHighlighted = false;
				this.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
				this.setSelected(false);
				this.repaint();
				this.library.handleToMain.getStage().showImage(null, this.library.handleToMe);
				this.library.lastSelectedCell = null; //special case where no deselection needed on next click, so nullify
				return;		
			}
			else
			{
				//deselected the most recently selected cell
				this.library.lastSelectedCell.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
				this.library.getLastSelectedCell().setHighlighted(false);
				this.library.lastSelectedCell.setSelected(false);
				this.library.lastSelectedCell.repaint();
			}
		}
			
		//mark this as the most recently selected cell
		this.library.lastSelectedCell = this;
		
		if (isHighlighted == false){
			//highlight the cell and send to big screen
			isHighlighted = true;
			this.setBorder( BorderFactory.createLineBorder(Constants.SELECTION_COLOR,3) );
			this.setSelected(true);
			this.repaint();
			this.library.handleToMain.getStage().showImage(mFile, this.library.handleToMe);
		}
		else
		{
			//deselect cell and clear big screen
			isHighlighted = false;
			this.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
			this.setSelected(false);
			this.repaint();
			this.library.handleToMain.getStage().showImage(null, this.library.handleToMe);
		}

	}
	
	public void mouseEntered(MouseEvent evt) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseMoved(MouseEvent arg0) {}
}