package main;

/**
 * The Logger is designed to handle all logging throughout the Voyager media
 * application.
 * 
 * @author Carl Angiolillo
 *
 */
public class Logger {
	
	/**
	 * This is the default priority level. Any messages of a lower priority will not be displayed.
	 */
	private static final int PRIORITY_LEVEL = 0;
	
	/**
	 * This priority level corresponds with a fatal error that will prevent the application 
	 * from running normally.
	 */
	public static final int ERROR = 3;
	
	/**
	 * This priority level corresponds with important information, such as whether we have
	 * loaded the library or not.
	 */
	public static final int IMPORTANT = 2;
	
	/**
	 * This priority level corresponds with mundane information, such as switching stations
	 * and loading individual images.
	 */
	public static final int INFO = 1;

	/**
	 * This priority level corresponds with debugging information, random stuff that should
	 * never actually see the light of day :)
	 */
	public static final int DEBUG = 0;
	
	/**
	 * Logging method that takes a priority. Allows you to set exactly how important as message
	 * is. 
	 * 
	 * @param p a priority constant (DEBUG, INFO, IMPORTANT, ERROR) describing the priority of the message
	 * @param msg String containing a message to log
	 */
	public static void log(int p, String msg){
		//if(p>=PRIORITY_LEVEL) System.out.println(msg);
		if(p==2) System.out.println(msg);
	}
	
}
