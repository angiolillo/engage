package backend.library;

import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * This class is used to store all of the media for an individual educational program for 
 * Pittsburgh Voyager. Primary use will be in the Enviornmental Science program, though
 * it can equally well be used for any other program.
 * 
 * @author Carl Angiolillo
 *
 */
public class ProgramMedia extends LibraryObject{
	
	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------

	/* The list of all stations (Birds, Watersheds, Plankton, etc) in this 
	 * particular educational program.
	 */
	private TreeMap<String, StationMedia> stations = new TreeMap<String, StationMedia>();

	//-------------------------------------------------------------------------
	// END Private Fields
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------
	
	/**
	 * Returns an Iterator of the StationMedia in this program.
	 * 
	 * @return An Iterator of all of the StationMedia in this particular program.
	 * @see StationMedia
	 */
	public Iterator getStations(){
		return stations.values().iterator();
	}
	
	/**
	 * Given a name, this method will return a StationMedia with that name, or null
	 * if none is found.
	 * 
	 * @param name the name of the station to seek for.
	 * @return A StationMedia with this name, or null if none is found.
	 */
	public StationMedia getStation(String name){
		return (StationMedia)stations.get(name);
	}
	
	/**
	 * Returns true if this ProgramMedia contains no staitons.
	 * 
	 * @return true if there are no stations in this program.
	 */
	public boolean isEmpty(){
		return stations.size()==0;
	}

	
	//-------------------------------------------------------------------------
	// END Getters and Setters
	//-------------------------------------------------------------------------
	
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	/**
	 * Creates a ProgramMedia from a directory containing several categories
	 * and a reference to the educational program in which this station resides.
	 * 
	 * @param programDirectory The directory contanining the images for this program.
	 * @param parent A reference to the MediaLibrary in which this program resides.
	 */
	public ProgramMedia(File programDirectory, MediaLibrary parent){
		super(programDirectory, parent);
		log("Loading...");
		
		// Loop through the contents of the program directory.
		File[] directoryContents = getFile().listFiles();
		for (int i=0; i<directoryContents.length; i++) {
			
			// For every file in the program directory, if it is a folder then add it
			// to the list of stations in this program. 
			if(MediaLibrary.isStandardDir(directoryContents[i])){
				StationMedia stationToAdd = new StationMedia(directoryContents[i],this);
				
				// IF the station is not empty, add it to the list.
				if(!stationToAdd.isEmpty()) stations.put(stationToAdd.getName(),stationToAdd);
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
	 * Prefetches the images associated with this program.
	 * @param waitForThumbImage true if we should wait for the thumbnail image to load
	 * @param waitForCentralImage true if we should wait for the central image to load
	 */
	public void prefetchImages(boolean waitForThumbImage, boolean waitForCentralImage) {
		Iterator i = this.getStations();
		while(i.hasNext()){
			((StationMedia)i.next()).prefetchImages(waitForThumbImage, waitForCentralImage);
		}
	}
	
	//-------------------------------------------------------------------------
	// END Prefetching
	//-------------------------------------------------------------------------
	
}
