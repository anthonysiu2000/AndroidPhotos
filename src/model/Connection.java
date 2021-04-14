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
public class Connection {
	//fields
	private String photoPath;
	protected String aName;
	
	/**
	 * Connection is a class that has interactions with albums.
	 * 
	 * @param name		The name of the album will be inputted by the user who creates it.
	 * @see Album#Album(String)
	 */
	//constructor
	public Connection(String path, String album) {
		this.photoPath = path;
		this.aName = album;
	}
	
	/**
	 * The method to get the path of the photo when requested.
	 * 
	 * @return aName
	 * @see Database#Database()
	 */
	//gets photo path
	public String getPath() {
		return photoPath;
	}
	
	/**
	 * The method to get the name of the album when requested.
	 * 
	 * @return aName
	 * @see Album#Album(String)
	 */
	//gets album name
	public String getAlbum() {
		return aName;
	}
}
