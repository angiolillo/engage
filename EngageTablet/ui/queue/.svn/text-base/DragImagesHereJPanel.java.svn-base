package ui.queue;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DragImagesHereJPanel extends JPanel {
	

	public DragImagesHereJPanel() {
		super();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		//Draw the text itself
		g2d.setColor(Color.gray);
		g2d.setFont(new Font("Arial",Font.BOLD,36));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
		g2d.drawString("Drag Images Here", 345, 60);
		
		
	}

}
