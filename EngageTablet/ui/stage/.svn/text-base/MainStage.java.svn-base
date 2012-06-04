package ui.stage;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.Constants;
import main.Logger;
import main.Engage;
import backend.library.MediaFile;

/**
 * The MainStage is where the action happens. It is a JPanel that displays the image
 * that is currently on the large flat-panel display, as buttons to control the 
 * retreating and advancing of the queue.
 * 
 * @author carl
 *
 */
public class MainStage extends JPanel implements ActionListener, KeyEventDispatcher{

	
	//-------------------------------------------------------------------------
	// Constants
	//-------------------------------------------------------------------------
	
	private static final int BUTTON_WIDTH = (int)(Constants.WINDOW_AREA.width-ImagePanel.PANEL_SIZE.width)/2;
	private static final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH,BUTTON_WIDTH);
	
	//-------------------------------------------------------------------------
	// END Constants
	//-------------------------------------------------------------------------
	
	
	
	//-------------------------------------------------------------------------
	// Private Fields
	//-------------------------------------------------------------------------
	
	MediaFile previousImage;
	MediaFile nextImage;
	private ImagePanel center;
	private StageClient bigScreen;
	private JButton previous;
	private JButton next;
	private Engage parent;
	
	//-------------------------------------------------------------------------
	// END Private Fields
	//-------------------------------------------------------------------------

	
	
	
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	/**
	 * Constructs the main stage. This is where the user sees the currently
	 * displayed image and is also able to control the advancement and retreat
	 * of this image.
	 * 
	 * @param parent the OnDemandMedia object in which this MainStage resides.
	 * @param address The string corresponding to the address of the computer driving the large flat-panel display.
	 * @param attemptConnection true if we should try to connect to the computer driving the main display.
	 */
	public MainStage(Engage parent, String address, boolean attemptConnection){
		super();
		this.parent = parent;

		// Listen for the "up" or "down" keys.
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

		// Make the previous button
		ImageIcon previousIcon = new ImageIcon("ui/stage/prev_button.png");
		previous = new JButton(previousIcon);
		previous.setPreferredSize(BUTTON_SIZE);
		previous.setMinimumSize(BUTTON_SIZE);
		previous.setMaximumSize(BUTTON_SIZE);
		previous.setFocusPainted(false);
		//previous.setBorder(BorderFactory.createEmptyBorder());
		previous.setActionCommand("previous");
		previous.addActionListener(this);
				
		// Make the next button
		ImageIcon nextIcon = new ImageIcon("ui/stage/next_button.png");
		next = new JButton(nextIcon);
		next.setPreferredSize(BUTTON_SIZE);
		next.setMinimumSize(BUTTON_SIZE);
		next.setMaximumSize(BUTTON_SIZE);
		next.setFocusPainted(false);
		//next.setBorder(BorderFactory.createEmptyBorder());
		next.setActionCommand("next");
		next.addActionListener(this);
		
		// Make the center panel. This panel will actually hold the image.
		center = new ImagePanel(null);
		
		// If the address is legitimate, and we want to connect,
		// it attempts to connect to that machine.
		if(address != null && attemptConnection) {
			try { bigScreen = new StageClient(address); }
			catch(UnknownHostException e) {
				Logger.log(Logger.ERROR,"Unable to connect to remote display at address: \""+address+"\""); }
		}
		
		// Add each of the elements to the main stage panel.
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(previous);
		this.add(center);
		this.add(next);
	}

	//-------------------------------------------------------------------------
	// END Constructor
	//-------------------------------------------------------------------------

	
	
	/**
	 * Sets an image on the central stage. If given null, leaves the main stage empty.
	 * Depending on where the image comes from, library or strip, it will deselect any 
	 * selected images in the other object.  
	 * 
	 * @param newImage the image to display on the stage
	 * @param comesFrom the object that the image comes from, either the library or strip
	 */
	public void showImage(MediaFile newImage, Object comesFrom){
	
		center.setMediaFile(newImage);
		
		if(comesFrom instanceof ui.medialibraryview.StationMediaLibrary) 
			parent.getCurrentStationStrip().releaseSelection();
		else parent.getLibrary().releaseSelection();
		
		// If we are connected to a screen, Make It Big! (tm)
		if(bigScreen != null){
			if(newImage == null) bigScreen.displayImage("null");
			else bigScreen.displayImage(newImage.getPath());
		}

	}
	
	
	
	
	//-------------------------------------------------------------------------
	// Advance and Retreat Listeners
	//-------------------------------------------------------------------------
	
	/**
	 * Listens for a button press. When a button is pressed, this method 
	 * determines whether it was the previous or the next button that has been 
	 * pressed.
	 * 
	 * @param event the action (previous or next) that relates to this event
	 */
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("previous"))
			parent.getCurrentStationStrip().retreatSelection();
		else if (event.getActionCommand().equals("next"))
			parent.getCurrentStationStrip().advanceSelection();
	}
	
	/**
	 * Listens for an "up" or "down" key event. If the up or down key has been
	 * typed (pressed and released) then we advance or retreat the strip.
	 * 
	 * @param event the KeyEvent that we are checking.
	 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		
		int key = event.getKeyCode();
		int ID = event.getID();
		
		// If the user released the down key, retreat selection.
		if(ID==KeyEvent.KEY_RELEASED && key==KeyEvent.VK_DOWN)		parent.getCurrentStationStrip().retreatSelection();
		
		// If the user released the up key, advance selection.
		else if(ID==KeyEvent.KEY_RELEASED && key==KeyEvent.VK_UP)	parent.getCurrentStationStrip().advanceSelection();
		
		// If the event refers to up or down keys, we have handled it.
		if(key==KeyEvent.VK_DOWN || key==KeyEvent.VK_UP)		return true;
		
		// If it's any other keys, let it pass through.
		else return false;
	
	}
	
	//-------------------------------------------------------------------------
	// END Advance and Retreat Listeners
	//-------------------------------------------------------------------------
		
	
}
