package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"name", "propertyType"})
public class OntProperty extends OntItem {

	private String name;
	private String propertyType;
	
	private OntProperty(){
		super(null);
	}
	
	public OntProperty(String url, String propertyType) {
		super(url);
		setPropertyType(propertyType);
	}
	
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name = "property-type")
	public String getPropertyType() {
		return propertyType;
	}
	
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
}
