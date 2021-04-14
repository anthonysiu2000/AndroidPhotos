package view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Database;
import model.User;

/**
 * Photos is a single-user photo application that allows storage and management of photos in one or more albums.
 * 
 * @author 		Anthony Siu
 * @author 		Benjamin Lee
 * @version		%I% %G%
 * @since		1.0
 *
 */
public class AdminController {
	
	/**
	 * a list view of all of the users from admin
	 */
	@FXML private ListView<String> userList;
	
	/**
	 * a text field of the usernames
	 */
	@FXML private TextField userNameField;
	
	/**
	 * buttons on the FXML of the list, add, delete, and log out
	 */
	@FXML private Button buttonList, buttonAdd, buttonDelete, buttonLogOut;
	@FXML private Text errorMessage;
	
	/**
	 * creates a stage instance
	 */
	//stores database and stage, as inputed according to main class
	private Stage stage;
	
	/**
	 * creates a database instance
	 */
	private Database database;
	

	private Scene prevScene;
	
	/**
	 * initializes buttonNumber to value 1 for button management
	 */
	//Used for button management
	private int buttonNumber = 1;
	
	/**
	 * 
	 */
	//Initiates the Observable List
	private ObservableList<String> obsList; 
	
	/**
	 * boolean for checking if usernames are listed
	 */
	//checks if we listed the usernames or not
	private boolean listed = false;
	
	/**
	 * Method created to initialize the scene for the admin control panel.
	 * 
	 * @param mainStage		The stage created for admin
	 * @param database		The database from with all user and album information
	 */
	//method called when initiating this scene
	public void start(Stage mainStage, Scene prevScene, Database database){
		this.prevScene = prevScene;
		this.stage = mainStage;
		this.database = database;
		errorMessage.setText("");
	}

	/**
	 * The method will be called when any button is pressed and perform actions depending on the button pressed.
	 * 
	 * <p>
	 * The list of what the method achieves is listed below:
	 * <p>
	 * <ul>
	 * <li> If the second button pressed is incorrect, we state an error, and do not pass the action
	 * <li> User will press Add twice, once to instruct user, and the second time to read textfield
	 * <li> First Button Press where it instructs user
	 * <li> Second Button Press where username cannot be whitespace
	 * <li> Inputs username to database
	 * <li> Add successful and gets arraylist of usernames and updates ListView
	 * <li> Add unsuccessful due to username existing
	 * <li> User presses list users, which will generate a list of usernames in the listview
	 * <li> Delete Button will only need one button press; no confirmation window
	 * <li> If empty list, output error
	 * <li> Deletes song from Song Library
	 * <li> If delete is unsuccessful
	 * <li> Gets arraylist of usernames
	 * <li> Updates ListView
	 * <li> Selects the next song
	 * <li> Admin chooses to logout
	 * </ul>
	 * 
	 * @param e				Action event variable
	 * @throws IOException	Input output exception if detected
	 */
	//Method called when any button is pressed; will perform action depending on button pressed
	public void buttonPress(ActionEvent e) throws IOException {
		Button b = (Button)e.getSource();
		
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNumber == 2 && b != buttonAdd) {
			errorMessage.setText("Error: Incorrect button press; Action reset.");
			userNameField.setText("");
			buttonNumber = 1;
			return;
		}
		errorMessage.setText("");
	
		//Implementing Button functions
		
		//User will press Add twice, once to instruct user, and the second time to read textfield
		if (b == buttonAdd) {
			
			//First Button Press
			if (buttonNumber == 1) {
				//Instructs user
				errorMessage.setText("Please input new username in the field, then press Add again.");
				buttonNumber = 2;
				
			//Second Button Press	
			} else {
				String inputUsername = userNameField.getText().trim();
				buttonNumber = 1;
				//username cannot be whitespace
				if (inputUsername.isBlank()) {
					errorMessage.setText("Error: no username inputted");
					userNameField.setText("");
					return;
				}
				
				//inputs username to database
				boolean addSuccessful = database.addUser(inputUsername);
				
				//add successful
				if (addSuccessful) {
					errorMessage.setText("Add Successful");
					userNameField.setText("");

					//gets arraylist of usernames
					ArrayList<User> users = database.getUsers();
					ArrayList<String> usernames = new ArrayList<String>();
					for (int i = 0; i < users.size(); i++) {
						usernames.add(users.get(i).getUsername());
					}
					
					//updates ListView
					obsList = FXCollections.observableArrayList(usernames); 
					userList.setItems(obsList); 
					
				//add unsuccessful due to username existing
				} else {
					errorMessage.setText("Error: username already exists");
					userNameField.setText("");
				}
			}
		}
	
		
		//User presses list users, which will generate a list of usernames in the listview
		else if (b == buttonList) {
			
			//gets arraylist of usernames
			ArrayList<User> users = database.getUsers();
			ArrayList<String> usernames = new ArrayList<String>();
			for (int i = 0; i < users.size(); i++) {
				usernames.add(users.get(i).getUsername());
			}
			
			//makes observable arraylist and outputs
			obsList = FXCollections.observableArrayList(usernames); 
			userList.setItems(obsList); 
			
			
			// select the first item
			userList.getSelectionModel().select(0);
			
			errorMessage.setText("Listing usernames.");
			userNameField.setText("");
			listed = true;
		}
		
		
		
		//Delete Button will only need one button press; no confirmation window
		else if (b == buttonDelete){
			
			//If empty list, output error
			if (!listed) {
				errorMessage.setText("Error: nothing to delete. Please List users");
				userNameField.setText("");
				return;
			}
			
			//Deletes user from user database
			int index = userList.getSelectionModel().getSelectedIndex();
			User userDeleted = database.getUsers().get(index);
			boolean deleteSuccessful = database.removeUser(userDeleted.getUsername());
			
			//If delete is unsuccessful
			if (!deleteSuccessful) {
				errorMessage.setText("Error: cannot delete Admin");
				userNameField.setText("");
				return;
			}
			
			//gets arraylist of usernames
			ArrayList<User> users = database.getUsers();
			ArrayList<String> usernames = new ArrayList<String>();
			for (int i = 0; i < users.size(); i++) {
				usernames.add(users.get(i).getUsername());
			}
			
			//updates ListView
			obsList = FXCollections.observableArrayList(usernames); 
			userList.setItems(obsList); 
			
			//Selects the next song
			if (users.size() == 0) {
				userList.getSelectionModel().select(0);
			}
			else if (index < users.size()) {
				userList.getSelectionModel().select(index);
			}
			else {
				userList.getSelectionModel().select(index - 1);
			}
			
			errorMessage.setText("Delete Successful.");
			userNameField.setText("");
		} 
		
		
		
		//NOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTE
		//make sure to back up database to disk here
		//NOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTE
		//admin chooses to logout
		else {
			
			//sets the new scene as a login scene
			stage.setScene(prevScene);
			stage.setTitle("Photos");
			stage.setResizable(false);
			stage.show();
			
		}
		
	}

}
