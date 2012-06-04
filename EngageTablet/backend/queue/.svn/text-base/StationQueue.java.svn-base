package backend.queue;

import java.util.Vector;

import main.Logger;

import backend.library.MediaLibrary;

/**
 * This class contains all of the GroupQueues that a particular instructor has
 * associated with a particular station.
 * 
 * @author Kayre Hylton
 *
 */
public class StationQueue {

	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------

	/**
	 * A vector of GroupQueues
	 */
	private Vector<GroupQueue> groups;
	/**
	 * The name of the station
	 */
	private String stationName;
	/**
	 * The path for the directory that holds instructor files
	 */
	private String path;
	/**
	 * The MediaLibrary
	 */
	private MediaLibrary library;
	/**
	 * The QueueLibrary
	 */
	private QueueLibrary qLibrary;
	
	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------

	/**
	 * Returns the groups an instructor has associated with this station
	 * @return a Vector of GroupQueues
	 */
	public Vector getGroups(){
		return groups;
	}

	/**
	 * Sets the groups an instructor has associated with this station
	 * @param groups a Vector of GroupQueues
	 */
	public void setGroups(Vector<GroupQueue> groups){
		this.groups = groups;
	}
	
	/**
	 * Gets the name of the station
	 * @return the name of the station
	 */
	public String getStationName(){
		return this.stationName;
	}

	/**
	 * Sets the name of the station
	 * @param stationName the name of the station
	 */
	public void setStationName(String stationName){
		this.stationName = stationName;
	}

	/**
	 * Sets the path for the directory that holds instructor files
	 * @param path the path for the directory that holds instructor files
	 */
	public void setPath(String path){
		this.path = path;
	}
	
	/**
	 * Gets the path for the directory that holds instructor files
	 * @return the path for the directory that holds instructor files
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
	 * Gets the QueueLibrary
	 * @return the QueueLibrary
	 */
	public QueueLibrary getQLibrary() {
		return qLibrary;
	}

	/**
	 * Sets the QueueLibrary
	 * @param library the QueueLibrary
	 */
	public void setQLibrary(QueueLibrary library) {
		qLibrary = library;
	}
		
	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------



	/**
	 * Creates a StationQueue from a path, MediaLibrary, QueueLibrary and station name.
	 * 
	 * @param path the path to the directory containing instructor files
	 * @param library the MediaLibrary
	 * @param qLibrary the QueueLibrary that manages this
	 * @param stationName the name of the station
	 */
	public StationQueue(String path, MediaLibrary library, 
			QueueLibrary qLibrary, String stationName) {
		setStationName(stationName);
		groups = new Vector<GroupQueue>();
		setPath(path);
		setMediaLibrary(library);
		setQLibrary(qLibrary);
	}

	/**
	 * Creates a StationQueue from a path, MediaLibrary, QueueLibrary and vector 
	 * of strings of XML that represent a station.
	 * 
	 * @param path the path to the directory containing instructor files
	 * @param library the MediaLibrary
	 * @param qLibrary the QueueLibrary that manages this
	 * @param station a vector containing strings. Each string is a line of XML, 
	 * which together represent a station
	 */
	protected StationQueue(String path, MediaLibrary library, 
			QueueLibrary qLibrary, Vector station) {
		groups = new Vector<GroupQueue>();
		setPath(path);
		setMediaLibrary(library);
		setQLibrary(qLibrary);
		
		if(station != null){
			
			for(int i=0; i<station.size(); i++){
				if(i==0){//set the station name
					String name = (String)station.get(i);
					setStationName(name.substring(6, name.length()-7));
				}
				else{
					Vector<String> group = new Vector<String>();
					if(((String)station.get(i)).equals("<group>")){
						i++;
						//add lines between group tags to a vector
						while(!((String)station.get(i)).equals("</group>")){
							group.add((String)station.get(i));
							i++;
						}
						//create a group from vector
						addInitialGroupQueue(new GroupQueue(path, library, qLibrary, group));
					}
				}
			}
			Logger.log(Logger.INFO, "StationQueue: Groups loaded for station " + stationName);
		}
	}

	
	//-------------------------------------------------------------------------
	// GroupQueue Methods
	//-------------------------------------------------------------------------

	/**
	 * Gets a specific GroupQueue
	 * @param group a String of the name of the group you want
	 * @return a GroupQueue for the group specified
	 */
	public GroupQueue getGroupQueue(String group){
		for(int i=0; i<groups.size(); i++){
			if(((GroupQueue)groups.get(i)).getGroupName().equals(group)){
				return (GroupQueue)groups.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Adds a specific GroupQueue to the collection of GroupQueues. This is
	 * only called during initial creation of this station.
	 * @param group the group to add to the collection of GroupQueues
	 */
	private void addInitialGroupQueue(GroupQueue group) {
		
		groups.add(group);
	}
	
	/**
	 * Adds a specific GroupQueue to the collection of GroupQueues
	 * @param group the group to add to the collection of GroupQueues
	 */
	public void addGroupQueue(GroupQueue group) {
		
		groups.add(group);
		qLibrary.saveQueues();
	}
	
	/**
	 * Adds a specific GroupQueue to a particular position in the 
	 * collection of GroupQueues
	 * @param group the GroupQueue to add
	 * @param position the position to add the GroupQueue to
	 */
	public void addGroupQueue(GroupQueue group, int position){
		if(position >= 0 && position < groups.size()){
			groups.add(position, group);
		}
		else{
			Logger.log(Logger.ERROR, "StationQueue: Can't add " + group.getGroupName() + " to position " + 
					position + " in station " + stationName + ". Invalid index");
		}
		qLibrary.saveQueues();
	}
	
	/**
	 * Removes a specific GroupQueue from the collection of GroupQueues
	 * @param group the GroupQueue to remove
	 */
	public void removeGroupQueue(GroupQueue group){
		groups.remove(group);
		qLibrary.saveQueues();
	}
	
	/**
	 * Removes the GroupQueue at a particular position in the collection 
	 * of GroupQueues
	 * @param position the position of the GroupQueue to remove
	 */
	public void removeGroupQueue(int position){
		if(position >= 0 && position < groups.size()){
			groups.remove(position);
		}
		else{
			Logger.log(Logger.ERROR, "StationQueue: Can't remove group at position " + 
					position + " in station " + stationName + ". Invalid index");
		}
		qLibrary.saveQueues();
	}
	
	/**
	 * Moves a GroupQueue from one position in the collection of GroupQueues to another.<br/>
	 * Note: the specified end position may not be the index of the position that the GroupQueue
	 * ends up in. For example:<br/><br/>
	 * Set of groups: {A, B, C, D, E}<br/>
	 * moveGroupQueue(0, 3) produces {B, C, A, D, E}.<br/>
	 * The group that was at position 0 is moved to where the group that was at position 3 was
	 * (i.e. after the "C"), even though it is now in position 2.
	 * @param position1 the current position of the GroupQueue to move
	 * @param position2 the position to move the GroupQueue to
	 */
	public void moveGroupQueue(int position1, int position2){
		if(position1 >= 0 && position1 < groups.size()){
			if(position2 >= 0 && position2 < groups.size()){
				if(position1 < position2){
					GroupQueue toMove = (GroupQueue)groups.remove(position1);
					groups.add(position2 - 1, toMove);
				}
				if(position2 < position1){
					GroupQueue toMove = (GroupQueue)groups.remove(position1);
					groups.add(position2, toMove);
				}
			}
			else{
				Logger.log(Logger.ERROR, "StationQueue: Can't move group to position " + 
						position2 + " in station " + stationName + ". Invalid index");
			}
		}
		else{
			Logger.log(Logger.ERROR, "StationQueue: Can't move group at position " + 
					position1 + " in station " + stationName + ". Invalid index");
		}
		qLibrary.saveQueues();
	}

	
	//-------------------------------------------------------------------------
	// XML methods
	//-------------------------------------------------------------------------

	/**
	 * Converts the current state of the StationQueue to XML tags
	 * @return a String of XML representing the StationQueue
	 */
	public String toXML() {
		String output = "";
		
		output += "<station>\n";
		output += "<name>" + stationName + "</name>\n";
		
		for(int i=0; i<groups.size(); i++){
			output += ((GroupQueue)groups.get(i)).toXML();
		}
		
		output += "</station>\n";
		
		return output;
	}

	public StationQueue copy() {
		StationQueue copy = new StationQueue(this.path, this.library, 
				this.qLibrary, this.stationName);
		copy.setGroups((Vector)this.getGroups().clone());
		return copy;
	}
	
}
