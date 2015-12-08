package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

public class Entity extends Item{

	private String entityName;
	private List<String> classURLList;
	

	public Entity(String url, String entityName) {
		super(url);
		setEntityName(entityName);
		classURLList = new ArrayList<String>();
	}

	@XmlElement(name = "entity-name")
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@XmlElement(name = "class-url")
	public List<String> getclassURLList(){
		return classURLList;
	}

	public void addTypeURL(String typeURL) {
		classURLList.add(typeURL);
	}
	
	public void setClassURLList(List<String> classURLList){
		this.classURLList = classURLList;
	}
	
}
