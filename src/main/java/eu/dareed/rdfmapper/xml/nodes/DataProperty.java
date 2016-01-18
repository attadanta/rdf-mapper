package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

/**
 * A data property represents a literal value associated with a resource in an RDF graph.
 *
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class DataProperty extends Property {
    protected String type;

    protected DataProperty() {
        super(PropertyType.DATA_PROPERTY);
    }

    /**
     * Data property constructor.
     *
     * @param uri the uri of the property.
     * @param type the uri of the data property's domain.
     */
    public DataProperty(String uri, String type) {
        this();
        this.uri = uri;
        this.type = type;
    }

    /**
     * The field containing the uri of this property's data type.
     *
     * @return data type uri, e.g. xsd:string.
     */
    @XmlElement(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
