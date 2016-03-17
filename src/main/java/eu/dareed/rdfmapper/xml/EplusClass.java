package eu.dareed.rdfmapper.xml;

import eu.dareed.rdfmapper.xml.nodes.Entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A helper class keeping track of energy plus classes and their metadata.
 *
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
class EplusClass extends EplusEntity {
    /**
     * The name as it appears in the data dictionary. Never {@code null}.
     */
    protected String name;

    /**
     * A list of classes suggested as this classes' ancestors.
     */
    protected List<EplusClass> suggestedSuperClasses;

    protected EplusClass(String name) {
        this();
        this.name = name;
    }

    public EplusClass() {
        this.suggestedSuperClasses = Collections.emptyList();
    }

    public Entity toEntity() {
        Entity entity = new Entity(uri, name);

        entity.setLabel(label);
        entity.setDescription(description);

        return entity;
    }

    public LinkedList<EplusClass> getAncestry() {
        LinkedList<EplusClass> ancestors = new LinkedList<>();

        ancestors.addAll(suggestedSuperClasses);
        ancestors.add(this);

        return ancestors;
    }
}
