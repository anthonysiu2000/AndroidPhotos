package model;

public class Connection {
	//fields
	private String photoPath;
	private String aName;
	//constructor
	public Connection(String path, String album) {
		this.photoPath = path;
		this.aName = album;
	}
	//gets photo path
	public String getPath() {
		return photoPath;
	}
	//gets album name
	public String getAlbum() {
		return aName;
	}
}
