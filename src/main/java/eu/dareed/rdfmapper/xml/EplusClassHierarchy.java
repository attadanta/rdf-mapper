package eu.dareed.rdfmapper.xml;

import eu.dareed.rdfmapper.xml.nodes.SubClassRelation;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
class EplusClassHierarchy {

    protected final EplusClassHierarchyNode rootNode;

    public EplusClassHierarchy() {
        this.rootNode = new EplusClassHierarchyNode(null);
    }

    /**
     * Extends the hierarchy by a single path from an ancestry tree.
     *
     * @param ancestry a list of classes where the first element is the most abstract entity and the last the most concrete.
     */
    protected void extend(LinkedList<EplusClass> ancestry) {
        EplusClassHierarchyNode contextNode = rootNode;
        while (!ancestry.isEmpty()) {
            EplusClass eplusClass = ancestry.pollFirst();
            if (contextNode.hasChild(eplusClass)) {
                contextNode = contextNode.getChild(eplusClass);
            } else {
                contextNode = contextNode.addChild(eplusClass);
            }
        }
    }

    protected List<SubClassRelation> getRelations() {
        List<SubClassRelation> result = new LinkedList<>();

        Deque<EplusClassHierarchyNode> queue = new LinkedList<>();
        for (EplusClassHierarchyNode root : rootNode.getChildren()) {
            queue.add(root);
        }

        while (!queue.isEmpty()) {
            EplusClassHierarchyNode contextNode = queue.pollFirst();

            for (EplusClassHierarchyNode contextNodeChild : contextNode.getChildren()) {
                result.add(new SubClassRelation(contextNode.className, contextNodeChild.className));
                queue.addFirst(contextNodeChild);
            }
        }

        return result;
    }
}
