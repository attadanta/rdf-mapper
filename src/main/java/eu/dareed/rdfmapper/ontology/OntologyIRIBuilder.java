package eu.dareed.rdfmapper.ontology;

import org.semanticweb.owlapi.model.IRI;

import eu.dareed.rdfmapper.xml.nodes.ClassEntity;
import eu.dareed.rdfmapper.xml.nodes.ClassProperty;

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
		return createIRI(classURL + "/" + property.getPropertyType() + "/" + property.getName() + "/" + property.getURL());
	}


	public IRI createIRI(String hierachyPart){
		return IRI.create(namespace + hierachyPart);
	}
	
}
