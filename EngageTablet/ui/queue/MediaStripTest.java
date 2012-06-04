package ui.queue;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

import main.Logger;

public class MediaStripTest {

	public static void main(String args[])
    {
        
		try {
		       UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {Logger.log(Logger.INFO, "Could not locate Metal Look&Feel: "+e.toString()); }

		JFrame frm = new JFrame("MediaStripTest");
        frm.setDefaultCloseOperation(3);
        
		Vector images1 = new Vector();
		images1.add(new ImageIcon("ui/queue/01.png"));
		images1.add(new ImageIcon("ui/queue/01.png"));
		images1.add(new ImageIcon("ui/queue/01.png"));
		
		Vector images2 = new Vector();
		images2.add(new ImageIcon("ui/queue/02.png"));
		images2.add(new ImageIcon("ui/queue/02.png"));
		images2.add(new ImageIcon("ui/queue/02.png"));
		images2.add(new ImageIcon("ui/queue/02.png"));
		images2.add(new ImageIcon("ui/queue/02.png"));
		
		Vector groups = new Vector();
		groups.add(images1);
		groups.add(images2);
	
		//String names[] = {"1","2"};
	
		//MediaStrip theStrip = new MediaStrip(groups,names);

        //pane.addMouseWheelListener(new TabbedPaneMouseWheelScroller());
        //pane.addMouseListener(new TabSelectionMouseHandler());
        //TabReorderHandler.enableReordering(pane);
      

        //pane.setPreferredSize(new Dimension(400, 400));
        //frm.getContentPane().add(theStrip.getCurrentStationView(), "Center");
        frm.pack();
        frm.setVisible(true);

    }

}