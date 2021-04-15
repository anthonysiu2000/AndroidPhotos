package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.NonAdmin;
import model.Photo;
import model.Tag;

/**
 * Photos is a single-user photo application that allows storage and management of photos in one or more albums.
 * 
 * @author 		Anthony Siu
 * @author 		Benjamin Lee
 * @version		%I% %G%
 * @since		1.0
 *
 */
public class SearchController {
	
	@FXML private ListView<String> photoListView;
	@FXML private TextField textFieldTag1Name, textFieldTag1Value, textFieldTag2Name, textFieldTag2Value;
	@FXML private DatePicker startDate, endDate;
	@FXML private RadioButton radioAndOr;
	@FXML private Button buttonDate, buttonTag, buttonCreate, buttonBack;
	@FXML private Text textError;

	//Initiates the Observable List
	private ObservableList<Photo> obsList; 
	
	//stores the user
	private NonAdmin nonAdmin;
	private Stage stage;
	private Scene prevScene;
	
	//used for button management
	private Button prevButton = null;
	private int buttonNum = 1;
	
	//used to store arraylist of photos
	ArrayList<Photo> photos = new ArrayList<Photo>();
	
	//initiates the scene for the user
	public void start(Stage mainStage, Scene prevScene, NonAdmin nonAdmin){
		
		this.nonAdmin = nonAdmin;
		this.stage = mainStage;
		this.prevScene = prevScene;
		
		setScene();
	}
	
	//method called to initialize the text of the scene
	public void setScene() {
		
		//updates ui to photos
		//obsList = FXCollections.observableArrayList(photos); 
		//photoListView.setItems(obsList); 
	}
		
	
	//method called when pressing the date range button
	public void dateRange(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2 && prevButton != buttonDate) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldTag1Name.setText("");
			textFieldTag1Value.setText("");
			textFieldTag2Name.setText("");
			textFieldTag2Value.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");

		//First Button Press
		if (buttonNum == 1) {
			//Instructs user
			textError.setText("Please input tag(s), choose and/or if applicable and then press Delete Tag again.");
			buttonNum = 2;
			
		//Second Button Press	
		} else {
			String tag1Name = textFieldTag1Name.getText().trim();
			String tag1Value = textFieldTag1Value.getText().trim();
			String tag2Name = textFieldTag2Name.getText().trim();
			String tag2Value = textFieldTag2Value.getText().trim();
			buttonNum = 1;
			//caption cannot be whitespace
			if (tag1Name.isBlank() || tag2Value.isBlank()) {
				textError.setText("Error: tag not properly inputted");
				textFieldTag1Name.setText("");
				textFieldTag1Value.setText("");
				return;
			}
			Tag tag1 = new Tag(tag1Name, tag1Value);
			Tag tag2 = new Tag(tag2Name, tag2Value);
			if (tag2Name.isBlank() && tag2Value.isBlank()) {
				tag2 = null;
			}
			
			boolean and = true;
			
			//gets the list of photos
			photos = nonAdmin.searchPhotoByTag(tag1, tag2, and);
			//reset back to normal
			textFieldTag1Name.setText("");
			textFieldTag1Value.setText("");
			textFieldTag2Name.setText("");
			textFieldTag2Value.setText("");
				
			setScene();
			textError.setText("Search Successful");
		}
	}
	
	//method called when pressing the search by tag button
	public void searchTag(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2 && prevButton != buttonTag) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldTag1Name.setText("");
			textFieldTag1Value.setText("");
			textFieldTag2Name.setText("");
			textFieldTag2Value.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");

		//First Button Press
		if (buttonNum == 1) {
			//Instructs user
			textError.setText("Please input tag(s), choose and/or if applicable and then press Delete Tag again.");
			buttonNum = 2;
			
		//Second Button Press	
		} else {
			String tag1Name = textFieldTag1Name.getText().trim();
			String tag1Value = textFieldTag1Value.getText().trim();
			String tag2Name = textFieldTag2Name.getText().trim();
			String tag2Value = textFieldTag2Value.getText().trim();
			buttonNum = 1;
			//caption cannot be whitespace
			if (tag1Name.isBlank() || tag2Value.isBlank()) {
				textError.setText("Error: tag not properly inputted");
				textFieldTag1Name.setText("");
				textFieldTag1Value.setText("");
				return;
			}
			Tag tag1 = new Tag(tag1Name, tag1Value);
			Tag tag2 = new Tag(tag2Name, tag2Value);
			if (tag2Name.isBlank() && tag2Value.isBlank()) {
				tag2 = null;
			}
			
			boolean and = radioAndOr.isSelected();
			
			//gets the list of photos
			photos = nonAdmin.searchPhotoByTag(tag1, tag2, and);
			//reset back to normal
			textFieldTag1Name.setText("");
			textFieldTag1Value.setText("");
			textFieldTag2Name.setText("");
			textFieldTag2Value.setText("");
				
			setScene();
			textError.setText("Search Successful");
		}
	}
	
	//method called when pressing the create album using results button
	public void createAlbum(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldTag1Name.setText("");
			textFieldTag1Value.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");
		
		//creates a new album and puts it into the database
		nonAdmin.createAlbumFromSearch(photos);
	}
	
	//method called when pressing back to Album List button
	public void backUserView(){
		stage.setScene(prevScene);
		stage.setTitle("UserView");
		stage.setResizable(false);
		stage.show();
	}

}
