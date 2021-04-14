package model;
/**
 * Photos is a single-user photo application that allows storage and management of photos in one or more albums.
 * 
 * @author 		Anthony Siu
 * @author 		Benjamin Lee
 * @version		%I% %G%
 * @since		1.0
 *
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Photo implements Comparable<Photo> {
	//fields
	private String filePath;
	protected String caption;
	protected Calendar calDate;
	protected ArrayList<Tag> tags;
	
	/**
	 * Photo is a class that holds an image file and has various characteristics associated with it. These characteristics include date, time, tags, file path, caption, and album.
	 * 
	 * @param imageFile		Name of the image file
	 * @see Album#Album(String)
	 */
	//constructor
	public Photo(File imageFile) {
		this.filePath = imageFile.getPath();
		this.caption = null;
		Date date = new Date(imageFile.lastModified());
		this.calDate = Calendar.getInstance();
		this.calDate.setTime(date);
		this.calDate.set(Calendar.MILLISECOND,0);
		this.tags = new ArrayList<Tag>();
		
	}
	/**
	 * This method gets the date of the photo.
	 * 
	 * @return calDate
	 */
	//gets date
	public Calendar getDate() {
		return calDate;
	}
	
	/**
	 * This method gets the caption of the photo.
	 * 
	 * @return caption
	 */
	//gets caption
	public String getCaption() {
		return caption;
	}
	
	/**
	 * This method gets the file path of the photo.
	 * 
	 * @return filePath
	 */
	//gets path
	public String getPath() {
		return filePath;
	}
	
	/**
	 * This method gets the tags of the photo.
	 * 
	 * @return tags
	 * @see Tag#Tag(String, String)
	 */
	//gets Tags
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	/**
	 * This method adds a tag to a photo and takes the required inputs for the tags.
	 * 
	 * @param inputTag		Tag taken as an input, the name and value.
	 * @see Tag#Tag(String, String)
	 */
	//adds a tag
	public void addTag(Tag inputTag) {
		tags.add(inputTag);
	}
	@Override
	/**
	 * This method compares the dates of the photos,
	 * 
	 * @param photo		A photo file
	 */
	public int compareTo(Photo photo) {
		return calDate.compareTo(photo.calDate);
	}
	
}
