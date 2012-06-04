package backend.library;

import java.io.File;

import javax.swing.JComponent;

import main.Logger;

/**
 * This very simple object provides the basis for all other LibraryObjects
 * (the ProgramMedia, CategoryMedia, MediaFiles, even the MediaLibrary itself)
 * It provides basic logging ability and access to the actual directory
 * or file that this LibraryObject originally came from.
 * 
 * @author Carl Angiolillo
 *
 */
public abstract class LibraryObject extends JComponent {

	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------

	private File myFile; 		// The file this LibraryObject is based on.
	private LibraryObject myParent; // The parent of this LibraryObject. Null
									// in the case of the MediaLibrary itself.

	//-------------------------------------------------------------------------
	// END Private Fields
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	/*
	 * Basic constructor. Takes the file and the parent of this Library Object
	 * and saves them.
	 */
	protected LibraryObject(File file, LibraryObject parent){
		myFile = file;
		myParent = parent;
	}

	//-------------------------------------------------------------------------
	// END Constructor
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------
	
	/*
	 * Gets the File associated with this particular LibraryObject.
	 */
	protected File getFile(){
		return myFile;
	}
	
	/**
	 * Gets the name of the file this LibraryObject is based on.
	 * 
	 * @return The name of the file this LibraryObject is based on including any optional numbers preceeding it or extensions after it.
	 */
	public String getName(){
		
		return myFile.getName();
	}

	/**
	 * Gets the name that is suitable for displaying. It removes any extension on this file, as well as 
	 * any numerical prefixes of the form "number,number,hyphen" e.g. "01-" or "12-". These prefixes
	 * are used to alphabetically order the library objects but sholud not be displayed.
	 * 
	 * @return The name of this MediaFile not including the extension and any numerical prefixes of the form "##-"
	 */
	public String getDisplayName(){
		String displayName = this.getName();
		int beginIndex = 0;
		int endIndex = displayName.length();
		
		if(displayName.contains(".")) 
			endIndex = displayName.lastIndexOf(".");
		
		//FIXME THis doesn't work
		if(	displayName.length() > 3 && 
			Character.isDigit(displayName.charAt(0))&&
			Character.isDigit(displayName.charAt(1))&&
			displayName.indexOf("-") == 2)
			beginIndex = 3;
	
		return displayName.substring(beginIndex,endIndex);
	}
	
	/*
	 * @return The parent of this LibraryObject.
	 */
	public LibraryObject getParent(){
		return myParent;
	}
	
	//-------------------------------------------------------------------------
	// END Getters and Setters
	//-------------------------------------------------------------------------
	
	
	//-------------------------------------------------------------------------
	// Logging Methods
	//-------------------------------------------------------------------------
	
	/**
	 * This is the standard log message for all library classes. Recursively 
	 * calls the logging method of the parent.
	 * 
	 * @param message The message to display.
	 */
	public void log(String message){
		if(myParent != null)
			myParent.log(" "+this.getName()+": "+message);
		else 
			Logger.log(Logger.INFO,"MediaLibrary: "+message);	
	}
	
	//-------------------------------------------------------------------------
	// END Logging Methods
	//-------------------------------------------------------------------------
	
}
