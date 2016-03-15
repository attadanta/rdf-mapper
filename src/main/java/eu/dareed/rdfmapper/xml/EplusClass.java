package eu.dareed.rdfmapper.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper class keeping track of energy plus classes and their metadata.
 *
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
class EplusClass {
    /**
     * The name as it appears in the data dictionary.
     */
    protected String name;

    /**
     * Human-friendly name to display.
     */
    protected String label;

    /**
     * This class' memo.
     */
    protected String description;

    /**
     * The assigned uri.
     */
    protected String uri;

    /**
     * A list of classes suggested as this classes' ancestors.
     */
    protected List<EplusClass> suggestedSuperClasses;

    public EplusClass() {
        this.suggestedSuperClasses = new ArrayList<>();
    }
}
