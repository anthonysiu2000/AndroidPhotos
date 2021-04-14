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
public class AlbumViewController {

	@FXML private ListView<String> photoListView;
	@FXML private TextField textFieldAlbum;
	@FXML private Button buttonAdd, buttonRemove, buttonCopy, buttonMove, 
				buttonSlideShowF, buttonSlideShowR, buttonEdit, buttonUserView;
	@FXML private Text textError;
	@FXML private Text textAlbum;

	//Initiates the Observable List
	private ObservableList<String> obsList; 
	
	//stores the user
	private NonAdmin nonAdmin;
	private Stage stage;
	private Scene prevScene;
	private Scene thisScene;
	private int index;
	
	//button Management
	private Button prevButton = null;
	private int buttonNum = 1;
	
	//initiates the scene for the user
	public void start(Stage mainStage, Scene prevScene, Scene thisScene, NonAdmin nonAdmin, int albumIndex){
		this.nonAdmin = nonAdmin;
		this.stage = mainStage;
		this.prevScene = prevScene;
		this.thisScene = thisScene;
		this.index = albumIndex;
		
		setScene(mainStage, albumIndex);
		resetListView(mainStage);
	}
	
	//method called to initialize the text of the scene
	public void setScene(Stage mainstage, int albumIndex) {
		textError.setText(" ");
		textAlbum.setText("Album: " + nonAdmin.getAlbums().get(albumIndex).getName());
	}
	
	
	//method called when we return to this scene
	public void resetListView(Stage mainStage) {
		textError.setText("");
		textFieldAlbum.setText("");
		
		//gets list of photo paths for user
		ArrayList<String> photos = new ArrayList<String>();
		for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
			if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
				photos.add(nonAdmin.getConnections().get(i).getPath());
			}
		}
		
		//updates ui to list of albums
		obsList = FXCollections.observableArrayList(photos); 
		photoListView.setItems(obsList); 
		// checks if the list is empty, and sends warning, not an error
		if (photos.isEmpty()) {
			textError.setText("Album List is Empty. Add albums please.");
		} else {
			photoListView.getSelectionModel().select(0);
		}
	}
	
	//method called when add photo button is pressed
	public void addPhoto(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldAlbum.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");
		
		String thisAlbum = nonAdmin.getAlbums().get(index).getName();
		
		
		
		
		
		
		
		
		
		
	}
	
	//method called when remove photo button is pressed
	public void removePhoto(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldAlbum.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");

		//gets this album
		String thisAlbum = nonAdmin.getAlbums().get(index).getName();
		
		//gets path of selected photo
		int index = photoListView.getSelectionModel().getSelectedIndex();
		int parse = 0;
		String path = null;
		for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
			//if the connection belongs to this album
			if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
				//the path of the photo of this album is set in path
				if (parse == index) {
					path = nonAdmin.getConnections().get(i).getPath();
					break;
				}
				parse++;
			}
		}
		
		boolean deleteSuccessful = nonAdmin.removePhoto(thisAlbum, path);
		//delete successful
		if (deleteSuccessful) {
			textError.setText("Copy Successful");
			textFieldAlbum.setText("");
			resetListView(stage);
		//copy unsuccessful due to photo existing or album not existing
		} else {
			textError.setText("Error: target album does not exist, or photo already exists in target alum");
			textFieldAlbum.setText("");
		}
		
	}
	
	//method called when copy photo button is pressed
	public void copyPhoto(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2 && prevButton != buttonCopy) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldAlbum.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");

		//First Button Press
		if (buttonNum == 1) {
			//Instructs user
			textError.setText("Please input album to be copied to, then press Copy again.");
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
			
			//gets this album
			String thisAlbum = nonAdmin.getAlbums().get(index).getName();
			
			//gets path of selected photo
			int index = photoListView.getSelectionModel().getSelectedIndex();
			int parse = 0;
			String path = null;
			for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
				//if the connection belongs to this album
				if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
					//the path of the photo of this album is set in path
					if (parse == index) {
						path = nonAdmin.getConnections().get(i).getPath();
						break;
					}
					parse++;
				}
			}
			
			//inputs photo to new album
			boolean copySuccessful = nonAdmin.copyPhoto(thisAlbum, inputAlbum, path);
			
			//copy successful
			if (copySuccessful) {
				textError.setText("Copy Successful");
				textFieldAlbum.setText("");
				resetListView(stage);
			//copy unsuccessful due to photo existing or album not existing
			} else {
				textError.setText("Error: target album does not exist, or photo already exists in target alum");
				textFieldAlbum.setText("");
			}
		}
	}
	
	//method called when move photo button is pressed
	public void movePhoto(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2 && prevButton != buttonCopy) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldAlbum.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");

		//First Button Press
		if (buttonNum == 1) {
			//Instructs user
			textError.setText("Please input album to be copied to, then press Move again.");
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
			
			//gets this album
			String thisAlbum = nonAdmin.getAlbums().get(index).getName();
			
			//gets path of selected photo
			int index = photoListView.getSelectionModel().getSelectedIndex();
			int parse = 0;
			String path = null;
			for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
				//if the connection belongs to this album
				if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
					//the path of the photo of this album is set in path
					if (parse == index) {
						path = nonAdmin.getConnections().get(i).getPath();
						break;
					}
					parse++;
				}
			}
			
			//inputs photo to new album
			boolean copySuccessful = nonAdmin.movePhoto(thisAlbum, inputAlbum, path);
			
			//move successful
			if (copySuccessful) {
				textError.setText("Move Successful");
				textFieldAlbum.setText("");
				resetListView(stage);
			//move unsuccessful due to photo existing or album not existing
			} else {
				textError.setText("Error: target album does not exist, or photo already exists in target alum");
				textFieldAlbum.setText("");
			}
		}
	}
	
	//method called when pressing the Edit/Display Photo button
	public void editPhoto() throws IOException {
		buttonNum = 1;
		
		//gets path of photo we want to edit
		int index = photoListView.getSelectionModel().getSelectedIndex();
		int parse = 0;
		String path = null;
		for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
			//if the connection belongs to this album
			if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
				//the path of the photo of this album is set in path
				if (parse == index) {
					path = nonAdmin.getConnections().get(i).getPath();
					break;
				}
				parse++;
			}
		}
		
		//switches scene to Photo Edit, given the currently selected photo
		FXMLLoader photoEditLoader = new FXMLLoader();
		photoEditLoader.setLocation(getClass().getResource("/view/PhotoEdit.fxml"));
		AnchorPane photoEditRoot = (AnchorPane)photoEditLoader.load();
		Scene photoEditScene = new Scene(photoEditRoot);
		PhotoEditController photoEditController = photoEditLoader.getController();
		
		//photoEditController.start(stage, thisScene, photoEditScene, nonAdmin, path);
		stage.setScene(photoEditScene);
		stage.setTitle("UserView");
		stage.setResizable(false);
		stage.show();
	}
	
	//method called when pressing Slideshow In order button
	public void slideshowF() throws IOException {
		buttonNum = 1;

		//switches scene to UserView,
		FXMLLoader slideshowLoader = new FXMLLoader();
		slideshowLoader.setLocation(getClass().getResource("/view/Slideshow.fxml"));
		AnchorPane slideshowRoot = (AnchorPane)slideshowLoader.load();
		Scene slideshowScene = new Scene(slideshowRoot);
		SlideshowController slideshowController = slideshowLoader.getController();
		
		//slideshowController.start(stage, thisScene, nonAdmin, index);
		stage.setScene(slideshowScene);
		stage.setTitle("searchView");
		stage.setResizable(false);
		stage.show();
	}

	//method called when pressing Slideshow Reverse order button
	public void slideshowR() throws IOException {
		buttonNum = 1;

		//switches scene to UserView,
		FXMLLoader slideshowLoader = new FXMLLoader();
		slideshowLoader.setLocation(getClass().getResource("/view/Slideshow.fxml"));
		AnchorPane slideshowRoot = (AnchorPane)slideshowLoader.load();
		Scene slideshowScene = new Scene(slideshowRoot);
		SlideshowController slideshowController = slideshowLoader.getController();
				
		//slideshowController.start(stage, thisScene, nonAdmin, index);
		stage.setScene(slideshowScene);
		stage.setTitle("searchView");
		stage.setResizable(false);
		stage.show();
	}

	//method called when pressing back to Album List button
	public void backUserView(){
		stage.setScene(prevScene);
		stage.setTitle("UserView");
		stage.setResizable(false);
		stage.show();
	}

}
