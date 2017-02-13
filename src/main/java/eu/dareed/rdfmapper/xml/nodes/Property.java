package eu.dareed.rdfmapper.xml.nodes;

import eu.dareed.rdfmapper.Environment;
import org.apache.jena.rdf.model.Model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
@XmlType(propOrder = {"label", "description", "uri"})
public abstract class Property {
    protected String name;
    protected String uri;
    protected String label;
    protected String description;
    protected PropertyType propertyType;

    protected Property() {
    }

    protected Property(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Property(String name, PropertyType propertyType, String uri) {
        this.name = name;
        this.propertyType = propertyType;
        this.uri = uri;
    }

    public DataProperty asDataProperty() {
        return (DataProperty) this;
    }

    public ObjectProperty asObjectProperty() {
        return (ObjectProperty) this;
    }

    @XmlElement(name = "uri", required = true)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @XmlElement(name = "label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public abstract Model describe(String subject, Environment environment);
}
