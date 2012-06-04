package backend.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import main.Logger;

/**
 * Loads the configuration preferences from the settings.cfg file
 * 
 * @author Kayre Hylton
 *
 */
public class ConfigReader {
	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------

	
	/**
	 * The password for deleting instructors and editting the default instructor
	 */
	private String password = "searchlight";
	
	/**
	 * The Vector of adresses for display screens
	 */
	private Vector<String> screens = new Vector<String>();
	
	/**
	 * The relative location of the Instructors folder
	 */
	private String instructorsFolderPath = "." + File.separator + "Data" +
	File.separator + "Instructors";
	
	/**
	 * The relative location of the Library folder
	 */
	private String libraryFolderPath = "." + File.separator + "Data" +
	File.separator + "Library";
	
	/**
	 * Whether or not the tablet is being used
	 */
	private boolean onTablet = false;
	
	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------

	/**
	 * Returns the password for deleting instructors or editing the default
	 * @return the password for deleting instructors or editing the default
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the password for deleting instructors or editing the default
	 * @param line the line from the settings file with the password
	 */
	private void setPassword(String line) {
		password = line.substring(line.indexOf("=")+1).trim();
	}
	
	/**
	 * Returns the addresses of screens to send images to
	 * @return a Vector of screen addresses
	 */
	public Vector getScreens() {
		return screens;
	}
	
	/**
	 * Returns the address of the screen to send images to
	 * @return the address of the screen to send images to 
	 */
	public String getScreen(){
		return (String)screens.get(0);
	}
	
	/**
	 * Adds a screen adress to the display screens
	 * @param line the line in the settings file with the screen address
	 */
	private void addScreen(String line) {
		String screen = line.substring(line.indexOf("=")+1).trim();
		if(screen.equals("none")){
			screen = null;
		}
		screens.add(screen);		
	}

	/**
	 * Returns the relative location of the Instructors folder
	 * @return the relative location of the Instructors folder
	 */
	public String getInstructorsFolderPath() {
		return instructorsFolderPath;
	}
	
	/**
	 * Sets the relative location of the Instructors folder
	 * @param line the line in the settings file with the relative 
	 * location of the Instructors folder
	 */
	private void setInstructorsFolderPath(String line) {
		instructorsFolderPath = line.substring(line.indexOf("=")+1).trim();
		instructorsFolderPath = instructorsFolderPath.replace("/", File.separator);
		instructorsFolderPath = instructorsFolderPath.replace("\\", File.separator);
	}

	/**
	 * Returns the relative location of the Library folder
	 * @return the relative location of the Library folder
	 */
	public String getLibraryFolderPath() {
		return libraryFolderPath;
	}

	/**
	 * Sets the relative location of the Library folder
	 * @param line the line in the settings file with the relative 
	 * location of the Library folder
	 */
	private void setLibraryFolderPath(String line) {
		libraryFolderPath = line.substring(line.indexOf("=")+1).trim();
		libraryFolderPath = libraryFolderPath.replace("/", File.separator);
		libraryFolderPath = libraryFolderPath.replace("\\", File.separator);
	}

	/**
	 * Returns whether or not this is running on a tablet
	 * @return true if this is running on a tablet, false otherwise
	 */
	public boolean isOnTablet() {
		return onTablet;
	}

	/**
	 * Sets whether or not this is running on a tablet
	 * @param line the line in the settings file with whether or not this 
	 * is running on a tablet
	 */
	private void setOnTablet(String line) {
		String screen = line.substring(line.indexOf("=")+1).trim();
		if(screen.equals("no")){
			onTablet = false;
		}
		if(screen.equals("yes")){
			onTablet = true;
		}
	}


	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	/**
	 * Creates a ConfigReader and loads the values from the settings file
	 *
	 */
	public ConfigReader(String settingsFile){
		
		File configFile = new File(settingsFile);
		String line;
		if(configFile.exists()){
			try {
				FileReader fReader = new FileReader(configFile);
				BufferedReader bReader = new BufferedReader(fReader);
				
				line = bReader.readLine();
				while(line != null){
					line.trim();
					if(!line.startsWith("#") && !line.equals("")){
						if(line.startsWith("password")){
							setPassword(line);
						}
						else if(line.startsWith("screen")){
							addScreen(line);
						}
						else if(line.startsWith("instructors_folder")){
							setInstructorsFolderPath(line);
						}
						else if(line.startsWith("library_folder")){
							setLibraryFolderPath(line);
						}
						else if(line.startsWith("on_tablet")){
							setOnTablet(line);
						}
					}
					line = bReader.readLine();
				}
				bReader.close();
			} catch (IOException e) {
				Logger.log(Logger.ERROR, "ConfigReader: Cannot read from settings file.");
			}	
		}
		else{
			Logger.log(Logger.ERROR, "ConfigReader: Settings file does not exist.");
		}
		if(screens.isEmpty()){
			screens.add(null);
		}
	}


}
