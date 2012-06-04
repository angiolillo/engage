package ui.nav;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import main.Engage;

/**
 * TODO carl this should probably be rolled into one class.
 * TODO carl comment this class
 * @author carl
 *
 */
public class LibraryToggleButton extends JButton implements ActionListener{
	
	private static ImageIcon SHOW_LIB_BUTTON = new ImageIcon("ui/nav/show_media_library.png");
	private static ImageIcon HIDE_LIB_BUTTON = new ImageIcon("ui/nav/hide_media_library.png");
	
	
	private Engage parent;
	
	private boolean libraryVisible = false;
	
	public LibraryToggleButton(Engage p){
		super();
		parent = p;
		
		this.setIcon(SHOW_LIB_BUTTON);
		this.setFocusPainted(false);
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e){
		libraryVisible = !libraryVisible;
		
		if(libraryVisible) {
			this.setIcon(HIDE_LIB_BUTTON);
			parent.showMediaLibrary(true);
		}
		else {
			this.setIcon(SHOW_LIB_BUTTON);	
			parent.showMediaLibrary(false);
		}
	}


}