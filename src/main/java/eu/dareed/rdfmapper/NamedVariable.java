package eu.dareed.rdfmapper;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class NamedVariable extends VariableReference {
    public final String variableName;

    NamedVariable(int beginIndex, int endIndex, String variableName) {
        super(beginIndex, endIndex);
        this.variableName = variableName;
    }

    @Override
    String resolve(VariableResolver resolver) {
        return resolver.resolveNamedVariable(variableName);
    }

    @Override
    public String toString() {
        return "NamedVariable{" +
                "variableName='" + variableName + '\'' +
                '}';
    }
}
