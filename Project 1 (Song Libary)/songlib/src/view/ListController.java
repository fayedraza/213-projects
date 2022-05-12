//Names: Fayed Raza and Steven Zyontz

package view;


import java.util.Optional;
import java.util.Scanner;

import application.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

public class ListController {
	
	List<Song> songList;
	
	@FXML  
	private TextField songName;
	@FXML  
	private TextField artistName;
	@FXML  
	private TextField albumName;
	@FXML  
	private TextField year;
	@FXML
	private Button addButton;
	
	@FXML
	private Button editButton;
	
	@FXML
	private Button deleteButton;
	
	Song defSong; 
	
	@FXML         
	ListView<Song> listView;                

	private ObservableList<Song> obsList;              

	public void start(Stage mainStage) {    
		
		songList=new ArrayList<Song>();
		loadFile();
		Collections.sort(songList);
		
		// create an ObservableList 
		// from an ArrayList  
		obsList = FXCollections.observableArrayList(songList); 

		listView.setItems(obsList); 

		// select the first item
		if(obsList.size() > 0) {
		  listView.getSelectionModel().selectFirst();
		  updateFields(mainStage);
		}

		
		// set listener for the items
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				updateFields(mainStage)
				//showItemInputDialog(mainStage)
				);
		addButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->
				addButtonMethod(mainStage)
				);
		

		editButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->{
				editButtonMethod(mainStage);
				}
	    );
		
		deleteButton.pressedProperty().addListener(
				(obs, oldVal, newVal)->{
				deleteButtonMethod(mainStage);
				//listView.getSelectionModel().selectNext();
				
		});
		

	}
	
	public void updateFields(Stage mainStage) {
		Song song = listView.getSelectionModel().getSelectedItem();
		
        if(song != null) {
		songName.setText(song.getName());
		artistName.setText(song.getArtist());
		albumName.setText(song.getAlbum());
		year.setText(song.getYear());
        }
	}
	
	public void addButtonMethod(Stage mainStage) {
		
		Alert alert;
		
		 defSong = new Song(songName.getText().trim(), artistName.getText().trim(), albumName.getText().trim(), year.getText().trim());
		 
		if(year.getText().trim().length() > 0 && Integer.parseInt(year.getText().trim()) < 1 ) { 
			    alert = new Alert(AlertType.ERROR);
		        alert.initOwner(mainStage);
	            alert.setTitle("Error");
	            alert.setContentText("Year needs to be a postive number!");
	            alert.showAndWait();
		}else {
		
		
		if(songName.getText().trim().length() == 0 || artistName.getText().trim().length() == 0) {
		   
			alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setContentText("Song and/or Artist is Empty!");
            alert.showAndWait();
            
		}else if(hasIllegalCharcter(songName.getText().trim()) || hasIllegalCharcter(artistName.getText().trim()) || hasIllegalCharcter(albumName.getText().trim())) {
			
		    alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setContentText("One of the inputs have an illegal character of, |!");
            alert.showAndWait();
            
	    }else if(!inList(songName.getText().trim()+" - "+artistName.getText().trim())) {
	    	
		    alert = new Alert(AlertType.CONFIRMATION);
	    	alert.initOwner(mainStage);
            alert.setTitle("Confirmation");
            alert.setContentText("Are you sure that you want to add " +  songName.getText().trim()+" by "+ artistName.getText().trim() +"?");
            
			
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
			songList.add(defSong);
			Collections.sort(songList);
			obsList = FXCollections.observableArrayList(songList); 
			listView.setItems(obsList);
			listView.getSelectionModel().select(songIndex(defSong.toString()));
			saveToFile();
            }
			
		}else if(inList(songName.getText().trim()+" - "+artistName.getText().trim())){
			alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setContentText(songName.getText().trim()+" by "+ artistName.getText().trim() + " already exists! Please "
            		+ "change Song Title and/or Artist.");
            alert.showAndWait();
		}
		}
	}
	
	public void editButtonMethod(Stage mainStage) {
		
		Song song  = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		
		Alert alert;
		
		if(song == null) {
			   alert = new Alert(AlertType.ERROR);
		        alert.initOwner(mainStage);
	            alert.setTitle("Error");
	            alert.setContentText("There are no songs to edit!");
	            alert.showAndWait();
	    }else if(year.getText().trim().length() > 0 && Integer.parseInt(year.getText().trim()) < 1 ) { 
			   alert = new Alert(AlertType.ERROR);
		        alert.initOwner(mainStage);
	            alert.setTitle("Error");
	            alert.setContentText("Year needs to be a postive number!");
	            alert.showAndWait();
		}else {
		
		
		if(songName.getText().trim().length() == 0 || artistName.getText().trim().length() == 0) {
		   
			alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setContentText("Song and/or Artist is Empty!");
            alert.showAndWait();
            
		}else if(hasIllegalCharcter(songName.getText().trim()) || hasIllegalCharcter(artistName.getText().trim()) || hasIllegalCharcter(albumName.getText().trim())) {
			
		    alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setContentText("One of the inputs have an illegal character of, | !");
            alert.showAndWait();
            
	    }else if(!inList(songName.getText().trim()+" - "+artistName.getText().trim())) {
	    	
		    alert = new Alert(AlertType.CONFIRMATION);
	    	alert.initOwner(mainStage);
            alert.setTitle("Confirmation");
            alert.setContentText("Are you sure that you want to edit " + song.getName() +" by "+ song.getArtist() + "?");
            		
            		
			
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
            String newName = songName.getText().trim()+" - "+artistName.getText().trim();
            songList.set(index, new Song(songName.getText().trim(), artistName.getText().trim(), albumName.getText().trim(), year.getText().trim()));
			Collections.sort(songList);
			obsList = FXCollections.observableArrayList(songList); 
			listView.setItems(obsList);
			listView.getSelectionModel().select(songIndex(newName));
			saveToFile();
            }
			
		}else if(inList(songName.getText().trim()+" - "+artistName.getText().trim())){
			alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setContentText(songName.getText().trim()+" by "+ artistName.getText().trim() + " already exists!");
            alert.showAndWait();
		}
		}
	}
	
	public void deleteButtonMethod(Stage mainStage) {
		Song song = listView.getSelectionModel().getSelectedItem();
		Alert alert;
		
		if(song == null) {
			
			    alert = new Alert(AlertType.ERROR);
		        alert.initOwner(mainStage);
	            alert.setTitle("Error");
	            alert.setContentText("There are no songs to delete!");
	            alert.showAndWait();
	            
	    }else if(inList(song.toString())) {
			
			
			
		alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(mainStage);
	    alert.setTitle("Confirmation");
	    alert.setContentText("Are you sure that you want to delete " + song.getName() +" by "+ song.getArtist() + "?");
				
	    Optional<ButtonType> result = alert.showAndWait();
	    
	    if (result.get() == ButtonType.OK) {
	    	
	    	int originalIndex = songIndex(song.toString());
	        int originalSize = obsList.size();
			songList.remove(originalIndex);
			obsList = FXCollections.observableArrayList(songList);
			listView.setItems(obsList);
			songName.clear();
			artistName.clear();
			albumName.clear();
			year.clear();
			
			if(obsList.size() > 0) {
				if(originalIndex+1 == originalSize) {
					listView.getSelectionModel().select(originalIndex-1);
			    }else {
				   listView.getSelectionModel().select(originalIndex);
			   } 
	       }
			
			saveToFile();
			
	    }
	    
		}
		
		
	}
	
	private void saveToFile() {
		
		
		
		try {
			BufferedWriter w = Files.newBufferedWriter(Paths.get("src/songList.txt"));
			w.write("");
			w.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		     FileWriter myWriter;
		
	         try {
	         
	        	myWriter = new FileWriter("src/songList.txt");
	        	
	        	
	        	for(Song currentSong : songList) {
		      myWriter.write("{\n");
		      myWriter.write(currentSong.getName()+"\n");
		      myWriter.write(currentSong.getArtist()+"\n");
		      myWriter.write(currentSong.getAlbum()+"\n");
		      myWriter.write(currentSong.getYear()+"\n");
		      myWriter.write("}\n");
	        	}
	        	
	        	myWriter.close();
	        	
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	
	
	
	private boolean hasIllegalCharcter(String s) {
		
		for(int i =0; i < s.length(); i++)
			if(s.charAt(i) == '|') {
				return true;
			}
		
		return false;
		
	}
	//returns true if the song with the given name is in the list
	private boolean inList(String name) {
		for(Song curr:songList) {
			if(curr.toString().toLowerCase().equals(name.toLowerCase()))
				return true;
		}
		return false;
	}
	
	//returns the index of the song with the given name, or 0. use inList before using!
	private int songIndex(String name) {
		for(int i=0; i<songList.size(); i++) {
			if(songList.get(i).toString().toLowerCase().equals(name.toLowerCase()))
				return i;
		}
		return 0;
	}
	
	

	public void loadFile() {
		try {
			BufferedReader input 
			= new BufferedReader(new FileReader("src/songList.txt"));
			String line="";
			String name="";
			String artist="";
			String album="";
			String year="";
			
			while((line=input.readLine())!=null) {
				if(line.equals("{")) {
					name=(line=input.readLine());
					artist=(line=input.readLine());
					album=(line=input.readLine());
					year=(line=input.readLine());
					songList.add(new Song(name,artist,album,year));
					line=input.readLine();
				}//end song instantiation
			}//end while
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}//end loadFile
	
}
