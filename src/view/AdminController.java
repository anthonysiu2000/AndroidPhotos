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

public class AdminController {
	@FXML private ListView<String> userList;
	@FXML private TextField userNameField;
	@FXML private Button buttonList, buttonAdd, buttonDelete, buttonLogOut;
	@FXML private Text errorMessage;
	
	//stores database and stage, as inputed according to main class
	private Stage stage;
	private Database database;
	
	//Used for button management
	private int buttonNumber = 1;

	//Initiates the Observable List
	private ObservableList<String> obsList; 
	
	//checks if we listed the usernames or not
	private boolean listed = false;
	
	//method called when initiating this scene
	public void start(Stage mainStage, Database database){
		this.stage = mainStage;
		this.database = database;
		errorMessage.setText("");
	}


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
			
			//Deletes song from Song Library
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
			FXMLLoader loginLoader = new FXMLLoader();
			loginLoader.setLocation(getClass().getResource("/view/Login.fxml"));
			AnchorPane loginRoot = (AnchorPane)loginLoader.load();
			Scene loginScene = new Scene(loginRoot);
			LoginController loginController = loginLoader.getController();
			
			loginController.start(stage, database);
			stage.setScene(loginScene);
			stage.setTitle("Photos");
			stage.setResizable(false);
			stage.show();
			
		}
		
	}

}
