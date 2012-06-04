package ui.splash;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JWindow;

import main.Logger;
 
/**
 *  A splash screen that produces the illusion of transparency for a top level Container.
 *  This splash screen also supports dynamic adding of a progress indicator (in the form of
 *  an animated GIF), and also dynamic text that is painted directly onto the splash screen.
 *  adapted from code by F. Fleischer
 *  
 *  @author Andy Tzou
 */
public class VoyagerSplash extends JWindow implements Runnable {
 
  
  //File path of the image to display, preferrably .gif or .png. Best choice
  //would be the png-24 format, since it supports 256 transparency levels. 
  private String imageFile;
 
  //A writable off screen image.
  private BufferedImage bufImage;
 
  //The rectangle to be captured.  
  private Rectangle rect;
  
  //True if initialization thread is running.  
  private boolean isAlive;
  
   //Image dimensions used to size the entire splash screen and for text positioning
  private int imageWidth;
  private int imageHeight;
  
   //Text to be displayed on splash screen
  private String loadText = "";
  
  //The animated image serving as progress indicator
  private Image animatedImage = null;
 
  /**
   *  Constructor for the SplashScreen object. The Splash Screen initially shows in "splash mode" that
   *  is bare-bones: no text (given in textToPaint) is not drawn, and the animated image (given in animatedFile)
   *  is also not drawn. Special methods later enable the text, animated image, and changes the splash image itself.
   *
   *  @param  splashFile file path of the splash image to display - this is required and painted at thread launch
   */
  public VoyagerSplash(String splashFile) {

    this.imageFile = splashFile;
    run();
  }

  /**
   * Designates an animated gif for splash screen that serves as a progress display.
   * This method can be called at any time, and will instantly be displayed when invoked
   * with a valid image file path. Note that the position of the animated image is controlled
   * in the paint() method of VoyagerSplash.
   * 
   * @param animatedFile file path to the animated image (usually gif)
   */
  public void setAnimated(String animatedFile)
  {
	  Logger.log(Logger.DEBUG,"Animating splash screen");
	  if (animatedFile.equalsIgnoreCase("") == false)
	  {
			this.animatedImage = (new ImageIcon(animatedFile)).getImage();
	  		repaint();
	  }
	  
  }
  
  /**
   * Sets text for the splash screen. This method can be called at any time, and will instantly
   * be displayed when invoked with a valid string. Note that the position of the text is controlled
   * in the paint() method of VoyagerSplash.
   * 
   * @param newText Text to render on splash screen
   */
  public void setText(String newText)
  {
	  if (newText.equalsIgnoreCase("") == false)
	  {
		  this.loadText = newText;
		  repaint();
	  }
  }
 
  
  /**
   *  Starts the initialization thread of the SplashScreen.
   */
  public void run() {
	  
    isAlive = true;
    // use ImageIcon, so we don't need to use MediaTracker
    Image image = new ImageIcon(imageFile).getImage();

    imageWidth = image.getWidth(this);
    imageHeight = image.getHeight(this);
    
    if (imageWidth > 0 && imageHeight > 0) {
      int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
      int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
      
      int OFF_CENTER = 35;
      // a Rectangle centered on screen
      rect = new Rectangle((screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2 - OFF_CENTER,
          imageWidth, imageHeight);
      
      //actually paint
      try 
	  	 {
	         bufImage = new Robot().createScreenCapture(rect);
	     } catch (AWTException e) { e.printStackTrace(); }
	     
	       // obtain the graphics context from the BufferedImage
	       Graphics2D g2D = bufImage.createGraphics();
	       // Draw the image over the screen shot
	       g2D.drawImage(image , 0, 0, this);
	       repaint();

     
      //size and locate the splash screen
      setSize(imageWidth, imageHeight); //possibly need setMaximum or setPreferred?
      setLocation( (screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2 - OFF_CENTER);
       
      this.setVisible(true);
    } else {
      System.err.println("File " + imageFile + " was not found or is not an image file.");
    }
    isAlive = false;
  }
 
  /**
   *  Disposes of the SplashScreen. To be called shortly before the main application
   *  is ready to be displayed.
   *
   *  @exception  IllegalStateException  Is thrown if the initialization thread
   *              has not yet reached it's end.
   */
  public void close() throws IllegalStateException {
    if (!isAlive) {
      dispose();
    } else {
      // better not dispose a SplashScreen that has not been painted on screen yet.
      throw new IllegalStateException("SplashScreen not yet fully initialized.");
    }
  }
 
  private static final int HPOS = 225;
  /**
   *  Overrides the paint() method of JWindow.
   *
   *  @param g The graphics context
   */
  public void paint(Graphics g) {
    Graphics2D g2D = (Graphics2D)g;
    g2D.drawImage(bufImage, 0, 0, this);
    
    Font paintFont = new Font("Arial", Font.PLAIN, 10);
    
    //FontMetrics fontMetrics = getFontMetrics(paintFont);
    //int stringWidth = fontMetrics.stringWidth(this.loadText);
    
    //position & render text, if available
    //if ( this.loadText.equalsIgnoreCase("") == false )
    {
        //g2D.setFont(paintFont);
        //g2D.setColor(Color.GRAY);
    	//g2D.drawString(this.loadText, HPOS, 2 * this.imageHeight / 3);
    	
    	paintFont = new Font("Arial", Font.BOLD, 16);
        g2D.setFont(paintFont);
        g2D.setColor(Color.BLACK);
    	g2D.drawString("Loading Content...", HPOS, (this.imageHeight / 2) + 10 );
    }
    
    //position & render animated image, if available
    if (this.animatedImage  != null){
    	paintFont = new Font("Arial", Font.BOLD, 16);
        g2D.setFont(paintFont);
        g2D.setColor(Color.BLACK);
    	g2D.drawString("Loading Content...", HPOS, (this.imageHeight / 2) + 10 );
    	g2D.drawImage(this.animatedImage, HPOS, (this.imageHeight / 2) + 20, this);
    }

  }

  //example driver
  public static void main(String[] args)
  {
	  VoyagerSplash splash = new VoyagerSplash("ui/splash/vger_splash.png");
	  
	  splash.run();
	  
	  try{
		  Thread.sleep(2000); //this is where you sleep the splash
	  } catch (InterruptedException e){ splash.close(); }

	  //set the text and specify the animated gif
	  splash.setText("Text message indicating what's currently loading");
	  splash.setAnimated("ui/splash/anim.gif");
	  
	  try{
		  Thread.sleep(5000); //here instead of sleeping, enter your loop that constantly loads and calls my setText
	  } catch (InterruptedException e){ splash.close(); }
	  
	  splash.close();
	  System.exit(0);
	  
  }
  
} 


