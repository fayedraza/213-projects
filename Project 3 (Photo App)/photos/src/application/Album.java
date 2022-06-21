package application;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @author Fayed Raza, Steven Zyontz 
 * 
 * <p>
 * This is a class of album where each has photos
 * 
 * @see  Photo
 */

public class Album implements Serializable{
	
	/**
	 * <p>
	 * name is name of album
	 * 
	 * photos is list of photos
	 * 
	 * serialVersionUID is an id
	 */
	
	private static final long serialVersionUID = 3104469435434051233L;
	public ArrayList<Photo> photos; //replace with created photo class
	public String name;
	
	/**
	 * <p>
	 * class constructor specifying name of album with an empty set of photos
	 */
	
	
	public Album(String name) {
		this.name=name;
		photos=new ArrayList<>();
	}
	
	/**
	 * <p>
	 * This method returns the name of album
	 * 
	 * @return name of album
	 * 
	 */
	
	public String toString() {
		return name;
	}
	
}