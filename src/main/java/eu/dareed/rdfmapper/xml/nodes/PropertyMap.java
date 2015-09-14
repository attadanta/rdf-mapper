package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PropertyMap {

	private List<OntProperty> propertyList;
	
	public PropertyMap() {
		propertyList = new ArrayList<>();
	}
	
	@XmlElement(name = "ont-property")
	public List<OntProperty> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<OntProperty> propertyList) {
		this.propertyList = propertyList;
	}
}
