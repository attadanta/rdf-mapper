package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

public class Entity extends Item{

	private String classURL;

	public Entity(String url, String classURL) {
		super(url);
		setClassURL(classURL);
	}

	@XmlElement(name = "class-url")
	public String getClassURL() {
		return classURL;
	}

	public void setClassURL(String classURL) {
		this.classURL = classURL;
	}

}
