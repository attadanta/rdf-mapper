package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class ObjectProperty extends Property {

    protected String object;

    protected ObjectProperty() {
        super(PropertyType.OBJECT_PROPERTY);
    }

    public ObjectProperty(String uri) {
        this();
        this.uri = uri;
    }

    @XmlElement(name = "object")
    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
