package eu.dareed.rdfmapper;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class VariableIndex extends VariableReference {
    public final int index;

    public VariableIndex(int beginIndex, int endIndex, int index) {
        super(beginIndex, endIndex);
        this.index = index;
    }

    @Override
    String resolve(VariableResolver resolver) {
        return resolver.resolveIndex(index);
    }

    @Override
    public String toString() {
        return "VariableIndex{" +
                "index=" + index +
                '}';
    }
}
