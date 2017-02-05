package eu.dareed.rdfmapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Context {
    public static final String indexVariable = "#";
    public static final String namedVariable = "$";

    private static final Pattern pattern = Pattern.compile(String.format("((\\%s)\\{(.+?)})|((%s)\\{(\\d+)?})", namedVariable, indexVariable));

    private final VariableResolver variableResolver;

    public Context(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    String resolveVariables(String sequence) {
        if (pattern.matcher(sequence).find()) {
            StringBuilder result = new StringBuilder();
            Matcher matcher = pattern.matcher(sequence);

            int lastMatchIndex = 0;
            while (matcher.find()) {
                result.append(sequence.subSequence(lastMatchIndex, matcher.start()));

                String variableName = matcher.group(3) == null ? "" : matcher.group(3);
                String variableIndexCharacterSequence = matcher.group(6) == null ? "" : matcher.group(6);

                if (variableName.isEmpty()) {
                    result.append(variableResolver.resolveIndex(Integer.parseInt(variableIndexCharacterSequence)));
                } else {
                    result.append(variableResolver.resolveNamedVariable(variableName));
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
