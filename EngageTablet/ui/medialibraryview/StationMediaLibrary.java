package ui.medialibraryview;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import main.Constants;
import main.Engage;
import backend.library.CategoryMedia;
import backend.library.MediaFile;
import backend.library.ProgramMedia;
import backend.library.StationMedia;

/**
 * The StationMediaLibrary is a view of a single station's media library.
 * 
 * @author Andy Tzou
 */
public class StationMediaLibrary extends JPanel{

	//determines horizontal "spread" of each column	
	protected Engage handleToMain;	
	public static int NUMBER_OF_COLUMNS = 5;
	public static int SCROLLBAR_WIDTH = 12;
	protected LibraryCell lastSelectedCell = null;

	//cardPanels that allow flipping between "pages" of the Media Library
	JPanel headerCards;
	JPanel columnCards;
	
	//panel containing Next and Prev navigation and a banner
	NavPanel navigationPanel;
	
	//internal lists of the columns and headers (pages)
	ArrayList columnList;
	ArrayList headerList;

	//pointer to this object
	StationMediaLibrary handleToMe;

	/**
	 * StationMediaLibrary arranges images in column format for a given teaching station. 
	 * @param mainFrame a pointer to the "main" window 
	 * @param pm a pointer to the backend program media
	 * @param stationName a text name of the station to load
	 */
	public StationMediaLibrary(Engage mainFrame, ProgramMedia pm, String stationName){

		this.handleToMe = this;
		this.handleToMain = mainFrame; //get a handle to the main container

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				
		headerCards = new JPanel();
		columnCards = new JPanel();
		
		columnList = new ArrayList();
		headerList = new ArrayList();
		
		headerCards.setLayout(new CardLayout());
		columnCards.setLayout(new CardLayout());
		
		//retrieve stations
		StationMedia sm = pm.getStation(stationName);
		
		int category_count = 0; 
		
		//retrieve categories
		Iterator categoryIter = sm.getCategories();
		
		//we alternate white and gray color per column, so use counter to alternate
		int odd_even_counter = 0;
		
		while (categoryIter.hasNext())
		{
			CategoryMedia cat = (CategoryMedia)categoryIter.next();
			
			//create a new page for every NUMBER_OF_COLUMNS'th column
			if (category_count % NUMBER_OF_COLUMNS == 0)
			{
				JPanel columnPanel = new JPanel(); //panel that will hold the columns	
				
				columnPanel.setLayout(new GridLayout(1, NUMBER_OF_COLUMNS)); //Rows, Columns, Hgap, Vgap
				JPanel headerPanel = new JPanel(); //panel that will hold the headers
	
				headerPanel.setLayout(new GridLayout(1, NUMBER_OF_COLUMNS));
				
				headerPanel.setBackground(new Color(202, 216, 234));
				//add new panels to lists
				columnList.add(columnPanel);
				headerList.add(headerPanel);
			}
			
			//add category to header panel 
			JLabel header = new JLabel("<html><p>" + cat.getDisplayName() + "</p></html>");
			//header.setFont(new Font("SansSerif", Font.BOLD, 14));		
			
			//force each header to be the size of a thumbnail to ensure proper alignment with columns
			header.setPreferredSize(new Dimension(Constants.THUMB_SIZE.width, (int)(header.getPreferredSize().getHeight()*2)));
			header.setHorizontalAlignment(JLabel.CENTER);
			((JPanel)(headerList.get(headerList.size() - 1))).add(header);
			
			//retrieve media files and add them to the column (done in MediaColumn class)
			Iterator mediaIter = cat.getMediaFiles();
			MediaColumn mc = new MediaColumn(mediaIter);
			
			if (odd_even_counter % 2 == 0)
				mc.setBackground(Color.getHSBColor(0f, 0f, .85f));
			else
				mc.setBackground(null);

			odd_even_counter++;
			
			//add column to the latest columnPanel in list
			((JPanel)(columnList.get(columnList.size() - 1))).add(mc);
			
			category_count++;
		}
		
		for(int i = 0; i < columnList.size(); ++i)
		{
			//scrollpane that will hold the column panel
			JScrollPane jsp = new JScrollPane((JPanel)columnList.get(i), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			JScrollBar vBar = jsp.getVerticalScrollBar();

			vBar.setPreferredSize(new Dimension( (int)vBar.getPreferredSize().getWidth() + SCROLLBAR_WIDTH, 
					 (int)vBar.getPreferredSize().getHeight()) );
			
			jsp.setVerticalScrollBar(vBar);
			
			
			//JScrollBar(int orientation, int value, int extent, int min, int max)
			columnCards.add(jsp, "jsp");
		}
		
		for(int i = 0; i < headerList.size(); ++i)
		{
			JPanel currentHeaderPanel = (JPanel)headerList.get(i);
			headerCards.add(currentHeaderPanel, "header");
		}
		
		headerCards.setPreferredSize(new Dimension(1024, 29));
		headerCards.setMaximumSize(new Dimension(1024, 29));
		
		
		navigationPanel = new NavPanel();
		this.add(navigationPanel);
		this.add(headerCards);
		this.add(columnCards);
	}
	
	/**
	 * Reports the last cell in the MediaLibrary that the user selected
	 * @return last cell in the MediaLibrary that the user selected
	 */
	public LibraryCell getLastSelectedCell()
	{
		return this.lastSelectedCell;
	}
	/**
	 * Returns the number of pages in the MediaLibrary
	 * @return the number of pages in the MediaLibrary
	 */
	public int getPageCount()
	{
		return columnList.size();
	}
	
	/**
	 * This unselects the currently selected cell, if any. This is useful if another part
	 * of the application (like the queue) needs to synchronize cell selection/de-selection.
	 */
	public void releaseSelection()
	{
		if (this.lastSelectedCell == null)
			return;
		
		this.lastSelectedCell.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		this.lastSelectedCell.setHighlighted(false);
		this.lastSelectedCell = null; //special case where no deselection needed on next click, so nullify
	}
	
	//-------------------------------------------------------------------------
	// MediaColumn
	//-------------------------------------------------------------------------	
	
	
	/**
	 * The fundamental column in a MediaLibrary is a MediaColumn that lays out
	 * images vertically.
	 */
	class MediaColumn extends JPanel
	{
		
		public MediaColumn(Iterator iter)
		{
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			//cycle through the images using the given iterator, create a LibraryCell
			//with it and add it to the column
			while (iter.hasNext())
			{
				MediaFile mf = (MediaFile)iter.next(); 

				LibraryCell imageCell = new LibraryCell(StationMediaLibrary.this, mf, handleToMe);
				imageCell.setAlignmentX(Component.CENTER_ALIGNMENT);

				this.add(imageCell);	
			}

		}
	}
	
	//-------------------------------------------------------------------------
	// NavPanel
	//-------------------------------------------------------------------------
	/**
	 * NavPanel contains a graphic banner along with Prev/Next navigation buttons
	 * to page through the Media Library
	 */
	public class NavPanel extends JPanel
	{
		BannerBar banner;
		int currentPage = 1;
		
		/**
		 * Creates a Navigational Panel that is ultimately added to the Media Library
		 */
		public NavPanel()
		{
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

			JButton prev = new JButton("");
			JButton next = new JButton("");
			
			ImageIcon prevImage = new ImageIcon("ui/medialibraryview/lib_prev.png");
			ImageIcon nextImage = new ImageIcon("ui/medialibraryview/lib_next.png");

			prev.setIcon(prevImage);
			next.setIcon(nextImage);
			
			prev.setFocusPainted(false);
			next.setFocusPainted(false);
			
			prev.setPreferredSize(new Dimension(prevImage.getIconWidth(), prevImage.getIconHeight()));
			next.setPreferredSize(new Dimension(nextImage.getIconWidth(), nextImage.getIconHeight()));
			
			banner = new BannerBar("ui/medialibraryview/banner_title.png");
			
			//page back by one
			prev.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					((CardLayout)(headerCards.getLayout())).previous(headerCards);
					((CardLayout)(columnCards.getLayout())).previous(columnCards);
					
					currentPage--;
					
					if (currentPage == 0)
						currentPage = getPageCount();
					
					banner.setText("Media Library (page " + currentPage + " of " + getPageCount() + ")");
				}
			});
			
			//page forward by one
			next.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					((CardLayout)(headerCards.getLayout())).next(headerCards);
					((CardLayout)(columnCards.getLayout())).next(columnCards);		
			
					//increment page number
					currentPage++;
					
					if (currentPage == getPageCount() + 1)
						currentPage = 1;
					
					banner.setText("Media Library (page " + currentPage + " of " + getPageCount() + ")");
				}
				
				}
			);
			
			//if there's only one page, disable the nav buttons
			if (columnList != null && columnList.size() <= 1)
			{
				prev.setEnabled(false);
				next.setEnabled(false);
			}
			
			add(prev); 
			add(banner);
			add(next);
			
		}
	}	
	
	private class BannerBar extends JLabel
	{
		public BannerBar(String path)
		{
			this.setIcon(new ImageIcon(path));
			this.setHorizontalTextPosition(JLabel.CENTER);
			
			//find suitable font
			this.setFont( new Font("ARIAL", Font.BOLD, 18) );
			this.setText("Media Library (page 1 of " + getPageCount() + ")");
		}
	}
}