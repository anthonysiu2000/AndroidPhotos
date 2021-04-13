package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class NonAdmin extends User {
	//fields
	private ArrayList<Album> albumList;
	private ArrayList<Photo> photoList;
	private ArrayList<Connection> conList;
	private int albumNumCreate;
	//constructor
	public NonAdmin(String name){
		this.username = name;
		this.albumList = new ArrayList<Album>();
		this.photoList = new ArrayList<Photo>();
		this.conList = new ArrayList<Connection>();
		this.albumNumCreate = 0;
		//generates photo objects and connections for stock username
		if (name.equals("stock")) {
			this.createAlbum("stock");
			//need to input stock files here using addphoto
		}
	}
	//gets list of albums for this user
	public ArrayList<Album> getAlbums() {
		return albumList;
	}
	//gets list of photos for this user
	public ArrayList<Photo> getPhotos() {
		return photoList;
	}
	//gets list of connections of this user
	public ArrayList<Connection> getConnections() {
		return conList;
	}
	//creates a new album; returns true if successful
	public boolean createAlbum(String aName) {
		for (int i = 0; i < albumList.size(); i++) {
			if (albumList.get(i).getName().equals(aName)) {
				return false;
			}
		}
		albumList.add(new Album(aName));
		return true;
	}
	//deletes an album; returns true if successful
	public boolean deleteAlbum(String aName) {
		for (int i = 0; i < albumList.size(); i++) {
			if (albumList.get(i).getName().equals(aName)) {
				albumList.remove(i);
				return true;
			}
		}
		return false;
	}
	//renames an album; returns true if successful
	public boolean renameAlbum(String oName, String nName) {
		for (int i = 0; i < albumList.size(); i++) {
			if (albumList.get(i).getName().equals(oName)) {
				albumList.get(i).aName = nName;
				return true;
			}
		}
		return false;
	}
	//adds a new photo into an album: returns true if successful
	public boolean addPhoto(String aName, File photoFile) {
		Photo photo = new Photo(photoFile);
		boolean albumFound = false;
		boolean photoFound = false;
		//finds the album that we want to insert photo
		for (int i = 0; i < albumList.size(); i++) {
			if (albumList.get(i).getName().equals(aName)) {
				albumFound = true;
				break;
			}
		}
		if (!albumFound) {
			return false;
		}
		//checks if a connection is already established between photo and album
		for (int j = 0; j < conList.size(); j++) {
			if (conList.get(j).getAlbum().equals(aName)) {
				if (conList.get(j).getPath().equals(photo.getPath())) {
					return false;
				}
			}
		}
		//if photo does not exist in database, adds it
		for (int j = 0; j < conList.size(); j++) {
			if (photoList.get(j).getPath().equals(photo.getPath())) {
				photoFound = true;
				break;
			}
		}
		if (!photoFound) {
			photoList.add(photo);
		}
		//inserts a new connection
		Connection con = new Connection(aName, photo.getPath());
		conList.add(con);
		return true;
	}
	//removes a photo from an album; returns true if successful
	public boolean removePhoto(String aName, String photoPath) {
		boolean albumFound = false;
		boolean conFound = false;
		int numCon = 0;
		//finds the album that we want to insert photo
		for (int i = 0; i < albumList.size(); i++) {
			if (albumList.get(i).getName().equals(aName)) {
				albumFound = true;
				break;
			}
		}
		if (!albumFound) {
			return false;
		}
		//finds photo in connection list and removes it
		for (int j = 0; j < conList.size(); j++) {
			if (conList.get(j).getPath().equals(photoPath)) {
				if (conList.get(j).getAlbum().equals(aName)) {
					conList.remove(j);
					conFound = true;
				}
				numCon++;
			}
		}
		if (!conFound) {
			return false;
		}
		//finds photo in the album and removes it if no other album references it
		if (numCon == 1) {
			for (int k = 0; k < photoList.size(); k++) {
				if (photoList.get(k).getPath().equals(photoPath)) {
					photoList.remove(k);
				}
			}
		}
		return true;
	}
	//adds or renames the caption of a specified photo; returns true if successful
	public boolean addCaption(String photoPath, String caption) {
		for (int k = 0; k < photoList.size(); k++) {
			if (photoList.get(k).getPath().equals(photoPath)) {
				photoList.get(k).caption = caption;
				return true;
			}
		}
		return false;
	}
	//adds a tag to a specified photo; returns true if successful
	public boolean addTag(String photoPath, String tagName, String tagValue) {
		//checks if the photo exists
		Tag tag = new Tag(tagName, tagValue);
		for (int k = 0; k < photoList.size(); k++) {
			if (photoList.get(k).getPath().equals(photoPath)) {
				photoList.get(k).tags.add(tag);
				return true;
			}
		}
		return false;
	}
	//removes a tag of a specified photo; returns true if successful
	public boolean removeTag(String photoPath, String tagName, String tagValue) {
		//checks if the photo exists
		for (int k = 0; k < photoList.size(); k++) {
			if (photoList.get(k).getPath().equals(photoPath)) {
				//checks if the tag exists
				for (int i = 0; i < photoList.get(k).tags.size(); i++) {
					if (photoList.get(k).tags.get(i).getName() == tagName && 
							photoList.get(k).tags.get(i).getValue() == tagValue) {
						photoList.get(k).tags.remove(i);
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}
	//copies a photo from an album to another; returns true if successful
	public boolean copyPhoto(String oAlbName, String nAlbName, String photoPath) {
		//checks if the photo exists
		for (int k = 0; k < photoList.size(); k++) {
			if (photoList.get(k).getPath().equals(photoPath)) {
				//checks if there is already an instance of the photo in the target album
				for (int i = 0; i < conList.size(); i++) {
					if (conList.get(i).getAlbum() == nAlbName && conList.get(i).getPath() == photoPath) {
						return false;
					}
				}
				conList.add(new Connection(nAlbName, photoPath));
				return true;
			}
		}
		return false;
	}
	//moves a photo from an album to another; returns true if successful
	public boolean movePhoto(String oAlbName, String nAlbName, String photoPath) {
		//copies photo to new album
		if (copyPhoto(oAlbName, nAlbName, photoPath)) {
			//removes old photo to album connection
			for (int i = 0; i < conList.size(); i++) {
				if (conList.get(i).getAlbum() == oAlbName && conList.get(i).getPath() == photoPath) {
					conList.remove(i);
					return true;
				}
			}
		}
		return false;
	}
	//returns a filtered array list of photos that in a specified date range
	public ArrayList<Photo> searchPhotoByDate(Calendar start, Calendar end) {
		//creates new array list
		ArrayList<Photo> sortedPhotos = new ArrayList<Photo>();
		for (int j = 0; j < photoList.size(); j++) {
			sortedPhotos.add(photoList.get(j));
		}
		//sorts array list in terms of calendar times
		Collections.sort(sortedPhotos);
		//removes photos that are not within the calendar range
		for (int i = sortedPhotos.size() - 1; i >= 0; i--) {
			if (sortedPhotos.get(i).getDate().compareTo(end) > 0 || sortedPhotos.get(i).getDate().compareTo(start) < 0) {
				sortedPhotos.remove(i);
			}
		}
		return sortedPhotos;
	}
	//returns an array of photos that have the tags specified; if 2, uses andOp boolean to specify "and" or "or"
	public ArrayList<Photo> serachPhotoByTag(Tag tag1, Tag tag2, boolean andOp) {
		//creates new array list
		ArrayList<Photo> sortedPhotos = new ArrayList<Photo>();
		for (int j = 0; j < photoList.size(); j++) {
			sortedPhotos.add(photoList.get(j));
		}
		//removes photos that do not have the specified tags
		for (int i = sortedPhotos.size() - 1; i >= 0; i--) {
			ArrayList<Tag> temp = sortedPhotos.get(i).getTags();
			//if there is only one tag
			if (tag2 == null) {
				for (int j = 0; j< temp.size(); j++) {
					boolean remove = true;
					if (temp.get(j).getName().equals(tag1.getName()) && temp.get(j).getValue().equals(tag1.getValue())) {
						remove = false;
					}
					if (remove) {
						sortedPhotos.remove(i);
					}
				}
			}
			//two tags
			else {
				for (int j = 0; j< temp.size(); j++) {
					boolean tag1Found = false;
					boolean tag2Found = false;
					//sets fields to true if tags are found
					if (temp.get(j).getName().equals(tag1.getName()) && temp.get(j).getValue().equals(tag1.getValue())) {
						tag1Found = true;
					}
					if (temp.get(j).getName().equals(tag2.getName()) && temp.get(j).getValue().equals(tag2.getValue())) {
						tag2Found = true;
					}
					//remove those with no tags
					if (tag1Found == false && tag2Found == false) {
						sortedPhotos.remove(i);
					}
					//In AND case, remove those with only one tag
					else if (andOp && (tag1Found ^ tag2Found)) {
						sortedPhotos.remove(i);
					}
				}
			}
		}
		return sortedPhotos;
	}
	//creates a new album and adds connections between the album and the copied photos
	public void createAlbumFromSearch(ArrayList<Photo> list) {
		albumNumCreate += 1;
		String newAlbumName = "CreatedAlbum" + String.valueOf(albumNumCreate);
		createAlbum(newAlbumName);
		for (int i = 0; i < list.size(); i++) {
			//inserts a new connection
			Connection con = new Connection(newAlbumName, list.get(i).getPath());
			conList.add(con);
		}
	}
	//returns number of photos in an album
	public int getAlbumNum(String aName) {
		int numPhotos = 0;
		for (int i = 0; i < conList.size(); i++) {
			if (conList.get(i).getAlbum().equals(aName)) {
				numPhotos++;
			}
		}
		return numPhotos;
	}
	//returns earliest Calendar entry of the Album
	public Calendar getAlbumEarliest(String aName) {
		Calendar earliest = photoList.get(0).getDate();
		for (int i = 0; i < conList.size(); i++) {
			if (conList.get(i).getAlbum().equals(aName)) {
				String temp = conList.get(i).getPath();
				for (int j = 0; j < photoList.size(); j++) {
					if (photoList.get(j).getPath().equals(temp) && photoList.get(j).getDate().compareTo(earliest) < 0) {
						earliest = photoList.get(j).getDate();
					}
				}
			}
		}
		return earliest;
	}
	//returns latest Calendar entry of the Album
	public Calendar getAlbumLatest(String aName) {
		Calendar latest = photoList.get(0).getDate();
		for (int i = 0; i < conList.size(); i++) {
			if (conList.get(i).getAlbum().equals(aName)) {
				String temp = conList.get(i).getPath();
				for (int j = 0; j < photoList.size(); j++) {
					if (photoList.get(j).getPath().equals(temp) && photoList.get(j).getDate().compareTo(latest) > 0) {
						latest = photoList.get(j).getDate();
					}
				}
			}
		}
		return latest;
	}
	
}
