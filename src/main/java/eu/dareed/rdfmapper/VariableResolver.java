package eu.dareed.rdfmapper;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public interface VariableResolver {
    String resolveNamedVariable(String variableName);

    String resolveIndex(int index);
}
