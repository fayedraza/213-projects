package view;

import javafx.event.EventHandler;
import java.util.Optional;

import application.User;
import application.Photo;
import application.Album;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.event.ActionEvent;
public class UserController {
	
	/**
	 * @author Fayed Raza, Steven Zyontz 
	 * 
	 * <p>
	 * This is a class of UserController where it works on the user scene
	 * 
	 */
	
	/** <p>
	 * user is user
	 * 
	 */

	User user;
	
	@FXML
	Button logoutButton;
	@FXML
	ListView<Album> albumListView;
	@FXML
	TextField albumNameField;
	@FXML
	Button addAlbumButton;
	@FXML
	TextField createAlbumField;
	@FXML
	Button albumNameButton;
	@FXML
	Button deleteAlbumButton;
	@FXML
	Button albumViewButton; 
	@FXML
	Button findPhotosButton;
	@FXML
	TextField photoCountLabel;
	@FXML
	TextField findPhotosTagField;
	@FXML
	DatePicker fromDate;
	@FXML
	DatePicker toDate;

	ObservableList<Album> list;
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	/**
	 * <p>
	 * This method initializes the scene
	 * 
	 * @param mainstage is stage
	 * 
	 * @exception IOException is exception thrown during streaming
	 */
	
	public void start(Stage mainStage) throws IOException {
		
		
		
		
		if(user.username.equals("stock")&& !inList("stock")) {
			user.albums.add(new Album("stock"));
			user.albums.get(0).photos.add(new Photo("book.jpeg","data/book.jpeg","",sdf.format(new File("data/book.jpeg").lastModified())));
			user.albums.get(0).photos.add(new Photo("deer.jpeg","data/deer.jpeg","",sdf.format(new File("data/deer.jpeg").lastModified())));
			user.albums.get(0).photos.add(new Photo("ny.jpeg","data/ny.jpeg","",sdf.format(new File("data/ny.jpeg").lastModified())));
			user.albums.get(0).photos.add(new Photo("kangaroo.jpeg","data/kangaroo.jpeg","",sdf.format(new File("data/kangaroo.jpeg").lastModified())));
			user.albums.get(0).photos.add(new Photo("ipad.jpeg","data/ipad.jpeg","",sdf.format(new File("data/ipad.jpeg").lastModified())));
			user.albums.get(0).photos.add(new Photo("plane.jpeg","data/plane.jpeg","",sdf.format(new File("data/plane.jpeg").lastModified())));
		}
		
	
		if(user.albums.size() >0) {
		    photoCountLabel.setText(""+user.albums.get(0).photos.size());
			}
		
		photoCountLabel.setEditable(false);
		
		list = FXCollections.observableArrayList(user.albums);
		
		albumListView.setItems(list);
		
		if(list.size() > 0) {
		 albumListView.getSelectionModel().selectFirst();
		}
		
		
	        	albumListView
				.getSelectionModel()
				.selectedIndexProperty()
				.addListener(
						(obs, oldVal, newVal) ->
							{
							
									updateFields( user.albums.get( albumListView.getSelectionModel().getSelectedIndex() ).photos.size() );
								
							}
						
				);
				
		findPhotosButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{
					try {
						viewResults(mainStage,user.albums,findPhotosTagField.getText());
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
	    );
		
		albumViewButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{
					try {
						viewImage(mainStage, user.albums.get(albumListView.getSelectionModel().getSelectedIndex()) );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
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
		
		deleteAlbumButton.pressedProperty().addListener(
				(obs,oldVal,newVal)->
				{
					try {
						deleteAlbum(mainStage);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				);
		
		albumNameButton.pressedProperty().addListener(
				(obs,oldVal,newVal)->
				{
					try {
						changeAlbumName(mainStage);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				);
		
		addAlbumButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{
					
					try {
						addAlbum(mainStage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				);
		
		
		albumListView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				updateFields(user.albums.get(albumListView.getSelectionModel().getSelectedIndex()).photos.size())
				//showItemInputDialog(mainStage)
				);
	
	}
	
	/**
	 * <p>
	 * This method updates the feilds
	 * 
	 * @param val is number of photos
	 * 
	 * @see UserController#start(Stage mainStage)
	 */
	
	public void updateFields(int val)  {
		photoCountLabel.setText(""+val);
	}
	
	/**
	 * <p>
	 * This method sets user
	 * 
	 * @param name is user
	 * 
	 * @see UserController#start(Stage mainStage)
	 */
	
	public void setUser(User name) {
		user=name;
	}
	
	/**
	 * <p>
	 * This method checks if album name exists
	 * 
	 * @param str is album name
	 *
	 * @return if album exists
	 * 
	 * @see UserController#start(Stage mainStage)
	 */
	
	public boolean inList(String str) {
		for(Album album: user.albums) {
			if(album.name.equals(str))
				return true;
		}
		return false;
	}
	
	
	/**
	 * <p>
	 * This method adds an album
	 * 
	 * @param mainstage is stage
	 * 
	 * @exception IOException is exception thrown during streaming
	 * 
	 * 
	 * @see UserController#start(Stage mainStage)
	 */
	
	public void addAlbum(Stage mainStage) throws IOException{
		if(doubleClick%2==0) {
		String name = createAlbumField.getText();
		if(!inList(name) && name.length() > 0) {
			user.albums.add(new Album(name));
			updateUser();
			ObservableList<Album> list = FXCollections.observableArrayList(user.albums);
			albumListView.setItems(list);
			albumListView.getSelectionModel().select(list.size()-1);
		}else {
			Alert alert;
			alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setContentText("Album name is empty or already exists!");
            doubleClick++;
            alert.showAndWait();
		}
		createAlbumField.clear();
		}
		doubleClick++;
	}
	int doubleClick=0;
	
	/**
	 * <p>
	 * This method chnages album name
	 * 
	 * @param mainstage is stage
	 * 
	 * @exception IOException is exception thrown during streaming
	 * 
	 * @exception FileNotFoundException is exception thrown when image is not found
	 * 
	 * @see UserController#start(Stage mainStage)
	 */
	public void changeAlbumName(Stage mainStage) throws FileNotFoundException, IOException {
		if(doubleClick%2==0) {
			String name = albumNameField.getText();
	   if(!inList(name) && name.length() > 0) {
		user.albums.get(albumListView.getSelectionModel().getSelectedIndex()).name=albumNameField.getText();
		updateUser();
		albumNameField.clear();
		ObservableList<Album> list = FXCollections.observableArrayList(user.albums);
		albumListView.setItems(list);
	   }else {
		   Alert alert;
			alert = new Alert(AlertType.ERROR);
           alert.initOwner(mainStage);
           alert.setTitle("Error");
           alert.setContentText("Album name is empty or already exists!");
           doubleClick++;
           alert.showAndWait();
	   }
		}
		doubleClick++;
	}
	
	/**
	 * <p>
	 * This method deletes an album
	 * 
	 * @param mainstage is stage
	 *
	 * 
	 * @exception IOException is exception thrown during streaming
	 * 
	 * @exception ClassNotFoundException is exception thrown when image is not found
	 * 
	 * @see UserController#start(Stage mainStage)
	 */
	
	public void deleteAlbum(Stage mainStage) throws FileNotFoundException, IOException {
		if(doubleClick%2==0) {
			user.albums.remove(albumListView.getSelectionModel().getSelectedIndex());
			updateUser();
			ObservableList<Album> list = FXCollections.observableArrayList(user.albums);
			albumListView.setItems(list);
			if(list.size() > 0) {
				 albumListView.getSelectionModel().selectFirst();
				}
		}
		doubleClick++;
	}
	
	/**
	 * <p>
	 * This method updates user
	 * 
	 * @see UserController#start(Stage mainStage)
	 */
	
	public void updateUser() throws FileNotFoundException, IOException {
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users"+File.separator+user.username+".dat"));
		oos.writeObject(user);
		oos.close();
	}
	
	/**
	 * <p>
	 * This method update the fields
	 * 
	 * @param mainstage is stage
	 *
	 * 
	 * @see UserController#start(Stage mainStage)
	 */
	
	public void updateFields(Stage mainStage) {
		if(doubleClick%2==0) {
		albumListView.refresh();
		if(list.size()>0) {
		albumNameField.setText(user.albums.get(albumListView.getSelectionModel().getSelectedIndex()).name);
		photoCountLabel.setText(""+user.albums.size());
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
	 * 
	 * @exception IOException is exception thrown during streaming
	 *
	 * 
	 * @see UserController#start(Stage mainStage)
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
	 * This method goes to views the images of an album
	 * 
	 * @param mainstage is stage
	 * 
	 * @param albumtosee is a list of albums
	 * 
	 * @exception IOException is exception thrown during streaming
	 * 
	 * @exception ClassNotFoundException is exception thrown when image is not found
	 * 
	 * @see UserController#start(Stage mainStage)
	 */
	
	
	public void viewImage(Stage mainStage, Album albumToSee) throws IOException, ClassNotFoundException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Album.fxml"));
		VBox root = (VBox)loader.load();
		ImagesController obCon = loader.getController();
		obCon.start(mainStage,albumToSee, user.albums, user);			
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	/**
	 * <p>
	 * This method goes to results scene
	 * 
	 * @param mainstage is stage
	 * 
	 * @param albums is list of albums
	 * 
	 * @param values are keys
	 * 
	 * @exception IOException is exception thrown during streaming
	 * 
	 * @exception ClassNotFoundException is exception thrown when image is not found
	 * 
	 * @see UserController#start(Stage mainStage)
	 */
	
	
	public void viewResults(Stage mainStage, ArrayList<Album> albums, String values) throws IOException, ClassNotFoundException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Result.fxml"));
		VBox root = (VBox)loader.load();
		ResultsController obCon = loader.getController();
		obCon.start(mainStage,albums,values,fromDate.getValue(), toDate.getValue(), user);			
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();
	}
	
}
