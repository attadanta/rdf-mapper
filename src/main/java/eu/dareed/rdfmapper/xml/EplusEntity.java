package eu.dareed.rdfmapper.xml;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
class EplusEntity {
/**
     * Human-friendly name to display. Never {@code null}.
     */
    protected String label;

    /**
     * This entity. Could be {@code null}.
     */
    protected String description;

    /**
     * The assigned uri. Never {@code null}.
     */
    protected String uri;
}
