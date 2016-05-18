package eu.dareed.rdfmapper.xml.verification;

import eu.dareed.rdfmapper.NamespaceResolver;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
import eu.dareed.rdfmapper.xml.nodes.Namespace;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
final class DefaultNamespaceDeclared implements Check {
    @Override
    public List<Offense> verify(Mapping context) {
        NamespaceResolver namespaceResolver = context.namespaceResolver();
        return namespaceResolver.containsNamespacePrefixMapping(Namespace.defaultNamespacePrefix)
                ? Collections.<Offense>emptyList()
                : Collections.singletonList(new Offense(Grade.WARNING, "No default namespace declared."));
    }

}
