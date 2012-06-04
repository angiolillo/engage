package ui.queue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;

import main.Constants;
import main.Logger;
import main.Engage;
import ui.DraggableImage;
import backend.library.MediaFile;
import backend.queue.GroupQueue;
import backend.queue.StationQueue;

public class MediaStrip extends MouseInputAdapter implements ActionListener {
	
	private static final int SCROLLBAR_HEIGHT = 12;
	private static final int FLOW_PADDING = 15;
	
	private HashMap<QueueImage, MediaFile> jlabelToMediaFile;
	private HashMap stationNameToQueueImage;
	
	//Var to hold main view
	private JTabbedPane tabView;
	
	//StationQueue this MediaStrip displays
	private StationQueue theStation;
	
    //used to slightly delay dragging so a click vs. a drag can be differentiated
	private long lastObservedPress = 0;
	
	//drag delay in ms
	public static final int DRAG_DELAY = 175;
	
	//Variable to indicate whether the tab views are being constructed for the first time */
	boolean firstDraw = true;
	
	private HashMap<String, GroupQueue> groupNamesToGroupQueues;
	private String currentStation;
	private Engage parent;
	private QueueImage[] labels = null;
	private Vector<Double> imageMidpoints = null;
	private QueueImage previousLineLabel = null;
	private int previousLineIndex = -1;
	private JPanel imagePanel = null;
	private boolean dragging = false;
	private QueueImage draggedLabel;
	private int draggedLabelIndex = -1;
	private boolean firstConstructView = true;
	private ImageIcon divLine = new ImageIcon("ui/queue/line.PNG");
	private ImageIcon divEmpty = new ImageIcon("ui/queue/lineEmpty.PNG");
	
	private QueueImage selectedImage;
	
	
	public MediaStrip(StationQueue station, Engage main) {
		
		parent = main;
		theStation = station;
		groupNamesToGroupQueues = new HashMap<String, GroupQueue>();
		
		ImageIcon tabHeight = new ImageIcon("ui/queue/tabIcon.png");
		
		//Create hashmap to hold labels --> mediafiles
		jlabelToMediaFile = new HashMap<QueueImage, MediaFile>();
			
		//Tab view
		tabView = new JTabbedPane();
		TabReorderHandler.enableReordering(tabView, this);
			
		//Get vector of groups for the current station
		Vector groups = station.getGroups();
			
		Logger.log(Logger.INFO, "MediaStrip: There are "+groups.size()+" groups in the current station.");
			
		//For each group, create its panel view of images
		for(int j=0;j<groups.size();j++) {
			
			Logger.log(Logger.INFO, "MediaStrip: Processing group: " + ((GroupQueue)groups.elementAt(j)).getGroupName());
				
			GroupQueue group = (GroupQueue)groups.elementAt(j);
			groupNamesToGroupQueues.put(((GroupQueue)groups.elementAt(j)).getGroupName(),(GroupQueue)groups.elementAt(j));

			//Add scrollable image view to tab view
			tabView.addTab(group.getGroupName(),tabHeight,constructView(group.getMediaFiles(), 0));
		}
		
		firstDraw = false;
			
		//Add "Add Tab" to end of this stations's tabpane
		tabView.addTab("Add Group", new ImageIcon("ui/queue/tabPlus.png"), new JPanel());
		
	}
	
	
	
	public GroupQueue getGroupQueueByName(String name) {
		return ((GroupQueue)groupNamesToGroupQueues.get(name));
	}
	
	public HashMap<String, GroupQueue> getQueuesByName() {
		return groupNamesToGroupQueues;
	}
	
	public JTabbedPane getView() {
		return tabView;
	}
	
	public HashMap getLabelsToMediaFiles() {
		return jlabelToMediaFile;
	}
	
	public Engage getMain() {
		return parent;
	}
	
	public StationQueue getStationQueue() {
		return theStation;
	}
	
	public MediaFile advanceSelection() {
		Logger.log(Logger.INFO,"MediaStrip: Advance selection.");
		
		// HOLY HACK-JOB BATMAN!
		this.findImagePanel();
		
		Component components[] = imagePanel.getComponents();
		
		labels = new QueueImage[components.length];
	
		if(components.length == 3) {
			if(selectedImage == null) {
				this.setSelectedImage((QueueImage) components[1]);
				return (MediaFile)jlabelToMediaFile.get(components[1]);
			}
			else {
				this.setSelectedImage(null);
				return null;
			}
		}
		
		for(int i=1;i<components.length-2;i+=2) {
			
			if(selectedImage == null) {
				this.setSelectedImage((QueueImage)components[1]);
				return (MediaFile)jlabelToMediaFile.get(components[1]);
			}
			
			if(selectedImage.equals(components[i]) && components[i+2] != null) {
				this.setSelectedImage((QueueImage) components[i+2]);
				return (MediaFile)jlabelToMediaFile.get(components[i+2]);
			}
		}
	
		this.setSelectedImage(null);
		return null;
	}
	
	public MediaFile retreatSelection() {
		Logger.log(Logger.INFO,"MediaStrip: Retreat selection.");
		
		// HOLY HACK-JOB BATMAN!
		this.findImagePanel();
		Component components[] = imagePanel.getComponents();
		
		labels = new QueueImage[components.length];
		
		if(components.length == 3) {
			if(selectedImage == null) {
				this.setSelectedImage((QueueImage) components[1]);
				return (MediaFile)jlabelToMediaFile.get(components[1]);
			}
			else {
				this.setSelectedImage(null);
				return null;
			}
		}
	
		for(int i=components.length-2;i>1;i-=2) {
			
			if(selectedImage == null && components[components.length-2] != null) {
				this.setSelectedImage((QueueImage)components[components.length-2]);
				return (MediaFile)jlabelToMediaFile.get(components[components.length-2]);
			}
			
			if(selectedImage.equals(components[i]) && components[i-2] != null) {
				this.setSelectedImage((QueueImage) components[i-2]);
				return (MediaFile)jlabelToMediaFile.get(components[i-2]);
			}
		}
	
		this.setSelectedImage(null);
		return null;
		
	}
	
	private void setSelectedImage(QueueImage image) {
		
		if(image != null) {
			if(image.getBorder() instanceof EmptyBorder) {
				
				int theIndex = image.getQueueIndex() - 2;
				
				JTabbedPane tabPane = this.getView();
				if(!(tabPane.getSelectedComponent() instanceof JPanel)) { imagePanel = null; return; }
				JPanel fullPanel = (JPanel)tabPane.getSelectedComponent();
				JScrollPane pane = (JScrollPane)(fullPanel.getComponent(1));
				pane.getHorizontalScrollBar().setValue(((Constants.THUMB_SIZE.width*theIndex) +
														(10*theIndex+1) + (FLOW_PADDING * theIndex) + 30));
				
				image.setSelected(true);
				image.setBorder(BorderFactory.createLineBorder(Constants.SELECTION_COLOR,3));
				selectedImage = image;
				parent.getStage().showImage((MediaFile)jlabelToMediaFile.get(selectedImage),this);
			}
			else { 
				image.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
				image.setSelected(false);
				selectedImage = null;
				parent.getStage().showImage(null,this);
			}	
		}
		else { 
			selectedImage = null;
			parent.getStage().showImage(null,this);
		}
		
		Iterator it = ((Set)jlabelToMediaFile.keySet()).iterator();
		
		while(it.hasNext()) {
			QueueImage label = (QueueImage)it.next();
			if(!label.equals(image)) {
				label.setSelected(false);
				label.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
			}
		}
		
		
	}
	
	public void releaseSelection() {
		if(selectedImage == null) return;
		selectedImage.setSelected(false);
		selectedImage.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		selectedImage = null;
	}
	
	
	//MOUSE HANDLER STUFF BELOW!
	
	
	/* Mouse-click listener that enables/disables image highlight/border */
	public void mouseClicked(MouseEvent e) {
		
		QueueImage image = (QueueImage)e.getComponent();
		
		//if(image.getCloseBoundingRectangle().contains(e.getPoint())) {
		if(Constants.DELETE_BUTTON_AREA.contains(e.getPoint())){	
			
			if(image.equals(selectedImage)) {
				this.setSelectedImage(null);
			}
			
			//HOLY HACK-JOB BATMAN!
			this.findImagePanel();
			
			if(imagePanel == null) return;
			
			Component components[] = imagePanel.getComponents();
			labels = new QueueImage[components.length];
			
			Vector<QueueImage> tempLabels = new Vector<QueueImage>();
			Vector<MediaFile> newImages = new Vector<MediaFile>();
			//int deleteAtIndex = 0;
			 
			 for(int i=0;i<labels.length;i++) {
				 labels[i] = (QueueImage)components[i];
				 if(!labels[i].equals(image)) tempLabels.add(labels[i]);
				 //else deleteAtIndex = i;
			 }
			 
			 for(int i=0;i<tempLabels.size();i++) {
					if(jlabelToMediaFile.get(tempLabels.get(i)) != null) {
						newImages.add(jlabelToMediaFile.get(tempLabels.get(i)));
					}
			 }	
			 
			 ((JTabbedPane)this.getView()).setComponentAt(
						((JTabbedPane)this.getView()).getSelectedIndex(),
						constructView(newImages,-1));
			
		}
		
		else {
			this.setSelectedImage(image);
		}
			
	}
	
	public void mousePressed(MouseEvent e) 
	{
		this.lastObservedPress = e.getWhen();
	}
	
	public void mouseDragged(MouseEvent e) 
	{	
		
		if ( (e.getWhen() - this.lastObservedPress) < MediaStrip.DRAG_DELAY) return;
		
		if(!dragging) {
			
			if(!(e.getComponent() instanceof ui.DraggableImage)) {
				draggedLabel = (QueueImage)e.getComponent();
				parent.spawnDraggableImage((MediaFile)jlabelToMediaFile.get(draggedLabel), SwingUtilities.convertPoint(draggedLabel, e.getX() - (draggedLabel.getIcon().getIconWidth() / 2), e.getY() - (draggedLabel.getIcon().getIconHeight() / 2), parent));
			}
				
			this.findImagePanel();
			
			if(imagePanel == null) return;
			
			Component components[] = imagePanel.getComponents();
			
			labels = new QueueImage[components.length];
			
			for(int i=0;i<components.length;i++) {
				labels[i] = (QueueImage)components[i];
				if(draggedLabel == labels[i]) {
					draggedLabelIndex = i;
				}
			}
			
			imageMidpoints = new Vector<Double>();
			
			for(int i=1;i<labels.length;i+=2) {
				imageMidpoints.add(Double.valueOf(new Double(labels[i].getX() + labels[i].getWidth() /2)));
			}
			
			dragging = true;
		}
		
		Point compPoint = SwingUtilities.convertPoint(
			    e.getComponent(),
			    new Point(e.getX() + e.getComponent().getWidth() / 2, e.getY() + e.getComponent().getHeight() / 2),
			    imagePanel);
		
		//Logger.log(Logger.DEBUG,"DragX: " + e.getX() + "DragY: " + e.getY());
		//Logger.log(Logger.DEBUG,"CompX: " + compPoint.getX() + "DragY: " + compPoint.getY());
		
		DraggableImage target = parent.getGhostImage();
		
		Point compPointToMain = SwingUtilities.convertPoint(
			    e.getComponent(),
			    new Point(e.getX() + e.getComponent().getWidth() / 2, e.getY() + e.getComponent().getHeight() / 2),
			    parent);
		
		
		
	if((Constants.QUEUE_AREA.y+100) < compPointToMain.y) {
		for(int i=0;i<=imageMidpoints.size();i++) {
			
			if(imageMidpoints.size() == 0) {
				
				labels[0].setIcon(divLine);
				
				previousLineLabel = labels[0];
				previousLineIndex = 0;
				this.getView().repaint();
				break;
			}
			
			if(i == imageMidpoints.size()) { 
				//Logger.log(Logger.DEBUG,draggedLabelIndex + " - " + labels.length);
				
				if(previousLineLabel != null && !previousLineLabel.equals(labels[i*2])) {
					previousLineLabel.setIcon(divEmpty);
				}
				
				if(draggedLabelIndex == labels.length-2) break;
				labels[i*2].setIcon(divLine); 
				
				previousLineLabel = labels[i*2];
				previousLineIndex = i*2;
				this.getView().repaint();
				break;
			} 
			
			
			double currentMidpoint = ((Double)imageMidpoints.elementAt(i)).doubleValue();
			
			if(compPoint.getX() > currentMidpoint) continue;
			if(compPoint.getX() < currentMidpoint) {
				//Logger.log(Logger.DEBUG,draggedLabelIndex + " -- " + i*2);
				
				if(previousLineLabel != null && !previousLineLabel.equals(labels[i*2])) {
					//Logger.log(Logger.DEBUG,"MidX: " +currentMidpoint+ "CompX: " + compPoint.getX());
					previousLineLabel.setIcon(divEmpty);
				}
				
				if(draggedLabelIndex == (i*2)+1 || draggedLabelIndex == (i*2)- 1) break;
				labels[i*2].setIcon(divLine); 
				
				previousLineLabel = labels[i*2];
				previousLineIndex = i*2;
				this.getView().repaint();
				break; 
			}
		}
	}
	else {
		if(previousLineLabel != null) previousLineLabel.setIcon(divEmpty);
		previousLineLabel = null;
		previousLineIndex = -1;
	}

   	    
		if(!(e.getComponent() instanceof ui.DraggableImage)) {
			
			QueueImage label = (QueueImage)e.getComponent();
			
			Point componentPoint = SwingUtilities.convertPoint(
			   label,
			   new Point(e.getX() - (label.getIcon().getIconWidth() / 2), e.getY() - (label.getIcon().getIconHeight() / 2)),
			   target);
		 
			target.dispatchEvent(new MouseEvent(target,
			    e.getID(),
			    e.getWhen(),
			    e.getModifiers(),
			    componentPoint.x,
			    componentPoint.y,
			    e.getClickCount(),
			    e.isPopupTrigger()));
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		
		this.lastObservedPress = 0;
		
		DraggableImage target = parent.getGhostImage();
		
		if(draggedLabelIndex > -1) {
		
			target.dispatchEvent(new MouseEvent(target,
				    e.getID(),
				    e.getWhen(),
				    e.getModifiers(),
				    e.getX(),
				    e.getY(),
				    e.getClickCount(),
				    e.isPopupTrigger()));
		}
		 
		 if(previousLineLabel != null && previousLineLabel.getIcon().equals(divLine)) {
			
			 Vector<QueueImage> tempLabels = new Vector<QueueImage>();
			 Vector<MediaFile> newImages = new Vector<MediaFile>();
			 
			 for(int i=0;i<labels.length;i++) {
				 tempLabels.add(labels[i]);
			 }
			 
			 if(draggedLabelIndex > -1) { 
				 tempLabels.remove(draggedLabelIndex);
				 Logger.log(Logger.INFO,"MediaStrip: Removing image at index: " + draggedLabelIndex);
				 tempLabels.insertElementAt(labels[draggedLabelIndex], previousLineIndex);	
			 }
			 
			 else {
				 QueueImage newImage = new QueueImage(target.getMediaFile());
				 tempLabels.insertElementAt(newImage,previousLineIndex);
				 jlabelToMediaFile.put(newImage, target.getMediaFile());	 
			 }
			 
			 for(int i=0;i<tempLabels.size();i++) {
					if(jlabelToMediaFile.get(tempLabels.get(i)) != null) {
						newImages.add(jlabelToMediaFile.get(tempLabels.get(i)));
					}
			 }	
			   
		
				((JTabbedPane)this.getView()).setComponentAt(
						((JTabbedPane)this.getView()).getSelectedIndex(),
						constructView(newImages, previousLineIndex+3));
			
			
		 }
		 
		 
		 
		 labels = null;
		 draggedLabelIndex = -10;
		 draggedLabel = null;
		 imageMidpoints = null;
		 previousLineLabel = null;
		 previousLineIndex = -1;
		 imagePanel = null;
		 dragging = false;
		 
	}
	
	public JPanel constructView(Vector mediaFiles, int activeIndex) {
		
		int totalWidth = 0;
		
		JPanel imagePanel;
		
		if(mediaFiles.size() == 0) { 
			imagePanel = new DragImagesHereJPanel();
			imagePanel.setLayout(new FlowLayout(FlowLayout.LEFT,FLOW_PADDING,0));
		}
		else {
			imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,FLOW_PADDING,0));
		}
		
		//imagePanel.setBackground(Color.white);
		 
		
		for(int i=0;i<mediaFiles.size();i++) {
			QueueImage line = new QueueImage(divEmpty);
			imagePanel.add(line);
			
			MediaFile mFile = ((MediaFile)mediaFiles.get(i));
			
			QueueImage image = new QueueImage(mFile);
			image.setQueueIndex(i);
			image.addMouseListener(this);
			image.addMouseMotionListener(this);
			
			if(((MediaFile)jlabelToMediaFile.get(selectedImage)) != null && 
					((MediaFile)jlabelToMediaFile.get(selectedImage)).getPath().contentEquals(mFile.getPath())) {
				image.setBorder(BorderFactory.createLineBorder(Constants.SELECTION_COLOR,3));
				image.setSelected(true);
			}
			else {
				image.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
				image.setSelected(false);
			}
			
			imagePanel.add(image);
			jlabelToMediaFile.put(image,mFile);
			
			totalWidth+=Constants.THUMB_SIZE.width;
			totalWidth+=10; 	//Line width
			totalWidth+=FLOW_PADDING;		//Flowlayout padding
		}
		
		QueueImage line = new QueueImage(divEmpty);
		imagePanel.add(line);
		
		JPanel fullView = new JPanel(new BorderLayout(0,0));
		JPanel barView = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		barView.setBackground((Color)UIManager.get("TabbedPane.selected"));
		//QueueImage groupName = new QueueImage(this.getCurrentStationView().getTitleAt(this.getCurrentStationView().getSelectedIndex()));
		//groupName.setFont(groupName.getFont().deriveFont(24));
		//barView.add(groupName);
		
		
		JButton deleteGroupButton = new JButton("Delete Group"); 
		deleteGroupButton.setActionCommand("delete_group");
		deleteGroupButton.addActionListener(this);
		
		JButton renameGroupButton = new JButton("Rename Group"); 
		renameGroupButton.setActionCommand("rename_group");
		renameGroupButton.addActionListener(this);
		
		barView.add(renameGroupButton);
		barView.add(deleteGroupButton);
		
		barView.setSize(1010,15);
		barView.setMaximumSize(new Dimension(1010,15));
		barView.setPreferredSize(new Dimension(1010,15));
		
		
		JScrollPane scrollView = new JScrollPane(imagePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		fullView.add(barView, BorderLayout.CENTER);
		fullView.add(scrollView, BorderLayout.SOUTH);
		
		JScrollBar hBar = scrollView.getHorizontalScrollBar();
		
		hBar.setPreferredSize(new Dimension( (int)hBar.getPreferredSize().getWidth(), 
				 (int)hBar.getPreferredSize().getHeight() + SCROLLBAR_HEIGHT) );
		
		scrollView.setSize(0,Constants.THUMB_SIZE.height+40);
		scrollView.setPreferredSize(new Dimension(0,Constants.THUMB_SIZE.height+40));
		scrollView.setMaximumSize(new Dimension(0,Constants.THUMB_SIZE.height+40));
		scrollView.setMinimumSize(new Dimension(0,Constants.THUMB_SIZE.height+40));
		
		scrollView.getHorizontalScrollBar().setMinimum(0);
		scrollView.getHorizontalScrollBar().setMaximum(totalWidth);
		
		if(activeIndex > 0) {
			//Logger.log(Logger.DEBUG,"MediaStrip: Active Index: " + activeIndex + "Total Width: " + totalWidth);
			//Logger.log(Logger.DEBUG,"MediaStrip: Scroll value: " + ((totalWidth/(mediaFiles.size()) * (activeIndex/2))));
			scrollView.getHorizontalScrollBar().setValue((totalWidth/(mediaFiles.size()) * (activeIndex/2)));
			//scrollView.getHorizontalScrollBar().setValue(scrollView.getHorizontalScrollBar().getMaximum());
		}
		
		if(!firstDraw && tabView.getSelectedIndex() > -1) {
			getGroupQueueByName(tabView.getTitleAt(tabView.getSelectedIndex())).setMediaFiles(mediaFiles);
		}
		
		return fullView;
	}
	
	public void findImagePanel() {
		JTabbedPane tabPane = this.getView();
		if(!(tabPane.getSelectedComponent() instanceof JPanel)) { imagePanel = null; return; }
		JPanel fullPanel = (JPanel)tabPane.getSelectedComponent();
		//Logger.log(Logger.DEBUG,fullPanel.getComponent(1).toString());
		JScrollPane scrollPane = (JScrollPane)(fullPanel.getComponent(1));
		imagePanel = (JPanel)((JPanel)((JViewport)scrollPane.getComponent(0)).getComponent(0));
		
	}
	
	public void actionPerformed( ActionEvent e ) {
	    String command = e.getActionCommand();
	    JFrame frame = new JFrame();
	    if (command.equals( "delete_group" )) {
	    	
	    	Object[] options = {"Don't Delete","Delete Group"};
	    	
	    	int n = JOptionPane.showOptionDialog(frame,
	    				"Are you sure you want to delete this group?",
	    				"Confirm Delete Group",
	    				JOptionPane.YES_NO_OPTION,
	    				JOptionPane.QUESTION_MESSAGE,
	    				null,
	    				options,
	    				options[1]);
	    	
	    	if(n == 1) {
	    		
	    		String groupName = tabView.getTitleAt(tabView.getSelectedIndex());
	    		
	    		if(this.getView().getSelectedIndex() == this.getView().getTabCount()-2) {
	    			this.getView().removeTabAt(this.getView().getSelectedIndex());
	    			this.getView().setSelectedIndex(this.getView().getSelectedIndex()-1);
	    		}
	    		else {
	    			this.getView().removeTabAt(this.getView().getSelectedIndex());
	    		}
	    		
	    		theStation.removeGroupQueue(getGroupQueueByName(groupName));
	    	}
	    }
	    
	    if (command.equals("rename_group")) {
			String message = "Enter a new name for the group:";
			
			//Modal dialog with OK/cancel and a text field
		    String text = JOptionPane.showInputDialog(frame, message);
		    if (text != null) {
		    	
		    	for(int i=0;i<tabView.getTabCount();i++) {
		    		if(text.contentEquals(tabView.getTitleAt(i))) {
		    			JOptionPane.showMessageDialog(new JFrame(), "You already have a group named that.");
		    			return;
		    		}
		    	}
		    	
		    	// Get correct GroupQueue and rename it
		    	GroupQueue queue = (GroupQueue)this.getGroupQueueByName(tabView.getTitleAt(tabView.getSelectedIndex()));
		    	queue.setGroupName(text);
		    	this.getQueuesByName().put(text, queue);
		    	
		    	// Reflect name change in tab
		    	tabView.setTitleAt(tabView.getSelectedIndex(),text);
	    }
	  }
	}
	
}