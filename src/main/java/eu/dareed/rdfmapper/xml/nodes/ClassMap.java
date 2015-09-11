package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ClassMap {

	private List<OntClass> ontClassList;
	
	public ClassMap() {
		ontClassList = new ArrayList<OntClass>();
	}

	@XmlElement(name = "ont-class")
	public List<OntClass> getOntClassList() {
		return ontClassList;
	}

	public void setOntClassList(List<OntClass> ontClassList) {
		this.ontClassList = ontClassList;
	}
	
}
