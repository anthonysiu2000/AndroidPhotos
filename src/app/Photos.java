
package app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.AdminController;
import view.AlbumViewController;
import view.LoginController;
import view.PhotoEditController;
import view.SearchController;
import view.SlideshowController;
import view.UserViewController;

/**
 * Photos is a single-user photo application that allows storage and management of photos in one or more albums.
 * 
 * @author 		Anthony Siu
 * @author 		Benjamin Lee
 * @version		%I% %G%
 * @since		1.0
 *
 */

public class Photos extends Application{

	/**
	 * The method Photos is the center of where the other classes refer back to and eventually get displayed.
	 * <p>
	 * This method loads in the FXML from the code and displays it on the screen.
	 * <p>

	 * @param args			Part of method creation.
	 * @throws Exception	Signals that an Exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Login.fxml"));
		
		Pane root = (Pane)loader.load();
		
		SongLibController libController = loader.getController();
		libController.start(primaryStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photos-Login");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	
	
	public static void main(String[] args) {
		launch(args);

	}
}