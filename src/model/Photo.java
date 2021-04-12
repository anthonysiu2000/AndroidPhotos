package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class Photo {
	//fields
	private String filePath;
	private String caption;
	private Calendar calDate;
	private ArrayList<Tag> tags;
	
	//constructor
	public Photo(File imageFile) {
		filePath = imageFile.getPath();
		caption = null;
		Date date = Date(imageFile.lastModified());
		tags = new ArrayList<Tag>();
	}
	//gets date
	
	//gets caption
	
	//gets date
	
	//adds a tag
	
}
