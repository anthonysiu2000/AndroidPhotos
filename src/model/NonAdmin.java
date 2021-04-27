package model;
/**
 * Photos is a single-user photo application that allows storage and management of photos in one or more albums.
 * 
 * @author 		Anthony Siu
 * @author 		Benjamin Lee
 * @version		%I% %G%
 * @since		1.0
 *
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class NonAdmin extends User {
	//fields
	private ArrayList<Album> albumList;
	private ArrayList<Photo> photoList;
	private ArrayList<Connection> conList;
	private int albumNumCreate;
	
	/**
	 * NonAdmin is a subclass of the User class that has special interactions with albums and photos.
	 * 
	 * @param name		The name of the user will be inputted by the admin who creates it.
	 * @see User#User()
	 */
	//constructor
	public NonAdmin(String name){
		this.username = name;
		this.albumList = new ArrayList<Album>();
		this.photoList = new ArrayList<Photo>();
		this.conList = new ArrayList<Connection>();
		this.albumNumCreate = 0;
		//generates photo objects and connections for stock username
		if (name.equals("stock")) {
			this.createAlbum("stock");
			this.addPhoto("stock", new File("docs/A.png"));
			this.addPhoto("stock", new File("docs/B.png"));
			this.addPhoto("stock", new File("docs/C.png"));
			this.addPhoto("stock", new File("docs/D.png"));
			this.addPhoto("stock", new File("docs/E.png"));
			this.addPhoto("stock", new File("docs/F.png"));
			//need to input stock files here using addphoto
		}
	}
	
	/**
	 * This method returns an ArrayList of all the albums for a specific user.
	 * 
	 * @return albumList
	 */
	//gets list of albums for this user
	public ArrayList<Album> getAlbums() {
		return albumList;
	}
	
	/**
	 * This method returns an ArrayList of Photos in all albums of a specific user.
	 * 
	 * @return photoList
	 */
	//gets list of photos for this user
	public ArrayList<Photo> getPhotos() {
		return photoList;
	}
	
	/**
	 * This method returns an ArrayList of all Connections of a user.
	 * 
	 * @return conList
	 */
	//gets list of connections of this user
	public ArrayList<Connection> getConnections() {
		return conList;
	}
	
	/**
	 * This method creates an album as designated by a user. Takes the name of the created album as an input and returns true if the album is successfully created.
	 * 
	 * @param aName		Name of the album
	 * @return boolean
	 */
	//creates a new album; returns true if successful
	public boolean createAlbum(String aName) {
		for (int i = 0; i < albumList.size(); i++) {
			if (albumList.get(i).getName().equals(aName)) {
				return false;
			}
		}
		albumList.add(new Album(aName));
		return true;
	}
	
	/**
	 * This method deletes an album designated by a user. Takes the name of the created album as an input and returns true if the album is successfully deleted.
	 * 
	 * @param aName		Name of the album
	 * @return boolean
	 */
	//deletes an album; returns true if successful
	public boolean deleteAlbum(String aName) {
		boolean foundAlbum = false;
		for (int i = albumList.size()-1; i >= 0; i--) {
			if (albumList.get(i).getName().equals(aName)) {
				albumList.remove(i);
				foundAlbum = true;
			}
		}
		if (!foundAlbum) {
			return false;
		}
		//removes connections linked to albums
		for (int j = conList.size()-1; j >= 0; j--) {
			if (conList.get(j).getAlbum().equals(aName)) {
				conList.remove(j);
			}
		}
		//removes photos with connection to this album only
		for (int k = photoList.size()-1; k >= 0; k--) {
			boolean photoFound = false;
			String photoPath = photoList.get(k).getPath();
			for (int l = 0; l < conList.size(); l++) {
				if (photoPath.equals(conList.get(l).getPath())) {
					photoFound = true;
				}
			}
			if (!photoFound) {
				photoList.remove(k);
			}
		}
		return true;
	}
	
	/**
	 * This method renames an album designated by a user. Takes the previous album and new album name as an input and returns true if the album is successfully renamed.
	 * 
	 * @param oName		Old name of the album
	 * @param nName		New name of the album
	 * @return boolean
	 */
	//renames an album; returns true if successful
	public boolean renameAlbum(String oName, String nName) {
		boolean foundAlbum = false;
		for (int i = 0; i < albumList.size(); i++) {
			if (albumList.get(i).getName().equals(oName)) {
				albumList.get(i).aName = nName;
				foundAlbum = true;
			}
		}
		if (!foundAlbum) {
			return false;
		}
		for (int j = 0; j < conList.size(); j++) {
			if (conList.get(j).getAlbum().equals(oName)) {
				conList.get(j).aName = nName;
			}
		}
		return true;
	}
	
	/**
	 * This method adds a new photo into an album designated by a user. Takes the album name and photo file as input and returns true if successful.
	 * 
	 * <p>
	 * The conditions included are:
	 * <ul>
	 * <li> Finds the album that we want to insert photo.
	 * <li> Checks if a connection is already established between photo and album.
	 * <li> If photo does not exist in database, adds it.
	 * <li> Inserts a new connection.
	 * </ul>
	 * 
	 * @param aName		Name of the album
	 * @param photoFile	Name of the photo file
	 * @return boolean
	 */
	//adds a new photo into an album: returns true if successful
	public boolean addPhoto(String aName, File photoFile) {
		Photo photo = new Photo(photoFile);
		boolean albumFound = false;
		boolean photoFound = false;
		//finds the album that we want to insert photo
		for (int i = 0; i < albumList.size(); i++) {
			if (albumList.get(i).getName().equals(aName)) {
				albumFound = true;
				break;
			}
		}
		if (!albumFound) {
			return false;
		}
		//checks if a connection is already established between photo and album
		for (int j = 0; j < conList.size(); j++) {
			if (conList.get(j).getAlbum().equals(aName)) {
				if (conList.get(j).getPath().equals(photo.getPath())) {
					return false;
				}
			}
		}
		//if photo does not exist in database, adds it
		for (int k = 0; k < photoList.size(); k++) {
			if (photoList.get(k).getPath().equals(photo.getPath())) {
				photoFound = true;
				break;
			}
		}
		if (!photoFound) {
			photoList.add(photo);
		}
		//inserts a new connection
		Connection con = new Connection(aName, photo.getPath());
		conList.add(con);
		return true;
	}
	
	/**
	 * This method removes a photo from an album designated by the user. Takes the names of the album and the photo as an input and returns true if successful.
	 * 
	 * <p>
	 * The conditions included are:
	 * <ul>
	 * <li> Finds the album that we want to insert photo
	 * <li> Finds photo in connection list and removes it
	 * <li> Finds photo in the album and removes it if no other album references it
	 * </ul>
	 * 
	 * @param aName		Name of the album
	 * @param photoPath	Name of the photo path
	 * @return boolean
	 */
	//removes a photo from an album; returns true if successful
	public boolean removePhoto(String aName, String photoPath) {
		boolean albumFound = false;
		boolean conFound = false;
		int numCon = 0;
		//finds the album that we want to remove photo
		for (int i = 0; i < albumList.size(); i++) {
			if (albumList.get(i).getName().equals(aName)) {
				albumFound = true;
				break;
			}
		}
		if (!albumFound) {
			return false;
		}
		//finds photo in connection list and removes it
		for (int j = conList.size()-1; j >= 0; j--) {
			if (conList.get(j).getPath().equals(photoPath)) {
				if (conList.get(j).getAlbum().equals(aName)) {
					conList.remove(j);
					conFound = true;
				}
				numCon++;
			}
		}
		if (!conFound) {
			return false;
		}
		//finds photo in the album and removes it if no other album references it
		if (numCon == 1) {
			for (int k = photoList.size()-1; k >= 0; k--) {
				if (photoList.get(k).getPath().equals(photoPath)) {
					photoList.remove(k);
				}
			}
		}
		return true;
	}
	
	/**
	 * This method adds or renames the caption of a specified photo and returns true if successful
	 * 
	 * @param photoPath		Name of the photo path
	 * @param caption		Caption that the user wants to add to the photo
	 * @return boolean
	 */
	//adds or renames the caption of a specified photo; returns true if successful
	public boolean addCaption(String photoPath, String caption) {
		for (int k = 0; k < photoList.size(); k++) {
			if (photoList.get(k).getPath().equals(photoPath)) {
				photoList.get(k).caption = caption;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method adds a tag to a specified photo and returns true if successful
	 * 
	 * @param photoPath		Name of the photo path
	 * @param tagName		Name of the tag added
	 * @param tagValue		Value of the tag added
	 * @return boolean
	 */
	//adds a tag to a specified photo; returns true if successful
	public boolean addTag(String photoPath, String tagName, String tagValue) {
		//checks if the photo exists
		Tag tag = new Tag(tagName, tagValue);
		for (int k = 0; k < photoList.size(); k++) {
			if (photoList.get(k).getPath().equals(photoPath)) {
				for (int i = 0; i < photoList.get(k).getTags().size(); i++) {
					if (photoList.get(k).getTags().get(i).getName().equals(tagName)) {
						if (photoList.get(k).getTags().get(i).getValue().equals(tagValue)) {
							return false;
						}
					}
				}
				photoList.get(k).addTag(tag);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method removes a tag of a specified photo and returns true if successful. Takes name of photo and tag and values of the tag as inputs.
	 * 
	 * @param photoPath		Name of the photo path
	 * @param tagName		Name of the tag added
	 * @param tagValue		Value of the tag added
	 * @return boolean
	 */
	//removes a tag of a specified photo; returns true if successful
	public boolean removeTag(String photoPath, String tagName, String tagValue) {
		//checks if the photo exists
		for (int k = 0; k < photoList.size(); k++) {
			if (photoList.get(k).getPath().equals(photoPath)) {
				//checks if the tag exists
				for (int i = photoList.get(k).tags.size()-1; i >= 0; i--) {
					if (photoList.get(k).tags.get(i).getName().equals(tagName) && 
							photoList.get(k).tags.get(i).getValue().equals(tagValue)) {
						photoList.get(k).tags.remove(i);
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}
	
	/**
	 * This method copies a photo from an album to another and returns true if successful. Takes old and new album names as inputs as well as photo name.
	 * 
	 * @param oAlbName		Old name of the album
	 * @param nAlbName		New name of the album
	 * @param photoPath		Name of the photo path
	 * @return boolean
	 */
	//copies a photo from an album to another; returns true if successful
	public boolean copyPhoto(String oAlbName, String nAlbName, String photoPath) {
		if (oAlbName.equals(nAlbName)) {
			return false;
		}
		//checks if the photo exists
		for (int k = 0; k < photoList.size(); k++) {
			if (photoList.get(k).getPath().equals(photoPath)) {
				//checks that the album exists
				boolean albumExists = false;
				for (int j = 0; j < albumList.size(); j++) {
					if (albumList.get(j).getName().equals(nAlbName)) {
						albumExists = true;
					}
				}
				if (!albumExists) {
					return false;
				}
				//checks if there is already an instance of the photo in the target album
				for (int i = 0; i < conList.size(); i++) {
					if (conList.get(i).getAlbum().equals(nAlbName) && conList.get(i).getPath().equals(photoPath)) {
						return false;
					}
				}
				conList.add(new Connection(nAlbName, photoPath));
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method moves a photo from an album to another and returns true if successful. Removes the photo from the old album. Takes old and new album names as inputs as well as photo name.
	 * 
	 * @param oAlbName		Old name of the album
	 * @param nAlbName		New name of the album
	 * @param photoPath		Name of the photo pathd
	 * @return boolean
	 */
	//moves a photo from an album to another; returns true if successful
	public boolean movePhoto(String oAlbName, String nAlbName, String photoPath) {
		if (oAlbName.equals(nAlbName)) {
			return false;
		}
		//copies photo to new album
		if (copyPhoto(oAlbName, nAlbName, photoPath)) {
			//removes old photo to album connection
			for (int i = conList.size()-1; i >= 0; i--) {
				if (conList.get(i).getAlbum().equals(oAlbName) && conList.get(i).getPath().equals(photoPath)) {
					conList.remove(i);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * This method returns a filtered array list of photos that in a specified date range.
	 * 
	 * @param start		Starting date range
	 * @param end		Ending date range
	 * @return sortedPhotos
	 */
	//returns a filtered array list of photos that in a specified date range
	public ArrayList<Photo> searchPhotoByDate(Calendar start, Calendar end) {
		//creates new array list
		ArrayList<Photo> sortedPhotos = new ArrayList<Photo>();
		for (int j = 0; j < photoList.size(); j++) {
			sortedPhotos.add(photoList.get(j));
		}
		//sorts array list in terms of calendar times
		Collections.sort(sortedPhotos);
		//removes photos that are not within the calendar range
		for (int i = sortedPhotos.size() - 1; i >= 0; i--) {
			if (sortedPhotos.get(i).getDate().compareTo(end) > 0 || sortedPhotos.get(i).getDate().compareTo(start) < 0) {
				sortedPhotos.remove(i);
			}
		}
		return sortedPhotos;
	}
	
	/**
	 * This method returns an array of photos that have the tags specified; if 2, uses andOp boolean to specify "and" or "or".
	 * 
	 * @param tag1		Value of the first tag
	 * @param tag2		Value of the second tag
	 * @param andOp		Boolean to specify "and" or "or" where 
	 * @return sortedPhotos
	 */
	//returns an array of photos that have the tags specified; if 2, uses andOp boolean to specify "and" or "or"
	public ArrayList<Photo> searchPhotoByTag(Tag tag1, Tag tag2, boolean andOp) {
		//creates new array list
		ArrayList<Photo> sortedPhotos = new ArrayList<Photo>();
		for (int j = 0; j < photoList.size(); j++) {
			sortedPhotos.add(photoList.get(j));
		}
		//removes photos that do not have the specified tags
		for (int i = sortedPhotos.size() - 1; i >= 0; i--) {
			ArrayList<Tag> temp = sortedPhotos.get(i).getTags();
			if (temp.size() == 0) {
				sortedPhotos.remove(i);
			}
			//if there is only one tag
			if (tag2 == null) {
				for (int j = 0; j< temp.size(); j++) {
					boolean remove = true;
					if (temp.get(j).getName().equals(tag1.getName()) && temp.get(j).getValue().equals(tag1.getValue())) {
						remove = false;
					}
					if (remove) {
						sortedPhotos.remove(i);
					}
				}
			}
			//two tags
			else {
				for (int j = 0; j< temp.size(); j++) {
					boolean tag1Found = false;
					boolean tag2Found = false;
					//sets fields to true if tags are found
					if (temp.get(j).getName().equals(tag1.getName()) && temp.get(j).getValue().equals(tag1.getValue())) {
						tag1Found = true;
					}
					if (temp.get(j).getName().equals(tag2.getName()) && temp.get(j).getValue().equals(tag2.getValue())) {
						tag2Found = true;
					}
					//remove those with no tags
					if (tag1Found == false && tag2Found == false) {
						sortedPhotos.remove(i);
					}
					//In AND case, remove those with only one tag
					else if (andOp && (tag1Found ^ tag2Found)) {
						sortedPhotos.remove(i);
					}
				}
			}
		}
		return sortedPhotos;
	}
	
	/**
	 * This method creates a new album and adds connections between the album and the copied photos
	 * 
	 * @param list		List of photos
	 */
	//creates a new album and adds connections between the album and the copied photos
	public void createAlbumFromSearch(ArrayList<Photo> list) {
		albumNumCreate += 1;
		String newAlbumName = "CreatedAlbum" + String.valueOf(albumNumCreate);
		createAlbum(newAlbumName);
		for (int i = 0; i < list.size(); i++) {
			//inserts a new connection
			Connection con = new Connection(newAlbumName, list.get(i).getPath());
			conList.add(con);
		}
	}
	
	/**
	 * This method returns the number of photos in an album and takes the name of the album as an input.
	 * 
	 * @param aName			Name of the album
	 * @return numPhotos	Number of the photos in the album
	 */
	//returns number of photos in an album
	public int getAlbumNum(String aName) {
		int numPhotos = 0;
		for (int i = 0; i < conList.size(); i++) {
			if (conList.get(i).getAlbum().equals(aName)) {
				numPhotos++;
			}
		}
		return numPhotos;
	}
	
	/**
	 * This method returns the earliest Calendar entry of the specified album.
	 * 
	 * @param aName			Name of the album
	 * @return earliest		Earliest date of a photo in the album
	 */
	//returns earliest Calendar entry of the Album
	public Calendar getAlbumEarliest(String aName) {
		if (photoList.size() == 0) {
			return null;
		}
		Calendar earliest = photoList.get(0).getDate();
		boolean PhotoInAlbum = false;
		for (int i = 0; i < conList.size(); i++) {
			if (conList.get(i).getAlbum().equals(aName)) {
				String temp = conList.get(i).getPath();
				for (int j = 0; j < photoList.size(); j++) {
					if (photoList.get(j).getPath().equals(temp) && (!PhotoInAlbum || photoList.get(j).getDate().compareTo(earliest) < 0)) {
						earliest = photoList.get(j).getDate();
						PhotoInAlbum = true;
					}
				}
			}
		}
		if (!PhotoInAlbum) {
			return null;
		}
		return earliest;
	}
	
	/**
	 * This method returns the latest Calendar entry of the specified album.
	 * 
	 * @param aName			Name of the album
	 * @return latest		Earliest date of a photo in the album
	 */
	//returns latest Calendar entry of the Album
	public Calendar getAlbumLatest(String aName) {
		if (photoList.size() == 0) {
			return null;
		}
		Calendar latest = photoList.get(0).getDate();
		boolean PhotoInAlbum = false;
		for (int i = 0; i < conList.size(); i++) {
			if (conList.get(i).getAlbum().equals(aName)) {
				String temp = conList.get(i).getPath();
				for (int j = 0; j < photoList.size(); j++) {
					if (photoList.get(j).getPath().equals(temp) && (!PhotoInAlbum || photoList.get(j).getDate().compareTo(latest) > 0)) {
						latest = photoList.get(j).getDate();
						PhotoInAlbum = true;
					}
				}
			}
		}
		if (!PhotoInAlbum) {
			return null;
		}
		
		return latest;
	}
	
}
