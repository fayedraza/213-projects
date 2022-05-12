//Names: Fayed Raza and Steven Zyontz

package application;


public class Song implements Comparable<Song>{
	
	String name;
	String artist;
	String album;
	String year;
	
	public Song(String name, String artist, String album, String year) {
		this.name=name;
		this.artist=artist;
		this.album=album;
		this.year=year;
	}
	
	public String getName() {
		return this.name;
	}
	public String getArtist() {
		return this.artist;
	}
	public String getAlbum() {
		return this.album;
	}
	public String getYear() {
		return this.year;
	}
	
	public String toString() {
		return this.name+" - "+ this.artist;
	}

	@Override
	public int compareTo(Song o) {
		
		if(toString().toLowerCase().compareTo(o.toString().toLowerCase())>0)
			return 1;
		else if(toString().toLowerCase().compareTo(o.toString().toLowerCase())<0)
			return -1;
		else return 0;
	}
	
	
}
