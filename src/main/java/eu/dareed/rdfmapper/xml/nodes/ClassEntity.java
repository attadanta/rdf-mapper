package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

public class ClassEntity extends Entity {

	private PropertyMap propertyMap;
	
	// to prevent instantiation without url
	private ClassEntity(){
		super(null, null);
	}
	
	public ClassEntity(String url, String className) {
		super(url, className);
		this.propertyMap = new PropertyMap();
	}


	@XmlElementWrapper( name = "properties" )
	@XmlElements( {
			@XmlElement( name="objectProperty", type = ObjectProperty.class ),
			@XmlElement( name="dataProperty", type = DataProperty.class ) } )
	public PropertyMap getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(PropertyMap propertyMap) {
		this.propertyMap = propertyMap;
	}
}
