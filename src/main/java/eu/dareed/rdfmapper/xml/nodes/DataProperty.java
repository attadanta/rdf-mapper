package eu.dareed.rdfmapper.xml.nodes;

import eu.dareed.rdfmapper.Environment;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.rdf.model.*;

import javax.xml.bind.annotation.XmlElement;

/**
 * A data property represents a literal value associated with a resource in an RDF graph.
 *
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class DataProperty extends Property {
    protected String type;
    protected String value;

    protected DataProperty() {
        super(PropertyType.DATA_PROPERTY);
    }

    /**
     * Data property constructor.
     *
     * @param uri  the uri of the property.
     * @param type the uri of the data property's domain.
     */
    public DataProperty(String uri, String type) {
        this();
        this.uri = uri;
        this.type = type;
    }

    /**
     * Data property constructor.
     *
     * @param uri  the uri of the property.
     * @param type the uri of the data property's domain.
     */
    public DataProperty(String uri, String type, String value) {
        this(uri, type);
        this.value = value;
    }

    /**
     * The field containing the uri of this property's data type.
     *
     * @return data type uri, i.e. xsd:string.
     */
    @XmlElement(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Model describe(String subject, Environment environment) {
        Model model = ModelFactory.createDefaultModel();
        Resource resource = model.createResource(subject);
        return describe(model, resource, environment);
    }

    @Override
    public Model describe(Model model, Resource subject, Environment environment) {
        if (value == null) {
            throw new IllegalStateException("No value is set for property " + uri);
        }

        String lex = environment.resolveSequence(value);

        Literal literal;
        if (type != null) {
            RDFDatatype typeName = TypeMapper.getInstance().getSafeTypeByName(type);
            literal = model.createTypedLiteral(lex, typeName);
        } else {
            literal = model.createLiteral(lex);
        }

        Statement statement = model.createStatement(subject, model.createProperty(environment.resolveURL(uri)), literal);
        model.add(statement);

        return model;
    }
}
