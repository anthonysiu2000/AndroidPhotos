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
public class Album {
	/**
	 * Album is a class that has special interactions with non admin users. Albums store photos that can be added by users.
	 * 
	 * @param name		The name of the album will be inputted by the user who creates it.
	 * @see User#User()
	 */
	//private fields
	protected String aName;
	
	//constructor
	public Album(String name) {
		this.aName = name;
	}
	
	/**
	 * The method to get the name of the album when requested.
	 * 
	 * @return aName
	 * @see Database#Database()
	 */
	public String getName() {
		return aName;
	}
}
