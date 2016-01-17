package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class DataProperty extends Property {

    protected String type;

    protected DataProperty() {
    }

    public DataProperty(String name, String uri) {
        super(name, uri);
    }

    @XmlElement(name = "type")
    public String getType() {
        return type;
    }
}
