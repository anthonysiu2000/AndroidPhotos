package model;

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
	//creates a new album; returns true is successful
	public boolean createAlbum(String aName) {
		for (int i = 0; i < albumList.size(); i++) {
			if (albumList.get(i).aName.equals(aName)) {
				return false;
			}
		}
		albumList.add(new Album(aName));
		return true;
	}
	

}
