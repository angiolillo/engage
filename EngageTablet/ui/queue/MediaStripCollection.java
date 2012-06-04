package ui.queue;

import java.util.HashMap;
import java.util.Vector;

import main.Engage;
import backend.queue.StationQueue;

public class MediaStripCollection {
	
	private Engage main;
	private HashMap stationNameToStrip;
	
	public MediaStripCollection (Vector stations, Engage main) {
		
		// Init vars
		this.main = main;
		stationNameToStrip = new HashMap();
		
		for(int i=0;i<stations.size();i++) {
			MediaStrip aStrip = new MediaStrip((StationQueue)stations.get(i), main);
			stationNameToStrip.put(((StationQueue)stations.get(i)).getStationName(), aStrip);
		}
		
	}
	
	public MediaStrip getStripForStation(String stationName) {
		return ((MediaStrip)stationNameToStrip.get(stationName));
	}

}
