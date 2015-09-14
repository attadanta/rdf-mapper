package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ObjectMap {

	private List<OntObject> ontObjectList;
	
	public ObjectMap() {
		setOntObjectList(new ArrayList<OntObject>());
	}

	@XmlElement(name = "ont-object")
	public List<OntObject> getOntObjectList() {
		return ontObjectList;
	}

	public void setOntObjectList(List<OntObject> ontObjectList) {
		this.ontObjectList = ontObjectList;
	}
}
