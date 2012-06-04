package backend.library;

import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * This class is designed to contain all of the media files for a particular 
 * station.
 * 
 * @author Carl Angiolillo
 *
 */
public class StationMedia extends LibraryObject{
	
	
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

	/*
	 * The list of all categories within this particular station.
	 */
	private TreeMap<String, CategoryMedia> categories = new TreeMap<String, CategoryMedia>();
	
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------
	
	/**
	 * Returns an Iterator of the CategoryMedia in this station.
	 * 
	 * @return An Iterator containing all of the CategoryMedia objects in
	 * 			this station.
	 * @see CategoryMedia
	 */
	public Iterator getCategories(){
		return categories.values().iterator();
	}
	
	/**
	 * Given a name, returns a category with that name, or null if none is found.
	 * 
	 * @param name the name of the category to seek for.
	 * @return A CategoryMedia with this name, or null if none is found.
	 */
	public CategoryMedia getCategory(String name){
		return (CategoryMedia)categories.get(name);
	}
	
	/**
	 * Returns true if this StationMedia contains no CategoryMedia files.
	 * 
	 * @return true if this station contains no categories
	 */
	public boolean isEmpty(){
		return categories.size()==0;
	}

	
	//-------------------------------------------------------------------------
	// END Getters and Setters
	//-------------------------------------------------------------------------
	
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	
	/**
	 * Creates a StationMedia from a directory containing several categories
	 * and a reference to the educational program in which this station resides.
	 * 
	 * @param stationDirectory contains all of the images for this station
	 * @param parent the program in which this station resides
	 */
	public StationMedia(File stationDirectory, ProgramMedia parent){
		super(stationDirectory, parent);
		log("Loading...");
		
		// Loop through the contents of the station directory.
		File[] directoryContents = getFile().listFiles();
		for (int i=0; i<directoryContents.length; i++) {
			
			// For every file in the station directory, if it is a folder then add it
			// to the list of categories in this station. 
			if(MediaLibrary.isStandardDir(directoryContents[i])){
				CategoryMedia categoryToAdd = new CategoryMedia(directoryContents[i],this);
				
				// If the category is not empty, add it to the list.
				if(!categoryToAdd.isEmpty()) categories.put(categoryToAdd.getName(),categoryToAdd);
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
	 * Prefetches the images associated with this station.
	 * @param waitForThumbImage true if we should wait for the thumbnail image to load
	 * @param waitForCentralImage true if we should wait for the central image to load
	 */
	public void prefetchImages(boolean waitForThumbImage, boolean waitForCentralImage) {
		Iterator i = this.getCategories();
		while(i.hasNext()){
			((CategoryMedia)i.next()).prefetchImages(waitForThumbImage, waitForCentralImage);
		}
	}
	
	//-------------------------------------------------------------------------
	// END Prefetching
	//-------------------------------------------------------------------------
	
}
