package eu.dareed.rdfmapper.xml.verification;

import eu.dareed.rdfmapper.MappingIO;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class MappingVerifier {
    protected final MappingIO io;
    protected final Profile profile;

    /**
     * Initializes a verifier with the default {@link Profile}.
     *
     * @return a verifier prepared with the default {@link Profile}.
     */
    public static MappingVerifier initialize() {
        return new MappingVerifier(Profile.defaultProfile());
    }

    public MappingVerifier(Profile profile) {
        this.io = new MappingIO();
        this.profile = profile;
    }

    public ParseAttempt tryParse(InputStream documentSource) throws IOException {
        return tryParse(IOUtils.toString(documentSource));
    }

    public ParseAttempt tryParse(Reader documentSource) throws IOException {
        return tryParse(IOUtils.toString(documentSource));
    }

    public ParseAttempt tryParse(String document) throws IOException {
        List<Offense> errors = new ArrayList<>();
        try (Reader validatingReader = new StringReader(document)) {
            errors.addAll(profile.validate(validatingReader));
        }

        if (errors.isEmpty()) {
            List<Offense> warnings = new ArrayList<>();
            Mapping mapping = null;

            try (Reader verifyingReader = new StringReader(document)) {
                mapping = io.loadXML(verifyingReader);
            } catch (JAXBException e) {
                errors.add(new Offense(Grade.ERROR, e.getMessage()));
            }

            if (mapping != null) {
                for (Offense offense : profile.verify(mapping)) {
                    if (offense.grade == Grade.WARNING) {
                        warnings.add(offense);
                    } else if (offense.grade == Grade.ERROR) {
                        errors.add(offense);
                    } else {
                        throw new UnsupportedOperationException("Offense type unknown: " + offense.grade.name());
                    }
                }

                return new ParseAttempt(errors, warnings, mapping);
            } else {
                return new ParseAttempt(errors, Collections.<Offense>emptyList(), null);
            }
        } else {
            return new ParseAttempt(errors, Collections.<Offense>emptyList(), null);
        }
    }
}
