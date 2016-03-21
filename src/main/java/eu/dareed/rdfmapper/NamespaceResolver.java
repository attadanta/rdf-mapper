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
        if (isAbsolute(uri)) {
            return uri;
        }

        if (isQualifiedURL(uri)) {
            String[] split = splitURL(uri);
            String prefix = split[0];
            String suffix = split[1];
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

    public boolean containsNamespacePrefixMapping(String prefix) {
        return nsMap.containsKey(prefix);
    }

    public boolean isQualifiedURL(String url) {
        return !isAbsolute(url) && url.contains(":");
    }

    public boolean isAbsolute(String url) {
        return url.startsWith("http");
    }

    public String[] splitURL(String url) {
        if (isAbsolute(url)) {
            throw new IllegalArgumentException("URL appears to be absolute: " + url);
        }

        if (isQualifiedURL(url)) {
            int i = url.indexOf(":");
            String prefix = url.substring(0, i);
            String suffix = url.substring(i + 1, url.length());

            return new String[]{prefix, suffix};
        } else {
            return new String[]{null, url};
        }
    }
}
