package backend.library;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.Constants;

/**
 * This JLabel helps to draw a thumbnail image.
 * 
 * @author carl
 *
 */
public class ThumbLabel extends JLabel {

	//-------------------------------------------------------------------------
	// Constants
	//-------------------------------------------------------------------------

	protected static Image deleteButtonImage = (new ImageIcon("ui/queue/imageDelete.png")).getImage();

	//-------------------------------------------------------------------------
	// END Constants
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Protected and Private Fields
	//-------------------------------------------------------------------------
	
	/*
	 * True if we should draw a delete button on this label, false otherwise.
	 */
	private boolean drawDeleteButton = true;
	
	/*
	 * The MediaFile this label is based off of.
	 */
	protected MediaFile mFile;
	
	/*
	 * Whether or not the image is selected
	 */
	boolean selected = false;

	//-------------------------------------------------------------------------
	// END Protected and Private Fields
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------

	/**
	 * Sets whether or not this ThumbLabel draws a selected border.
	 * 
	 * @param s true if this ThumbLabel is selected.
	 */
    public void setSelected(boolean s) {
    	selected = s;
    }
    
    /**
     * Returns true if this ThumbLabel is selected.
     * 
     * @return true if this ThumbLabel is selected
     */
    public boolean getSelected() {
    	return selected;
    }

    //-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------

    
	
	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------
	
	/**
	 * Basic constructor. Takes a MediaFile (and a boolean) and creates a JLabel
	 * based on the thumbnail image of that MediaFile.
	 * 
	 * @param media the MediaFile this Thumb is based on.
	 * @param drawDeleteButton true if a delete button should be drawn.
	 */
	public ThumbLabel(MediaFile media, boolean drawDeleteButton){	
		super(new ImageIcon(media.getThumbImage()));
		mFile = media;
		this.drawDeleteButton = drawDeleteButton;
		
		/*
		//TODO Carl figure out which of these I need.
		Dimension size = new Dimension(Constants.THUMB_LABEL_AREA.width, Constants.THUMB_LABEL_AREA.height);
		/*this.setSize(size);
		this.setBounds(Constants.THUMB_LABEL_AREA);
		this.setPreferredSize(size);
		this.setMinimumSize(size);*/
		
		Dimension size = new Dimension(Constants.THUMB_LABEL_AREA.width, Constants.THUMB_LABEL_AREA.height);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		
	}

	/**
	 * This constructor is used only by the visual queue classes.
	 * 
	 * @param image The Image to base this JLabel on.
	 */
	public ThumbLabel(ImageIcon image){
		super(image);
	}
	
	//-------------------------------------------------------------------------
	// END Constructors
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Painting Method
	//-------------------------------------------------------------------------
	
	/**
	 * Painting method for the thumbnail image.
	 */
    public void paintComponent(Graphics g){
    	
    	// Fetch the image we are to draw.
    	Image image = ((ImageIcon)this.getIcon()).getImage();
    	
    	if(mFile != null) {
    		
    		// Draw the black artboard background.
    		Graphics2D g2d = (Graphics2D)g;
    		g2d.setColor(Color.BLACK);
    		g2d.fill(Constants.BLACK_ARTBOARD_AREA);
    		
    		// Calculate the location of the image and draw it.
    		Dimension imageSize = new Dimension(image.getWidth(this),image.getHeight(this));
    		Point centerOffset = Constants.getCenterOffset(Constants.BLACK_ARTBOARD_AREA, imageSize);
    		g.drawImage(image,centerOffset.x,centerOffset.y,this);
    		
    		
    		if(drawDeleteButton) 
    			g.drawImage(deleteButtonImage, Constants.DELETE_BUTTON_AREA.x,Constants.DELETE_BUTTON_AREA.y,this);
    		
    		if(!selected) {
    			// Draw the background for the text
    			g2d.setColor(Color.DARK_GRAY);
    			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
    			g2d.fill(Constants.THUMB_TEXT_AREA);

    			// Draw the text itself
    			g2d.setColor(Color.white);
    			g2d.setFont(new Font("Arial",Font.PLAIN,12));
    			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
    			g2d.drawString(mFile.getDisplayName(), Constants.THUMB_TEXT_AREA.x+4, Constants.THUMB_TEXT_AREA.y+15);
    		}
    		else {
    			//Draw the background for the text
    			g2d.setColor(Constants.SELECTION_COLOR);
    			//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    			g2d.fill(Constants.THUMB_TEXT_AREA);

    			// Draw the text itself
    			g2d.setColor(Color.white);
    			g2d.setFont(new Font("Arial",Font.BOLD,14));
    			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
    			g2d.drawString("Now Showing", Constants.THUMB_TEXT_AREA.x+30, Constants.THUMB_TEXT_AREA.y+15);
    		}
    	}
    	
    	// If there is no associated media file, just draw the image.
    	else g.drawImage(image,0,0,this);
    }

	//-------------------------------------------------------------------------
	// END Painting Method
	//-------------------------------------------------------------------------


    
}




//TODO Carl delete this
//BEGIN JONATHAN'S CODE
// KEPT just in case we find anything missing from the current implementation.
/*
ImageIcon i = (ImageIcon)this.getIcon();
g.drawImage(i.getImage(), 3, 3, this);


if(libraryImage) {

	g.drawImage(deleteIcon, i.getIconWidth()+3 - deleteIcon.getWidth(this), 3, this);

	Graphics2D g2d = (Graphics2D)g;
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
	g2d.fill(new Rectangle(3, (int)(Constants.THUMB_SIZE.height*0.8)+4, this.getIcon().getIconWidth(), 
			(int)(Constants.THUMB_SIZE.height*0.2)));

	// If this is a file, we make special arrangements...
	if(mFile != null) { 

		String myName = mFile.getName();
		
		String displayName;

		if(myName.length() > 18) {
			StringBuffer name = new StringBuffer(myName.substring(0, 15));
			name.append("...");
			displayName = name.toString();
		}
		else displayName = myName;


		g2d.setColor(Color.white);
		g2d.setFont(new Font("Arial",Font.PLAIN,12));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		g2d.drawString(displayName, 5, (int)(Constants.THUMB_SIZE.height*0.98));
	}
}	

 */