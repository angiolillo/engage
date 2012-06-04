package backend.library;

import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Used to store all of the MediaFiles within a single category.
 * 
 * @author Carl Angiolillo
 *
 */
public class CategoryMedia extends LibraryObject{
	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------
	
	// A list of all of the media files stored in this category.
	private TreeMap<String, MediaFile> mediaFiles = new TreeMap<String, MediaFile>();
	
	//-------------------------------------------------------------------------
	// END Private Fields
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------
	
	/**
	 * Returns an Iterator of the MediaFiles in this category.
	 * 
	 * @return An Iterator of all the MediaFiles in this category.
	 * @see MediaFile
	 */
	public Iterator getMediaFiles(){
		return mediaFiles.values().iterator();
	}
	
	/**
	 * Given the name, return the MediaFile with that name, or null if none
	 * is found.
	 * 
	 * @param name the name of the media file to seek for.
	 * @return A MediaFile with this name, or null if none is found.
	 */
	public MediaFile getMediaFile(String name){
		return (MediaFile)mediaFiles.get(name);
	}
	
	/**
	 * Returns true if this CategoryMedia contains no MediaFiles.
	 * 
	 * @return true if this category has no media files
	 */
	public boolean isEmpty(){
		return mediaFiles.size()==0;
	}
	
	//-------------------------------------------------------------------------
	// END Getters and Setters
	//-------------------------------------------------------------------------
	

	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	/**
	 * Creates a CategoryMedia from a directory containing images, and a
	 * reference to the station in which this category resides.
	 * 
	 * @param categoryDirectory the directory in which the media resides
	 * @param parent the station in which this category resides
	 */
	public CategoryMedia(File categoryDirectory, StationMedia parent){
		super(categoryDirectory, parent);
		log("Loading...");
		
		// Loop through the contents of the category directory.
		File[] directoryContents = getFile().listFiles();
		for (int i=0; i<directoryContents.length; i++) {
			
			// For every file in the category directory that is NOT a folder, add it as a media
			// item to the list of media files in this category.
			if(!directoryContents[i].isDirectory()){
				MediaFile fileToAdd = MediaFile.convertToMediaFile(directoryContents[i], this);
				
				//If the conversion succeeded then we will add the file to our list.
				if (fileToAdd != null) mediaFiles.put(fileToAdd.getName(), fileToAdd);
			}
			
		}
		log("Done!");
	}

	//-------------------------------------------------------------------------
	// END Constructor
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Prefetching
	//-------------------------------------------------------------------------
	
	/**
	 * Prefetches the images associated with this category.
	 * @param waitForThumbImage true if we should wait for the thumbnail image to load
	 * @param waitForCentralImage true if we should wait for the central image to load
	 */
	public void prefetchImages(boolean waitForThumbImage, boolean waitForCentralImage) {
		Iterator i = this.getMediaFiles();
		while(i.hasNext()){
			((MediaFile)i.next()).loadImages(waitForThumbImage, waitForCentralImage);
		}
	}
	
	//-------------------------------------------------------------------------
	// END Prefetching
	//-------------------------------------------------------------------------
	
}
