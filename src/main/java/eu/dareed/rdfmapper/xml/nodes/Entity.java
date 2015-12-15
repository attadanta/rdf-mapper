package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

public class Entity extends Item{

	private String entityName;
	private List<String> classURIList;
	

	public Entity(String uri, String entityName) {
		super(uri);
		setEntityName(entityName);
		classURIList = new ArrayList<String>();
	}

	@XmlElement(name = "entity-name")
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@XmlElement(name = "class-uri")
	public List<String> getclassURLList(){
		return classURIList;
	}
	
	public void setClassURIList(List<String> classURIList){
		this.classURIList = classURIList;
	}
	
}
