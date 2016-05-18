package eu.dareed.rdfmapper.xml.nodes;

import eu.dareed.rdfmapper.NamespaceResolver;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "mapping")
@XmlType(propOrder = {"namespaces", "entities", "taxonomy"})
public class Mapping {
    private List<Namespace> namespaces;
    private List<Entity> entities;
    private List<SubClassRelation> taxonomy;

    public Mapping() {
        this.namespaces = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.taxonomy = new ArrayList<>();
    }

    @XmlElementWrapper(name = "namespaces")
    @XmlElements({
            @XmlElement(name = "ns")
    })
    public List<Namespace> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(List<Namespace> namespaces) {
        this.namespaces = namespaces;
    }

    public NamespaceResolver namespaceResolver() {
        Map<String, String> namespacesMap = new HashMap<>();

        for (Namespace namespace : namespaces ) {
            namespacesMap.put(namespace.getPrefix(), namespace.getUri());
        }

        return new NamespaceResolver(namespacesMap);
    }

    @XmlElementWrapper(name = "entities")
    @XmlElements({
            @XmlElement(name = "entity")
    })
    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    @XmlElementWrapper(name = "taxonomy")
    @XmlElements({
            @XmlElement(name = "relation")
    })
    public List<SubClassRelation> getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(List<SubClassRelation> taxonomy) {
        this.taxonomy = taxonomy;
    }
}
