package ui.queue;

import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.MouseInputAdapter;

import backend.queue.GroupQueue;

public class TabReorderHandler extends MouseInputAdapter {
		public static void enableReordering(JTabbedPane pane, MediaStrip strip) {
			TabReorderHandler handler = new TabReorderHandler(pane, strip);
			pane.addMouseListener(handler);
			pane.addMouseMotionListener(handler);
		}
		
		private JTabbedPane tabPane;
		private MediaStrip theStrip;
		private int draggedTabIndex;
		
		
		public void mousePressed(MouseEvent e)
        {
            draggedTabIndex = tabPane.getUI().tabForCoordinate(tabPane, e.getX(), e.getY());
        }
		
		public void mouseClicked(MouseEvent e) {
			/*
			if(e.getClickCount() == 2) {
				
				JFrame frame = new JFrame();
				String message = "Enter a new name for the group:";
				
				//Modal dialog with OK/cancel and a text field
			    String text = JOptionPane.showInputDialog(frame, message);
			    if (text != null) {
			    	
			    	for(int i=0;i<tabPane.getTabCount();i++) {
			    		if(text.contentEquals(tabPane.getTitleAt(i))) {
			    			JOptionPane.showMessageDialog(new JFrame(), "You already have a group named that.");
			    			return;
			    		}
			    	}
			    	
			    	// Get correct GroupQueue and rename it
			    	GroupQueue queue = (GroupQueue)theStrip.getGroupQueueByName(tabPane.getTitleAt(tabPane.getSelectedIndex()));
			    	queue.setGroupName(text);
			    	theStrip.getQueuesByName().put(text, queue);
			    	
			    	// Reflect name change in tab
			    	tabPane.setTitleAt(tabPane.getSelectedIndex(),text);
			    }
				
			} */
		}
		
		public void mouseReleased(MouseEvent e) {
			if(draggedTabIndex == (tabPane.getTabCount()-1)) {
				createNewAddTab();
			}
			draggedTabIndex = -1;
		}

		public void mouseDragged(MouseEvent e) {
			if(draggedTabIndex == -1 || draggedTabIndex == (tabPane.getTabCount()-1)) {
				return;
			}
			
			int targetTabIndex = tabPane.getUI().tabForCoordinate(tabPane,
					e.getX(), e.getY());
			if(targetTabIndex != -1 && targetTabIndex != draggedTabIndex && targetTabIndex != (tabPane.getTabCount()-1)) {
				boolean isForwardDrag = targetTabIndex > draggedTabIndex;
				tabPane.insertTab(tabPane.getTitleAt(draggedTabIndex),
						tabPane.getIconAt(draggedTabIndex),
						tabPane.getComponentAt(draggedTabIndex),
						tabPane.getToolTipTextAt(draggedTabIndex),
						isForwardDrag ? targetTabIndex+1 : targetTabIndex);
				draggedTabIndex = targetTabIndex;
				tabPane.setSelectedIndex(draggedTabIndex);
			}
		}
		
		public void createNewAddTab() {
			
			JFrame frame = new JFrame();
			String message = "Enter a name for the new group";
			
			//Modal dialog with OK/cancel and a text field
		    String text = JOptionPane.showInputDialog(frame, message,"Add Group to Queue",JOptionPane.QUESTION_MESSAGE);
		    if (text != null) {
		    	
		    	for(int i=0;i<tabPane.getTabCount();i++) {
		    		if(text.contentEquals(tabPane.getTitleAt(i))) {
		    			JOptionPane.showMessageDialog(new JFrame(), "You already have a group named that.");
		    			return;
		    		}
		    	}
		    	
		    	GroupQueue newGroup = new GroupQueue(theStrip.getStationQueue().getPath(),
		    										 theStrip.getStationQueue().getMediaLibrary(),
		    										 theStrip.getStationQueue().getQLibrary(),
		    										 text);
		    	
		    	theStrip.getQueuesByName().put(text, newGroup);
		    	theStrip.getStationQueue().addGroupQueue(newGroup);
		    										 
		    	tabPane.setTitleAt(tabPane.getTabCount()-1,text);
				tabPane.setIconAt(tabPane.getTabCount()-1, new ImageIcon("ui/queue/tabIcon.png"));
				tabPane.setSelectedIndex(tabPane.getTabCount()-1);
				tabPane.setComponentAt(tabPane.getTabCount()-1, theStrip.constructView(new Vector(), -1));
				tabPane.addTab("Add Group",new ImageIcon("ui/queue/tabPlus.png"), new JPanel());
		    }
		    else {
		    	tabPane.setSelectedIndex(0);
		    }
		}
		
		protected TabReorderHandler(JTabbedPane pane, MediaStrip strip)
        {
            tabPane = pane;
            theStrip = strip;
            draggedTabIndex = -1;
        }
}