package eu.dareed.rdfmapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Context implements VariableResolver {
    public static final String indexVariable = "#";
    public static final String namedVariable = "$";

    private static final Pattern pattern = Pattern.compile(String.format("((\\%s)\\{(.+?)})|((%s)\\{(\\d+)?})", namedVariable, indexVariable));

    private final Context broader;
    private final VariableResolver variableResolver;

    public Context(Context broader, VariableResolver variableResolver) {
        this.broader = broader;
        this.variableResolver = variableResolver;
    }

    public Context(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
        this.broader = null;
    }

    public Context augment(VariableResolver resolver) {
        return new Context(this, resolver);
    }

    @Override
    public String resolveNamedVariable(String variableName) {
        String currentContextResolution = variableResolver.resolveNamedVariable(variableName);

        if (currentContextResolution == null) {
            return broader != null ? broader.resolveNamedVariable(variableName) : null;
        } else {
            return currentContextResolution;
        }
    }

    @Override
    public String resolveIndex(int index) {
        String currentContextResolution = variableResolver.resolveIndex(index);

        if (currentContextResolution == null) {
            return broader != null ? broader.resolveIndex(index) : null;
        } else {
            return currentContextResolution;
        }
    }

    public String resolveVariables(String sequence) {
        if (pattern.matcher(sequence).find()) {
            StringBuilder result = new StringBuilder();
            Matcher matcher = pattern.matcher(sequence);

            int lastMatchIndex = 0;
            while (matcher.find()) {
                result.append(sequence.subSequence(lastMatchIndex, matcher.start()));

                String variableName = matcher.group(3) == null ? "" : matcher.group(3);
                String variableIndexCharacterSequence = matcher.group(6) == null ? "" : matcher.group(6);

                if (variableName.isEmpty()) {
                    result.append(resolveIndex(Integer.parseInt(variableIndexCharacterSequence)));
                } else {
                    result.append(resolveNamedVariable(variableName));
                }

                lastMatchIndex = matcher.end();
            }

            result.append(sequence.substring(lastMatchIndex, sequence.length()));
            return result.toString();
        } else {
            return sequence;
        }
    }
}
