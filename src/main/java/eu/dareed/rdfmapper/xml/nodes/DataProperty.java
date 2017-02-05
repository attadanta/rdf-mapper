package eu.dareed.rdfmapper.xml.nodes;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.*;
import eu.dareed.rdfmapper.Environment;

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
     * @param uri  the uri of the property.
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

    @Override
    public Model describe(String subject, Environment environment) {
        Model model = ModelFactory.createDefaultModel();

        String value = environment.getContext().resolveIndex(identifier);

        Literal literal;
        if (type != null) {
            RDFDatatype typeName = TypeMapper.getInstance().getSafeTypeByName(type);
            literal = model.createTypedLiteral(value, typeName);
        } else {
            literal = model.createLiteral(value);
        }

        Statement statement = model.createStatement(model.createResource(subject), model.createProperty(uri), literal);
        model.add(statement);

        return model;
    }
}
