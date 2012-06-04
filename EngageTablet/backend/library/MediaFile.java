package backend.library;

import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;

import javax.swing.ImageIcon;

import main.Constants;

/**
 * Every file we can display is a kind of MediaFile. To actually instantiate a media file, you 
 * must actually create the specific kind of MediaFile that represents the file -- there is no 
 * such thing as a "defualt MediaFile".
 * 
 * @author Carl Angiolillo
 *
 *TODO Corl plenty of out of memory errors mayybe you can load images on demand?
 *
 */
public abstract class MediaFile extends LibraryObject{

	
	//-------------------------------------------------------------------------
	// Constants
	//-------------------------------------------------------------------------
	
	/** 
	 * Determines the speed of image scaling. Can be changed to: SCALE_DEFAULT,
	 * SCALE_FAST, SCALE_SMOOTH, SCALE_REPLICATE, SCALE_AREA_AVERAGING (slow)
	 * I've played around and they all seem comperable in speed.
	 */ 
	static public final int	  SCALE_SPEED = Image.SCALE_FAST;
	
	/**
	 * There is currently only one recognized filetype, and that is an image.
	 */ 
	static public final String IMAGE_TYPE = "image";
	//static protected final String VIDEO_TYPE = "video";
	//static protected final String MODEL_TYPE = "model"; //3-D?
	//static protected final String MAP_TYPE = "map"; //realtime maps
	//static protected final String TV_TYPE = "tv"; //Live TV stream?
	//static protected final String AUDIO_TYPE = "audio";
	
	//-------------------------------------------------------------------------
	// END Constants
	//-------------------------------------------------------------------------


	//-------------------------------------------------------------------------
	// Protected Fields
	//-------------------------------------------------------------------------
	
	protected Image centralImage;	//The central image, displayed on the stage.
	protected Image thumbImage; 	//The thumbnail image.
	
	private double heightToWidth = 0; //The height-to-width ratio of the image.
	
	// Necessary to block until the image has fully loaded.
	private MediaTracker mt = new MediaTracker(this);
	
	//-------------------------------------------------------------------------
	// END Protected Fields
	//-------------------------------------------------------------------------
	
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	/**
	 * @param file The original media file.
	 * @param parent The CategoryMedia containing this MediaFile.
	 */
	public MediaFile(File file, CategoryMedia parent) {
		super(file, parent);
	}

	//-------------------------------------------------------------------------
	// END Constructor
	//-------------------------------------------------------------------------
	
	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------
	

	
	/**
	 * @return The Image to be displayed in the center pane.
	 */	
	public Image getCentralImage(){
		
		// We want to make sure the central image is definately loaded if it has not been loaded yet.
		loadImages(false,true);
		return centralImage;
	}

	/**
	 * @return The thumbnail Image to be displayed in the library and queue.
	 */
	public Image getThumbImage(){
		
		// We do not particularly care if the central image has finished loading at this point.
		loadImages(true, false);
		return thumbImage;
	}
	
	/**
	 * Returns the path (relative to the the library) of this media file.
	 * 
	 * For example, the image "Ducks.jpg" in the category "Fowl" in the station "Birds" 
	 * in the program "Enviornmental Science" would return the String
	 * "Data/Library/Enviornmental Science/Birds/Fowl/Ducks.jpg"
	 * Note that we will always use the Unix standard file seperators.
	 * 
	 * @return A String of this file's pathname.
	 */
	public String getPath(){
		String systemIndependentPath = this.getFile().getPath().replaceAll("\\\\","/");
		String[] pathArray = systemIndependentPath.split("/"); 
		int n = pathArray.length;
		
		// We only need the program, station, category, and file
		if(n<=3) log("File pathnames must have a depth of at least four: Program/Station/Category/File. This file does not.");
		String shortPath = pathArray[n-4]+"/"+pathArray[n-3]+"/"+pathArray[n-2]+"/"+pathArray[n-1]; 
		
		return shortPath;
	}
	
	//-------------------------------------------------------------------------
	// END Getters and Setters
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Imaging Methods
	//-------------------------------------------------------------------------

	/*
	 * Used to set both the large image and the thumbnail image. If the image 
	 * is not of the right size it will be resized. If the image is not of the
	 * right ratio, it will modify the image by adding a background color and
	 * then resizing it.
	 */
	protected void loadImages(boolean waitForThumbImage, boolean waitForCentralImage){
				
		// If they images are not set, set them.
		if(centralImage == null || thumbImage == null) {

			// One of them is null, so we need to get the original image.
			Image original = (new ImageIcon(this.getFile().getAbsolutePath())).getImage();

			// If we don't already have the height to width ratio, get it.
			if (heightToWidth==0) {
				//loadImage(original); TODO I'm pretty sure I don't need this...
				heightToWidth = (double)original.getHeight(null)/original.getWidth(null);
			}

			// Check to see if the image is taller or wider than the standard h/w ratio.
			if(heightToWidth >= Constants.HEIGHT_TO_WIDTH){

				// The image is taller than (or equal to) the standard h/w ratio, so we must restrict by the height.
				if(centralImage==null) 	
					centralImage = original.getScaledInstance(-1,Constants.CENTER_SIZE.height,SCALE_SPEED);
				if(thumbImage==null) 
					thumbImage 	 = original.getScaledInstance(-1,Constants.THUMB_SIZE.height,SCALE_SPEED);

				log("Generating thumbnail from tall image, ratio:"+heightToWidth);

			}else {

				// The image is wider than the standard h/w ratio, so we must restrict by the width.
				if(centralImage==null)
					centralImage = original.getScaledInstance(Constants.CENTER_SIZE.width,-1,SCALE_SPEED);
				if(thumbImage==null)
					thumbImage 	 = original.getScaledInstance(Constants.THUMB_SIZE.width,-1,SCALE_SPEED);

				log("Generating thumbnail from wide image, ratio: "+heightToWidth);			
			}
			System.gc();


		}
		if(centralImage !=null && waitForCentralImage) loadImage(centralImage);
		if(waitForThumbImage)	loadImage(thumbImage);

	}

	/**
     * Creates an image object and ensures that it is completely
     * loaded before returning the object.
     * 
     * @param load the image.
     */
    protected synchronized void loadImage(Image img) {
		mt.addImage(img,1);
		try { 
			mt.waitForAll(); 
		} catch (InterruptedException  e) {}
		mt.removeImage(img);
    }
    
	//-------------------------------------------------------------------------
	// END Imaging Methods
	//-------------------------------------------------------------------------
	
	
	//-------------------------------------------------------------------------
	// Static File Identification Methods
	//-------------------------------------------------------------------------
	
	/*
	 * This static method is used to analyze the filetype and, if possible, convert it to the 
	 * correct kind of MediaFile. It will return null if it does not recognize the file type 
	 * it is given.
	 */
	protected static MediaFile convertToMediaFile(File file, CategoryMedia parent) {
		if(!file.isFile()) {
			parent.log(" "+file.getName()+": is not a file.");
			return null;
		}
		
		else if(file.getName().startsWith(".")||file.isHidden()){
			parent.log(" "+file.getName()+": is a hidden file.");
			return null;
		}
				
		String type = MediaFile.getType(file);	
		if(type == null){
			parent.log(" "+file.getName()+": \""+getExtension(file)+"\" is an unsupported file type.");
			return null;			
		}
		
		if(type.equals(IMAGE_TYPE)) return new ImageFile(file,parent);

		return null;
	}
	
	/*
	 * Given a file, returns a constant representing the file type. If it 
	 * cannot determine the file type, null is returned.
	 */
	private static String getType(File file) {
		String extension = getExtension(file);
		
		if (extension.equals("jpeg") ||
			extension.equals("png") ||
			extension.equals("gif") ||
			extension.equals("jpg"))
			return IMAGE_TYPE;
		else return null;
	}	

	/*
	 * Returns the extension of of the file in question.
	 */
	private static String getExtension(File file){
		String filename = file.getName().toLowerCase();
		return filename.substring(filename.lastIndexOf(".")+1);
	}
	
	//-------------------------------------------------------------------------
	// END Static File Identification Methods
	//-------------------------------------------------------------------------

	
}
