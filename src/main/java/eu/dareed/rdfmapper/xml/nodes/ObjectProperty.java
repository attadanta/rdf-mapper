package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

/**
 * An object property represents a relation between two resources in an RDF graph.
 *
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

    public ObjectProperty(String uri, String objectURI) {
        this(uri);
        this.object = objectURI;
    }

    /**
     * The field containing the uri pattern of the object that this object property refers to.
     *
     * @return the object's uri pattern, <em>o</em>, in an rdf triple (<em>s</em>, <em>p</em>, <em>o</em>).
     */
    @XmlElement(name = "object")
    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
