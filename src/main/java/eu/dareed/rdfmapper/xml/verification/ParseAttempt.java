package eu.dareed.rdfmapper.xml.verification;

import eu.dareed.rdfmapper.xml.nodes.Mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the results of the validation and verification steps of a verification {@link Profile}.
 *
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class ParseAttempt {
    private final List<Offense> errors;
    private final List<Offense> warnings;

    protected final Mapping mapping;

    public ParseAttempt(List<Offense> errors, List<Offense> warnings, Mapping mapping) {
        this.errors = errors;
        this.warnings = warnings;
        this.mapping = mapping;
    }

    public boolean containsOffenses() {
        return !(errors.isEmpty() && warnings.isEmpty());
    }

    public boolean containsErrors() {
        return !errors.isEmpty();
    }

    public boolean containsWarnings() {
        return !warnings.isEmpty();
    }

    public boolean parsingSucceeded() {
        return mapping != null;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public List<Offense> allOffenses() {
        List<Offense> allOffenses = new ArrayList<>(errors.size() + warnings.size());

        allOffenses.addAll(errors);
        allOffenses.addAll(warnings);

        return allOffenses;
    }

    public List<Offense> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public List<Offense> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
