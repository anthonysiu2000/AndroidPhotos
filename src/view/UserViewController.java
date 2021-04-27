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
import model.NonAdmin;

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
	private UserViewController uvc;
	
	//used for button management
	private Button prevButton = null;
	private int buttonNum = 1;
	
	//initiates the scene for the user
	public void start(Stage mainStage, Scene prevScene, Scene thisScene, NonAdmin nonAdmin, UserViewController uvc){
		
		this.nonAdmin = nonAdmin;
		this.stage = mainStage;
		this.prevScene = prevScene;
		this.thisScene = thisScene;
		this.uvc = uvc;
		textError.setText(" ");
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
		
		if (!albums.isEmpty()) {
			albumListView.getSelectionModel().select(0);
		}
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
	
	//Method called to reset details when finishing an action
	public void resetDetail(Stage mainStage) {
		textAlbumName.setText("Name: ");
		textPhotoNum.setText("Photo #: ");
		textEarliest.setText("Earliest Date: ");
		textLatest.setText("Latest Date: ");
		textFieldAlbum.setText("");
		
	}
	
	//method called when we return to this scene
	public void resetListView(Stage mainStage) {
		textError.setText("");
		textWelcome.setText("Welcome " + nonAdmin.getUsername());
		prevButton = null;
		
		//gets list of albums for user
		ArrayList<String> albums = new ArrayList<String>();
		for (int i = 0; i < nonAdmin.getAlbums().size(); i++) {
			albums.add(nonAdmin.getAlbums().get(i).getName());
		}
		
		//updates ui to list of albums
		obsList = FXCollections.observableArrayList(albums); 
		albumListView.setItems(obsList); 
	}
	
	//method called when pressing the open album button
	public void openAlbum() throws IOException {
		buttonNum = 1;
		resetDetail(stage);
		int index = albumListView.getSelectionModel().getSelectedIndex();

		//switches scene to Album view, given the currently selected album
		FXMLLoader albumViewLoader = new FXMLLoader();
		albumViewLoader.setLocation(getClass().getResource("/view/AlbumView.fxml"));
		AnchorPane albumViewRoot = (AnchorPane)albumViewLoader.load();
		Scene albumViewScene = new Scene(albumViewRoot);
		AlbumViewController albumViewController = albumViewLoader.getController();
		
		albumViewController.start(stage, thisScene, albumViewScene, nonAdmin, index);
		stage.setScene(albumViewScene);
		stage.setTitle("AlbumView");
		stage.setResizable(false);
		stage.show();
	}
	
	//method called when pressing search photos button
	public void searchPhotos() throws IOException {
		resetDetail(stage);
		buttonNum = 1;

		//switches scene to UserView,
		FXMLLoader searchLoader = new FXMLLoader();
		searchLoader.setLocation(getClass().getResource("/view/Search.fxml"));
		AnchorPane searchRoot = (AnchorPane)searchLoader.load();
		Scene searchScene = new Scene(searchRoot);
		SearchController searchController = searchLoader.getController();
		
		searchController.start(stage, thisScene, nonAdmin, uvc);
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
		buttonNum = 1;
		resetDetail(stage);
		stage.setScene(prevScene);
		stage.setTitle("Photos");
		stage.setResizable(false);
		stage.show();
	}
	
	//method called when pressing create album button
	public void createAlbum(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2 && prevButton != buttonCreate) {
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
			//album cannot be whitespace
			if (inputAlbum.isBlank()) {
				textError.setText("Error: no album inputted");
				textFieldAlbum.setText("");
				return;
			}
			
			//inputs album to database
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

				
			//add unsuccessful due to album existing
			} else {
				textError.setText("Error: album already exists");
				textFieldAlbum.setText("");
			}
		}
		
	}
	
	//method called when pressing delete album button
	public void deleteAlbum(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If we are on the second or third button press, put out an error
		if (buttonNum == 2) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldAlbum.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");

		//If empty list, output error
		if (nonAdmin.getAlbums().size() == 0) {
			textError.setText("Error: nothing to delete.");
			textFieldAlbum.setText("");
			return;
		}
		
		//Deletes album from user database
		int index = albumListView.getSelectionModel().getSelectedIndex();
		if (index < 0) {
			textError.setText("No Album Selected");
			textFieldAlbum.setText("");
			return;
		}
		String albumDeleted = nonAdmin.getAlbums().get(index).getName();
		boolean asdf = nonAdmin.deleteAlbum(albumDeleted);
		if (!asdf) {
			textError.setText("Album not deleted properly");
			textFieldAlbum.setText("");
			return;
		}
		
		//gets list of albums for user
		ArrayList<String> albums = new ArrayList<String>();
		for (int i = 0; i < nonAdmin.getAlbums().size(); i++) {
			albums.add(nonAdmin.getAlbums().get(i).getName());
		}
		
		//updates ListView
		obsList = FXCollections.observableArrayList(albums); 
		albumListView.setItems(obsList); 
		resetDetail(stage);
		
		textError.setText("Delete Successful.");
		textFieldAlbum.setText("");
		if (nonAdmin.getAlbums().size() == 0) {
			return;
		}
		else if (index < nonAdmin.getAlbums().size()) {
			albumListView.getSelectionModel().select(index);
		}
		else {
			albumListView.getSelectionModel().select(index - 1);
		}
		
	}
	
	//method called when pressing rename album button
	public void renameAlbum(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if ((buttonNum == 2) && prevButton != buttonRename) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldAlbum.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");
		
		//First Button Press
		if (buttonNum == 1) {
			//Instructs user
			textError.setText("Please input new name for selected album then press Create again.");
			buttonNum = 2;
		
		//Second Button Press	
		} else {
			int index = albumListView.getSelectionModel().getSelectedIndex();
			if (index < 0) {
				textError.setText("No Album Selected");
				textFieldAlbum.setText("");
				return;
			}
			String newName = textFieldAlbum.getText().trim();
			buttonNum = 1;
			//album cannot be whitespace
			if (newName.isBlank()) {
				textError.setText("Error: no album inputted");
				textFieldAlbum.setText("");
				return;
			}
					
			//inputs album to database
			nonAdmin.renameAlbum(nonAdmin.getAlbums().get(index).getName(), newName);
			
			textError.setText("Album Rename Successful");
			textFieldAlbum.setText("");
			//gets list of albums for user
			ArrayList<String> albums = new ArrayList<String>();
			for (int i = 0; i < nonAdmin.getAlbums().size(); i++) {
				albums.add(nonAdmin.getAlbums().get(i).getName());
			}
					
			//updates ui to list of albums
			obsList = FXCollections.observableArrayList(albums); 
			albumListView.setItems(obsList); 
				
			albumListView.getSelectionModel().select(index);
		}
	}
}
