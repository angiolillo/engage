package main;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import ui.DraggableImage;
import ui.login.LoginScreen;
import ui.medialibraryview.StationMediaLibrary;
import ui.nav.Navbar;
import ui.queue.MediaStrip;
import ui.queue.MediaStripCollection;
import ui.splash.VoyagerSplash;
import ui.stage.MainStage;
import backend.config.ConfigReader;
import backend.library.MediaFile;
import backend.library.MediaLibrary;
import backend.queue.QueueLibrary;

/**
 * 
 * @author Carl Angiolillo
 *
 */
public class Engage extends JFrame implements WindowListener{
	
	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------
	
	// The MediaLibrary where the images are stored.
	private MediaLibrary media;
	
	// The QueueLibrary where the instructor queues are stored.
	private QueueLibrary queues;
	
	// Application splash window
	private VoyagerSplash splash = new VoyagerSplash("ui/splash/vger_splash.png");
	
	// Used to fetch and store the name of the instructor and the educational program.
	private LoginScreen loginScreen;
	
	// The navigation bar across the top of the screen
	private Navbar navbar;
	
	// The stage on which we display the main image.
	private MainStage stage;
	
	// The visual library for this station.
	private StationMediaLibrary stationLibrary;
	
	// A media strip is a visual display of a queue that an instructor can interact with.
	private MediaStripCollection strips;
	
	// The particular queue that is currently displayed.
	private JTabbedPane stationStrip;
	
	// Used to store an image that's being dragged
	protected DraggableImage ghostImage;

	// Holds all of the user supplied information from the settings file
	private ConfigReader configReader;
	
	//-------------------------------------------------------------------------
	// END Private Fields
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------
	
	/**
	 * Gets the strips.
	 * 
	 * @return MediaStripCollection all of the media strips (visual image queues)
	 */
	public MediaStripCollection getStrips() {return strips;}

	/**
	 * Gets the strip corresponding to the currently selected station.
	 * 
	 * @return MediaStrip the media strip (visual image queue) corresponding to the current station
	 */
	public MediaStrip getCurrentStationStrip() {return strips.getStripForStation(navbar.getStation());}
	
	/**
	 * Gets the visual media library for the currently selected station.
	 * 
	 * @return StationMediaLibrary the visual media library corresponding to the currently selected station
	 */
	public StationMediaLibrary getLibrary() {return stationLibrary;}
	
	/**
	 * Gets the stage with the previous and next buttons and the "Now Showing" image.
	 * 
	 * @return MainStage the stage object
	 */
	public MainStage getStage() {return stage;}
	
	/**
	 * Gets an iterator of StationMedia from the media library.
	 * 
	 * @return an Iterator of StationMedia
	 */
	public Iterator getStations(){
		return media.getProgram(loginScreen.getProgramName()).getStations();
	}

	//-------------------------------------------------------------------------
	// Getters and Setters
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------

	/**
	 * Constructor for the OnDemandMedia object.
	 */
	public Engage(){
		  
		// Put up splash screen
		splash.setText("Please be patient as your curriculum is being prepared");
		splash.run();
		
		
		//Set up the look and feel (to the default Java L&F) and prepare the window.
		try { UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		// Loads the queues and the media library.
		loadData();
		this.setBounds(Constants.WINDOW_AREA);
		
		// Pause for two seconds, then show the login screen and remove the splash
		try{Thread.sleep(2000);} catch (InterruptedException e){ splash.close(); }
		presentLoginScreen();
	}

	//-------------------------------------------------------------------------
	// END Constructors
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Constructor Helper Methods
	//-------------------------------------------------------------------------
	
	/*
	 * Sets up the application to run on a tablet computer. 
	 */
	private void setOnTablet() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("img.gif");
		Cursor emptyCursor = toolkit.createCustomCursor(image , new Point(0,0), "main/emptyCursor.png");
		this.setCursor(emptyCursor);
		this.setUndecorated(true);
	}
	
	/*
	 * Loads the media library and the queue and then creates a login screen.
	 */
	private void loadData() {
		
		// Load a configuration reader
		configReader = new ConfigReader("settings.cfg");
	
		// Changes settings based on whether or not we are on the tablet
		if(configReader.isOnTablet()){
			setOnTablet();
		}
		else{
			Constants.WINDOW_AREA.height += 20;
		}
		
		// Load the media library.
		media = new MediaLibrary(configReader.getLibraryFolderPath());
	
		// Load the instructor queues
		queues = new QueueLibrary(configReader.getInstructorsFolderPath(), media);
		
		// Load the login screen, set password and add a window listener.
		loginScreen = new LoginScreen(queues, media.getPrograms());
		loginScreen.setPassword(configReader.getPassword());
		loginScreen.addWindowListener(this);
	}

	/**
	 * Presents the user with a login screen. Waits until a user logs in before
	 * continuing.
	 */
	public void presentLoginScreen() {

		loginScreen.showLoginScreen();
		
		//FIXME Splash problems
		// Animation here works just fine, even if we sleep...
		//splash.setAnimated("ui/splash/anim.gif");
		//try{Thread.sleep(6000);} 
		//catch (InterruptedException e1){}
		
	}
	
	/**
	 * This method is called when the login screen has finished being called.
	 * @param e The window event (login window being dismissed) that fired this windowDeactivated call.
	 */
	public void windowDeactivated(WindowEvent e) {
		
		if(loginScreen.isDone()) this.generateMainScreen();
			
	}

	/**
	 * Initializes the fields that depend on the Login Screen
	 */
	private void generateMainScreen(){
		
		// Prefetch all images in this program.
		if(Constants.PRELOAD_ENTIRE_PROGRAM){
			//Timer timer = new Timer();
			//timer.schedule(new LoadTask(), 0);
			media.prefetchProgramImages(loginScreen.getProgramName(), 
							Constants.WAIT_FOR_THUMB_IMAGE, 
							Constants.WAIT_FOR_CENTER_IMAGE);
		}
		
				
		// Set up the contentPane to add all of the elements to.
		Container contentPane = this.getContentPane();
		contentPane.removeAll();
		contentPane.setLayout(null);
		
		navbar = new Navbar(this);
		navbar.setBounds(Constants.NAVBAR_AREA);
		contentPane.add(navbar);
		
		// Load the viewing stage.
		stage = new MainStage(this, configReader.getScreen(), loginScreen.isConnectedToScreen());
		stage.setBounds(Constants.STAGE_AREA);
		contentPane.add(stage);
		
		// Load the visual media library.
		stationLibrary = new StationMediaLibrary(this,
												media.getProgram(loginScreen.getProgramName()),
												navbar.getStation());
		stationLibrary.setBounds(Constants.STAGE_AREA);
		showMediaLibrary(false);
		contentPane.add(stationLibrary);
		
		// Load the strips for this instructor and this educational program.
		strips = new MediaStripCollection(queues.getStationQueues(loginScreen.getInstructorName(), loginScreen.getProgramName()), 
										  this);
		
		// Add the queue strip to the frame.
		stationStrip = strips.getStripForStation(navbar.getStation()).getView();
		stationStrip.setBounds(Constants.QUEUE_AREA);
		contentPane.add(stationStrip);
				
		this.setContentPane(contentPane);
		this.setVisible(true);
		
		splash.close();
		
		
		//If we didn't preload everything, go ahead and load it in the background.
		if(!Constants.PRELOAD_ENTIRE_PROGRAM){
			Timer timer = new Timer();
			timer.schedule(new LoadTask(), 0);
		}
	}
	
	// This inner class is intended simply to prefetch any remaning images that have 
	// not already been displayed.
	class LoadTask extends TimerTask {
		public void run(){
			media.prefetchProgramImages(loginScreen.getProgramName(), 
					Constants.WAIT_FOR_THUMB_IMAGE, 
					Constants.WAIT_FOR_CENTER_IMAGE);
		}
	}
	
	
	//-------------------------------------------------------------------------
	// END Constructor Helper Methods
	//-------------------------------------------------------------------------
	
	
	
	//-------------------------------------------------------------------------
	// Main Method
	//-------------------------------------------------------------------------
	
	/**
	 * This is the main application, which creates an OnDemandMedia frame and 
	 * displays it.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args){
		new Engage();
	}
	
	//-------------------------------------------------------------------------
	// END Main Method
	//-------------------------------------------------------------------------

	
	//-------------------------------------------------------------------------
	// Action Methods
	//-------------------------------------------------------------------------
	
	/**
	 * This occurs when the user clicks on the "Show Media Library" button.
	 * @param show true if the media library should be visible, false otherwise.
	 */
	public void showMediaLibrary(boolean show){
		stationLibrary.setVisible(show);
		stage.setVisible(!show);
	}
	
	/**
	 * This method is called when a user switches to a different station.
	 * @param newStation The name of the new station to switch to.
	 */
	public void switchStation(String newStation){
		
		// Refresh the visual library.
		this.getContentPane().remove(stationLibrary);
		boolean wasVisible = stationLibrary.isVisible();
		stationLibrary = new StationMediaLibrary(this,
					media.getProgram(loginScreen.getProgramName()),
					newStation);
		stationLibrary.setVisible(wasVisible);
		stationLibrary.setBounds(Constants.STAGE_AREA);
		this.getContentPane().add(stationLibrary);
		stationLibrary.revalidate();
		stationLibrary.repaint();

		
		// Refresh the station queue.
		this.getContentPane().remove(stationStrip);
		stationStrip = strips.getStripForStation(newStation).getView();
		stationStrip.setBounds(Constants.QUEUE_AREA);
		this.getContentPane().add(stationStrip);
		stationStrip.revalidate();
		stationStrip.repaint();

	}
	
	//-------------------------------------------------------------------------
	// END Action Methods
	//-------------------------------------------------------------------------

	
	
	//-------------------------------------------------------------------------
	// Image Dragging Methods
	//-------------------------------------------------------------------------
	
	/**
	 * Gets the ghosted image for dragging purposes.
	 * 
	 * @return DraggableImage the image being dragged
	 */
	public DraggableImage getGhostImage() {
		return ghostImage;
	}
	
	/**
	 * Spawns a draggable image, for use when the user begins to drag
	 * an image from the media library or the strips (visual image queues).
	 * 
	 * @param mf The MediaFile that the drag initiated
	 * @param origin The location of the initial drag
	 */
	public void spawnDraggableImage(MediaFile mf, Point origin)
	{
		ghostImage = new DraggableImage(mf,this);
		
		ghostImage.setLocation(origin);
		ghostImage.setVisible(true);

		this.getContentPane().add(ghostImage);
		this.getContentPane().setComponentZOrder(ghostImage, 0); //make sure image is ontop
		this.repaint();
	}
	
	/**
	 * Releases the image that is being dragged. Used when the user releases the 
	 * dragged image.
	 */
	public void DespawnDraggableImage()
	{
		this.getContentPane().remove(ghostImage);
	}	
	
	//-------------------------------------------------------------------------
	// END Image Dragging Methods
	//-------------------------------------------------------------------------
	
	/** Included only to implement WindowListener interface. Not functional.
	 * @param arg0 Not used
	 */
	public void windowOpened(WindowEvent arg0) {}
	/** Included only to implement WindowListener interface. Not functional.
	 * @param arg0 Not used
	 */
	public void windowClosing(WindowEvent arg0) {}
	/** Included only to implement WindowListener interface. Not functional.
	 * @param arg0 Not used
	 */
	public void windowClosed(WindowEvent arg0) {}
	/** Included only to implement WindowListener interface. Not functional.
	 * @param arg0 Not used
	 */
	public void windowIconified(WindowEvent arg0) {}
	/** Included only to implement WindowListener interface. Not functional.
	 * @param arg0 Not used
	 */
	public void windowDeiconified(WindowEvent arg0) {}
	/** Included only to implement WindowListener interface. Not functional.
	 * @param arg0 Not used
	 */
	public void windowActivated(WindowEvent arg0) {}
	
}

