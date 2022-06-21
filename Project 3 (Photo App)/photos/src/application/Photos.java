package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;



/**
 * @author Fayed Raza, Steven Zyontz 
 * 
 * <p>
 * This is a class of photo
 * 
 * @see  Photo
 */


public class Photos extends Application {
	

/**
 * <p>
 * This method starts the app at login
 * 
 * @param primary stage which is the stage
 *
 *@exception IOException if exception thrown during streaming
 * 
 */
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle("Photos");
		
		// set up FXML loader
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/Login.fxml"));
		
		// load the fxml
		VBox root = (VBox)loader.load();
		

		// get the controller (Do NOT create a new Controller()!!)
		// instead, get it through the loader
		
		ObjectController controller = loader.getController();
		controller.start(primaryStage);

		Scene scene = new Scene(root, 645, 410);
		primaryStage.setScene(scene);
		primaryStage.show(); 

	}


	
	public static void main(String[] args) {
		launch(args);
	}
}
