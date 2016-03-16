package eu.dareed.rdfmapper.xml;

import eu.dareed.rdfmapper.xml.nodes.Entity;

import java.util.Collections;
import java.util.List;

/**
 * A helper class keeping track of energy plus classes and their metadata.
 *
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
class EplusClass {
    /**
     * The name as it appears in the data dictionary. Never {@code null}.
     */
    protected String name;

    /**
     * Human-friendly name to display. Never {@code null}.
     */
    protected String label;

    /**
     * This class' memo. Could be {@code null}.
     */
    protected String description;

    /**
     * The assigned uri. Never {@code null}.
     */
    protected String uri;

    /**
     * A list of classes suggested as this classes' ancestors.
     */
    protected List<EplusClass> suggestedSuperClasses;

    public EplusClass() {
        this.suggestedSuperClasses = Collections.emptyList();
    }

    public Entity toEntity() {
        Entity entity = new Entity(uri, name);

        entity.setLabel(label);
        entity.setDescription(description);

        return entity;
    }
}
