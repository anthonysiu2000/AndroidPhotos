package view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
	
	/**
	 * This method initiates the scene for theh album view for the user.
	 * 
	 * @param mainStage			Main stage for the control panel
	 * @param prevScene			Previous stage from the control panel
	 * @param thisScene			Current stage from the control panel
	 * @param nonAdmin			A non admin user	
	 * @param albumIndex		The index of the album in the database
	 * @see AlbumController
	 */
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
	
	/**
	 * This method is called to initialize the text of the scene.
	 * 
	 * @param mainstage
	 * @param albumIndex
	 */
	//method called to initialize the text of the scene
	public void setScene(Stage mainstage, int albumIndex) {
		textError.setText(" ");
		textAlbum.setText("Album: " + nonAdmin.getAlbums().get(albumIndex).getName());
	}
	
	/**
	 * This method is called when the scene is returned to.
	 * 
	 * @param mainStage
	 */
	//method called when we return to this scene
	public void resetListView(Stage mainStage) {
		textFieldAlbum.setText("");
		
		//gets list of photo paths for user
		ArrayList<String> photos = new ArrayList<String>();
		for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
			//finds connections that have the same album as this one
			
			String conAlbumName = nonAdmin.getConnections().get(i).getAlbum();
			String thisAlbumName = nonAdmin.getAlbums().get(index).getName();
			
			if (conAlbumName.equals(thisAlbumName)) {
				photos.add(nonAdmin.getConnections().get(i).getPath());
				
			}
		}
		
		//sets obslist to a list of blank lines
		//gets arraylist of images
		ArrayList<String> blank = new ArrayList<String>();
		ArrayList<Image> images = new ArrayList<Image>();
		for (int i = 0; i < photos.size(); i++) {
			blank.add(String.valueOf(i));
			images.add(new Image(new File(photos.get(i)).toURI().toString()));
		}
		
		//updates ui to list of albums
		obsList = FXCollections.observableArrayList(blank); 
		photoListView.setItems(obsList); 
		
		
		
		//sets each cell to hold an image
		photoListView.setCellFactory(listView -> 
		new ListCell<String>() {
            private ImageView imgView = new ImageView();
            @Override
            public void updateItem(String thing, boolean empty) {
                super.updateItem(thing, empty);
                if (empty || Integer.parseInt(thing) >= images.size()) {
                    setText(null);
                    setGraphic(null);
                } else {
                	imgView.setImage(images.get(Integer.parseInt(thing)));
                	setText(thing);
                	setGraphic(imgView);
                	imgView.setFitHeight(30);;
                	imgView.setFitWidth(40);;
                }
            }
        });
		
		
		// checks if the list is empty, and sends warning, not an error
		if (!photos.isEmpty()) {
			photoListView.getSelectionModel().select(0);
		}
	}
	
	/**
	 * The method is called when the add photo button is pressed.
	 * 
	 * @param e			Action event variable
	 */
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
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
		        new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(stage);
		
		boolean addSuccessful = nonAdmin.addPhoto(thisAlbum, selectedFile);
		//add successful
		if (addSuccessful) {
			textError.setText("Add Successful");
			textFieldAlbum.setText("");
			resetListView(stage);
		//add unsuccessful 
		} else {
			textError.setText("Error: something went wrong importing the file");
			textFieldAlbum.setText("");
		}
		
	}
	
	/**
	 * The method is called when the remove button is pressed.
	 * 
	 * @param e		Action event variable
	 */
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
		int pathIndex = photoListView.getSelectionModel().getSelectedIndex();
		int parse = 0;
		String path = null;
		for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
			//if the connection belongs to this album
			if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
				//the path of the photo of this album is set in path
				if (parse == pathIndex) {
					path = nonAdmin.getConnections().get(i).getPath();
					break;
				}
				parse++;
			}
		}
		
		boolean deleteSuccessful = nonAdmin.removePhoto(thisAlbum, path);
		//delete successful
		if (deleteSuccessful) {
			textError.setText("Delete Successful");
			textFieldAlbum.setText("");
			resetListView(stage);
		//copy unsuccessful due to photo existing or album not existing
		} else {
			textError.setText("Error: target album does not exist, or photo already exists in target alum");
			textFieldAlbum.setText("");
		}
		
	}
	
	/**
	 * The method is called when the copy photo button is pressed.
	 * 
	 * <p>
	 * Actions acheived in the method:
	 * <p>
	 * <ul>
	 * <li>If the second button pressed is incorrect, we state an error, and do not pass the action
	 * <li>First Button Press
	 * <li>Second Button Press
	 * <li>Gets path of selected photo
	 * <li>Inputs photo to new album
	 * <li>Checks if the photo is copied properly
	 * </ul>
	 * 
	 * @param e			Action event variable
	 */
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
			int pathIndex = photoListView.getSelectionModel().getSelectedIndex();
			int parse = 0;
			String path = null;
			for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
				//if the connection belongs to this album
				if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
					//the path of the photo of this album is set in path
					if (parse == pathIndex) {
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
	
	/**
	 * The method called when move photo button is pressed.
	 * 
	 * <p>
	 * Actions acheived in the method:
	 * <p>
	 * <ul>
	 * <li>If the second button pressed is incorrect, we state an error, and do not pass the action
	 * <li>First Button Press
	 * <li>Second Button Press
	 * <li>Gets path of selected photo
	 * <li>Inputs photo to new album
	 * <li>Checks if the photo is copied properly
	 * </ul>
	 * 
	 * @param e			Action event variable
	 */
	//method called when move photo button is pressed
	public void movePhoto(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2 && prevButton != buttonMove) {
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
			int pathIndex = photoListView.getSelectionModel().getSelectedIndex();
			int parse = 0;
			String path = null;
			for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
				//if the connection belongs to this album
				if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
					//the path of the photo of this album is set in path
					if (parse == pathIndex) {
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
	
	/**
	 * The method that is called when pressing the Edit/Display Photo button.
	 * 
	 * @throws IOException		Input output exception check
	 */
	//method called when pressing the Edit/Display Photo button
	public void editPhoto() throws IOException {
		buttonNum = 1;
		
		//gets path of photo we want to edit
		int photoIndex = photoListView.getSelectionModel().getSelectedIndex();
		if (photoIndex < 0) {
			textError.setText("No photos to enter");
			return;
		}
		int parse = 0;
		String path = null;
		for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
			//if the connection belongs to this album
			if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
				//the path of the photo of this album is set in path
				if (parse == photoIndex) {
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
		
		photoEditController.start(stage, thisScene, nonAdmin, path);
		stage.setScene(photoEditScene);
		stage.setTitle("PhotoEditView");
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * The method called when pressing Slideshow In order button
	 * 
	 * @throws IOException
	 */
	//method called when pressing Slideshow In order button
	public void slideshowF() throws IOException {
		buttonNum = 1;

		//if no photos, return an error
		int photoIndex = photoListView.getSelectionModel().getSelectedIndex();
		if (photoIndex < 0) {
			textError.setText("No photos to show");
			return;
		}
		
		//switches scene to UserView,
		FXMLLoader slideshowLoader = new FXMLLoader();
		slideshowLoader.setLocation(getClass().getResource("/view/Slideshow.fxml"));
		AnchorPane slideshowRoot = (AnchorPane)slideshowLoader.load();
		Scene slideshowScene = new Scene(slideshowRoot);
		SlideshowController slideshowController = slideshowLoader.getController();
		
		slideshowController.start(stage, thisScene, nonAdmin, index, true);
		stage.setScene(slideshowScene);
		stage.setTitle("SlideshowView");
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * The method called when pressing Slideshow Reverse order button.
	 * 
	 * @throws IOException
	 */
	//method called when pressing Slideshow Reverse order button
	public void slideshowR() throws IOException {
		buttonNum = 1;

		//if no photos, return an error
		int photoIndex = photoListView.getSelectionModel().getSelectedIndex();
		if (photoIndex < 0) {
			textError.setText("No photos to show");
			return;
		}
		
		//switches scene to UserView,
		FXMLLoader slideshowLoader = new FXMLLoader();
		slideshowLoader.setLocation(getClass().getResource("/view/Slideshow.fxml"));
		AnchorPane slideshowRoot = (AnchorPane)slideshowLoader.load();
		Scene slideshowScene = new Scene(slideshowRoot);
		SlideshowController slideshowController = slideshowLoader.getController();
				
		slideshowController.start(stage, thisScene, nonAdmin, index, false);
		stage.setScene(slideshowScene);
		stage.setTitle("searchView");
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * This is a method called when pressing back to Album List button.
	 */
	//method called when pressing back to Album List button
	public void backUserView(){
		stage.setScene(prevScene);
		stage.setTitle("UserView");
		stage.setResizable(false);
		stage.show();
	}

}
