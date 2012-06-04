package backend.queue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import main.Logger;
import backend.library.MediaLibrary;
import backend.library.ProgramMedia;
import backend.library.StationMedia;

/**
 * This class contains all the ProgramQueues associated with an instructor. 
 * A ProgramQueue should exists for every program that exists, unless the
 * file heirarchy has been changed since the last login.
 * 
 * @author Kayre Hylton
 *
 */
public class InstructorQueue {

	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------
	
	/**
	 * A Vector of ProgramQueues
	 */
	private Vector<ProgramQueue> programs;
	
	/**
	 * The path for the directory that holds instructor files
	 */
	private String path;

	/**
	 * The name of the instructor
	 */
	private String instructorName;

	/**
	 * The MediaLibrary
	 */
	private MediaLibrary library;
	/**
	 * The QueueLibrary that manages this InstructorQueue
	 */
	private QueueLibrary qLibrary;
	
	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------
	
	/**
	 * Returns the programs associated with this instructor
	 * @return a Vector of ProgramQueues
	 */
	public Vector<ProgramQueue> getPrograms(){
		return programs;
	}

	/**
	 * Sets the progranms associated with this instructor
	 * @param programs a Vector of ProgramQueues
	 */
	public void setPrograms(Vector<ProgramQueue> programs){
		this.programs = programs;
	}
	
	/**
	 * Returns the name of the instructor
	 * @return the name of instructor
	 */
	public String getInstructorName() {
		return this.instructorName;
	}

	/**
	 * Sets the name of the instructor
	 * @param instructorName the name of the instructor
	 */
	public void setInstructorName(String instructorName){
		this.instructorName = instructorName;
	}
	
	/**
	 * Sets the path for the directory that holds instructor files
	 * @param path the path to the folder which holds instructor XML files
	 */
	public void setPath(String path){
		this.path = path;
	}
	
	/**
	 * Gets the path for the directory that holds instructor files
	 * @return the path to the folder which holds instructor XML files
	 */
	public String getPath(){
		return path;
	}
	
	/**
	 * Returns the MediaLibrary
	 * @return the MediaLibrary
	 */
	public MediaLibrary getMediaLibrary(){
		return library;
	}
	
	/**
	 * Sets the MediaLibrary
	 * @param library teh MediaLibrary
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
	// Constructor
	//-------------------------------------------------------------------------



	/**
	 * Creates an InstructorQueue from a path, MediaLibrary, QueueLibrary and instructor name.
	 * 
	 * @param path the path to the directory containing instructor files
	 * @param library the MediaLibrary
	 * @param qLibrary the QueueLibrary that manages this
	 * @param instructorName the name of the instructor
	 */
	public InstructorQueue(String path, MediaLibrary library, 
			QueueLibrary qLibrary, String instructorName){
		this.setInstructorName(instructorName);
		this.programs = new Vector<ProgramQueue>();
		this.setPath(path);
		this.setMediaLibrary(library);
		this.setQLibrary(qLibrary);
		if(profileExists()){//if the instructor exists, create from their xml file
			loadFromXML();
		}
		else{//if instructor does not exist...
			if(profileExists("default")){//...but a default does, create from default
				loadFromXML("default");
			}
		}
	}
	
	


	/**
	 * Creates an InstructorQueue from a path, MediaLibrary, QueueLibrary, instructor name
	 * and boolean which determines whether or not the InstructorQueue's subqueues will have
	 * content in them.
	 * 
	 * @param path the path to the directory containing instructor files
	 * @param library the MediaLibrary
	 * @param qLibrary the QueueLibrary that manages this
	 * @param instructorName the name of the instructor
	 * @param hasContent if false, content-less programs of stations will be added.
	 * if true, content will be loaded from an XML file, as per usual
	 */
	protected InstructorQueue(String path, MediaLibrary library, 
			QueueLibrary qLibrary, String instructorName, boolean hasContent) {
		this.setInstructorName(instructorName);
		this.programs = new Vector<ProgramQueue>();
		this.setPath(path);
		this.setMediaLibrary(library);
		this.setQLibrary(qLibrary);
		if(hasContent){
			if(profileExists()){
				loadFromXML();
			}
			else{
				loadFromXML("default");
			}
		}
		else{
			addEmptyStructure();
		}
	}
	
	//-------------------------------------------------------------------------
	// ProgramQueue methods
	//-------------------------------------------------------------------------
	


	/**
	 * Returns a specific ProgramQueue
	 * @param program a String of the name of the program you want
	 * @return the ProgramQueue for the program pecified
	 */
	public ProgramQueue getProgramQueue(String program){
		for(int i=0; i<programs.size(); i++){
			if(((ProgramQueue)programs.get(i)).getProgramName().equals(program)){
				return (ProgramQueue)programs.get(i);
			}
		}
		return null;
	}

	/**
	 * Adds a program to the collection of programs
	 * @param program the ProgramQueue to add to programs
	 */
	public void addProgramQueue(ProgramQueue program) {
		
		programs.add(program);
	}

	/**
	 * Removes a specific program from the collection of programs
	 * @param programName the ProgramQueue to remove from programs
	 */
	public void removeProgramQueue(String programName) {
		for(int i=0; i<programs.size(); i++){
			if(((ProgramQueue)programs.get(i)).getProgramName().equals(programName)){
				programs.remove(i);
				Logger.log(Logger.INFO, "InstructorQueue: Removed program " + programName + 
						" from instructor " + instructorName);
			}
		}
	}
	
	//-------------------------------------------------------------------------
	// XML Methods
	//-------------------------------------------------------------------------
	
		
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
	 * Populates the collection of ProgramQueues from those laid out in the XML file
	 * associated with this InstructorQueue
	 */
	private void loadFromXML(){
		loadFromXML(this.instructorName);
	}
	
	/**
	 * Populates the collection of ProgramQueues from those laid out in a specified 
	 * XML file
	 * @param fileName a String of the name of the instructor whose file this will read from
	 */
	private void loadFromXML(String fileName){
		try {
			File dir = new File(path);
			if(dir == null){
				Logger.log(Logger.ERROR, "InstructorQueue: Cannot load instructor " + fileName + " from " 
						+path + ". It is not a valid path");
			}
			else if(!dir.isDirectory()){
				Logger.log(Logger.ERROR, "InstructorQueue: Cannot load instructor " + fileName + " from "
						+path + ". It is not a directory");
			}
			else{
				FileReader fReader = new FileReader(path + File.separator + fileName + ".xml");
				BufferedReader bReader = new BufferedReader(fReader);
				String line = bReader.readLine();
				
				while(line != null){
					if(line.equals("<program>")){
						Vector<String> program = new Vector<String>();
						line = bReader.readLine();
						while(!line.equals("</program>")){//add the lines between program tags to a vector
							program.add(line);
							line = bReader.readLine();
						}
						//create program from vector
						ProgramQueue aProgram = new ProgramQueue(path, library, qLibrary, program); 
						addProgramQueue(aProgram);
					}
					line = bReader.readLine();
				}
				Logger.log(Logger.IMPORTANT, "InstructorQueue: Programs loaded for instructor " + fileName);
				bReader.close();
			}
		} catch (Exception e) {
			Logger.log(Logger.ERROR, "InstructorQueue: Error reading from file " + fileName + ".xml");
			Logger.log(Logger.ERROR, "InstructorQueue: " + e.getMessage());
		}
	}
	
	/**
	 * Saves the state of the InstructorQueue to a specified XML file
	 * @param fileName a String of the name of the instructor to name the file after
	 */
	public void toXML(String fileName){
		String output = "";
		output += "<" + fileName + ">\n";
		
		for(int i=0; i<programs.size(); i++){
			output += ((ProgramQueue)programs.get(i)).toXML();
		}
		
		output += "</" + fileName + ">";
		
		try {
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(path +
					File.separator + fileName + ".xml"));
			bWriter.write(output);
						
			bWriter.close();
			
		} catch (IOException e) {
			Logger.log(Logger.ERROR, "InstructorQueue: Error writing XML file for " + fileName);
		}
		
	}
	
	/**
	 * Saves the state of the InstructorQueue to an XML file named after this instructor
	 */
	public void toXML() {
		toXML(this.instructorName);
		
	}
	
	//-------------------------------------------------------------------------
	// Other Methods
	//-------------------------------------------------------------------------
	

	/**
	 * Checks if a file exists for the instructor represented by this InstructorQueue
	 * @return true if there is an XML file for this instructor, false otherwise
	 */
	private boolean profileExists() {
		
		return profileExists(this.instructorName);
	}
	
	/**
	 * Checks is a file exists for a particular instructor
	 * @param name a String of the name of an instructor
	 * @return true if there is an XML file for this instructor, false otherwise
	 */
	private boolean profileExists(String name){
		File instructorDirectory = new File(path);
		if(instructorDirectory != null){
			if(instructorDirectory.isDirectory()){
				File[] instructorProfiles = instructorDirectory.listFiles();
				for(int i=0; i<instructorProfiles.length; i++){
					if(isXMLFile(instructorProfiles[i])){
						
						if(instructorProfiles[i].getName().equals(name + ".xml")){
							return true;
						}
					}
				}
			}
			else{
				Logger.log(Logger.ERROR, "InstructorQueue: Cannot read files from " + path + 
						". It is not a directory");
			}
		}
		else{
			Logger.log(Logger.ERROR, "InstructorQueue: Cannot read files from " + path + 
					". It is not a valid path");
		}
		return false;
	}
	
	/**
	 * Adds content-less programs of stations to this instructor
	 */
	public void addEmptyStructure(){
		this.setPrograms(new Vector<ProgramQueue>());
		Iterator programs = library.getPrograms();
		while(programs.hasNext()){
			ProgramMedia pMedia = (ProgramMedia)programs.next(); 
			
			Iterator stations = pMedia.getStations();
			Vector<StationQueue> stationsVector = new Vector<StationQueue>();
			while(stations.hasNext()){
				stationsVector.add(new StationQueue(
						path, library, qLibrary, ((StationMedia)stations.next()).getName()));
			}
			ProgramQueue pQueue = new ProgramQueue(path, library, qLibrary, pMedia.getName());
			pQueue.setStations(stationsVector);
			this.addProgramQueue(pQueue);
		}
	}


	
}
