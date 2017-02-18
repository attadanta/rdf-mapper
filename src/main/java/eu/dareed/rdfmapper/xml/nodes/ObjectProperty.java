package eu.dareed.rdfmapper.xml.nodes;

import eu.dareed.rdfmapper.Context;
import eu.dareed.rdfmapper.Environment;
import eu.dareed.rdfmapper.VariableReference;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlElement;
import java.util.LinkedList;
import java.util.List;

/**
 * An object property represents a relation between two resources in an RDF graph.
 *
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class ObjectProperty extends Property {
    private static final Logger log = LoggerFactory.getLogger(ObjectProperty.class);

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

    @Override
    public Model describe(String subject, Environment environment) {
        Model model = ModelFactory.createDefaultModel();

        Context context = environment.getContext();

        List<VariableReference> variableReferences = context.collectVariableReferences(object);

        if (unresolvedVariableReferences(variableReferences).isEmpty()) {
            String objectURI = context.resolveVariables(object);
            objectURI = environment.getNamespaceResolver().resolveURI(objectURI);

            Statement statement = model.createStatement(model.createResource(subject), model.createProperty(environment.resolveURL(uri)), model.createResource(objectURI));
            model.add(statement);
        } else {
            log.warn("Could not resolve all variable references while linking {}", subject);
        }

        return model;
    }

    protected List<VariableReference> unresolvedVariableReferences(List<VariableReference> referenceList) {
        List<VariableReference> result = new LinkedList<>();

        for (VariableReference reference : referenceList) {
            if (!reference.resolved) {
                result.add(reference);
            }
        }

        return result;
    }
}
