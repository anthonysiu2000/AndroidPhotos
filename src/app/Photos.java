//made by Anthony Siu and Benjamin Lee


package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.AdminController;
import view.AlbumViewController;
import view.LoginController;
import view.PhotoEditController;
import view.SearchController;
import view.SlideshowController;
import view.UserViewController;
import model.Database;

public class Photos extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//initiates database, later should read in a database object from a file
		Database database = new Database();
		
		
		//loads in the fmxls
		FXMLLoader loginLoader = new FXMLLoader();
		FXMLLoader photoEditLoader = new FXMLLoader();
		FXMLLoader searchLoader = new FXMLLoader();
		FXMLLoader slideshowLoader = new FXMLLoader();
		FXMLLoader userViewLoader = new FXMLLoader();
		FXMLLoader adminLoader = new FXMLLoader();
		FXMLLoader albumViewLoader = new FXMLLoader();
		loginLoader.setLocation(getClass().getResource("/view/Login.fxml"));
		photoEditLoader.setLocation(getClass().getResource("/view/PhotoEdit.fxml"));
		searchLoader.setLocation(getClass().getResource("/view/Search.fxml"));
		slideshowLoader.setLocation(getClass().getResource("/view/Slideshow.fxml"));
		userViewLoader.setLocation(getClass().getResource("/view/UserView.fxml"));
		adminLoader.setLocation(getClass().getResource("/view/Admin.fxml"));
		albumViewLoader.setLocation(getClass().getResource("/view/AlbumView.fxml"));
		
		
		//creates an anchorpane objects with associated fmxls
		AnchorPane loginRoot = (AnchorPane)loginLoader.load();
		AnchorPane photoEditRoot = (AnchorPane)photoEditLoader.load();
		AnchorPane searchRoot = (AnchorPane)searchLoader.load();
		AnchorPane slideshowRoot = (AnchorPane)slideshowLoader.load();
		AnchorPane userViewRoot = (AnchorPane)userViewLoader.load();
		AnchorPane adminRoot = (AnchorPane)adminLoader.load();
		AnchorPane albumViewRoot = (AnchorPane)albumViewLoader.load();
		
		
		//gets button and other functionality from controller class
		LoginController loginController = loginLoader.getController();
		PhotoEditController photoEditController = photoEditLoader.getController();
		SearchController searchController = searchLoader.getController();
		SlideshowController slideshowController = slideshowLoader.getController();
		UserViewController userViewController = userViewLoader.getController();
		AdminController adminController = adminLoader.getController();
		AlbumViewController albumViewController = albumViewLoader.getController();

		
		//Creates Scenes from the anchorpane roots
		Scene loginScene = new Scene(loginRoot);
		Scene photoEditScene = new Scene(photoEditRoot);
		Scene searchScene = new Scene(searchRoot);
		Scene slideshowScene = new Scene(slideshowRoot);
		Scene userViewScene = new Scene(userViewRoot);
		Scene adminScene = new Scene(adminRoot);
		Scene albumViewScene = new Scene(albumViewRoot);
		
		
		//initializes the primaryStage as the login scene to the UI
		loginController.start(primaryStage, database);
		primaryStage.setScene(loginScene);
		primaryStage.setTitle("Photos");
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	
	
	public static void main(String[] args) {
		launch(args);

	}
}