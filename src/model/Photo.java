//made by Anthony Siu and Benjamin Lee

package model;

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
	//gets date
	public Calendar getDate() {
		return calDate;
	}
	//gets caption
	public String getCaption() {
		return caption;
	}
	//gets path
	public String getPath() {
		return filePath;
	}
	//gets Tags
	public ArrayList<Tag> getTags() {
		return tags;
	}
	//adds a tag
	public void addTag(Tag inputTag) {
		tags.add(inputTag);
	}
	@Override
	public int compareTo(Photo photo) {
		return calDate.compareTo(photo.calDate);
	}
	
}
