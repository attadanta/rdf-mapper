package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

public class ObjectEntity extends Entity{
	
	private PropertyList propertyList;
	
	// to prevent instantiation without url
	private ObjectEntity(){
		super(null, null);
	}
	
	public ObjectEntity(String url, String classURL) {
		super(url, classURL);
		this.setPropertyList(new PropertyList());
	}

	@XmlElement(name = "property-list")
	public PropertyList getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(PropertyList propertyList) {
		this.propertyList = propertyList;
	}
	
}
