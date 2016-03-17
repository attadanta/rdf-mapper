package eu.dareed.rdfmapper.xml;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class EplusClassHierarchyNode {
    protected final String className;
    protected final Map<String, EplusClassHierarchyNode> children;

    public EplusClassHierarchyNode(String className) {
        this.className = className;
        this.children = new LinkedHashMap<>();
    }

    protected EplusClassHierarchyNode getChild(EplusClass eplusClass) {
        return children.get(eplusClass.name);
    }

    protected boolean hasChild(EplusClass eplusClass) {
        return hasChild(eplusClass.name);
    }

    protected boolean hasChild(String className) {
        return children.containsKey(className);
    }

    protected Collection<EplusClassHierarchyNode> getChildren() {
        return children.values();
    }

    protected EplusClassHierarchyNode addChild(EplusClass eplusClass) {
        EplusClassHierarchyNode childNode = new EplusClassHierarchyNode(eplusClass.name);
        children.put(eplusClass.name, childNode);
        return childNode;
    }
}
