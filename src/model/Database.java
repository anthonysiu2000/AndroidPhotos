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
	//constructor
	public Database() {
		this.userbase = new ArrayList<User>();
		this.userbase.add(new Admin("admin", this));
		this.userbase.add(new NonAdmin("stock"));
	}
	
	//get list of users
	public ArrayList<User> getUsers() {
		return userbase;
	}
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
