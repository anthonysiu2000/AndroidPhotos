
//made by Anthony Siu and Benjamin Lee


package app;

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

public class Photos extends Application{

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