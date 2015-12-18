package eu.dareed.rdfmapper.ontology;

import eu.dareed.rdfmapper.URIBuilder;
import eu.dareed.rdfmapper.xml.nodes.ClassEntity;
import eu.dareed.rdfmapper.xml.nodes.ClassProperty;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
import eu.dareed.rdfmapper.xml.nodes.SubClassRelation;
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
        URIBuilder uriBuilder = new URIBuilder(mapping.getNamespaceMap());
        String addPrefix = "defaultns";

        for (ClassEntity classEntity : mapping.getClassMap().getClassList()) {

            IRI classIRI = IRI.create(uriBuilder.buildURIString(classEntity.getURI(), addPrefix));
            
            OWLClass owlClass = dataFactory.getOWLClass(classIRI);

            ontologyManager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(owlClass));

            if (classEntity.getLabel() != null) {
                OWLAnnotation classLabel = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(classEntity.getLabel()));
                ontologyManager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(classIRI, classLabel));
            }


            for (ClassProperty property : classEntity.getPropertyMap().getPropertyList()) {
                if (property.getPropertyType() == null) {
                    System.err.println("Invalid property type in entity " + classEntity.getEntityName());
                    continue;
                }
                IRI propertyIRI = IRI.create(uriBuilder.buildURIString(property.getURI(), addPrefix));

                if (property.getPropertyType().equals("object-property")) {
                    OWLObjectProperty owlProperty = dataFactory.getOWLObjectProperty(propertyIRI);
                    ontologyManager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(owlProperty));
//					ontologyManager.addAxiom(ontology, dataFactory.getOWLObjectPropertyDomainAxiom(owlProperty, owlClass));
                } else if (property.getPropertyType().equals("data-property")) {
                    OWLDataProperty owlProperty = dataFactory.getOWLDataProperty(propertyIRI);
                    ontologyManager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(owlProperty));
//					ontologyManager.addAxiom(ontology, dataFactory.getOWLDataPropertyDomainAxiom(owlProperty, owlClass));

                    String dataType = property.getDataType();
                    if (dataType == null) {
                        System.out.println("Invalid datatype in entity " + classEntity.getEntityName());
                    } else {
                        OWLDatatype owlDatatype = dataFactory.getOWLDatatype(IRI.create(dataType));
                        ontologyManager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(owlProperty, owlDatatype));
                    }

                } else {
                    System.err.println("Invalid property type in entity " + classEntity.getEntityName());
                    continue;
                }

                OWLAnnotation propertyLabel = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(property.getLabel()));
                ontologyManager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(propertyIRI, propertyLabel));
            }

        }

        // Add subclass relations from taxonomy map
        for (SubClassRelation relation : mapping.getTaxonomyMap().getSubRelList()) {
            IRI subIRI = IRI.create(uriBuilder.buildURIString(relation.getSubClass(), addPrefix));
            OWLClass subClass = dataFactory.getOWLClass(subIRI);
            IRI superIRI = IRI.create(uriBuilder.buildURIString(relation.getSuperClass(), addPrefix));
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
