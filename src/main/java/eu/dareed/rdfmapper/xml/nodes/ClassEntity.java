package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

public class ClassEntity extends Entity{

	private PropertyMap propertyMap;
	
	// to prevent instantiation without url
	private ClassEntity(){
		super(null, null);
	}
	
	public ClassEntity(String url, String className) {
		super(url, className);
		this.propertyMap = new PropertyMap();
	}

	
	@XmlElement(name = "property-map")
	public PropertyMap getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(PropertyMap propertyMap) {
		this.propertyMap = propertyMap;
	}
}
