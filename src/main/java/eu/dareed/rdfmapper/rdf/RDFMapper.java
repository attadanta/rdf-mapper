package eu.dareed.rdfmapper.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import eu.dareed.rdfmapper.Environment;
import eu.dareed.rdfmapper.MappingData;
import eu.dareed.rdfmapper.MappingDataEntity;
import eu.dareed.rdfmapper.NamespaceResolver;
import eu.dareed.rdfmapper.xml.nodes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RDFMapper {
    private static final Logger log = LoggerFactory.getLogger(RDFMapper.class);

    public Model mapToRDF(MappingData data, Mapping mapping) {
        Model model = ModelFactory.createDefaultModel();

        for (Namespace namespace : mapping.getNamespaces()) {
            if (!namespace.getPrefix().equals(Namespace.defaultNamespacePrefix)) {
                model.setNsPrefix(namespace.getPrefix(), namespace.getUri());
            }
        }

        NamespaceResolver resolver = mapping.namespaceResolver();
        Environment environment = new Environment(resolver);

        List<Entity> classList = mapping.getEntities();
        Map<String, List<Integer>> classIndexMap = buildClassIndexMap(classList);

        for (MappingDataEntity dataEntity : data.getDataEntities()) {
            Environment entityEnvironment = environment.augment(dataEntity);

            // get class entity from entity map
            if (!classIndexMap.containsKey(dataEntity.getType().toLowerCase())) {
                log.info("Class `{}' not found in class map.", dataEntity.getType());
                continue;
            }

            for (Integer entityIndex : classIndexMap.get(dataEntity.getType().toLowerCase())) {
                Entity entityType = classList.get(entityIndex);

                // add triples for 'is instance of class xy'
                String subjectURL = entityEnvironment.resolveURL(entityType.getUri());

                Resource subject = model.createResource(subjectURL);
                for (String classURI : entityType.getTypes()) {
                    subject.addProperty(RDF.type, model.createResource(resolver.resolveURI(classURI)));
                }

                // add triples for properties
                for (Property classProperty : entityType.getProperties()) {
                    model.add(classProperty.describe(subjectURL, entityEnvironment));
                }
            }

        }

        return model;
    }

    private Map<String, List<Integer>> buildClassIndexMap(List<Entity> classList) {
        Map<String, List<Integer>> indexMap = new HashMap<>();
        for (int i = 0; i < classList.size(); i++) {
            Entity entity = classList.get(i);
            String classKey = entity.getName().toLowerCase();

            List<Integer> entitiesList;
            if (indexMap.containsKey(classKey)) {
                entitiesList = indexMap.get(classKey);
            } else {
                entitiesList = new LinkedList<>();
                indexMap.put(classKey, entitiesList);
            }

            entitiesList.add(i);
        }
        return indexMap;
    }
}
