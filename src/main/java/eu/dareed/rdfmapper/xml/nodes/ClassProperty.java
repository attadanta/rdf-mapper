package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"identifier", "propertyType", "dataType", "objectURI"})
public class ClassProperty extends Item {

	private int identifier;
	private String propertyType;
	private String dataType;
	private String objectURI;
	
	private ClassProperty(){
		super(null);
	}
	
	public ClassProperty(String uri) {
		super(uri);
	}
	
	@XmlElement(name = "identifier")
	public int getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(int identifier) {
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

	@XmlElement(name = "object-uri")
	public String getObjectURI() {
		return objectURI;
	}

	public void setObjectURI(String objectURI) {
		this.objectURI = objectURI;
	}
}
