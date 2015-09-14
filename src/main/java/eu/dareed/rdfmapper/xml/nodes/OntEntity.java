package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"classURL", "propertyMap"})
public class OntEntity extends OntItem{

	private String classURL;
	protected PropertyMap propertyMap;

	public OntEntity(String url, String classURL) {
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
	
	@XmlElement(name = "property-map")
	public PropertyMap getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(PropertyMap propertyMap) {
		this.propertyMap = propertyMap;
	}
}
