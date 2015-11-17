package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"name", "propertyType", "dataType"})
public class ClassProperty extends Item {

	private String identifier;
	private String propertyType;
	private String dataType;
	
	private ClassProperty(){
		super(null);
	}
	
	public ClassProperty(String url) {
		super(url);
	}
	
	@XmlElement(name = "identifier")
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@XmlElement(name = "property-type")
	public String getPropertyType() {
		return propertyType;
	}
	
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	@XmlElement(name = "datatype")
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
