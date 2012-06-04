package ui.stage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.Constants;
import backend.library.MediaFile;

/**
 * The image panel handles the drawing of the central image, the black border behind it, and the Now Showing title. That's it.
 * 
 * @author carl
 *
 */
public class ImagePanel extends JPanel{

	//-------------------------------------------------------------------------
	// Constants
	//-------------------------------------------------------------------------
	
	private final static int SIDE_THICKNESS = 13;
	private final static int TOP_THICKNESS = 37;
	
	private static ImageIcon BACKGROUND_IMAGE = new ImageIcon("ui/stage/stage_background.png");
	protected final static Dimension PANEL_SIZE = new Dimension(776,470);
	
	//-------------------------------------------------------------------------
	// END Constants
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------

	private Image stageImage;
	private MediaFile centralMediaFile;
	private String stageTitle;

	//-------------------------------------------------------------------------
	// END Private Fields
	//-------------------------------------------------------------------------
	
	
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	/**
	 * Constructor for an Image Panel. Takes an initial image, draws the a 
	 * background with "Now Showing", and if the image is not null, it draws
	 * a black backdrop with the image on top.
	 * 
	 * @param newImage The initial image to display, or null for none.
	 */
	public ImagePanel(MediaFile newImage){
		super();
		
		this.centralMediaFile = newImage;
		this.setStageTitle();
		
		this.setMinimumSize(PANEL_SIZE);
		this.setPreferredSize(PANEL_SIZE);
		this.setMaximumSize(PANEL_SIZE);
		this.setSize(PANEL_SIZE);
		

	}

	//-------------------------------------------------------------------------
	// END Constructor
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Painting Methods
	//-------------------------------------------------------------------------

	/**
	 * This is the standard paintComponent method. It draws the background, the
	 * black letterbox borders, the image itself, and the image's title.
	 * 
	 * @param g the Graphics on which to draw.
	 */
	public void paintComponent(Graphics g)
	{	
		g.drawImage(BACKGROUND_IMAGE.getImage(),0,0,null);
		
		g.setFont(Constants.TITLE_FONT);
		FontMetrics fontMetrics = getFontMetrics( Constants.TITLE_FONT );
		int titleWidth = fontMetrics.stringWidth(stageTitle);
		g.drawString(stageTitle, (int)((PANEL_SIZE.width-titleWidth)/2), 23);
		
		if (stageImage != null){
			Point p = getOffset(stageImage);
			
			// Draws a black background.
			g.setColor(Color.BLACK);
			g.fillRect(SIDE_THICKNESS,TOP_THICKNESS,Constants.CENTER_SIZE.width, Constants.CENTER_SIZE.height);
			
			// Draws an image on top of the black background.
			g.drawImage(stageImage, p.x+SIDE_THICKNESS, p.y+TOP_THICKNESS, this);
		}
	}
	
	private Point getOffset(Image image){
		int x = (Constants.CENTER_SIZE.width - image.getWidth(null))/2;
		int y = (Constants.CENTER_SIZE.height -image.getHeight(null))/2;
		return new Point(x,y);
	}

	//-------------------------------------------------------------------------
	// END Painting Methods
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Image Display Methods
	//-------------------------------------------------------------------------
	
	/**
	 * This method dismisses the previously shown image and paints the new image.
	 * If the new image is null, it simply shows the background.
	 * 
	 * @param newImage the image to be displayed on the ImagePanel
	 */
	public void setMediaFile(MediaFile newImage) {	
		this.centralMediaFile = newImage;
		
		if (centralMediaFile != null) 
			stageImage = centralMediaFile.getCentralImage();
		else
			stageImage = null;

		setStageTitle();
		this.repaint();
	}
	
	/*
	 * Sets the title to reflect the name of the currently displayed image.
	 */
	private void setStageTitle(){
		if(centralMediaFile == null)
			stageTitle = "Now Showing";
		else
			stageTitle = "Now Showing:  "+centralMediaFile.getDisplayName();
	}

	//-------------------------------------------------------------------------
	// Image Display Methods
	//-------------------------------------------------------------------------
	
}
