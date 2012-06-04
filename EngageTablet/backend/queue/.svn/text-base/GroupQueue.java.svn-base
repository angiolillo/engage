package backend.queue;

import java.util.Vector;

import main.Logger;

import backend.library.MediaFile;
import backend.library.MediaLibrary;

/**
 * This class contains all the MediaFiles which are associated with a group
 * 
 * @author Kayre Hylton
 *
 */
public class GroupQueue {
	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------

	/**
	 * A vector of MediaFiles
	 */
	private Vector<MediaFile> mediaFiles;
	/**
	 * The name of a group
	 */
	private String groupName;
	/**
	 * The path for the directory that holds instructor files
	 */
	private String path;
	/**
	 * The MediaLibrary
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
	 * Returns the MediaFiles associated with this instructor group
	 * @return a Vector of MediaFiles
	 */
	public Vector getMediaFiles(){
		return mediaFiles;
	}

	/**
	 * Sets the MediaFiles associated with the instructor group
	 * @param mediaFiles a Vector of MediaFiles
	 */
	public void setMediaFiles(Vector<MediaFile> mediaFiles){
		this.mediaFiles = mediaFiles;
		qLibrary.saveQueues();
	}
	
	/**
	 * Sets the name of the group
	 * @param groupName a String of the name of the group
	 */
	public void setGroupName(String groupName){
		this.groupName = groupName;
		qLibrary.saveQueues();
	}
	
	/**
	 * Gets the name of the group
	 * @return a Stinr of the name of the group
	 */
	public String getGroupName(){
		return groupName;
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
	public QueueLibrary getQLibrary(){
		return qLibrary;
	}
	
	/**
	 * Sets the QueueLibrary
	 * @param qLibrary the QueueLibrary
	 */
	public void setQLibrary(QueueLibrary qLibrary){
		this.qLibrary = qLibrary;
	}
	
	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------

	/**
	 * Creates a GroupQueue from a path, MediaLibrary, QueueLibrary and group name.
	 * 
	 * @param path the path to the directory containing instructor files
	 * @param library the MediaLibrary
	 * @param qLibrary the QueueLibrary that manages this
	 * @param groupName the name of the group
	 */
	public GroupQueue(String path, MediaLibrary library, QueueLibrary qLibrary, String groupName) {
		this.groupName = groupName;
		this.mediaFiles = new Vector<MediaFile>();
		setPath(path);
		setMediaLibrary(library);
		setQLibrary(qLibrary);
	}

	/**
	 * Creates a GroupQueue from a path, MediaLibrary, QueueLibrary and vector 
	 * of strings of XML that represent a group.
	 * 
	 * @param path the path to the directory containing instructor files
	 * @param library the MediaLibrary
	 * @param qLibrary the QueueLibrary that manages this
	 * @param group a vector containing strings. Each string is a line of XML, 
	 * which together represent a group
	 */
	protected GroupQueue(String path, MediaLibrary library, QueueLibrary qLibrary, Vector group) {
		this.mediaFiles = new Vector<MediaFile>();
		setPath(path);
		setMediaLibrary(library);
		setQLibrary(qLibrary);
		
		if(group != null){
			
			for(int i=0; i<group.size(); i++){
				if(i==0){
					String name = (String)group.get(i);
					this.groupName = name.substring(6, name.length()-7);
				}
				else{
					String mediaPath = (String)group.get(i);
					mediaPath = mediaPath.substring(11, mediaPath.length()-12);
					MediaFile testFile = this.library.getMediaFile(mediaPath);
					if(testFile != null){
					addMediaFile(testFile);
					}
				}
			}
			Logger.log(Logger.INFO, "GroupQueue: Media files loaded for group " + groupName);
		}
	}

	
	//-------------------------------------------------------------------------
	// MediaFile Methods
	//-------------------------------------------------------------------------

	
	/**
	 * Adds a specific MediaFile to a collection of MediaFiles
	 * @param file the MediaFile to add
	 */
	public void addMediaFile(MediaFile file){
		
		mediaFiles.add(file);
		
	}
	
	/**
	 * Adds a specific MediaFile to a particular position in the 
	 * collection of MediaFiles
	 * @param file the MediaFile to add
	 * @param position the position to add the MediaFile to
	 */
	public void addMediaFile(MediaFile file, int position){
		if(position >= 0 && position < mediaFiles.size()){
			mediaFiles.add(position, file);
		}
		else{
			Logger.log(Logger.ERROR, "GroupQueue: Can't add " + file.getName() + " to position " + 
					position + " in group " + groupName + ". Invalid index");
		}
		qLibrary.saveQueues();
	}
	
	/**
	 * Removes a specific MediaFile from a collection of MediaFiles
	 * @param file the MediaFile to remove
	 */
	public void removeMediaFile(MediaFile file){
		mediaFiles.remove(file);
		qLibrary.saveQueues();
	}
	
	/**
	 * Removes the MediaFile at a specific position in the collection 
	 * of MediaFiles
	 * @param position the position of the MediaFile to remove
	 */
	public void removeMediaFile(int position){
		if(position >= 0 && position < mediaFiles.size()){
			mediaFiles.remove(position);
		}
		else{
			Logger.log(Logger.ERROR, "GroupQueue: Can't remove file at position " + 
					position + " in group " + groupName + ". Invalid index");
		}
		qLibrary.saveQueues();
	}
	
	/**
	 * Moves a MediaFile from one position in the collection of MediaFiles to another.<br/>
	 * Note: the specified end position may not be the index of the position that the MediaFile
	 * ends up in. For example:<br/><br/>
	 * Set of MediaFiles: {A, B, C, D, E}<br/>
	 * moveMediaFile(0, 3) produces {B, C, A, D, E}.<br/>
	 * The MediaFile that was at position 0 is moved to where the MediaFile that was at position 3 was
	 * (i.e. after the "C"), even though it is now in position 2.
	 * @param position1 the current position of the MediaFile to move
	 * @param position2 the position to move the MediaFile to
	 */
	public void moveMediaFile(int position1, int position2){
		if(position1 >= 0 && position1 < mediaFiles.size()){
			if(position2 >= 0 && position2 < mediaFiles.size()){
				if(position1 < position2){
					MediaFile toMove = (MediaFile)mediaFiles.remove(position1);
					mediaFiles.add(position2 - 1, toMove);
				}
				if(position2 < position1){
					MediaFile toMove = (MediaFile)mediaFiles.remove(position1);
					mediaFiles.add(position2, toMove);
				}
			}
			else{
				Logger.log(Logger.ERROR, "GroupQueue: Can't move file to position " + 
						position2 + " in group " + groupName + ". Invalid index");
			}
		}
		else{
			Logger.log(Logger.ERROR, "GroupQueue: Can't move file at position " + 
					position1 + " in group " + groupName + ". Invalid index");
		}
		qLibrary.saveQueues();
	}
	
	
	
	/**
	 * Converts the current state of the GroupQueue to XML tags
	 * @return a String of XML representing the GroupQueue
	 */
	public String toXML() {
		String output = "";
		
		output += "<group>\n";
		output += "<name>" + groupName + "</name>\n";
		
		for(int i=0; i<mediaFiles.size(); i++){
			if(mediaFiles.get(i) != null){
				output += "<mediaFile>" + ((MediaFile)mediaFiles.get(i)).getPath()
				+ "</mediaFile>\n";
			}
		}
		
		output += "</group>\n";
		return output;
	}
	
}
