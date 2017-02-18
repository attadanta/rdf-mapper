package eu.dareed.rdfmapper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
        return resolveVariables(collectVariableReferences(sequence), sequence);
    }

    public String resolveVariables(List<VariableReference> variableReferences, String sequence) {
        if (!variableReferences.isEmpty()) {
            StringBuilder result = new StringBuilder();

            int lastMatchIndex = 0;
            for (VariableReference variableReference : variableReferences) {
                result.append(sequence.substring(lastMatchIndex, variableReference.beginPos));
                result.append(variableReference.resolved ? variableReference.value : "");
                lastMatchIndex = variableReference.endPos;
            }

            result.append(sequence.substring(lastMatchIndex, sequence.length()));
            return result.toString();
        } else {
            return sequence;
        }
    }

    public List<VariableReference> collectVariableReferences(String sequence) {
        if (pattern.matcher(sequence).find()) {
            List<VariableReference> result = new LinkedList<>();

            Matcher matcher = pattern.matcher(sequence);
            while (matcher.find()) {
                int beginIndex = matcher.start();
                int endIndex = matcher.end();

                String variableName = matcher.group(3) == null ? "" : matcher.group(3);
                String variableIndexCharacterSequence = matcher.group(6) == null ? "" : matcher.group(6);

                VariableReference variableReference;
                String value;

                if (variableName.isEmpty()) {
                    int index = Integer.parseInt(variableIndexCharacterSequence);
                    variableReference = new VariableIndex(beginIndex, endIndex, index);
                    value = resolveIndex(index);
                } else {
                    variableReference = new NamedVariable(beginIndex, endIndex, variableName);
                    value = resolveNamedVariable(variableName);
                }

                variableReference.value = value;
                variableReference.resolved = value != null;

                result.add(variableReference);
            }

            return result;
        } else {
            return Collections.emptyList();
        }
    }
}
