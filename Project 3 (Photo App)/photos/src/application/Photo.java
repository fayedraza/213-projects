package application;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @author Fayed Raza, Steven Zyontz 
 * 
 * <p>
 * This is a class of photo
 * 
 * @see  Photos
 */

public class Photo implements Serializable{
	
	/**
	 * <p>
	 * name is name of photo
	 * 
	 * path is path of photo
	 * 
	 * caption is caption of photo
	 * 
	 * dateTime is dateTime of photo
	 * 
	 * tags is tags of photo
	 * 
	 * serialVersionUID is an id
	 */

	private static final long serialVersionUID = 2006027737427922085L;
	public String name;
	public String path;
	public String caption;
	public String dateTime;
	public ArrayList<String> tags;
	
	/**
	 * <p>
	 * class constructor specifying name, path, caption, and datetime of photo with an empty set of tags
	 */
	
	
	public Photo(String name, String path, String caption, String dateTime) {
		this.name=name;
		this.path=path;
		this.caption=caption;
		this.dateTime = dateTime;
		tags=new ArrayList<String>();
	}
	
}
