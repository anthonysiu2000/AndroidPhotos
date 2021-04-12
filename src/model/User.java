package model;

abstract class User {
	
	//protected fields
	protected String username;
	
	//constructor
	public User(String name) {
		this.username = name;
	}
	
	//get username of user
	public String getUsername() {
		return username;
	}
	
	//logout
	public Class<Void> logout() {
		return void.class;
	}
	
}
