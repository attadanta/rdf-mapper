package eu.dareed.rdfmapper.rdf;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.thaiopensource.util.UriEncoder;
import eu.dareed.rdfmapper.Environment;
import eu.dareed.rdfmapper.MappingData;
import eu.dareed.rdfmapper.MappingDataEntity;
import eu.dareed.rdfmapper.NamespaceResolver;
import eu.dareed.rdfmapper.xml.nodes.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
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

        List<Entity> classList = mapping.getEntities();
        Map<String, Integer> classIndexMap = buildClassIndexMap(classList);

        for (MappingDataEntity dataEntity : data.getDataEntities()) {
            // get class entity from entity map
            if (!classIndexMap.containsKey(dataEntity.getType().toLowerCase())) {
                log.info("Class `{}' not found in class map.", dataEntity.getType());
                continue;
            }
            Entity entityType = classList.get(classIndexMap.get(dataEntity.getType().toLowerCase()));

            // add triples for 'is instance of class xy'
            String subjectURI = completeURI(resolver.resolveURI(entityType.getUri()), dataEntity);

            Resource subject = model.createResource(subjectURI);
            for (String classURI : entityType.getTypes()) {
                subject.addProperty(RDF.type, model.createResource(resolver.resolveURI(classURI)));
            }

            Environment environment = new Environment(resolver);

            // add triples for properties
            for (Property classProperty : entityType.getProperties()) {
                model.add(classProperty.describe(subjectURI, environment.augment(dataEntity)));
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

    private List<Integer> unresolvablePropertyReferences(String uri, MappingDataEntity dataEntity) {
        List<Integer> references = new ArrayList<>();

        for (Integer reference : propertyReferences(uri)) {
            if (!dataEntity.containsAttributeWithIndex(reference)) {
                references.add(reference);
            }
        }

        return references;
    }

    private List<Integer> propertyReferences(String uri) {
        List<Integer> references = new ArrayList<>();

        String[] splitURI = uri.split("\\$");
        for (int i = 1; i < splitURI.length; i += 2) {
            references.add(Integer.parseInt(splitURI[i]));
        }

        return references;
    }
}
