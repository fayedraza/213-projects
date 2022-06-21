package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import application.Album;
import application.Photo;
import application.User;
import javafx.scene.control.TextField;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ImagesController {
	
	/**
	 * @author Fayed Raza, Steven Zyontz 
	 * 
	 * <p>
	 * This is a class of ImagesController where it works on the images scene
	 * 
	 * @see  Photo
	 */
	
	
	@FXML
	Button logoutButton;
	@FXML
	ListView<String> photoView;
	@FXML
	Button addPhotoButton;
	@FXML
	Button deletePhotoButton;
	@FXML
	Button captionButton;
	@FXML
	TextField imagePath;
	@FXML
	TextField captionArea;
	@FXML
	Button addTagButton;
	@FXML
	Button deleteTagButton;
	@FXML
	ComboBox addTagField;
	@FXML
	ListView<String> tagView;
	@FXML
	Button moveDestinationButton;
	@FXML
	TextField destinationField;
	@FXML
	Button copyDestinationButton;
	@FXML
	Button displayPhotoButton;
	@FXML
	Button slideshowButton;

	String newTagName;
    ObservableList<String> list;
    
    /**
     *  <p>
	 * u is user
	 */
    
    User u;
    
    FileChooser fileChooser = new FileChooser();
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    ObservableList<String> tagsList;
    
    
	/**
	 * <p>
	 * This method initializes images of an album scene
	 * 
	 * @param mainstage is a stage 
	 * 
	 * @param albumToSee is album
	 * 
	 * @param albums is list of albums
	 * 
	 * @param user is a user
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 * 
	 * @exception IOException is thrown during streaming
	 * 
	 * @exception FileNotFoundException when file is not found
	 */
    
	public void start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user) throws IOException, ClassNotFoundException {
		
		 fileChooser.setTitle("Choose Image");
		 fileChooser.getExtensionFilters().addAll( new ExtensionFilter("Image Files", "*.png", "*.jpeg", "*.gif", "*.bmp") );
		 
	
		u = user;
		  
        list = FXCollections.observableArrayList(getImageNames(albumToSee.photos));
        photoView.setItems(list);
        
        if(list.size() > 0) {
        	photoView.getSelectionModel().select(0);
        	
        	captionArea.setText(albumToSee.photos.get(0).caption);
   		
   		 tagsList = FXCollections.observableArrayList(albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()).tags);
           
   		 tagView.setItems(tagsList);
   		 
   		 if(tagsList.size() > 0) {
			 tagView.getSelectionModel().select(0);
		 }
        }
        
       
        addTagField.setEditable(true);
        addTagField.getItems().addAll("(location,)","(person,)","(weather,)");
        
        for(int i=0; i < albums.size(); i++) {
        	for(int j=0; j < albums.get(i).photos.size(); j++) {
        		for(int k=0; k < albums.get(i).photos.get(j).tags.size(); k++) {
        			String [] words = albums.get(i).photos.get(j).tags.get(k).split(",");
        			
        			
        			if(! addTagField.getItems().contains(words[0]+",)") && words[0].startsWith("(")) {
        				addTagField.getItems().add(words[0]+",)");
        			}
            		
            	}
        	}
        }
        
        if(list.size() > 0) {
        photoView.getSelectionModel().selectFirst();
  		}
        
        addTagButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					addTag(mainStage,albumToSee, addTagField.getEditor().getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		);
        
        photoView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				
				updateFields(mainStage, albumToSee)
			
				);
        
        slideshowButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{  
					try {
						openSlideShow(mainStage,albumToSee);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				}
		);
        
        displayPhotoButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{  
					try {
						openImage(mainStage,albumToSee);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				}
		);
        
        copyDestinationButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{  
					try {
						copyPhoto(mainStage,albumToSee,albums, false);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				}
		);
        
        moveDestinationButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{  
					
					try {
						copyPhoto(mainStage,albumToSee,albums, true);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch(IndexOutOfBoundsException e) {
						
					}
					 
				}
		);
		
		logoutButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{
					try {
						logout(mainStage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		);
		
		
		addPhotoButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					addPhoto(mainStage,albumToSee);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		);
		
		
		deletePhotoButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					deleteImage(mainStage,albumToSee);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch(IndexOutOfBoundsException e) {
					
				}
			}
			
		}
		);
		

		
		captionButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{
					try {
						addCaption(mainStage,albumToSee);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch(IndexOutOfBoundsException e) {
						
					}
					
				}
		);
		
		deleteTagButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					deleteTag(mainStage,albumToSee);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		);
		
		
		
	}
	
	/**
	 * <p>
	 * This method opens a slideshow
	 * 
	 * @param mainstage is a stage 
	 * 
	 * @param albumToSee is album
	 * 
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 * 
	 * @exception IOException is thrown during streaming

	 */
	
	public void openSlideShow(Stage mainStage,Album albumToSee) throws IOException {
		if(doubleClick%2==0) {
			
			
              if(albumToSee.photos.size() == 0 ) {
  				
  				Alert alert;
  				alert = new Alert(AlertType.ERROR);
  	            alert.initOwner(mainStage);
  	            alert.setTitle("Error");
  	            alert.setContentText("There are no photos!");
  	          doubleClick++;
  	            alert.showAndWait();
  			}else {
		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Slideshow.fxml"));
			VBox root = (VBox)loader.load();
			SlideShowController obCon = loader.getController();
			obCon.start(mainStage,albumToSee.photos);
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Slideshow");
			stage.show();
  			}

		}
		
		doubleClick++;
	}
	
	/**
	 * <p>
	 * This method opens an image
	 * 
	 * @param mainstage is a stage 
	 * 
	 * @param albumToSee is album
	 * 
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 * 
	 * @exception IOException is thrown during streaming
	 */
	
	public void openImage(Stage mainStage,Album albumToSee) throws IOException {
		if(doubleClick%2==0) {
			
			
              if(photoView.getSelectionModel().getSelectedIndex() < 0 ) {
  				
  				Alert alert;
  				alert = new Alert(AlertType.ERROR);
  	            alert.initOwner(mainStage);
  	            alert.setTitle("Error");
  	            alert.setContentText("Item isn't selected or there is no photo!");
  	          doubleClick++;
  	            alert.showAndWait();
  			}else {
		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Photo.fxml"));
			VBox root = (VBox)loader.load();
			PhotoController obCon = loader.getController();
			obCon.start(mainStage,albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()));
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Image");
			stage.show();
  			}

		}
		
		doubleClick++;
	}
	
	/**
	 * <p>
	 * This method copies a photo to its destination album
	 * 
	 * @param mainstage is a stage 
	 * 
	 * @param albumToSee is album
	 * 
	 * @param albums is list of albums
	 * 
	 * @param hasToDelete tells if we are moving the file or not
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 * 
	 * @exception IOException is thrown during streaming
	 * 
	 * @exception FileNotFoundException when file is not found
	 */
	
	
	public void copyPhoto(Stage mainStage, Album albumToSee, ArrayList<Album> albums, boolean hasToDelete) throws FileNotFoundException, IOException {
		String destinationName = destinationField.getText();
		if(doubleClick%2==0) {
	  		   
  			if(photoView.getSelectionModel().getSelectedIndex() < 0 ) {
  				
  				Alert alert;
  				alert = new Alert(AlertType.ERROR);
  	            alert.initOwner(mainStage);
  	            alert.setTitle("Error");
  	            alert.setContentText("Item isn't selected or there is no photo!");
  	           doubleClick++;
  	            alert.showAndWait();
  			}else if(!destinationExists(destinationName,albums) || destinationName.length() == 0) {
  				
  				Alert alert;
  				alert = new Alert(AlertType.ERROR);
  	            alert.initOwner(mainStage);
  	            alert.setTitle("Error");
  	            alert.setContentText("Album doesn't exist or it is empty!");
  	          doubleClick++;
  	            alert.showAndWait();
  			} else {
  				Alert alert;
  				
  	            
  			
  				for(Album album :  albums) {
  					if(album.name.equals(destinationName) && !inList(albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()).name,album)) {
  						alert = new Alert(AlertType.CONFIRMATION);
  		  		    	alert.initOwner(mainStage);
  		  	            alert.setTitle("Confirmation");
  		  	            alert.setContentText("Are you sure that you want to copy/move image to new album?");
  		  	    	
  		  	            Optional<ButtonType> r = alert.showAndWait();
  		  	            if (r.get() == ButtonType.OK) {
  		  	            	
  		  	            	Photo p = albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex());
  		                    Photo newPhoto = new Photo(p.name,p.path,p.caption,p.dateTime);
  		                    newPhoto.tags= new ArrayList<String>(p.tags);
  		            	     album.photos.add(newPhoto);
  		            	 
  		            	 if(hasToDelete) {
  		            		albumToSee.photos.remove(photoView.getSelectionModel().getSelectedIndex());
  			       			list = FXCollections.observableArrayList(getImageNames(albumToSee.photos));
  			       	        photoView.setItems(list);
  			       			  if(list.size() > 0) {
  			       			        photoView.getSelectionModel().selectFirst();
  			       			  }
  			       			  
  			       			
  		            	 }
  		            	destinationField.clear();
  		            	  updateUser();
  		  	            }
  		            	
  		              
  	
  					}else if(album.name.equals(destinationName) && inList(albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()).name,album)) {
  		  				alert = new Alert(AlertType.ERROR);
  		  	            alert.initOwner(mainStage);
  		  	            alert.setTitle("Error");
  		  	            alert.setContentText("Image is aleady in album!");
  		  	        doubleClick++;
  		  	            alert.showAndWait();
  		  	            
  					}
  						
  				}
  	            }
  			  
  			
  			
  			
  		}
  		doubleClick++;
	}
	
	/**
	 * <p>
	 * This method adds a tag
	 * 
	 * @param mainstage is a stage 
	 * 
	 * @param albumToSee is album
	 * 
	 * @param newValue is new tag name
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 * 
	 * @exception IOException is thrown during streaming
	 * 
	 * @exception FileNotFoundException when file is not found
	 */
	
	
	public void addTag(Stage mainStage, Album albumToSee, String newValue) throws FileNotFoundException, IOException {
		
	  		   
  			if(photoView.getSelectionModel().getSelectedIndex() < 0 ) {
  				
  				Alert alert;
  				alert = new Alert(AlertType.ERROR);
  	            alert.initOwner(mainStage);
  	            alert.setTitle("Error");
  	            alert.setContentText("Item isn't selected or there is no tag!");
  	          doubleClick++;
  	            alert.showAndWait();
  			}else {
  				
  				String w[] = newValue.split(",");
  			
  			
  				if(! addTagField.getItems().contains(w[0]+",)") && w[0].startsWith("(")) {
    				addTagField.getItems().add(w[0]+",)");
    			}
  				
  				if(albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()).tags.contains(newValue)) {
  					Alert alert;
  	  				alert = new Alert(AlertType.ERROR);
  	  	            alert.initOwner(mainStage);
  	  	            alert.setTitle("Error");
  	  	            alert.setContentText("Tag already exists!");
  	  	          doubleClick++;
  	  	            alert.showAndWait();
  				}else {
  				
  				
  			albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()).tags.add(newValue);
  			
  			
  			tagsList = FXCollections.observableArrayList(albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()).tags);
            
  	   		 tagView.setItems(tagsList);
  	   	
  	   	tagView.getSelectionModel().select(albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()).tags.size()-1);
  	 
  	  updateUser();
  				}
  			}
  			
 
  			
  			
	}
	
	
	/**
	 * <p>
	 * This method deletes a tag
	 * 
	 * @param mainstage is a stage 
	 * 
	 * @param albumToSee is album
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 * 
	 * @exception IOException is thrown during streaming
	 * 
	 * @exception FileNotFoundException when file is not found
	 */
	
	public void deleteTag(Stage mainStage, Album albumToSee) throws FileNotFoundException, IOException {

  			if(photoView.getSelectionModel().getSelectedIndex() < 0 ) {
  				
  				Alert alert;
  				alert = new Alert(AlertType.ERROR);
  	            alert.initOwner(mainStage);
  	            alert.setTitle("Error");
  	            alert.setContentText("Item isn't selected or there is no photo!");
  	          doubleClick++;
  	            alert.showAndWait();
  			}else if(tagView.getSelectionModel().getSelectedIndex() < 0 ) {
  				
  				Alert alert;
  				alert = new Alert(AlertType.ERROR);
  	            alert.initOwner(mainStage);
  	            alert.setTitle("Error");
  	            alert.setContentText("Tag isn't selected or there is no tag!");
  	          doubleClick++;
  	            alert.showAndWait();
  			
		   }else {
  			albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()).tags.remove(tagView.getSelectionModel().getSelectedIndex());
  			System.out.println("tag removed");
  			tagsList = FXCollections.observableArrayList(albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()).tags);
            
 	   		 tagView.setItems(tagsList);
 	   		 
  	    if(tagsList.size() > 0) {
  	      tagView.getSelectionModel().select(0);
  	    }
  	      captionArea.clear();
  	    

  			      updateUser();
  			}
  			
	}
	
	/**
	 * <p>
	 * This method updates the feilds
	 * 
	 * @param mainstage is a stage 
	 * 
	 * @param albumToSee is album
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 */
	
	public void updateFields(Stage mainStage, Album albumToSee) {
		
		try {
			int index  = photoView.getSelectionModel().getSelectedIndex();
			
		Photo p = albumToSee.photos.get(index);
		
       
		captionArea.setText(p.caption);
		
		 tagsList = FXCollections.observableArrayList(p.tags);
        
		 tagView.setItems(tagsList);
		 
		 if(tagsList.size() > 0) {
			 tagView.getSelectionModel().select(0);
		 }
		}catch(Exception e) {
			
		}
		
	}
	
	/**
	 * <p>
	 * This method adds a photo
	 * 
	 * @param mainstage is a stage 
	 * 
	 * @param albumToSee is album
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 * 
	 * @exception IOException is thrown during streaming
	 */
	
	int doubleClick=0;
	public void addPhoto(Stage mainStage, Album albumToSee) throws IOException {
		File selectedFile;
		
		//if(doubleClick%2==0) {
			
	     selectedFile = fileChooser.showOpenDialog(mainStage);
		String path = String.valueOf(selectedFile.getAbsolutePath());
			
		String date = sdf.format(selectedFile.lastModified());
		
		String imageName = getName(path);
		System.out.println("length; "+imageName.length());
	
			System.out.println("here");
			System.out.println("Size: "+albumToSee.photos.size());
			
			if(!inList(imageName, albumToSee)) {
		    albumToSee.photos.add(new Photo(imageName,path,"",date));
			list = FXCollections.observableArrayList(getImageNames(albumToSee.photos));
			System.out.println("Size: "+albumToSee.photos.size());
		   
	        photoView.setItems(list);
	        photoView.getSelectionModel().select(list.size()-1);
	        updateUser();
			}else {
				Alert alert;
  				alert = new Alert(AlertType.ERROR);
  	            alert.initOwner(mainStage);
  	            alert.setTitle("Error");
  	            alert.setContentText("Image already exists!");
  	            alert.showAndWait();
			}
		
		//}
		//doubleClick++;
	}
	
	 /**
	 * <p>
	 * This method gets name of photo
	 * 
	 * @param text is path of photo
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 */
	
	private String getName(String text) {
		// TODO Auto-generated method stub
		String[] names = text.split("/");
		
		
		if( names[names.length-1].endsWith(".bmp") ||  names[names.length-1].endsWith(".gif") ||  names[names.length-1].endsWith(".jpeg") ||  names[names.length-1].endsWith(".png") )
		return names[names.length-1];
		
		return "";
	}
	
	 /**
 	 * <p>
 	 * This method tells if album name exists
 	 * 
 	 * @param str is an album
 	 *
 	 * @param albumToSee is list of albums
 	 * 
 	 * @return album name exists
 	 * 
 	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
 	 */

	public boolean inList(String str, Album albumToSee) {
		for(Photo p :  albumToSee.photos) {
			if(p.name.equals(str))
				return true;
		}
		return false;
	}
	
	 /**
 	 * <p>
 	 * This method tells if destination to move/copy the photo exists
 	 * 
 	 * @param str is an album
 	 * 
 	 * @param albums is list of albums
 	 * 
 	 * @return destination exists
 	 * 
 	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
 	 */
	
	private boolean destinationExists(String str, ArrayList<Album> albums) {
		for(Album album :  albums) {
			if(album.name.equals(str))
				return true;
		}
		return false;
	}
	
	 /**
 	 * <p>
 	 * This method gets names of photos
 	 * 
 	 * @param photos is list of photos
 	 * 
 	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
 	 */
	
    private ArrayList<String> getImageNames(ArrayList<Photo> photos) {
		ArrayList<String> result = new ArrayList<String> ();
		
		for(Photo p :photos) {
			result.add(p.name);
		}
		
		return result;
	}
    
    /**
 	 * <p>
 	 * This method deletes image
 	 * 
 	 * @param mainstage is stage
 	 * 
 	 * @param albumToSee is album
 	 * 
 	 * @exception IOException is exception thrown during streaming
 	 * 
 	 * 
 	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
 	 */
    
    public void deleteImage(Stage mainStage,Album albumToSee) throws IOException {
		if(doubleClick%2==0) {
		   
			if(photoView.getSelectionModel().getSelectedIndex() < 0 ) {
				Alert alert;
				alert = new Alert(AlertType.ERROR);
	            alert.initOwner(mainStage);
	            alert.setTitle("Error");
	            alert.setContentText("Item isn't selected or there is nothing to delete!");
	            doubleClick++;
	            alert.showAndWait();
			}else {	   
	            	albumToSee.photos.remove(photoView.getSelectionModel().getSelectedIndex());
	       			list = FXCollections.observableArrayList(getImageNames(albumToSee.photos));
	       	        photoView.setItems(list);
	       	        
	       			  if(list.size() > 0) {
	       			        photoView.getSelectionModel().select(0);
	       			  }else if(list.size() == 0) {
	       				captionArea.setText("");
	       			  }
	       			  
	       			updateUser();
			}
		}
		doubleClick++;
	}
    
    /**
	 * <p>
	 * This method goes updates user
	 * 
	 * @param mainstage is stage
	 * 
	 * @param albumToSee is album
	 * 
	 * @exception IOException is exception thrown during streaming
	 *
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 */
    
    public void addCaption(Stage mainStage,Album albumToSee) throws IOException {
  		if(doubleClick%2==0) {
  		   
  			if(photoView.getSelectionModel().getSelectedIndex() < 0 ) {
  				
  				Alert alert;
  				alert = new Alert(AlertType.ERROR);
  	            alert.initOwner(mainStage);
  	            alert.setTitle("Error");
  	            alert.setContentText("Item isn't selected or there is no photo!");
  	          doubleClick++;
  	            alert.showAndWait();
  			}else {
  			albumToSee.photos.get(photoView.getSelectionModel().getSelectedIndex()).caption = captionArea.getText();
  			System.out.println(captionArea.getText());
  			list = FXCollections.observableArrayList(getImageNames(albumToSee.photos));
  	        photoView.setItems(list);
  			 
  			      updateUser();
  			}
  			
  			
  		}
  		doubleClick++;
  	}
    
    /**
	 * <p>
	 * This method logs off
	 * 
	 * @param mainstage is stage
	 * 
	 * @exception IOException is exception thrown during streaming
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 */

	public void logout(Stage mainStage) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
		VBox root = (VBox)loader.load();
		ObjectController obCon = loader.getController();
		obCon.start(mainStage);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();

	}
	
	
	 
	/**
	 * <p>
	 * This method goes updates user
	 * 
	 * @exception IOException is exception thrown during streaming
	 * 
	 * @exception ClassNotFoundException is exception thrown when image is not found
	 * 
	 * @see ImagesController#start(Stage mainStage, Album albumToSee, ArrayList<Album> albums, User user)
	 */
	
     public void updateUser() throws FileNotFoundException, IOException {
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users"+File.separator+u.username+".dat"));
		oos.writeObject(u);
		oos.close();
	}

	

	

}
