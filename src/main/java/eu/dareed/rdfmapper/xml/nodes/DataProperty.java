package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class DataProperty extends Property {
    protected String type;

    protected DataProperty() {
        super(PropertyType.DATA_PROPERTY);
    }

    public DataProperty(String uri, String type) {
        this();
        this.uri = uri;
        this.type = type;
    }

    public DataProperty(String name, String uri, int identifier) {
        super(name, PropertyType.DATA_PROPERTY, uri, identifier);
    }

    @XmlElement(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
