package backend.library;

import java.io.File;

/**
 * This class represents an image file. It is fairly useless unless we plan on including 
 * other kinds of files at some point.
 * 
 * @author Carl Angiolillo
 */
public class ImageFile extends MediaFile {
	
	/**
	 * @param imageFile the File this image comes from
	 * @param parent the CategoryMedia that contains this image
	 */
	public ImageFile(File imageFile, CategoryMedia parent) {
		super(imageFile, parent);
		//this.setImages((new ImageIcon(this.getFile().getAbsolutePath())).getImage() );
	} 
	
}
