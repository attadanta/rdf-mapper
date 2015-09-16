package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PropertyMap {

	private List<ClassProperty> propertyList;
	
	public PropertyMap() {
		propertyList = new ArrayList<ClassProperty>();
	}
	
	@XmlElement(name = "class-property")
	public List<ClassProperty> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<ClassProperty> propertyList) {
		this.propertyList = propertyList;
	}
}
