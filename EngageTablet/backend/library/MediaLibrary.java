package backend.library;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeMap;

import main.Logger;

/**
 * The MediaLibrary contains all of the media files that Pittsburgh Voyager 
 * uses.
 * 
 * @author Carl Angiolillo
 *
 */
public class MediaLibrary extends LibraryObject{

	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------

	/* 
	 * The list of all educational programs in this library.
	 */ 
	private TreeMap<String, ProgramMedia> programs = new TreeMap<String, ProgramMedia>(); 

	//-------------------------------------------------------------------------
	// END Private Fields
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------
	
	/**
	 * Returns an Iterator of ProgramMedia files.
	 * 
	 * @return An Iterator of ProgramMedia files.
	 * @see ProgramMedia
	 */
	public Iterator getPrograms(){
		return programs.values().iterator();
	}
	
	/**
	 * Used to procure a list of the names of all of the programs.
	 * @return a String[] containing all of the program names.
	 */
	public String[] getProgramNames(){
		Iterator progs = this.getPrograms();
		String[] names = new String[programs.size()];
		for(int i=0; i<programs.size(); i++){
			names[i] = ((ProgramMedia) progs.next()).getName();
		}
		return names;
	}

	/**
	 * Given the name of an educational program, returns a ProgramMedia with this name,
	 * or null if none is found.
	 * 
	 * @param name the name of the program to seek for.
	 * @return A ProgramMedia with this name, or null if none is found.
	 */
	public ProgramMedia getProgram(String name){
		return (ProgramMedia)programs.get(name);
	}
	
	/**
	 * This method uses a relative pathname to find the associated MediaFile.
	 * All paths are of the form: "Data/Library/Program/Station/Category/file.ext"
	 * 
	 * @param path The relative location of the media. This must be a unix-style path (using '/')
	 * @return A MediaFile representing that location, or null if path is invalid.
	 */
	public MediaFile getMediaFile(String path){
		
		// Split the path into each of the component parts along the file separator.
		// This application uses the unix standard for hierarchical structure.
		String[] p = path.split("/");
		if(p.length!=4)Logger.log(Logger.ERROR,"Invalid file path:"+path+". File paths should have a depth of four.");

		// TODO I should probably take out these null constructors...
		ProgramMedia program = null;
		StationMedia station = null;
		CategoryMedia category = null;
		MediaFile file = null;
		
		// We can ignore p[0] and p[1] because they are merely the name of the database and
		// library respecitvely. They will most likely be "Data" and "Library".
		try{program = this.getProgram(p[0]);}
		catch (Exception e){}
		if (program == null){
			Logger.log(Logger.ERROR,"Unable to locate program \""+p[0]+"\" on path: "+path);
			return null;
		}
		
		try{station = program.getStation(p[1]);}
		catch (Exception e){}
		if (station == null){
			Logger.log(Logger.ERROR,"Unable to locate station \""+p[1]+"\" on path: "+path);
			return null;
		}
		
		try{category = station.getCategory(p[2]);}
		catch (Exception e){}
		if (category == null){
			Logger.log(Logger.ERROR,"Unable to locate category \""+p[2]+"\" on path: "+path);
			return null;
		}
		
		try{file = category.getMediaFile(p[3]);}
		catch (Exception e){}
		if (file == null) {
			Logger.log(Logger.ERROR,"Unable to locate media file \""+p[3]+"\" on path: "+path);
			return null;
		}
		
		log("File found at: "+path);
		return file;
	}
	
	//-------------------------------------------------------------------------
	// END Getters and Setters
	//-------------------------------------------------------------------------
	
	
	
	//-------------------------------------------------------------------------
	// Prefetching
	//-------------------------------------------------------------------------

	/**
	 * Goes through the MediaLibrary, and "prefetches" the images. This means
	 * loading them if they are not already loaded, and waiting for them to load
	 * depending on the values of the two booleans.
	 * 
	 * @param programName A string containing the name of the program to "prefetch"
	 * @param waitForThumbImage true if we should use a MediaTracker to ensure that the thumbnail is loaded before returning.
	 * @param waitForCentralImage true if we should use a MediaTracker to ensure that the large central image is loaded before returning.
	 */
	public void prefetchProgramImages(String programName, boolean waitForThumbImage, boolean waitForCentralImage){
		long startTime = (new GregorianCalendar()).getTimeInMillis();
		Logger.log(Logger.IMPORTANT, "MediaLibrary: Prefetching images.");
		getProgram(programName).prefetchImages(waitForThumbImage, waitForCentralImage);

		long endTime = (new GregorianCalendar()).getTimeInMillis();
		double elapsedTime = ((double)(endTime-startTime))/1000;
		Logger.log(Logger.IMPORTANT,"MediaLibrary: Done! Took " + elapsedTime + " seconds to prefetch \""+programName+"\" program images.");
	}

	//-------------------------------------------------------------------------
	// END Prefetching
	//-------------------------------------------------------------------------

	

	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	
	/**
	 * This constructor will turn a pathname into the entire library
	 * for use with the OnDemandMedia application. It expects the path of the
	 * main "Library" folder, which is where the library is kept.
	 * 
	 * @param libraryPath The (absolute or relative) location of the Library folder.
	 */
	public MediaLibrary(String libraryPath) {
		super(convertPathToDirectory(libraryPath), null);
		
		// If the path sucessfully resolved to a directory, begin loading the library.
		if(this.getFile() != null) {
			long startTime = (new GregorianCalendar()).getTimeInMillis();
			Logger.log(Logger.IMPORTANT,"MediaLibrary: Loading library from \""+this.getFile().getAbsolutePath()+"\".");
			
			// Loop through the contents of the library directory.
			File[] directoryContents = this.getFile().listFiles();
			for (int i=0; i<directoryContents.length; i++) {
				
				// Every folder in the main library directory represents another educational program.
				if(isStandardDir(directoryContents[i])) {
					ProgramMedia programToAdd = new ProgramMedia(directoryContents[i],this);
					
					// If the program is not empty, add it to the list of programs.
					if(!programToAdd.isEmpty()) programs.put(programToAdd.getName(),programToAdd);
				}
			}
			long endTime = (new GregorianCalendar()).getTimeInMillis();
			double elapsedTime = ((double)(endTime-startTime))/1000;
			Logger.log(Logger.IMPORTANT,"MediaLibrary: Done! Took " + elapsedTime + " seconds to load library.");
		} 
		
		// If the path was not resolved to a directory, we cannot load the library.
		else 
			Logger.log(Logger.ERROR, "Error loading library. I could not find the directory at \""+libraryPath+"\".");
		
	}

	//-------------------------------------------------------------------------
	// END Constructor
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Static Methods
	//-------------------------------------------------------------------------
	
	/*
	 * Converts a path into a directory if possible. If it is not possible for
	 * some reason, then the method returns null.
	 */
	private static File convertPathToDirectory (String path){
		File directory = null;
		
		// Attempt to load the folder containing the media library.
		try{
			directory = new File(path);
		}catch (NullPointerException e){
			Logger.log(Logger.IMPORTANT, "\""+path+"\" is a null path.");
		}
		
		// Make certain that we have actually loaded a directory.
		if(isStandardDir(directory))
			return directory;
		else
			return null;
	}
	
	/*
	 * Determines whether or not the File it is given is a standard directory,
	 * which means it must be a directory, but it also must not be a system
	 * directory in order to be considered "standard".
	 */
	protected static boolean isStandardDir(File file){
		
		// It must exist and be a directory.
		if (!file.exists()) return false;
		if (!file.isDirectory()) return false;
		
		// Also, we do not wish to look through system directories.
		if (file.getName().startsWith(".")) return false;
		if (file.isHidden()) return false;
		return true;
	}
	
	//-------------------------------------------------------------------------
	// END Static Methods
	//-------------------------------------------------------------------------

	
}
