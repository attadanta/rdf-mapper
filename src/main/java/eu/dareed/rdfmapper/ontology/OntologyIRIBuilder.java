package eu.dareed.rdfmapper.ontology;

import eu.dareed.rdfmapper.xml.nodes.ClassProperty;
import org.semanticweb.owlapi.model.IRI;

public class OntologyIRIBuilder {
	
	private String namespace;

	public OntologyIRIBuilder(String namespace) {
		if (namespace != null) {
			this.namespace = namespace;
		}else {
			this.namespace = "http://DefaultNamespace/EntityMap#";
		}
	}


	public IRI createClassIRI(String url) {
		return createIRI("entity-class" + "/" + url);
	}

	
	public IRI createPropertyIRI(String classURL, ClassProperty property) {
		return createIRI(classURL + "/" + property.getPropertyType() + "/" + property.getIdentifier() + "/" + property.getURI());
	}


	public IRI createIRI(String hierachyPart){
		return IRI.create(namespace + hierachyPart);
	}
	
}
