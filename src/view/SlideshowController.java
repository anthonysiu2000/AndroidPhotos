package view;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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
public class SlideshowController {
	@FXML private ImageView imageview;
	@FXML private Button buttonLeft, buttonRight, buttonAlbum;
	
	//stores user
	private Stage stage;
	private Scene prevScene;
	private NonAdmin nonAdmin;
	private int index;
	private boolean forward;
	
	//makes the arraylist of photo paths
	ArrayList<String> paths = new ArrayList<String>();
	
	//stores index of the current index of the slideshow image
	private int slideshowIndex = 0;
	
	
	public void start(Stage mainstage, Scene prevScene, NonAdmin nonAdmin, int index, boolean forward) {
		this.stage = mainstage;
		this.prevScene = prevScene;
		this.nonAdmin = nonAdmin;
		this.index = index;
		this.forward = forward;
		
		//sets the arraylist according to forward or backward
		if (forward) {
			//gets list of photo paths for user
			for (int i = 0; i < nonAdmin.getConnections().size(); i++) {
				if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
					paths.add(nonAdmin.getConnections().get(i).getPath());
				}
			}
		} else {
			//gets list of photo paths for user
			for (int i = nonAdmin.getConnections().size()-1; i >= 0; i--) {
				if (nonAdmin.getConnections().get(i).getAlbum().equals(nonAdmin.getAlbums().get(index).getName())) {
					paths.add(nonAdmin.getConnections().get(i).getPath());
				}
			}
		}
		
		//sets the photo in the image view to the first photo in the arraylist
		
	}
	
	//method will advance the photo by one
	public void forward() {
		if (paths.size() == 0) {
			return;
		}
		
		//sets the photo in the image view to the next photo in the arraylist
		if (slideshowIndex < paths.size()-1) {
			
		} else {
			return;
		}
	}
	
	//method will go to the previous photo in the list
	public void backward() {
		if (paths.size() == 0) {
			return;
		}

		//sets the photo in the image view to the previous photo in the arraylist
		if (slideshowIndex > 0) {
			
		} else {
			return;
		}
		
	}
	
	//method will go back to album view
	public void goBack() {
		stage.setScene(prevScene);
		stage.setTitle("AlbumView");
		stage.setResizable(false);
		stage.show();
	}

}
