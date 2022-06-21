package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import application.Album;
import application.Photo;
import application.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResultsController {
	
	/**
	 * @author Fayed Raza, Steven Zyontz 
	 * 
	 * <p>
	 * This is a class of ResultsController where it works on the results scene
	 * 
	 */
	
	
	
	@FXML
	Button logoutButton;
	
	@FXML
	Button resultAlbumButton;
	
	@FXML
	ListView<String> resultView;
	
	@FXML
	TextField resultAlbumField;
	
	/**
	 * <p>
	 * user is user
	 * 
	 */
	
	
	User user;
	
	ObservableList<String> list;
	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
	 
	 
	 /**
		 * <p>
		 * This method initializes the scene
		 * 
		 * @param mainstage is stage
		 * 
		 * @param albums is the albums
		 * 
		 *@param values is the keys
		 * 
		 * @param localDate is from date
		 * 
		 * @param localDate2 is to date
		 * 
		 * @param user is user
		 * 
		 */
	 
	public void start(Stage mainStage, ArrayList<Album> albums, String values, LocalDate localDate, LocalDate localDate2, User user) {
		
		this.user = user;
		
		 ArrayList<Photo> result = new ArrayList<Photo>();
		 ArrayList<Photo> albumsWithInDateRange = new ArrayList<Photo>();
		 
		 if(localDate != null && localDate2 != null) {
			
			 String fromDate = formatter.format(localDate);
			 String toDate = formatter.format(localDate2);
		 for(int i=0; i < albums.size(); i++) {
	        	for(int j=0; j < albums.get(i).photos.size(); j++) {
	        		if( inRange(fromDate, albums.get(i).photos.get(j).dateTime.split("\\s+")[0]) && inRange( albums.get(i).photos.get(j).dateTime.split("\\s+")[0], toDate) && !inList(albumsWithInDateRange,albums.get(i).photos.get(j).name)) {
	        			albumsWithInDateRange.add(albums.get(i).photos.get(j));
	        		}
	        	}
	        }
		 }else {
			 for(int i=0; i < albums.size(); i++) {
		        	for(int j=0; j < albums.get(i).photos.size(); j++) {
		        		albumsWithInDateRange.add(albums.get(i).photos.get(j));
		        	}
		        }
		 }
		 
		 if(values.length() > 0) {
			 ArrayList<String> tags = getKeys(values);
		 for(int i=0; i < albumsWithInDateRange.size(); i++) {
	        	for(int j=0; j < albumsWithInDateRange.get(i).tags.size(); j++) {
	        	
	            if(hasTag(albumsWithInDateRange.get(i).tags, albumsWithInDateRange.get(i).tags.get(j), tags)  && !inList(result,albumsWithInDateRange.get(i).name)) {
	        		result.add(albumsWithInDateRange.get(i) );
	        	  }
	        	}
	        }
		 }else {
			 result.addAll(albumsWithInDateRange);
		 }
		 

		   list = FXCollections.observableArrayList(getImageNames(result));
	        
		   resultView.setItems(list);
	        
	        if(list.size() > 0) {
	        resultView.getSelectionModel().selectFirst();
	  		}
		
		resultAlbumButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{
					
					try {
						addAlbum(mainStage, result);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
		
		
	}
	
	
	 /**
		 * <p>
		 * This method initializes the scene
		 * 
		 * @param phototags is list of tags
		 * 
		 * @param key is the key
		 * 
		 *@param tags is list of tags
		 * 
		 * @return if photo has the required tags or not
		 * 
		 * @see ResultsController#start(Stage mainStage, ArrayList<Album> albums, String values, LocalDate localDate, LocalDate localDate2, User user)
		 * 
		 */
	 
	
	private boolean hasTag(ArrayList<String> phototags, String key, ArrayList<String> tags) {
		
		for(String w : tags) {
			if(w.contains("AND")) {
		         String[] w2 = w.split("AND");
		         if(w2[0].trim().equals(key) ) {
		        	 String w3 = w2[1].trim();
		        	 
		        	 for(int i=0; i < phototags.size(); i++) {
		        		 if(phototags.get(i).equals(w3)){
		        	 return true;
		        		 }
		        	 }
		         }
			}else {
				
				if(w.equals(key)) {
					return true;
				}
				
				
			}
		}

		return false;
	}

	 /**
	 * <p>
	 * This method checks to see if album name exists
	 * 
	 * @param str is album name
	 * 
	 * @return if album name exists
	 * 
	 * @see ResultsController#start(Stage mainStage, ArrayList<Album> albums, String values, LocalDate localDate, LocalDate localDate2, User user)
	 * 
	 */
	
	private boolean inList(String str) {
		
		for(Album album: user.albums) {
			if(album.name.equals(str))
				return true;
		}
		return false;
	}

	 /**
	 * <p>
	 * This method logs of page
	 * 
	 * @param mainstage is stage
	 * 
	 * @exception IOException which is exception thrown during streaming
	 * 
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
    
    int click =0;
    
    /**
   	 * <p>
   	 * This adds album
   	 * 
   	 * @param mainstage is stage
   	 * 
   	 * @param result is photos from search
   	 * 
   	 * @exception IOException which is exception thrown during streaming
   	 * 
   	 * @see ResultsController#start(Stage mainStage, ArrayList<Album> albums, String values, LocalDate localDate, LocalDate localDate2, User user)
   	 */

    public void addAlbum(Stage mainStage, ArrayList<Photo> result) throws IOException{
    	if(click % 2== 0) {
    	String name = resultAlbumField.getText();
    	if(result.size() == 0) {
    		Alert alert;
			alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setContentText("Album is Empty!");
            click++;
            alert.showAndWait();
    	}else if(name.length() == 0) {
    		System.out.println("here");
    		Alert alert;
			alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            click++;
            alert.setContentText("Album name is Empty!");
         }else {
		
        	
		if(!inList(name)) {
			Alert alert;
			  alert = new Alert(AlertType.CONFIRMATION);
		    	alert.initOwner(mainStage);
	            alert.setTitle("Confirmation");
	            alert.setContentText("Are you sure that you want to create and add songs to new album?");
	            
				
	            Optional<ButtonType> r = alert.showAndWait();
	            if (r.get() == ButtonType.OK) {
			Album newAlbum = new Album(name);
			newAlbum.photos.addAll(result);
			user.albums.add(newAlbum);
			updateUser();
	            }

		}else {
			Alert alert;
			alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setContentText("Album Name Already Exists!");
            alert.showAndWait();
		}
    	}
    	}
    	click++;
	}
    
    /**
   	 * <p>
   	 * This updates user
   	 *
   	 * 
   	 * @exception IOException which is exception thrown during streaming
   	 * 
   	 * @exception FileNotFoundException which is exception when file is not found
   	 * 
   	 * @see  ResultsController#addAlbum(Stage mainStage, ArrayList<Photo> result)
   	 * 
   	 */
    
public void updateUser() throws FileNotFoundException, IOException {
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users"+File.separator+user.username+".dat"));
		oos.writeObject(user);
		oos.close();
	}

/**
	 * <p>
	 * This methods gets image name
	 * 
	 * @param photos is list of photos
	 * 
	 * @return list of the names of the photos
	 * 
	 *  @see ResultsController#start(Stage mainStage, ArrayList<Album> albums, String values, LocalDate localDate, LocalDate localDate2, User user)
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
 * This methods gets the keys
 * 
 * @param s is the key
 * 
 * @return list of keys to look at
 * 
 *  @see ResultsController#start(Stage mainStage, ArrayList<Album> albums, String values, LocalDate localDate, LocalDate localDate2, User user)
 */

private static ArrayList<String> getKeys(String s){
	
	ArrayList<String> result = new ArrayList<String>();
	
	if(s.contains("and") || s.contains("AND")) {
		String[] words;
		if(s.contains("and")) {
			words = s.split("and");
		}else {
			words = s.split("AND");
		}
		
		result.add( getKeys( words[0].trim() ).get(0) +"AND"+   getKeys(words[1].trim()).get(0));
		
	}else if(s.contains("or") || s.contains("OR")) {
		String[] words;
		if(s.contains("or")) {
			words = s.split("or");
		}else {
			words = s.split("OR");
		}
		result.addAll(getKeys(words[0].trim() ));
		result.addAll(getKeys( words[1].trim() ));
	}else {
		String[] words = s.split("=");
		result.add("("+words[0].trim()+","+words[1].trim()+")");
	}
	return result;
	
}


/**
 * <p>
 * This methods sees if the dates are in range or not
 * 
 * @param time1 is date of image 1
 * 
 * @param time2 is date of image 2
 * 
 * @param time3 is date of image 3
 * 
 * @return if the dates of this image are in the range or not
 * 
 *  @see ResultsController#start(Stage mainStage, ArrayList<Album> albums, String values, LocalDate localDate, LocalDate localDate2, User user)
 */

private boolean inRange(String time1, String time2, String time3) {
	return inRange(time1,time2) && inRange(time2,time3);
}


/**
	 * <p>
	 * This method checks to see if album name exists
	 * 
	 * @param p is list of photos
	 * 
	 * @return if photo name exists
	 * 
	 * @see ResultsController#start(Stage mainStage, ArrayList<Album> albums, String values, LocalDate localDate, LocalDate localDate2, User user)
	 * 
	 */
	
	private boolean inList(List<Photo> p ,String str) {
		
		for(Photo  ph: p) {
			if(ph.name.equals(str))
				return true;
		}
		return false;
	}

/**
 * <p>
 * This methods sees if time2 comes after time 1
 * 
 * @param time1 is date of image 1
 * 
 * @param time2 is date of image 2
 *
 * 
 * @return if time2 comes after time 1
 * 
 *  @see ResultsController#inRange(String time1, String time2, String time3)
 */


private boolean inRange(String time1, String time2) {
	String datesOfTime1[] = time1.split("/");
	String datesOfTime2[] = time2.split("/");
	System.out.println(Arrays.toString(datesOfTime1));
	System.out.println(Arrays.toString(datesOfTime2));
	if (Integer.parseInt(datesOfTime2[2]) - Integer.parseInt(datesOfTime1[2]) > 0) {
		return true;
	}else if (Integer.parseInt(datesOfTime2[2]) - Integer.parseInt(datesOfTime1[2]) < 0) {
		return false;
	}else {
		if (Integer.parseInt(datesOfTime2[0]) - Integer.parseInt(datesOfTime1[0]) > 0) {
			return true;
		}else if (Integer.parseInt(datesOfTime2[0]) - Integer.parseInt(datesOfTime1[0]) < 0) {
			return false;
		}else {
			if (Integer.parseInt(datesOfTime2[1]) - Integer.parseInt(datesOfTime1[1]) > 0) {
				return true;
			}else if (Integer.parseInt(datesOfTime2[1]) - Integer.parseInt(datesOfTime1[1]) < 0) {
				return false;
			}else {
				return true;
			}
		}
	}

}

}
