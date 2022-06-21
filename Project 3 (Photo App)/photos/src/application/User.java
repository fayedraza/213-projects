package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Fayed Raza, Steven Zyontz 
 * 
 * <p>
 * This is a class of User
 * 
 * @see  Album
 */


public class User implements Serializable{
	
	/**
	 * <p>
	 * username is name of user
	 * 
	 * fileName is name of file
	 * 
	 * albums is list of albums
	 * 
	 * serialVersionUID is an id
	 */

	private static final long serialVersionUID = 1L;
	public String username;
	public ArrayList<Album> albums;
	static String dir = "users";
	String fileName;
	

	/**
	 * <p>
	 * class constructor specifying username, filename, and albums
	 */
	
	public User(String username) {
		this.username=username;
		fileName=username+".dat";
		albums=new ArrayList<>();
	}
	
	/**
	 * <p>
	 * This method returns the name of user
	 * 
	 * @return name of user
	 * 
	 */
	
	public String toString() {
		return username;
	}
	
	/**
	 * <p>
	 * This method writes when user updates
	 * 
	 * @param user a user
	 * 
	 * @exception IOException if exception thrown during streaming
	 * 
	 */

	public static void write(User user) throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(user.dir+File.separator+user.fileName));
		oos.writeObject(user);
	}
	
	/**
	 * <p>
	 * This method reads when user updates
	 * 
	 * @param name a name of the user
	 * 
	 * @exception IOException if exception thrown during streaming
	 * @exception ClassNotFoundException if class is not found
	 * 
	 */
	
	public static User read(String name) throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dir+File.separator+name+".dat"));
		return (User)ois.readObject();
	}
	
}
