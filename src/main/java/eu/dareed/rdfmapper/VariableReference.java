package eu.dareed.rdfmapper;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public abstract class VariableReference {
    final int beginPos;
    final int endPos;

    public boolean resolved;
    public String value;

    VariableReference(int beginPos, int endPos) {
        this.beginPos = beginPos;
        this.endPos = endPos;
        this.resolved = false;
    }

    abstract String resolve(VariableResolver resolver);
}
