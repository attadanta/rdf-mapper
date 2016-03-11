package eu.dareed.rdfmapper.rdf;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.thaiopensource.util.UriEncoder;
import eu.dareed.rdfmapper.MappingData;
import eu.dareed.rdfmapper.MappingDataEntity;
import eu.dareed.rdfmapper.URIBuilder;
import eu.dareed.rdfmapper.xml.nodes.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RDFMapper {

    public Model mapToRDF(MappingData data, Mapping mapping) {
        Model model = ModelFactory.createDefaultModel();

        for (Namespace namespace : mapping.getNamespaces()) {
            if (!namespace.getPrefix().equals(Namespace.defaultNamespacePrefix)) {
                model.setNsPrefix(namespace.getPrefix(), namespace.getUri());
            }
        }

        URIBuilder uriBuilder = new URIBuilder(mapping.getNamespaces());

        List<Entity> classList = mapping.getEntities();
        Map<String, Integer> classIndexMap = buildClassIndexMap(classList);

        for (MappingDataEntity dataEntity : data.getDataEntities()) {
            // get class entity from entity map
            if (!classIndexMap.containsKey(dataEntity.getType().toLowerCase())) {
                System.err.println("Class " + dataEntity.getType() + " not found in class-map.");
                continue;
            }
            Entity clsEnt = classList.get(classIndexMap.get(dataEntity.getType().toLowerCase()));

            // add triples for 'is instance of class xy'
            String subjectURI = completeURI(uriBuilder.resolveURI(clsEnt.getUri()), dataEntity);
            Resource subject = model.createResource(subjectURI);
            for (String classURI : clsEnt.getTypes()) {
                subject.addProperty(RDF.type, model.createResource(uriBuilder.resolveURI(classURI)));
            }

            // add triples for properties
            for (Property classProperty : clsEnt.getProperties()) {
            	String predicateURI = uriBuilder.resolveURI(classProperty.getUri());

                if (classProperty.getPropertyType() == PropertyType.DATA_PROPERTY) {
                	int fieldIndex = classProperty.getIdentifier();
                	String objectString = dataEntity.getAttributeByIndex(fieldIndex);
                    String dataType = ((DataProperty) classProperty).getType();
                    if(dataType != null){
                    	RDFDatatype type = TypeMapper.getInstance().getSafeTypeByName(dataType);
                    	subject.addProperty(model.getProperty(predicateURI), model.createTypedLiteral(objectString, type));
                    }else{
                    	subject.addProperty(model.getProperty(predicateURI), objectString);
                    }
                } else if (classProperty.getPropertyType() == PropertyType.OBJECT_PROPERTY) {
                    String objectPattern = classProperty.asObjectProperty().getObject();
                    String objectURI;

                    try {
                        objectURI = uriBuilder.resolveURI(completeURI(objectPattern, dataEntity));
                    } catch (IndexOutOfBoundsException e) {
                        System.err.println("No field present in entity: " + objectPattern);
                        continue;
                    }

                    Resource object = model.getResource(objectURI);
                    subject.addProperty(model.getProperty(predicateURI), object);
                }
            }
        }

        return model;
    }

    private Map<String, Integer> buildClassIndexMap(List<Entity> classList) {
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < classList.size(); i++) {
            Entity clsEnt = classList.get(i);
            indexMap.put(clsEnt.getName().toLowerCase(), i);
        }
        return indexMap;
    }

    private String completeURI(String uri, MappingDataEntity dataEntity) {
        String[] splittedURI = uri.split("\\$");
        for (int i = 1; i < splittedURI.length; i += 2) {
            int idx = Integer.parseInt(splittedURI[i]);
            String attributeValue = dataEntity.getAttributeByIndex(idx);
            splittedURI[i] = UriEncoder.encode(attributeValue);
        }
        return StringUtils.join(splittedURI);
    }

}
