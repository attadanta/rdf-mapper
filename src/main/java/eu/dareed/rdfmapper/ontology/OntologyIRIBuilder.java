package eu.dareed.rdfmapper.ontology;

import eu.dareed.rdfmapper.xml.nodes.Property;
import org.semanticweb.owlapi.model.IRI;

public class OntologyIRIBuilder {

    private String namespace;

    public OntologyIRIBuilder(String namespace) {
        if (namespace != null) {
            this.namespace = namespace;
        } else {
            this.namespace = "http://DefaultNamespace/EntityMap#";
        }
    }


    public IRI createClassIRI(String url) {
        return createIRI("entity-class" + "/" + url);
    }


    public IRI createPropertyIRI(String classURL, Property property) {
        return createIRI(classURL + "/" + property.getPropertyType().name() + "/" + property.getIdentifier() + "/" + property.getUri());
    }


    public IRI createIRI(String hierachyPart) {
        return IRI.create(namespace + hierachyPart);
    }

}
