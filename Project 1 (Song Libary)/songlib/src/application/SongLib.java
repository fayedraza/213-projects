//Names: Fayed Raza and Steven Zyontz

package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import view.ListController;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Song Library");
		
		// set up FXML loader
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/songLibrary.fxml"));
		
		// load the fxml
		VBox root = (VBox)loader.load();
		

		// get the controller (Do NOT create a new Controller()!!)
		// instead, get it through the loader
		
		ListController listController = loader.getController();
		//System.out.println(listController);
		listController.start(primaryStage);

		Scene scene = new Scene(root, 645, 410);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show(); 

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

}
