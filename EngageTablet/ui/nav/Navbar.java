package ui.nav;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Constants;
import main.Logger;
import main.Engage;

public class Navbar extends JPanel implements ActionListener {

	
	// Used to logout back to the login screen.
	private LogoutButton logoutButton;
	
	// Used to fetch and store the name of the current station.
	private StationSelector stationDropDown;
	
	private JLabel navbarHeader;
	
	// The toggle button for the media library.
	private LibraryToggleButton libraryToggle;
	
	Engage parent;
	
	public Navbar(Engage parent){
		super();
		this.setLayout(null);
		
		this.parent = parent;
		
		// DON't use bounds, set up a nice layout...
		
		// Add the logout button.
		logoutButton = new LogoutButton(parent);
		logoutButton.setBounds(Constants.LOGOUT_BUTTON_AREA);
		this.add(logoutButton);
	
		// Add the drop down for selecting the station.
		stationDropDown = new StationSelector(parent.getStations());
		stationDropDown.addActionListener(this);
		stationDropDown.setBounds(Constants.STATION_DROP_DOWN_AREA);
		this.add(stationDropDown);
	
		// Add the decorative header between the station selector and the library toggle.
		navbarHeader = new JLabel(new ImageIcon("ui/nav/navbar_header.png"));
		navbarHeader.setBounds(Constants.NAVBAR_HEADER_AREA);
		this.add(navbarHeader);
		
		// Add the library toggle button.
		libraryToggle = new LibraryToggleButton(parent);
		libraryToggle.setBounds(Constants.LIBRARY_TOGGLE_BUTTON_AREA);
		this.add(libraryToggle);
	}

	
	public String getStation(){
		return stationDropDown.getStation();
	}

	//Called when someone hits the station dropdown
	public void actionPerformed(ActionEvent arg0) {
		parent.switchStation(stationDropDown.getStation());
		Logger.log(Logger.INFO,"Station has been switched to:"+stationDropDown.getStation());
	}
	
	
}
