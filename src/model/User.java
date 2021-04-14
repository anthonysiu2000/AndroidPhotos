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
public abstract class User {

	//protected fields
	protected String username;
	
	/**
	 * This method gets the username of the User and outputs it as a String.
	 * 
	 * @return username
	 */
	//get user name of user
	public String getUsername() {
		return username;
	}
}
