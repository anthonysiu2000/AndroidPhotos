package view;

import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.NonAdmin;
import model.Tag;

public class PhotoEditController {

	@FXML private ImageView imageView;
	@FXML private TextField textFieldFirst, textFieldSecond;
	@FXML private Button buttonCaption, buttonAdd, buttonDelete, buttonBack;
	@FXML private Text textError, textCaption, textDate, textTag;

	//stores the user
	private NonAdmin nonAdmin;
	private Stage stage;
	private Scene prevScene;
	private String path;
	
	//button Management
	private Button prevButton = null;
	private int buttonNum = 1;
	
	//initiates the scene for the user
	public void start(Stage mainStage, Scene prevScene, NonAdmin nonAdmin, String path){
		this.nonAdmin = nonAdmin;
		this.stage = mainStage;
		this.prevScene = prevScene;
		this.path = path;
		
		setScene();
		
	}
	
	//method called to initialize the text of the scene
	public void setScene() {
		textError.setText(" ");
		//finds photo
		for (int i = 0; i < nonAdmin.getPhotos().size(); i++) {
			if (nonAdmin.getPhotos().get(i).getPath().equals(path)) {
				//sets caption
				String caption = nonAdmin.getPhotos().get(i).getCaption();
				if (caption == null) {
					textCaption.setText("Caption: ");
				} else {
					textCaption.setText("Caption: " + nonAdmin.getPhotos().get(i).getCaption());
				}
				
				//sets date text
				textDate.setText(nonAdmin.getPhotos().get(i).getDate().getTime().toString());
				
				//sets Tags
				ArrayList<Tag> tagList = nonAdmin.getPhotos().get(i).getTags();
				if (tagList.size() == 0) {
					break;
				}
				String tags = "Tags: " +tagList.get(0).getName() + ": " + tagList.get(0).getValue() + " ";
				for (int j = 1; j < tagList.size(); j++) {
					tags = tags + "| " + tagList.get(j).getName() + ": " + tagList.get(i).getValue() + " ";
				}
				textTag.setText(tags);
				break;
			}
		}
		//sets the imageview to the image provided by path
		Image image = new Image(new File(path).toURI().toString());
		imageView.setImage(image);
		
	}
	
	//method used to add a caption to the photo
	public void addCaption(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2 && prevButton != buttonCaption) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldFirst.setText("");
			textFieldSecond.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");

		//First Button Press
		if (buttonNum == 1) {
			//Instructs user
			textError.setText("Please input caption in the first field, then press Add/Edit Caption again.");
			buttonNum = 2;
			
		//Second Button Press	
		} else {
			String caption = textFieldFirst.getText().trim();
			buttonNum = 1;
			//captioncannot be whitespace
			if (caption.isBlank()) {
				textError.setText("Error: no caption inputted");
				textFieldFirst.setText("");
				textFieldSecond.setText("");
				return;
			}

			//adds Caption
			nonAdmin.addCaption(path, caption);
			
			//reset back to normal
			textFieldFirst.setText("");
			textFieldSecond.setText("");
			
			setScene();
			textError.setText("Caption added");
		}
		
	}
	//method used to add a tag to the photo
	public void addTag(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2 && prevButton != buttonAdd) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldFirst.setText("");
			textFieldSecond.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");

		//First Button Press
		if (buttonNum == 1) {
			//Instructs user
			textError.setText("Please input tag category in the first field and tag value in the second field and then press Add Tag again.");
			buttonNum = 2;
			
		//Second Button Press	
		} else {
			String tag1 = textFieldFirst.getText().trim();
			String tag2 = textFieldSecond.getText().trim();
			buttonNum = 1;
			//caption cannot be whitespace
			if (tag1.isBlank() || tag2.isBlank()) {
				textError.setText("Error: tag not properly inputted");
				textFieldFirst.setText("");
				textFieldSecond.setText("");
				return;
			}

			//adds Caption
			boolean addSuccessful = nonAdmin.addTag(path, tag1, tag2);
			if (addSuccessful) {
				//reset back to normal
				textFieldFirst.setText("");
				textFieldSecond.setText("");
				
				setScene();
				textError.setText("Tag added");
			} else {
				textFieldFirst.setText("");
				textFieldSecond.setText("");
				textError.setText("Error: tag already exists");
				
			}
		}
	}
	//method used to delete a tag to the photo
	public void removeTag(ActionEvent e) {
		Button b = (Button)e.getSource();
		prevButton = b;
		//If the second button pressed is incorrect, we state an error, and do not pass the action
		if (buttonNum == 2 && prevButton != buttonDelete) {
			textError.setText("Error: Incorrect button press; Action reset.");
			textFieldFirst.setText("");
			textFieldSecond.setText("");
			buttonNum = 1;
			return;
		}
		textError.setText("");

		//First Button Press
		if (buttonNum == 1) {
			//Instructs user
			textError.setText("Please input tag category in the first field and tag value in the second field and then press Delete Tag again.");
			buttonNum = 2;
			
		//Second Button Press	
		} else {
			String tag1 = textFieldFirst.getText().trim();
			String tag2 = textFieldSecond.getText().trim();
			buttonNum = 1;
			//caption cannot be whitespace
			if (tag1.isBlank() || tag2.isBlank()) {
				textError.setText("Error: tag not properly inputted");
				textFieldFirst.setText("");
				textFieldSecond.setText("");
				return;
			}

			//adds Caption
			boolean deleteSuccessful = nonAdmin.removeTag(path, tag1, tag2);
			if (deleteSuccessful) {
				//reset back to normal
				textFieldFirst.setText("");
				textFieldSecond.setText("");
				
				setScene();
				textError.setText("Tag deleted");
			} else {
				textFieldFirst.setText("");
				textFieldSecond.setText("");
				textError.setText("Error: tag does not exists");
				
			}
		}
		
	}
	
	//method used to get back to album view
	public void goBack() {
		stage.setScene(prevScene);
		stage.setTitle("AlbumView");
		stage.setResizable(false);
		stage.show();
		
	}
	

}
