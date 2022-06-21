package view;

import java.util.Optional;

import application.Album;
import application.Photo;
import application.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectController {
	
	/**
	 * @author Fayed Raza, Steven Zyontz 
	 * 
	 * <p>
	 * This is a class of ObjectController where it works on the object
	 * 
	 * @see  Photo
	 */
	

	String username="";
	ArrayList<String> usernames;
	
	@FXML
	TextField loginField;
	@FXML
	Button loginButton;
	@FXML
	Button logoutButtonAdmin;
	
	/**
	 * <p>
	 * This method initializes the scene
	 * 
	 * @param mainstage is stage
	 *
	 * 
	 */
	
	public void start(Stage mainStage) {
		// TODO Auto-generated method stub
		
		
		loginButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{
					try {
						login(mainStage);
					} catch (IOException | ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				);


		
	}
	
	/**
	 * <p>
	 * This method changes the scene
	 * 
	 * @param mainstage is stage
	 *
	 *@exception IOException is exception thrown during scenes
	 *
	 *@exception ClassNotFoundException is exception thrown when class is not fou
	 * 
	 */
	
	public void changeSceneAdmin(Stage mainStage) throws IOException, ClassNotFoundException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
		VBox root = (VBox)loader.load();
		AdminController adCon = loader.getController();
		adCon.start(mainStage);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();

	}
	
	/**
	 * <p>
	 * This method changes the scene
	 * 
	 * @param mainstage is stage
	 *
	 *@param user is current user
	 *
	 *@exception IOException is exception thrown during scenes
	 *
	 *@exception ClassNotFoundException is exception thrown when class is not found
	 * 
	 */
	
public void changeSceneUser(Stage mainStage,User user) throws IOException, ClassNotFoundException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/User.fxml"));
		VBox root = (VBox)loader.load();
		UserController usCon = loader.getController();
		usCon.setUser(user);
		usCon.start(mainStage);
		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.show();

	}

/**
 * <p>
 * This method changes the scene
 * 
 * @param mainstage is stage
 *
 *@param user is current user
 *
 *@exception IOException is exception thrown during scenes
 *
 *@exception ClassNotFoundException is exception thrown when class is not found
 *
 *@see ObjectController#start(Stage)
 * 
 */
	
	int stopDoubleClick=0;
	public void login(Stage mainStage) throws IOException, ClassNotFoundException {
		if(stopDoubleClick%2==0) {
		boolean loggedIn = false;
		username=loginField.getText();
			if(username.equals("admin")) {
				changeSceneAdmin(mainStage);
			}
			else {
				File folder = new File("users");
				for(File file:folder.listFiles()) {
					if(file.getName().equals(username+".dat")) {
						ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getPath()));
						User user = (User)ois.readObject();
						loggedIn = true;
						changeSceneUser(mainStage,user);
					}
				}
				
				if(!loggedIn) {
					Alert alert;
	  				alert = new Alert(AlertType.ERROR);
	  	            alert.initOwner(mainStage);
	  	            alert.setTitle("Error");
	  	            alert.setContentText("Username does not exist or input is empty!");
	  	          stopDoubleClick++;
	  	            alert.showAndWait();
	  	          loginField.clear();
				}
			}
		}
		stopDoubleClick++;
	}//end loginMethod


	
}
