package model;

import java.io.File;
import java.util.ArrayList;

public class NonAdmin extends User {
	//fields
	private ArrayList<Album> albumList;
	private ArrayList<Photo> photoList;
	private ArrayList<Connection> conList;
	//constructor
	public NonAdmin(){
		albumList = new ArrayList<Album>();
		photoList = new ArrayList<Photo>();
		conList = new ArrayList<Connection>();
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
}
