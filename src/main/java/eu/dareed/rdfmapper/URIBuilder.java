package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.nodes.Namespace;

import java.util.Collection;

public class URIBuilder {

    private Collection<? extends Namespace> namespaces;

    public URIBuilder(Collection<? extends Namespace> namespaces) {
        this.namespaces = namespaces;
    }


    public String buildURIString(String uriPart) {
        if (!uriPart.contains(":")) {
            return uriPart;
        }

        int prefixIdx = uriPart.indexOf(':');
        String prefix = uriPart.substring(0, prefixIdx);
        if (prefix.equals("http") || prefix.equals("https")) {
            return prefix + ":" + uriPart.substring(prefixIdx + 1);
        } else {
            return buildURIString(findNamespace(prefix) + uriPart.substring(prefixIdx + 1));
        }
    }


    public String buildURIString(String uriPart, String addPrefix) {
        String innerURI = buildURIString(uriPart);
        if (addPrefix != null) {
            return buildURIString(addPrefix + ":" + innerURI);
        } else {
            return innerURI;
        }

    }


    private String findNamespace(String prefix) {
        for (Namespace ns : namespaces) {
            if (ns.getPrefix().equals(prefix)) {
                return ns.getUri();
            }
        }
        System.out.println("Error: namespace with prefix: " + prefix + " not found!");
        return null;
    }

}
