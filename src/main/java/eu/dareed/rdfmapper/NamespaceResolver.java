package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.nodes.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class NamespaceResolver {
    private static final Logger log = LoggerFactory.getLogger(NamespaceResolver.class);

    protected final Map<String, String> nsMap;

    /**
     * Default constructor.
     *
     * @param namespacesMap a map assigning each namespace prefix a full url.
     */
    public NamespaceResolver(Map<String, String> namespacesMap) {
        this.nsMap = namespacesMap == null ? Collections.<String, String>emptyMap() : namespacesMap;
    }

    public String resolveURI(String uri) {
        if (uri.startsWith("http")) {
            return uri;
        }

        if (uri.contains(":")) {
            int i = uri.indexOf(":");
            String prefix = uri.substring(0, i);
            String suffix = uri.substring(i + 1, uri.length());
            if (prefix.isEmpty()) {
                return nsMap.get(Namespace.defaultNamespacePrefix) + suffix;
            } else {
                if (nsMap.containsKey(prefix)) {
                    return nsMap.get(prefix) + suffix;
                } else {
                    log.warn("Namespace prefix not mapped: `" + prefix + "'.");
                    return suffix;
                }
            }
        }

        log.warn("Bad uri: `" + uri + "'.");
        return uri;
    }
}
