//made by Anthony Siu and Benjamin Lee

package model;

import java.util.ArrayList;

public class Admin extends User{
	//fields
	Database database;
	//constructor
	public Admin(String name, Database database){
		this.username = name;
		this.database = database;
	}
	//obtains a reference to the current database
	public Database getDatabase() {
		return database;
	}
	//gets users
	public ArrayList<User> getUsers() {
		return database.getUsers();
	}
	//adds a user
	public boolean addUser(String username) {
		return database.addUser(username);
	}
	//removes a user
	public boolean removeUser(String username) {
		return database.removeUser(username);
	}
}
