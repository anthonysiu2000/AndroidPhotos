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
import java.util.ArrayList;

public class Database {
	//fields
	private ArrayList<User> userbase;
	
	/**
	 * Database is the class that stores much of the information of the albums, photos and users.
	 * 
	 */
	//constructor
	public Database() {
		this.userbase = new ArrayList<User>();
		this.userbase.add(new Admin("admin", this));
		this.userbase.add(new NonAdmin("stock"));
	}
	
	/**
	 * This method returns an array list of users in the database, called userbase.
	 * 
	 * @return userbase
	 * @see User#User()
	 */
	//get list of users
	public ArrayList<User> getUsers() {
		return userbase;
	}
	
	/**
	 * This method directly creates a user in the data base using the string username as the input.
	 * 
	 * @param username		Name of the user in the database
	 * @return	boolean
	 * @see User#User()
	 */
	//add a user
	public boolean addUser(String username) {
		for (int i = 0; i < userbase.size(); i++) {
			if (userbase.get(i).username.equals(username)) {
				return false;
			}
		}
		userbase.add(new NonAdmin(username));
		return true;
	}
	
	/**
	 * This method directly removes a user in the data base using the string username as the input.
	 * 
	 * @param username		Name of the user in the database
	 * @return boolean
	 * @see User#User()
	 */
	//remove a user
	public boolean removeUser(String username) {
		if (username.equals("admin")) {
			return false;
		}
		for (int i = 0; i < userbase.size(); i++) {
			if (userbase.get(i).username.equals(username)) {
				userbase.remove(i);
				return true;
			}
		}
		return false;
	}
}
