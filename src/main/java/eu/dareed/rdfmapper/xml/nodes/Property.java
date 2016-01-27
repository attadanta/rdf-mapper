package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
@XmlType(propOrder = {"label", "uri", "name", "identifier"})
public abstract class Property {
    protected String name;
    protected String uri;
    protected String label;
    protected int identifier;
    protected PropertyType propertyType;

    protected Property() {
    }

    protected Property(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Property(String name, PropertyType propertyType, String uri, int identifier) {
        this.name = name;
        this.propertyType = propertyType;
        this.uri = uri;
        this.identifier = identifier;
    }

    public DataProperty asDataProperty() {
        return (DataProperty) this;
    }

    public ObjectProperty asObjectProperty() {
        return (ObjectProperty) this;
    }

    @XmlElement(name = "name", required = true)
    public String getName() {
        return name;
    }

    @XmlElement(name = "uri", required = true)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @XmlElement(name = "id", required = true)
    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    @XmlElement(name = "label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlTransient
    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }
}
