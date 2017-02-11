package eu.dareed.rdfmapper.energyplus;

import eu.dareed.rdfmapper.NamespaceResolver;
import eu.dareed.rdfmapper.xml.nodes.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OntologyMapper {

    private final static Logger log = LoggerFactory.getLogger(OntologyMapper.class);

    private OWLOntologyManager ontologyManager;
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;

    public OntologyMapper() {
        ontologyManager = OWLManager.createOWLOntologyManager();
        try {
            ontology = ontologyManager.createOntology();
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        dataFactory = ontologyManager.getOWLDataFactory();
    }


    public OWLOntology getOntology() {
        return ontology;
    }


    public void loadOntologyFromFile(String path) {
        try {
            ontology = ontologyManager.loadOntologyFromOntologyDocument(new File(path));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        this.dataFactory = ontologyManager.getOWLDataFactory();
    }


    public void mapXMLToOntology(Mapping mapping) {
        NamespaceResolver namespaceResolver = mapping.namespaceResolver();
        Map<String, OWLClassExpression> owlClasses = new HashMap<>();

        for (Entity classEntity : mapping.getEntities()) {
            IRI classIRI = IRI.create(namespaceResolver.resolveURI(classEntity.getUri()));

            OWLClass owlClass = dataFactory.getOWLClass(classIRI);
            owlClasses.put(classEntity.getName(), owlClass);

            ontologyManager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(owlClass));

            if (classEntity.getLabel() != null) {
                OWLAnnotation classLabel = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(classEntity.getLabel()));
                ontologyManager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(classIRI, classLabel));
            }

            if (classEntity.getDescription() != null) {
                OWLAnnotation classDescription = dataFactory.getOWLAnnotation(dataFactory.getRDFSComment(), dataFactory.getOWLLiteral(classEntity.getDescription()));
                ontologyManager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(classIRI, classDescription));
            }

            for (Property property : classEntity.getProperties()) {
                IRI propertyIRI = IRI.create(namespaceResolver.resolveURI(property.getUri()));

                if (property.getPropertyType() == PropertyType.OBJECT_PROPERTY) {
                    OWLObjectProperty owlProperty = dataFactory.getOWLObjectProperty(propertyIRI);
                    ontologyManager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(owlProperty));
                } else if (property.getPropertyType() == PropertyType.DATA_PROPERTY) {
                    OWLDataProperty owlProperty = dataFactory.getOWLDataProperty(propertyIRI);
                    ontologyManager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(owlProperty));

                    String dataType = property.asDataProperty().getType();
                    if (dataType == null) {
                        log.warn("No data type assignment in property: " + classEntity.getName());
                    } else {
                        OWLDatatype owlDatatype = dataFactory.getOWLDatatype(IRI.create(dataType));
                        ontologyManager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(owlProperty, owlDatatype));
                    }
                } else {
                    throw new RuntimeException("No known property type in property `" + property.getUri() + " of `" + classEntity.getName() + "'.");
                }

                OWLAnnotation propertyLabel = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(property.getLabel()));
                ontologyManager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(propertyIRI, propertyLabel));
            }

        }

        // Add subclass relations from taxonomy map
        for (SubClassRelation relation : mapping.getTaxonomy()) {
            assert owlClasses.containsKey(relation.getSubClass());
            assert owlClasses.containsKey(relation.getSuperClass());

            OWLClassExpression subClass = owlClasses.get(relation.getSubClass());
            OWLClassExpression superClass = owlClasses.get(relation.getSuperClass());

            ontologyManager.addAxiom(ontology, dataFactory.getOWLSubClassOfAxiom(subClass, superClass));
        }
    }


    public void saveOntologyToFile(String path) {
        try {
            ontologyManager.saveOntology(ontology, IRI.create(new File(path)));
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }
}
