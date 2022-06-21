package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import application.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PhotoController {
	
	/**
	 * @author Fayed Raza, Steven Zyontz 
	 * 
	 * <p>
	 * This is a class of PhotoController where it works on the individual photo scene
	 * 
	 * @see  Photo
	 */
	
	
	
	@FXML
	Label captionField;
	@FXML
	ListView<String> photoTagView;
	 ObservableList<String> list;
	 
	 @FXML
	 ImageView photoView;
	 @FXML
	 Label dateTimeLabel;
	 
		/**
		 * <p>
		 * This method initializes the scene
		 * 
		 * @param mainstage is stage
		 * 
		 * @param photo is the photo to display
		 * 
		 * @exception FileNotFoundException is photo does not exist
		 * 
		 */
	public void start(Stage mainStage, Photo photo) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		captionField.setText(photo.caption);
		list = FXCollections.observableArrayList(photo.tags);
		
		if(list.size() > 0) {
			photoTagView.getSelectionModel().select(0);
	  	}
		
		 InputStream stream = new FileInputStream(photo.path);
	     Image image = new Image(stream);
		photoView.setImage(image);
		dateTimeLabel.setText(photo.dateTime);
		 photoTagView.setItems(list);
	}

	
}
