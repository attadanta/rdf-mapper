package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class ObjectProperty extends Property {

    protected String object;

    protected ObjectProperty() {
    }

    public ObjectProperty(String name, String uri) {
        super(name, uri);
    }

    @XmlElement(name = "object")
    public String getObject() {
        return object;
    }
}
