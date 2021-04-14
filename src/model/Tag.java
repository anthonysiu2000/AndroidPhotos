//made by Anthony Siu and Benjamin Lee

package model;

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
