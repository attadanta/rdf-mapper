package eu.dareed.rdfmapper.xml.verification;

import eu.dareed.rdfmapper.xml.nodes.Mapping;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the sets of criteria against which a mapping is validated and verified.
 *
 * <p>
 *     Validation refers to the conformity of a mapping to an XML schema. All offenses found therein are reported as
 *     errors. Verification refers to the conformity to a set of domain-specific rules. Offenses reported may or may not
 *     be reported as errors.
 * </p>
 *
 * @see Grade
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Profile {
    private static final String xsdSchema = "http://www.w3.org/2001/XMLSchema";

    private final Validator validator;
    private final List<Check> checks;

    public static Profile defaultProfile() {
        Validator validator;
        try (InputStream schema = MappingVerifier.class.getResourceAsStream("/mapping.xsd")) {
            validator = initializeXSDSchema(schema);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read mapping schema.", e);
        } catch (SAXException e) {
            throw new RuntimeException("Couldn't parse mapping schema.", e);
        }
        List<Check> checks = new ArrayList<>();
        checks.add(new TypeNamesInTaxonomy());
        checks.add(new DefaultNamespaceDeclared());
        checks.add(new DeclaredNamespaces());

        return new Profile(validator, checks);
    }

    public List<Offense> verify(Mapping mapping) {
        List<Offense> offenses = new ArrayList<>();

        for (Check check : checks) {
            offenses.addAll(check.verify(mapping));
        }

        return offenses;
    }

    public List<Offense> validate(InputStream inputStream) {
        return validate(new StreamSource(inputStream));
    }

    public List<Offense> validate(Reader reader) {
        return validate(new StreamSource(reader));
    }

    protected List<Offense> validate(StreamSource streamSource) {
        List<Offense> offenses = new ArrayList<>();

        try {
            validator.validate(streamSource);
        } catch (SAXException | IOException e) {
            offenses.add(new Offense(Grade.ERROR, e.getMessage()));
        }

        return offenses;
    }

    public Profile(Validator validator, List<Check> checks) {
        this.validator = validator;
        this.checks = checks == null ? Collections.<Check>emptyList() : checks;
    }

    private static Validator initializeXSDSchema(InputStream schemaSource) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(xsdSchema);

        Schema schema = factory.newSchema(new StreamSource(schemaSource));
        return schema.newValidator();
    }
}
