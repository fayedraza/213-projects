package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import application.Photo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SlideShowController {
	/**
	 * @author Fayed Raza, Steven Zyontz 
	 * 
	 * <p>
	 * This is a class of SlideShowController where it works on the slideshow scene
	 *
	 */
	
	 @FXML
	 ImageView photoSlideshowView;
	 @FXML
	Button slideshowNextButton;
	@FXML
	Button slideshowPrevButton;
	/**
	 
	 * <p>
	 * i is index 
	 */
	
	
	int i;
	
	/**
	 * <p>
	 * This method initializes the scene
	 * 
	 * @param mainstage is stage
	 * 
	 * @param photos is list of photos
	 * 
	 * @exception FileNotFoundException when file is not found
	 * 
	 */
 
	
	public void start(Stage mainStage, ArrayList<Photo> photos) throws FileNotFoundException {
		 
		i=0;
		InputStream stream = new FileInputStream(photos.get(i).path);
	     Image image = new Image(stream);
	     photoSlideshowView.setImage(image);
	     
	     slideshowNextButton.pressedProperty().addListener(
					(obs, oldVal, newVal)->
					{  
						try {
							setImage(mainStage,photos,i+1, true);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
					}
			);
	     
	     
	     slideshowPrevButton.pressedProperty().addListener(
					(obs, oldVal, newVal)->
					{  
						try {
							setImage(mainStage,photos,i-1, false);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
					}
			);
	        
		
	}
	
	int click =0;
	
	/**
	 * <p>
	 * This method sets the image
	 * 
	 * @param mainstage is stage
	 * 
	 * @param photos is list of photos
	 * 
	 * @param j is index
	 * 
	 * @param next tells if it is going to the next image or previous image
	 * 
	 * @exception FileNotFoundException when file is not found
	 * 
	 * @see SlideShowController#setImage(Stage mainStage, ArrayList<Photo> photos, int j, boolean next) 
	 */
	
	public void setImage(Stage mainStage, ArrayList<Photo> photos, int j, boolean next) throws FileNotFoundException {
		
		if(click % 2 == 0) {
		if(j < 0 || j >= photos.size()) {
			return;
		}
			this.i=j;
	
		InputStream stream = new FileInputStream(photos.get(j).path);
	     Image image = new Image(stream);
	     photoSlideshowView.setImage(image);
		}
		click++;
		
	}

	

}
