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

public class Admin extends User{
	/**
	 * An admin is a subclass of user that has special permissions from non admin users. There is only one admin in a photos library website and they can do special commands listed below:
	 * 
	 * @param name		The name of the admin will automatically be admin.
	 * @param database	The admin class will have the parameter from the Database class.
	 * @see Database#Database()
	 * @see User#User()
	 */
	
	/**
	 * a compilation of lists of usernames and other data
	 */
	//fields
	Database database;
	//constructor
	public Admin(String name, Database database){
		this.username = name;
		this.database = database;
	}
	
	/**
	 * The reference to the current database of the admin class is obtained and returned in the output.
	 * 
	 * @return database
	 * @see Database#Database()
	 */
	public Database getDatabase() {
		return database;
	}
	
	/**
	 * An array list of the users from the database class is returned in the output.
	 * 
	 * @return database.getUsers()
	 * @see Database#getUsers()
	 */
	public ArrayList<User> getUsers() {
		return database.getUsers();
	}
	
	/**
	 * A user is added into the database while taking the String username as input. The username is returned in the output.
	 * 
	 * @return database.addUser(username)
	 * @see Database#addUser(String)
	 */
	public boolean addUser(String username) {
		return database.addUser(username);
	}
	
	/**
	 * A user is removed from the database while taking the String username as input. The username is returned in the output.
	 * 
	 * @return database.removeUser(username)
	 * @see Database#removeUser(String)
	 */
	public boolean removeUser(String username) {
		return database.removeUser(username);
	}
}
