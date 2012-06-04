package backend.queue;

import java.util.Vector;

import main.Logger;

import backend.library.MediaLibrary;

/**
 * This class contains all the StationQueues that are associated with this ProgramQueue.
 * A StationQueue should exist for every station that exists in a program, unless the
 * file heirarchy has been changed since the last login.
 * 
 * @author Kayre Hylton
 *
 */
public class ProgramQueue {

	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------
	
	/**
	 * A Vector of StationQueues
	 */
	private Vector<StationQueue> stations;
	/**
	 * The name of the program
	 */
	private String programName;
	/**
	 * The path for the directory that holds instructor files
	 */
	private String path;
	/**
	 * The Media Library
	 */
	private MediaLibrary library;
	/**
	 * The QueueLibrary that manages this
	 */
	private QueueLibrary qLibrary;
	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------
	
	/**
	 * Returns the stations associated with this program
	 * @return a Vector of StationQueues
	 */
	public Vector getStations(){
		return stations;
	}
	
	/**
	 * Sets the stations associated with this program
	 * @param stations a Vector of StationQueues
	 */
	public void setStations(Vector<StationQueue> stations){
		this.stations = stations;
	}

	/**
	 * Returns the name of the program
	 * @return the name of the program
	 */
	public String getProgramName(){
		return programName;
	}

	/**
	 * Sets the name of the program
	 * @param programName a string of the name to set the program to
	 */
	public void setProgramName(String programName){
		this.programName = programName;
	}

	/**
	 * Sets the path for the directory that holds instructor files
	 * @param path the path to the folder which holds instructor XML files
	 */
	public void setPath(String path){
		this.path = path;
	}
	
	/**
	 * Gets the path to the folder which holds instructor XML files
	 * @return the path to the folder which holds instructor XML files
	 */
	public String getPath(){
		return path;
	}
	
	/**
	 * Gets the MediaLibrary
	 * @return the MediaLibrary
	 */
	public MediaLibrary getMediaLibrary(){
		return library;
	}
	
	/**
	 * Sets the MediaLibrary
	 * @param library the MediaLibrary
	 */
	public void setMediaLibrary(MediaLibrary library){
		this.library = library;
	}
	
	/**
	 * Gets the QueueLibrary that manages this
	 * @return the QueueLibrary that manages this
	 */
	public QueueLibrary getQLibrary() {
		return qLibrary;
	}

	/**
	 * Sets the QueueLibrary that manages this
	 * @param library the QueueLibrary that manages this
	 */
	public void setQLibrary(QueueLibrary library) {
		qLibrary = library;
	}
	
	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------
	


	/**
	 * Creates a ProgramQueue from a path, MediaLibrary, QueueLibrary and program name.
	 * 
	 * @param path the path to the directory containing instructor files
	 * @param library the MediaLibrary
	 * @param qLibrary the QueueLibrary that manages this
	 * @param programName the name of the program
	 */
	public ProgramQueue(String path, MediaLibrary library, 
			QueueLibrary qLibrary, String programName) {
		setProgramName(programName);
		this.stations = new Vector<StationQueue>();
		setPath(path);
		setMediaLibrary(library);
		setQLibrary(qLibrary);
	}

	/**
	 * Creates a ProgramQueue from a path, MediaLibrary, QueueLibrary and vector 
	 * of strings of XML that represent a program.
	 * 
	 * @param path the path to the directory containing instructor files
	 * @param library the MediaLibrary
	 * @param qLibrary the QueueLibrary that manages this
	 * @param program a vector containing strings. Each string is a line of XML, 
	 * which together represent a program
	 */
	protected ProgramQueue(String path, MediaLibrary library, 
			QueueLibrary qLibrary, Vector program) {
		this.stations = new Vector<StationQueue>();
		setPath(path);
		setMediaLibrary(library);
		setQLibrary(qLibrary);
		
		if(program != null){
			
			for(int i=0; i<program.size(); i++){
				if(i==0){//save name of program
					String name = (String)program.get(i);
					setProgramName(name.substring(6, name.length()-7));
				}
				else{
					Vector<String> station = new Vector<String>();
					if(((String)program.get(i)).equals("<station>")){
						i++;
						//add lines between station tags to a vector
						while(!((String)program.get(i)).equals("</station>")){
							station.add(((String)program.get(i)));
							i++;
						}
						//create a station from vector
						addStationQueue(new StationQueue(path, library, qLibrary, station));
					}
				}
			}
			Logger.log(Logger.INFO, "ProgramQueue: Stations loaded for program " + programName);
		}
	}

	
	//-------------------------------------------------------------------------
	// StationQueue Methods
	//-------------------------------------------------------------------------
	
	
	/**
	 * Returns a specific StationQueue
	 * @param station a String of the name of a station
	 * @return the StationQueue for the station specified
	 */
	public StationQueue getStationQueue(String station){
		for(int i=0; i<stations.size(); i++){
			if(((StationQueue)stations.get(i)).getStationName().equals(station)){
				return (StationQueue)stations.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Adds a StationQueue to the collection of stations
	 * @param station the StationQueue to add to the collection of stations
	 */
	public void addStationQueue(StationQueue station) {
		
		stations.add(station);
	}

	/**
	 * Removes a StationQueue from the collection of stations
	 * @param stationName the StationQueue to remove from the collection of stations
	 */
	public void removeStationQueue(String stationName) {
		for(int i=0; i<stations.size(); i++){
			if(((StationQueue)stations.get(i)).getStationName().equals(stationName)){
				stations.remove(i);
				Logger.log(Logger.INFO, "ProgramQueue: Removed station " + stationName + 
						" from program " + programName);
			}
		}
		
	}
	
	//-------------------------------------------------------------------------
	// Other Methods
	//-------------------------------------------------------------------------
	
	
	/**
	 * Converts the current state of the ProgramQueue to XML tags
	 * @return a String of XML representing the ProgramQueue
	 */
	public String toXML() {
		String output = "";
		
		output += "<program>\n";
		output += "<name>" + programName + "</name>\n";
		
		for(int i=0; i<stations.size(); i++){
			output += ((StationQueue)stations.get(i)).toXML();
		}
		
		output += "</program>\n";
		return output;
	}

	/**
	 * Creates a copy of thie ProgramQueue
	 * @return a copy of this ProgramQueue
	 */
	public ProgramQueue copy() {
		ProgramQueue copy = new ProgramQueue(this.path, this.library, 
				this.qLibrary, this.programName);
		copy.setStations((Vector)this.getStations().clone());
		return copy;
	}



	
}
