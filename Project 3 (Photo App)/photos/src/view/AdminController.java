package view;


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
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminController {
	
	/**
	 * @author Fayed Raza, Steven Zyontz 
	 * 
	 * <p>
	 * This is a class of AdminController where it works on the admin scene
	 * 
	 */
	
	
	
	@FXML
	Button logoutButtonAdmin;
	@FXML
	ListView<User> userListView;
	@FXML
	TextField addUserField;
	@FXML
	Button addUserButton;
	@FXML
	Button deleteUserButton;
	
	/**
	 * <p>
	 * user is list of users
	 */
	
	
	ArrayList<User> users = new ArrayList<>();
	
	/**
	 * <p>
	 * This method initializes the scene
	 * 
	 * @param mainstage is stage

	 * @exception FileNotFoundException is photo does not exist
	 * 
	 * @exception IOException is thrown during streaming
	 * 
	 */
	
	public void start(Stage mainStage) throws IOException, ClassNotFoundException {
		
		loadList();
		
		logoutButtonAdmin.pressedProperty().addListener(
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
		
		addUserButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{
					try {
						addUser(mainStage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				);
		
		deleteUserButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				{
					try {
						deleteUser(mainStage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				);
		
		ObservableList<User> list = FXCollections.observableArrayList(users);
		userListView.setItems(list);
		if(list.size() > 0) {
			userListView.getSelectionModel().selectFirst();
			}
		
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
	
	
	 /**
		 * <p>
		 * This method adds user
		 * 
		 * @param mainstage is stage
		 * 
		 * @exception IOException which is exception thrown during streaming
		 * 
		 * @see AdminController#start(Stage mainStage)
		 * 
		 */
	
	int doubleClick=0;
	public void addUser(Stage mainStage) throws IOException {
		if(doubleClick%2 ==0) {
		
		if(!inList(addUserField.getText()) && addUserField.getText().trim().length() > 0 ){
			File file = new File("users/"+addUserField.getText()+".dat");
			if(!file.exists()) {
				
				file.createNewFile();
			}
			users.add(new User(addUserField.getText()));
			ObservableList<User> list = FXCollections.observableArrayList(users);
			userListView.setItems(list);
			
				userListView.getSelectionModel().select(list.size()-1);
				
			updateList();
		}else {
			Alert alert;
  		    alert = new Alert(AlertType.ERROR);
  	            alert.initOwner(mainStage);
  	            alert.setTitle("Error");
  	            alert.setContentText("Album name already exists or input is empty");
  	            doubleClick++;
  	            alert.showAndWait();
  	          
		}
		addUserField.clear();
		}
		doubleClick++;
	}
	
	 /**
		 * <p>
		 * This method checks to see if album name exists
		 * 
		 * @param str is album name
		 * 
		 * @return if album name exists
		 * 
		 * @see AdminController#start(Stage mainStage)
		 * 
		 */
	
	public boolean inList(String str) {
		for(User user: users) {
			if(user.username.equals(str))
				return true;
		}
		return false;
	}
	
	 /**
		 * <p>
		 * This method deletes user
		 * 
		 * @param mainstage is stage
		 * 
		 * @exception IOException which is exception thrown during streaming
		 * 
		 * @see AdminController#start(Stage mainStage)
		 * 
		 */
	
	public void deleteUser(Stage mainStage) throws IOException {
		if(doubleClick%2==0) {
			File folder = new File("users");
			
			for(File file:folder.listFiles()) {
				
				if(file.getName().equals(users.get(userListView.getSelectionModel().getSelectedIndex()).username+".dat")) {
				//File file = new File("/users/"+users.get(userListView.getSelectionModel().getSelectedIndex()).username+".dat");
				file.delete();
				users.remove(userListView.getSelectionModel().getSelectedIndex());
			
				ObservableList<User> list = FXCollections.observableArrayList(users);
				userListView.setItems(list);
				if(list.size() > 0) {
					userListView.getSelectionModel().selectFirst();
					}
				updateList();
				break;
				
				}
			}
		}
		doubleClick++;
	}
	
	   /**
   	 * <p>
   	 * This method updates user
   	 *
   	 * 
   	 * @exception IOException which is exception thrown during streaming
   	 * 
   	 * @see  AdminController#start(Stage mainStage)
   	 * 
   	 */
	
	public void updateList() throws IOException{
		ObjectOutputStream oos;
		for(User user:users) {
			oos = new ObjectOutputStream(new FileOutputStream("users"+File.separator+user.username+".dat"));
			oos.writeObject(user);
			oos.close();
		}
		
		
	}
	
	   /**
   	 * <p>
   	 * This method loads list
   	 *
   	 * 
   	 * @exception IOException which is exception thrown during streaming
   	 * 
   	 * @exception ClassNotFoundException which is exception when class is not found
   	 * 
   	 * @see  AdminController#start(Stage mainStage)
   	 * 
   	 */
	
	public void loadList() throws IOException, ClassNotFoundException{
		File folder = new File("users");
		ObjectInputStream ois;
		for(File file:folder.listFiles()) {
			//System.out.println(file.getName());
			ois = new ObjectInputStream(new FileInputStream(file.getPath()));
			User user = (User)ois.readObject();
			users.add(user);
			ois.close();
			//usernames.add(line);
		}
	}//end load
	
}
