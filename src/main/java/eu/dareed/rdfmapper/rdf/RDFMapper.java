package eu.dareed.rdfmapper.rdf;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import eu.dareed.rdfmapper.MappingData;
import eu.dareed.rdfmapper.MappingDataEntity;
import eu.dareed.rdfmapper.URIBuilder;
import eu.dareed.rdfmapper.xml.nodes.ClassEntity;
import eu.dareed.rdfmapper.xml.nodes.ClassProperty;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RDFMapper {

    public Model mapToRDF(MappingData data, Mapping mapping) {
        Model model = ModelFactory.createDefaultModel();
        URIBuilder uriBuilder = new URIBuilder(mapping.getNamespaceMap());

        List<ClassEntity> classList = mapping.getClassMap().getClassList();
        Map<String, Integer> classIndexMap = buildClassIndexMap(classList);

        for (MappingDataEntity dataEntity : data.getDataEntities()) {
            // get class entity from entity map
            if (!classIndexMap.containsKey(dataEntity.getType())) {
                System.err.println("Class " + dataEntity.getType() + " not found in class-map.");
                continue;
            }
            ClassEntity clsEnt = classList.get(classIndexMap.get(dataEntity.getType()));

            // add triples for 'is instance of class xy'
            String subjectURI = completeURI(uriBuilder.buildURIString(clsEnt.getURI()), dataEntity);
            Resource subject = model.createResource(subjectURI);
            for (String classURI : clsEnt.getclassURLList()) {
                subject.addProperty(RDF.type, model.createResource(uriBuilder.buildURIString(classURI)));
            }

            // add triples for properties
            for (ClassProperty classProperty : clsEnt.getPropertyMap().getPropertyList()) {
            	String predicateURI = uriBuilder.buildURIString(classProperty.getURI());
             
                if (classProperty.getPropertyType().equals("data-property")) {
                	int fieldIndex = classProperty.getIdentifier();
                	String objectString = dataEntity.getAttributeByIndex(fieldIndex);
                    if(classProperty.getDataType() != null){
                    	RDFDatatype type = TypeMapper.getInstance().getSafeTypeByName(classProperty.getDataType());
                    	subject.addProperty(model.getProperty(predicateURI), model.createTypedLiteral(objectString, type));
                    }else{
                    	subject.addProperty(model.getProperty(predicateURI), objectString);
                    }
                    
                    
                } else if (classProperty.getPropertyType().equals("object-property")) {
                	String objectURI = uriBuilder.buildURIString(completeURI(classProperty.getObjectURI(), dataEntity));
                	Resource object = model.getResource(objectURI);
                    subject.addProperty(model.getProperty(predicateURI), object);
                }
            }
        }

        return model;
    }
    

    private Map<String, Integer> buildClassIndexMap(List<ClassEntity> classList) {
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < classList.size(); i++) {
            ClassEntity clsEnt = classList.get(i);
            indexMap.put(clsEnt.getEntityName(), i);
        }
        return indexMap;
    }

    private String completeURI(String uri, MappingDataEntity dataEntity) {
        String[] splittedURI = uri.split("\\$");
        for (int i = 1; i < splittedURI.length; i += 2) {
            int idx = Integer.parseInt(splittedURI[i]);
            splittedURI[i] = dataEntity.getAttributeByIndex(idx);
        }
        return StringUtils.join(splittedURI);
    }

}
