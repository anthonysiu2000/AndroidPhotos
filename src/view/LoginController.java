package view;
<<<<<<< HEAD

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Database;
import model.User;

=======
/**
 * Photos is a single-user photo application that allows storage and management of photos in one or more albums.
 * 
 * @author 		Anthony Siu
 * @author 		Benjamin Lee
 * @version		%I% %G%
 * @since		1.0
 *
 */
>>>>>>> f381ef93705231b2352f33c7b9a2063915c2fd7b
public class LoginController {
	@FXML private TextField userNameField;
	@FXML private Button buttonLogin;
	@FXML private Text errorMessage;
	
	private Stage stage;
	
	//stores database, as inputed according to main class
	Database database;
	
	
	//method called to initialize the scene
	public void start(Stage mainStage, Database database){
		this.stage = mainStage;
		this.database = database;
		errorMessage.setText("");
	}

	//Method called when button is pressed
	public void buttonPress(ActionEvent e) throws IOException {
		String inputName = userNameField.getText();
		
		//switches scene to Admin if user enters admin
		if (inputName.equals("admin")) {
			
			//sets the new scene as an admin scene
			FXMLLoader adminLoader = new FXMLLoader();
			adminLoader.setLocation(getClass().getResource("/view/Admin.fxml"));
			AnchorPane adminRoot = (AnchorPane)adminLoader.load();
			Scene adminScene = new Scene(adminRoot);
			AdminController adminController = adminLoader.getController();
			
			adminController.start(stage, database);
			stage.setScene(adminScene);
			stage.setTitle("AdminView");
			stage.setResizable(false);
			stage.show();
		
		//switches scene to UserView, if the user is found
		} else {
			//finds user and puts it into "user" variable
			ArrayList<User> users = database.getUsers();
			boolean nameFound = false;
			User user;
			for (int i = 0; i < users.size(); i++) {
				if (inputName.equals(users.get(i).getUsername())) {
					user = users.get(i);
				}
			}
			if (!nameFound) {
				errorMessage.setText("Error: username not found");
				return;
			}
			
			//switches scene to UserView,
			FXMLLoader userViewLoader = new FXMLLoader();
			userViewLoader.setLocation(getClass().getResource("/view/UserView.fxml"));
			AnchorPane userViewRoot = (AnchorPane)userViewLoader.load();
			Scene userViewScene = new Scene(userViewRoot);
			UserViewController userViewController = userViewLoader.getController();
			
			//userViewController.start(stage, user);
			stage.setScene(userViewScene);
			stage.setTitle("UserView");
			stage.setResizable(false);
			stage.show();
			
		}
		return;
		
	}
	
}
