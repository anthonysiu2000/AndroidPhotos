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
public class Tag {

	//private fields
	private String tagName;
	private String tagValue;
	
	/**
	 * The Tag class creates tags for photos. Tags have two parameters, name and value, which are taken as inputs.
	 * 
	 * @param name		Broad category of the tag (eg. location, person)
	 * @param value		Specific category of the tag (eg. Rutgers, John Doe)
	 * @see Photo#Photo(java.io.File)
	 */
	//constructor
	public Tag(String name, String value) {
		this.tagName = name;
		this.tagValue = value;
	}
	
	/**
	 * This method returns the name of the tag as a String.
	 * 
	 * @return tagName
	 */
	//get name
	public String getName() {
		return tagName;
	}
	
	/**
	 * This method returns the value of the tag as a String.
	 * 
	 * @return tagValue
	 */
	//get tag value
	public String getValue() {
		return tagValue;
	}
	
}
