package backend.queue;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import backend.library.MediaLibrary;
import backend.library.ProgramMedia;
import backend.library.StationMedia;
import main.Logger;

/**
 * This class manages all of the queues associated with an instructor.
 * The QueueLibrary holds a Vector of InstructorQueues.
 * 
 * @author Kayre Hylton
 *
 */
public class QueueLibrary {

	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------
	
	/**
	 * A Vector of InstructorQueues
	 */
	private Vector<InstructorQueue> instructors;
	/**
	 * The MediaLibrary
	 */
	private MediaLibrary library;
	/**
	 * The path for the directory that holds instructor files
	 */
	private String path;
	/**
	 * The current instructor
	 */
	private InstructorQueue currentInstructor;
	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------
	
	/**
	 * Returns all the instructors that have been created
	 * 
	 * @return A Vector of InstructorQueues
	 */
	public Vector getInstructors(){
		return instructors;
	}
	
	/**
	 * Sets the collection of instructors
	 * @param instructors a Vector of InstructorQueues
	 */
	public void setInstructors(Vector<InstructorQueue> instructors){
		this.instructors = instructors;
	}

	/**
	 * Returns the MediaLibrary
	 * 
	 * @return the MediaLibrary
	 */
	public MediaLibrary getMediaLibrary(){
		return library;
	}
	
	/**
	 * Sets the MediaLibrary
	 * 
	 * @param library the MediaLibrary
	 */
	public void setMediaLibrary(MediaLibrary library){
		this.library = library;
	}
	
	/**
	 * Sets the path that holds all the instructor files
	 * 
	 * @param path the path to the folder which holds instructor XML files
	 */
	public void setPath(String path){
		this.path = path;
	}
	
	/**
	 * Returns the path that holds all the instructor files
	 * 
	 * @return the path to the folder which holds instructor XML files
	 */
	public String getPath(){
		return path;
	}
	
	/**
	 * Returns the current instructor
	 * 
	 * @return the InstructorQueue for the current instructor
	 */
	public InstructorQueue getCurrentInstructor(){
		return currentInstructor;
	}
	
	/**
	 * Sets the current instructor
	 * @param currentInstructor the InstructorQueue for the current instructor
	 */
	public void setCurrrentInstructor(InstructorQueue currentInstructor){
		this.currentInstructor = currentInstructor;
	}
		
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	
	/**
	 * Creates a new QueueLibrary from a path and a MediaLibrary. It creates instructors
	 * from any existing instructor files, and ensures there is a default instructor
	 * 
	 * @param path the path where instructor files are located
	 * @param library the MediaLibrary
	 */
	public QueueLibrary(String path, MediaLibrary library){
		this.instructors = new Vector<InstructorQueue>();
		setMediaLibrary(library);
		setPath(path);
		loadFromXML();
		updateDefaultInstructor();
	}

	
	//-------------------------------------------------------------------------
	// InstructorQueue methods
	//-------------------------------------------------------------------------
	
	/**
	 * Gets a particular instructor
	 * 
	 * @param instructor a String of an instructor's name
	 * @return the InstructorQueue for a particular instructor
	 */
	public InstructorQueue getInstructorQueue(String instructor){
		for(int i=0; i<instructors.size(); i++){
			if(((InstructorQueue)instructors.get(i)).getInstructorName().equals(instructor)){
				return (InstructorQueue)instructors.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Returns an array of the names of all the instructors
	 * @return an Array of Strings of instructor names
	 */
	public String[] getInstructorNames(){
		String[] names = new String[instructors.size()];
		int nameIndex = 0;
		for(int i=0; i<instructors.size(); i++){
			String name = ((InstructorQueue)instructors.get(i)).getInstructorName();
			//skip default when adding names
			if(!name.equals("default")){
				names[nameIndex] = name;
				nameIndex++;
			}
		}
		//add default to the end
		if(nameIndex < names.length){
		names[nameIndex] = "edit default";
		}
		return names;
	}
	
	/**
	 * Adds a particular instructor to the collection of InstructorQueues
	 * @param instructor the InstructorQueue to add to instructors
	 */
	public void addInstructorQueue(InstructorQueue instructor) {
		this.instructors.add(instructor);
		
	}

	/**
	 * Removes a particular instructor from the collection of InstructorQueues
	 * @param instructorName the name of the InstructorQueue to remove from instructors
	 */
	public void removeInstructorQueue(String instructorName) {
		for(int i=0; i<instructors.size(); i++){
			if(((InstructorQueue)instructors.get(i)).getInstructorName().equals(instructorName)){
				instructors.remove(i);
				File toDelete = new File(path + File.separator + instructorName + ".xml");
				if(toDelete != null){
					toDelete.delete();
				}
				Logger.log(Logger.INFO, "QueueLibrary: Removed instructor " + instructorName);
			}
		}
	}
	
	/**
	 * Removes particular instructors from the collection of InstructorQueues
	 * @param instructorNames a Array of Strings of names of instructors to remove
	 */
	public void removeInstructors(Vector instructorNames){
		for(int i=0; i<instructorNames.size(); i++){
			removeInstructorQueue((String)instructorNames.get(i));
		}
		
	}
	
	//-------------------------------------------------------------------------
	// StationQueue Methods
	//-------------------------------------------------------------------------
	

	/**
	 * Returns all of the StationQueues associated with a particular instructor and program
	 * @param instructor the logged-in instructor
	 * @param program the selected program
	 * @return a Vector of the StationQueues associated with the specified 
	 * instructor-program combination
	 */
	public Vector getStationQueues(String instructor, String program){
		InstructorQueue iQueue = this.getInstructorQueue(instructor);
		InstructorQueue defaultInstructor = this.getInstructorQueue("default");
		boolean isChanged = false;
		if(iQueue == null){//if no instructor exists, create one from the default
			isChanged = true;
			Logger.log(Logger.INFO, "QueueLibrary: Instructor " + instructor + " does not exist");
			this.addInstructorQueue(new InstructorQueue(path, library, this, instructor));
			Logger.log(Logger.IMPORTANT, "QueueLibrary: Instructor " + instructor + 
			" created from default instructor");
			iQueue = this.getInstructorQueue(instructor);
		}
		
		if(library.getProgram(program) != null){//program does exist
			this.setCurrrentInstructor(iQueue);
			ProgramQueue pQueue = iQueue.getProgramQueue(program);
			ProgramQueue defaultPQueue = defaultInstructor.getProgramQueue(program);
			if(pQueue != null){//program exists in this instructor's profile
				Vector savedStations = pQueue.getStations();
				Vector currentStations = defaultPQueue.getStations();
				//make sure all the stations are in the saved profile
				for(int i = 0; i<currentStations.size(); i++){
					String stationName = ((StationQueue)currentStations.get(i)).getStationName();
					if(pQueue.getStationQueue(stationName) == null){
						pQueue.addStationQueue(((StationQueue)currentStations.get(i)).copy());
						Logger.log(Logger.INFO, "QueueLibrary: Adding Station" + stationName +
								" from the default to instructor " + iQueue.getInstructorName());
						isChanged = true;
					}
				}
				
				//remove stations which no longer exist
				for(int i = 0; i<savedStations.size(); i++){
					String stationName = ((StationQueue)savedStations.get(i)).getStationName();
					if(defaultPQueue.getStationQueue(stationName) == null){
						pQueue.removeStationQueue(stationName);
						Logger.log(Logger.INFO, "QueueLibrary: Removing Station" +	stationName +
								" from instructor " + iQueue.getInstructorName() + " as it no longer exists");
						isChanged = true;
					}
				}
				
				if(isChanged){
					this.saveQueues();
				}
				
				return pQueue.getStations();
			}
			else{//program does not exist for this instructor
				Logger.log(Logger.INFO, "QueueLibrary: Program " + program + 
						" does not exist for instructor" + instructor);
				iQueue.addProgramQueue(defaultPQueue.copy());
				this.saveQueues();
				return iQueue.getProgramQueue(program).getStations();
			}
		}
		else{//program does not exist
			Logger.log(Logger.ERROR, "QueueLibrary: Program " + program + " does not exist");
			return null;
		}
		
	}
	
	
	
	//-------------------------------------------------------------------------
	// XML Methods
	//-------------------------------------------------------------------------
	
	
	/**
	 * Goes through the files in the instructor directory and creates InstructorQueues 
	 * from the XML and adds them to instructors
	 */
	private void loadFromXML() {
		File instructorDirectory = new File(path);
		if(instructorDirectory != null){
			if(instructorDirectory.isDirectory()){
				File[] instructorProfiles = instructorDirectory.listFiles();
				for(int i=0; i<instructorProfiles.length; i++){
					if(isXMLFile(instructorProfiles[i])){
						String name = instructorProfiles[i].getName();
						name = name.substring(0, name.length()-4);
						addInstructorQueue(new InstructorQueue(path, library, this, name));
						
					}
				}
			}
			else{
				Logger.log(Logger.ERROR, "QueueLibrary: Cannot load instructors from " + path + 
						". It is not a directory.");
			}
		}
		else{
			Logger.log(Logger.ERROR, "QueueLibrary: Cannot load instructors from " + path + 
					". It is not a valid path.");
		}
	}

	/**
	 * Determines whether a file is an XML file or not
	 * @param file the file to check
	 * @return true if the file ends with ".xml", false otherwise
	 */
	private boolean isXMLFile(File file) {
		if(file.getName().endsWith(".xml")){
			return true;
		}
		return false;
	}
	
	/**
	 * Saves the queues for the current instructor to an XML file
	 */
	public void saveQueues(){
		getCurrentInstructor().toXML();
		Logger.log(Logger.INFO, "QueueLibrary: Saving changes made to instructor " + getCurrentInstructor().getInstructorName());
	}
	
	
	//-------------------------------------------------------------------------
	// Other Methods
	//-------------------------------------------------------------------------
	
	
	/**
	 * Makes sure that the "default.xml" file is up to date. If none exists, one is created.
	 * If one exists, it ensures that it contains all of the programs and stations
	 */
	private void updateDefaultInstructor(){
		InstructorQueue defaultInstructor = getInstructorQueue("default");
		if(defaultInstructor == null){//no default exists - create a blank one
			defaultInstructor = new InstructorQueue(path, library, this, "default", false);
			addInstructorQueue(defaultInstructor);
			defaultInstructor.toXML();
		}
		else{//default exists
			Iterator libPrograms = library.getPrograms();
			//Vector<ProgramQueue> defaultPrograms = defaultInstructor.getPrograms();
			while(libPrograms.hasNext()){
				ProgramMedia pm = (ProgramMedia)libPrograms.next();
				String programName = pm.getName();
				
				//if a program exists and is not in the default, add it
				if(defaultInstructor.getProgramQueue(programName) == null){
					defaultInstructor.addProgramQueue(new ProgramQueue(path, library, this, programName));
					Logger.log(Logger.INFO, "QueueLibrary: Added program" + programName + " to default instructor");
				}
				
				Iterator libStations = pm.getStations();
				while(libStations.hasNext()){
					StationMedia sm = (StationMedia)libStations.next();
					String stationName = sm.getName();
					
					//if a station exists and is not in the default, add it
					if(defaultInstructor.getProgramQueue(programName)
							.getStationQueue(stationName) == null){
						defaultInstructor.getProgramQueue(programName)
						.addStationQueue(new StationQueue(path, library, this, stationName));
						Logger.log(Logger.INFO, "QueueLibrary: Added station " + stationName + " in program " 
								+ programName + " to default instructor");
					}
				}

			}
			
//			for(int i = 0; i<defaultPrograms.size(); i++){
//				
//				//if a program is in the default but no longer exists, remove it
//				String programName = ((ProgramQueue)defaultPrograms.get(i)).getProgramName();
//				if(library.getProgram(programName) == null){
//					defaultInstructor.removeProgramQueue(programName);
//					i--;
//					Logger.log(Logger.INFO, "QueueLibrary: Program " + programName + 
//							" no longer exists. Removing from default");
//				}
//				else{//program still exists... check its stations
//					Vector defaultStations = ((ProgramQueue)defaultPrograms.get(i)).getStations();
//					for(int j = 0; j<defaultStations.size(); j++){
//						
//						//if a station is in the default but no longer exists, remove it
//						String stationName = ((StationQueue)defaultStations.get(j)).getStationName();
//						if(((ProgramMedia)library.getProgram(programName))
//								.getStation(stationName) == null){
//							defaultInstructor.getProgramQueue(programName)
//							.removeStationQueue(stationName);
//							j--;
//							Logger.log(Logger.INFO, "QueueLibrary: Station " + stationName + " in program " 
//									+ programName + " no longer exists. Removing from default");
//						}
//					}
//				}
//			}
		}
		defaultInstructor.toXML();
	}
	

}
