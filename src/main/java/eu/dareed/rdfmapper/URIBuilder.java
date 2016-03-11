package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.nodes.Namespace;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class URIBuilder {

    protected final Map<String, String> nsMap;

    public URIBuilder(Collection<? extends Namespace> namespaces) {
        this.nsMap = mapNamespaces(namespaces);
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
                    System.err.println("Warning, ns prefix not found: " + prefix);
                    return suffix;
                }
            }
        }

        System.err.println("Warning, bad uri: " + uri);
        return uri;
    }

    protected Map<String, String> mapNamespaces(Collection<? extends Namespace> namespaces) {
        Map<String, String> map = new HashMap<>();

        for (Namespace namespace : namespaces ) {
            map.put(namespace.getPrefix(), namespace.getUri());
        }

        return map;
    }

}
