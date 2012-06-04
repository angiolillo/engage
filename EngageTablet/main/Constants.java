package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * This class contains static constants for use in the rest of the Engage
 * application.
 * 
 * @author carl
 *
 */
public class Constants {

	//-------------------------------------------------------------------------
	// Modifiable Size Constants
	//-------------------------------------------------------------------------
	
	// These are the constants which determine the size of everything in the 
	// application. Make sure to test these after changing as it may cause
	// elements to overlap or crop.
	
	
	/**
	 * This is the ratio of height/width. It was taken off the size of the flat-panel display 
	 * Engage was designed for. It is also 9/16. 
	 */
	static public final double HEIGHT_TO_WIDTH	= 0.5625; // =9/16
	
	static private final int CENTER_IMAGE_WIDTH  = 750;
	static private final int THUMB_IMAGE_WIDTH	= 158;
	
	static private final int STARTING_X = 0;
	static private final int STARTING_Y = 0;
	
	static private final int SCREEN_WIDTH 	= 1024;
	static private final int SCREEN_HEIGHT 	= 768;
	
	static private final int NAVBAR_HEIGHT = 40; 
	static private final int LOGOUT_BUTTON_WIDTH = 125;
	static private final int STATION_DROP_DOWN_WIDTH = 300;
	static private final int LIBRARY_TOGGLE_BUTTON_WIDTH = 225;
	
	static private final int QUEUE_HEIGHT = 207;

	//-------------------------------------------------------------------------
	// END Modifiable Size Constants
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// END Modifiable Size Constants
	//-------------------------------------------------------------------------

	/**
	 * When false, boot time is decreased but there is a delay (~3s) when 
	 * switching stations due to waiting for the thumbnails in the media library 
	 * to load. Recommend true.
	 */
	static public final boolean WAIT_FOR_THUMB_IMAGE = true;
	
	/**
	 * When false, boot time is decreased but adds delay (~.5s) when placing image 
	 * on stage. Recommend false.
	 */
	static public final boolean WAIT_FOR_CENTER_IMAGE = false;
	
	/**
	 * When false, boot time is decreased but adds noticable delays during the first
	 * fifteen to thirty seconds of instructor use as the remaining images are loaded 
	 * after the main frame is painted. Recommend true.
	 */
	static public final boolean PRELOAD_ENTIRE_PROGRAM = true;
	
	//-------------------------------------------------------------------------
	// END Modifiable Size Constants
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Image Dimensions
	//-------------------------------------------------------------------------

	/**
	 * The dimensions of the center image to be painted on the stage. 
	 */
	static public final Dimension CENTER_SIZE = new Dimension(CENTER_IMAGE_WIDTH,
											(int)(HEIGHT_TO_WIDTH*CENTER_IMAGE_WIDTH));
	
	/**
	 * The dimensions of the thumbnail image to be painted in the library and 
	 * in the queue.
	 */
	static public final Dimension THUMB_SIZE = new Dimension(THUMB_IMAGE_WIDTH, 
											(int)(HEIGHT_TO_WIDTH*THUMB_IMAGE_WIDTH));
	
	//-------------------------------------------------------------------------
	// END Image Dimensions
	//-------------------------------------------------------------------------
	
	
	
	//-------------------------------------------------------------------------
	// Main Screen Areas
	//-------------------------------------------------------------------------
	
	/**
	 * The complete area of the window comprising the entire Engage application.
	 */
	static public final Rectangle WINDOW_AREA = new Rectangle(STARTING_X, 
											STARTING_Y, 
											SCREEN_WIDTH, 
											SCREEN_HEIGHT);
	
	/**
	 * The area of the "navbar" along the top of the window. The navbar contains
	 * the logout button, station selector, and media library toggle.
	 */
	static public final Rectangle NAVBAR_AREA = new Rectangle(STARTING_X,
											STARTING_Y,
											SCREEN_WIDTH,
											NAVBAR_HEIGHT);
	
	/**
	 * The area used by the queue strip along the bottom of the screen.
	 */
	static public final Rectangle QUEUE_AREA = new Rectangle(STARTING_X,
											(STARTING_Y+(SCREEN_HEIGHT-QUEUE_HEIGHT)), 
											SCREEN_WIDTH, 
											QUEUE_HEIGHT);
	
	/**
	 * The area used by the stage and the previous and next buttons.
	 */
	static public final Rectangle STAGE_AREA = new Rectangle(STARTING_X,
											(STARTING_Y+NAVBAR_HEIGHT),
											SCREEN_WIDTH,
											(SCREEN_HEIGHT-(NAVBAR_HEIGHT+QUEUE_HEIGHT)));
	
	/**
	 * The font used for any titles.
	 */
	public static final Font TITLE_FONT = new Font ("ARIAL",Font.BOLD,18);
	
	//-------------------------------------------------------------------------
	// END Main Screen Areas
	//-------------------------------------------------------------------------
	
	
	
	//-------------------------------------------------------------------------
	// Navbar Areas
	//-------------------------------------------------------------------------

	/**
	 * The area used by the logout button in the upper left-hand corner of the screen.
	 */
	static public final Rectangle LOGOUT_BUTTON_AREA = new Rectangle(0,
											0,
											LOGOUT_BUTTON_WIDTH,
											NAVBAR_HEIGHT);

	/**
	 * The area used by the station drop down between the logout button and the library toggle.
	 */
	static public final Rectangle STATION_DROP_DOWN_AREA = new Rectangle(LOGOUT_BUTTON_WIDTH,
											0, 
											(STATION_DROP_DOWN_WIDTH), 
											NAVBAR_HEIGHT);

	/** 
	 * The area between the station drop down and the library toggle where we draw a pretty picture.
	 */
	static public final Rectangle NAVBAR_HEADER_AREA = new Rectangle(LOGOUT_BUTTON_WIDTH+STATION_DROP_DOWN_WIDTH,
											0,
											(SCREEN_WIDTH - (LOGOUT_BUTTON_WIDTH+STATION_DROP_DOWN_WIDTH+LIBRARY_TOGGLE_BUTTON_WIDTH)),
											NAVBAR_HEIGHT);

	/**
	 * The area used by the library toggle button.
	 */
	static public final Rectangle LIBRARY_TOGGLE_BUTTON_AREA = new Rectangle((SCREEN_WIDTH-LIBRARY_TOGGLE_BUTTON_WIDTH),
											0,
											LIBRARY_TOGGLE_BUTTON_WIDTH, 
											NAVBAR_HEIGHT);
	
	//-------------------------------------------------------------------------
	// END Navbar Areas
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Thumbnail Constants
	//-------------------------------------------------------------------------
	
	static private final Dimension DELETE_BUTTON_SIZE = new Dimension(22,22);
	static private final int BORDER_THICKNESS = 3;
	static private final int TEXT_BOX_HEIGHT = 20;
	
	/**
	 * The entire area of the thumbnail image (it's actually a JLabel).
	 */
	static public final Rectangle THUMB_LABEL_AREA = new Rectangle(0,0,
													THUMB_SIZE.width + (2*BORDER_THICKNESS),
													THUMB_SIZE.height + (2*BORDER_THICKNESS));
	
	/**
	 * The area of the black artboard that is drawn behind images that aren't the exact aspect ratio of the display.
	 */
	static public final Rectangle BLACK_ARTBOARD_AREA = new Rectangle(BORDER_THICKNESS, 
													BORDER_THICKNESS,
													THUMB_SIZE.width, 
													THUMB_SIZE.height);
	
	/**
	 * The area on the thumbnail that is covered by the delete button.
	 */
	static public final Rectangle DELETE_BUTTON_AREA = new Rectangle(BORDER_THICKNESS+THUMB_SIZE.width-DELETE_BUTTON_SIZE.width,
													BORDER_THICKNESS,
													DELETE_BUTTON_SIZE.width,
													DELETE_BUTTON_SIZE.height);
	
	/**
	 * The area used by the text label on the thumbnail image.
	 */
	static public final Rectangle THUMB_TEXT_AREA  = new Rectangle(BORDER_THICKNESS, 
													BORDER_THICKNESS+THUMB_SIZE.height-TEXT_BOX_HEIGHT, 
													THUMB_SIZE.width, 
													TEXT_BOX_HEIGHT); 
	
	/** 
	 * Used to store the selection border color for thumbnail images
	 */
	static public final Color SELECTION_COLOR = Color.getHSBColor(.99f, .98f, .54f);
	//static public final Color SELECTION_COLOR = Color.LIGHT_GRAY;
	
	//-------------------------------------------------------------------------
	// Thumbnail Constants
	//-------------------------------------------------------------------------
	
	
	
	//-------------------------------------------------------------------------
	// Static Methods
	//-------------------------------------------------------------------------
	
	/**
	 * Gets the offset when you want one smallSize to exist within a larger 
	 * bigArea. If you position the smallSized object at the given point, it
	 * will be exactly in the center of the bigArea
	 * 
	 * @param bigArea The area in which we wish to center the smallSize
	 * @param smallSize The size of the object we wish to center within the bigArea
	 * @return Point corresponding with the location to place the smallSize object to center it within the bigArea.
	 */
	static public Point getCenterOffset(Rectangle bigArea, Dimension smallSize){
		int x = (bigArea.width - smallSize.width)/2;
		int y = (bigArea.height -smallSize.height)/2;
		return new Point(x+bigArea.x,y+bigArea.y);
	}
	
	
	//-------------------------------------------------------------------------
	// END Static Methods
	//-------------------------------------------------------------------------

	
}
