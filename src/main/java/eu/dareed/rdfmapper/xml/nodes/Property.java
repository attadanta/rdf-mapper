package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Property {
    protected String name;
    protected String uri;
    protected int identifier;

    protected Property() {
    }

    public Property(String name, String uri, int identifier) {
        this.name = name;
        this.uri = uri;
        this.identifier = identifier;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    @XmlElement(name = "uri")
    public String getURI() {
        return uri;
    }

    @XmlElement(name = "identifier")
    public int getIdentifier() {
        return identifier;
    }
}
