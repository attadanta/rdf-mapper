package eu.dareed.rdfmapper.ontology;

import eu.dareed.rdfmapper.NamespaceResolver;
import eu.dareed.rdfmapper.xml.nodes.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;

public class OntologyMapper {

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

        for (Entity classEntity : mapping.getEntities()) {
            IRI classIRI = IRI.create(namespaceResolver.resolveURI(classEntity.getUri()));

            OWLClass owlClass = dataFactory.getOWLClass(classIRI);

            ontologyManager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(owlClass));

            if (classEntity.getLabel() != null) {
                OWLAnnotation classLabel = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(classEntity.getLabel()));
                ontologyManager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(classIRI, classLabel));
            }


            for (Property property : classEntity.getProperties()) {
                if (property.getPropertyType() == null) {
                    System.err.println("Invalid property type in entity " + classEntity.getName());
                    continue;
                }
                IRI propertyIRI = IRI.create(namespaceResolver.resolveURI(property.getUri()));

                if (property.getPropertyType() == PropertyType.OBJECT_PROPERTY) {
                    OWLObjectProperty owlProperty = dataFactory.getOWLObjectProperty(propertyIRI);
                    ontologyManager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(owlProperty));
//					ontologyManager.addAxiom(ontology, dataFactory.getOWLObjectPropertyDomainAxiom(owlProperty, owlClass));
                } else if (property.getPropertyType() == PropertyType.DATA_PROPERTY) {
                    OWLDataProperty owlProperty = dataFactory.getOWLDataProperty(propertyIRI);
                    ontologyManager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(owlProperty));
//					ontologyManager.addAxiom(ontology, dataFactory.getOWLDataPropertyDomainAxiom(owlProperty, owlClass));

                    String dataType = ((DataProperty) property).getType();
                    if (dataType == null) {
                        System.out.println("Invalid datatype in entity " + classEntity.getName());
                    } else {
                        OWLDatatype owlDatatype = dataFactory.getOWLDatatype(IRI.create(dataType));
                        ontologyManager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(owlProperty, owlDatatype));
                    }

                } else {
                    System.err.println("Invalid property type in entity " + classEntity.getName());
                    continue;
                }

                OWLAnnotation propertyLabel = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(property.getLabel()));
                ontologyManager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(propertyIRI, propertyLabel));
            }

        }

        // Add subclass relations from taxonomy map
        for (SubClassRelation relation : mapping.getTaxonomy()) {
            IRI subIRI = IRI.create(namespaceResolver.resolveURI(relation.getSubClass()));
            OWLClass subClass = dataFactory.getOWLClass(subIRI);
            IRI superIRI = IRI.create(namespaceResolver.resolveURI(relation.getSuperClass()));
            OWLClass superClass = dataFactory.getOWLClass(superIRI);

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
