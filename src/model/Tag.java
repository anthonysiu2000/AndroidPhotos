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
	//constructor
	public Tag(String name, String value) {
		this.tagName = name;
		this.tagValue = value;
	}
	
	//get name
	public String getName() {
		return tagName;
	}
	//get tag value
	public String getValue() {
		return tagValue;
	}
	
}
