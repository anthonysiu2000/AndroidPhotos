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
import model.NonAdmin;
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
public class UserViewController {
	
	@FXML private ListView<String> albumListView;
	@FXML private TextField textFieldAlbum;
	@FXML private Button buttonCreate, buttonDelete, buttonRename, buttonOpen, buttonSearch, buttonLogOut;
	@FXML private Text textWelcome, textPhotoNum, textAlbumName, textEarliest, textLatest, textError;

	//Initiates the Observable List
	private ObservableList<String> obsList; 
	
	//stores the user
	private NonAdmin nonAdmin;
	private Stage stage;
	private Scene prevScene;
	private Scene thisScene;
	
	//used for button management
	private Button button = null;
	private int buttonNum = 0;
	
	//initiates the scene for the user
	public void start(Stage mainStage, Scene prevScene, Scene thisScene, NonAdmin nonAdmin){
		
		this.nonAdmin = nonAdmin;
		this.stage = mainStage;
		this.prevScene = prevScene;
		this.thisScene = thisScene;
		textError.setText("");
		textWelcome.setText("Welcome " + nonAdmin.getUsername());
		
		//gets list of albums for user
		ArrayList<String> albums = new ArrayList<String>();
		for (int i = 0; i < nonAdmin.getAlbums().size(); i++) {
			albums.add(nonAdmin.getAlbums().get(i).getName());
		}
		
		//updates ui to list of albums
		obsList = FXCollections.observableArrayList(albums); 
		albumListView.setItems(obsList); 

		// checks if the list is empty, and sends warning, not an error
		if (albums.isEmpty()) {
			textError.setText("Album List is Empty. Add albums please.");
		}
		
		// List listening function
		albumListView.getSelectionModel().selectedIndexProperty().addListener(
				(obs, oldVal, newVal) -> showDetail(mainStage));
		
	}

	//Method called when selecting an item from this list
	public void showDetail(Stage mainStage) {
		int index = albumListView.getSelectionModel().getSelectedIndex();
		if (index < 0) {
			return;
		}
		//updates information for each album
		String albumName = nonAdmin.getAlbums().get(index).getName();
		textAlbumName.setText("Name: " + albumName);
		textPhotoNum.setText("Photo #: " + String.valueOf(nonAdmin.getAlbumNum(albumName)));
		if (nonAdmin.getAlbumEarliest(albumName) == null) {
			textEarliest.setText("Earliest Date: N/A");
			textLatest.setText("Latest Date: N/A");
		} else {
			textEarliest.setText("Earliest Date: " + nonAdmin.getAlbumEarliest(albumName).getTime().toString());
			textLatest.setText("Latest Date: " + nonAdmin.getAlbumLatest(albumName).getTime().toString());
		}
	}
	
	//method called when pressing the open album button
	public void openAlbum() throws IOException {
		int index = albumListView.getSelectionModel().getSelectedIndex();

		//switches scene to Album view, given the currently selected album
		FXMLLoader albumViewLoader = new FXMLLoader();
		albumViewLoader.setLocation(getClass().getResource("/view/AlbumView.fxml"));
		AnchorPane albumViewRoot = (AnchorPane)albumViewLoader.load();
		Scene albumViewScene = new Scene(albumViewRoot);
		AlbumViewController albumViewController = albumViewLoader.getController();
		
		//albumViewController.start(stage, thisScene, albumViewScene, nonAdmin, index);
		stage.setScene(albumViewScene);
		stage.setTitle("UserView");
		stage.setResizable(false);
		stage.show();
	}
	
	//method called when pressing search photos button
	public void searchPhotos() throws IOException {

		//switches scene to UserView,
		FXMLLoader searchLoader = new FXMLLoader();
		searchLoader.setLocation(getClass().getResource("/view/Search.fxml"));
		AnchorPane searchRoot = (AnchorPane)searchLoader.load();
		Scene searchScene = new Scene(searchRoot);
		SearchController searchController = searchLoader.getController();
		
		//searchController.start(stage, thisScene, nonAdmin);
		stage.setScene(searchScene);
		stage.setTitle("searchView");
		stage.setResizable(false);
		stage.show();
	}

	//NOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTE
	//make sure to back up user to database to disk here
	//NOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTENOTE
	//method called when pressing logout button
	public void logout(){

		stage.setScene(prevScene);
		stage.setTitle("Photos");
		stage.setResizable(false);
		stage.show();
	}
	
	//method called when pressing create album button
	public void createAlbum(ActionEvent e) {
		Button b = (Button)e.getSource();
		
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2 && b != buttonCreate) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldAlbum.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");

		//First Button Press
		if (buttonNum == 1) {
			//Instructs user
			textError.setText("Please input new album in the field, then press Create again.");
			buttonNum = 2;
			
		//Second Button Press	
		} else {
			String inputAlbum = textFieldAlbum.getText().trim();
			buttonNum = 1;
			//username cannot be whitespace
			if (inputAlbum.isBlank()) {
				textError.setText("Error: no album inputted");
				textFieldAlbum.setText("");
				return;
			}
			
			//inputs username to database
			boolean createSuccessful = nonAdmin.createAlbum(inputAlbum);
			
			//add successful
			if (createSuccessful) {
				textError.setText("Album Create Successful");
				textFieldAlbum.setText("");

				//gets list of albums for user
				ArrayList<String> albums = new ArrayList<String>();
				for (int i = 0; i < nonAdmin.getAlbums().size(); i++) {
					albums.add(nonAdmin.getAlbums().get(i).getName());
				}
				
				//updates ui to list of albums
				obsList = FXCollections.observableArrayList(albums); 
				albumListView.setItems(obsList); 

				
			//add unsuccessful due to username existing
			} else {
				textError.setText("Error: album already exists");
				textFieldAlbum.setText("");
			}
		}
		
	}
	
	//method called when pressing delete album button
	public void deleteAlbum() {
		
		//If we are on the second button press, put out an error
		if (buttonNum == 2) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldAlbum.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");
		
	}
	
	//method called when pressing rename album button
	public void renameAlbum(ActionEvent e) {
		
	}
}
