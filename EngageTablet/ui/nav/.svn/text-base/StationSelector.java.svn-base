package ui.nav;

import java.awt.Font;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComboBox;

import backend.library.StationMedia;

public class StationSelector extends JComboBox{
	
	private static final String BUFFER = " ";
	
	private Vector<String> names = new Vector<String>();
	
	public StationSelector(Iterator iterator){		
		super();
	
		while(iterator.hasNext()){
			StationMedia sm = (StationMedia)iterator.next();
			
			this.addItem(BUFFER.concat(sm.getDisplayName()));
			names.add(sm.getName());
		}
		
		Font myFont = this.getFont();
		myFont = myFont.deriveFont(16f);
		this.setFont(myFont);
	}
	
	/**
	 * Returns the currently selected station.
	 * @return String contaning the currently selected station.
	 */
	public String getStation(){
		return names.get(this.getSelectedIndex());
	}
	
}
