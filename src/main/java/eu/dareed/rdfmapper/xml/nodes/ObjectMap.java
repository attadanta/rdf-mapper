package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ObjectMap {

	private List<ObjectEntity> objectList;
	
	public ObjectMap() {
		setObjectList(new ArrayList<ObjectEntity>());
	}

	@XmlElement(name = "object-entity")
	public List<ObjectEntity> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<ObjectEntity> objectList) {
		this.objectList = objectList;
	}
}
