package eu.dareed.rdfmapper.rdf;

import com.hp.hpl.jena.Jena;
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import eu.dareed.rdfmapper.MappingData;
import eu.dareed.rdfmapper.MappingDataEntity;
import eu.dareed.rdfmapper.xml.nodes.ClassEntity;
import eu.dareed.rdfmapper.xml.nodes.ClassProperty;
import eu.dareed.rdfmapper.xml.nodes.EntityMap;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RDFMapper {

    public Model mapIDFToRDF(MappingData data, EntityMap entityMap) {
        Model model = ModelFactory.createDefaultModel();

        List<ClassEntity> classList = entityMap.getClassMap().getClassList();
        Map<String, Integer> classIndexMap = buildClassIndexMap(classList);

        for (MappingDataEntity dataEntity : data.getDataEntities()) {
            // get class entity from entity map
            if (!classIndexMap.containsKey(dataEntity.getType())) {
                System.err.println("Class " + dataEntity.getType() + " not found in entity-map.");
                continue;
            }
            ClassEntity clsEnt = classList.get(classIndexMap.get(dataEntity.getType()));

            // add triples for 'is instance of class xy'
            Resource subject = model.createResource(completeURL(clsEnt.getURL(), dataEntity));
            for (String classURL : clsEnt.getclassURLList()) {
//				com.hp.hpl.jena.vocabulary
                subject.addProperty(RDF.type, model.createResource(classURL));
            }

            // add triples for properties
            for (ClassProperty classProperty : clsEnt.getPropertyMap().getPropertyList()) {
             
                if (classProperty.getPropertyType().equals("data-property")) {
                	int fieldIndex = Integer.parseInt(classProperty.getIdentifier());
                	String objectString = dataEntity.getAttributeByIndex(fieldIndex);
                    if(classProperty.getDataType() != null){
                    /*	*	*	*	under construction	*	*	*	*/
                    	RDFDatatype type = new TypeMapper().getSafeTypeByName(entityMap.getNamespace() + classProperty.getDataType());
                    	subject.addProperty(model.getProperty(classProperty.getURL()), model.createTypedLiteral(objectString, type));
                	/*	*	*	*	*	*	*	*	*	*	*	*	*/
                    }else{
                    	subject.addProperty(model.getProperty(classProperty.getURL()), objectString);
                    }
                    
                    
                } else if (classProperty.getPropertyType().equals("object-property")) {
                	Resource object = model.getResource(completeURL(classProperty.getObjectURL(), dataEntity));
                    subject.addProperty(model.getProperty(classProperty.getURL()), object);
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

    private String completeURL(String url, MappingDataEntity dataEntity) {
        String[] splittedURL = url.split("\\$");
        for (int i = 1; i < splittedURL.length; i += 2) {
            int idx = Integer.parseInt(splittedURL[i]);
            splittedURL[i] = dataEntity.getAttributeByIndex(idx);
        }
        return StringUtils.join(splittedURL);
    }

}
