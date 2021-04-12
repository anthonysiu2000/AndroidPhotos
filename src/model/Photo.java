package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Photo {
	//fields
	private String filePath;
	protected String caption;
	protected Calendar calDate;
	protected ArrayList<Tag> tags;
	
	//constructor
	public Photo(File imageFile) {
		filePath = imageFile.getPath();
		caption = null;
		Date date = new Date(imageFile.lastModified());
		calDate = Calendar.getInstance();
		calDate.setTime(date);
		calDate.set(Calendar.MILLISECOND,0);
		tags = new ArrayList<Tag>();
		
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
	
}
