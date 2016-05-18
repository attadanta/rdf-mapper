package eu.dareed.rdfmapper.xml.verification;

import eu.dareed.rdfmapper.NamespaceResolver;
import eu.dareed.rdfmapper.xml.nodes.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
final class DeclaredNamespaces implements Check {
    @Override
    public List<Offense> verify(Mapping context) {
        List<Offense> result = new ArrayList<>();
        NamespaceResolver namespaceResolver = context.namespaceResolver();

        for (Entity entity : context.getEntities()) {
            processURI(namespaceResolver, "entity", entity.getName(), entity.getUri(), result);

            for (String type : entity.getTypes()) {
                processURI(namespaceResolver, "entity type", type, type, result);
            }

            for (Property property : entity.getProperties()) {
                processURI(namespaceResolver, "property", Integer.toString(property.getIdentifier()), property.getUri(), result);
                String propertyName = property.getUri() + " of property " + property.getIdentifier() + " in entity " + entity.getName();

                if (property.getPropertyType() == PropertyType.OBJECT_PROPERTY) {
                    ObjectProperty objectProperty = property.asObjectProperty();
                    processURI(namespaceResolver, "object", propertyName, objectProperty.getObject(), result);
                } else {
                    DataProperty dataProperty = property.asDataProperty();
                    processURI(namespaceResolver, "data property type", propertyName, dataProperty.getType(), result);
                }
            }
        }

        return result;
    }

    private void processURI(NamespaceResolver namespaceResolver, String entityType, String entityName, String entityURL, List<Offense> accumulator) {
        if (!namespaceResolver.isAbsolute(entityURL)) {
            String[] splitURL = namespaceResolver.splitURL(entityURL);
            String prefix = splitURL[0];

            if (prefix != null && !prefix.isEmpty()) {
                if (!namespaceResolver.containsNamespacePrefixMapping(prefix)) {
                    Offense offense = new Offense(Grade.WARNING, "Cannot resolve uri of " + entityType + " " + entityName + ", unmapped prefix: `" + prefix + "'.");
                    accumulator.add(offense);
                }
            }
        }
    }
}
