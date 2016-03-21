package eu.dareed.rdfmapper.xml.verification;

import eu.dareed.rdfmapper.MappingIO;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class MappingVerifier {
    private static final String xsdSchema = "http://www.w3.org/2001/XMLSchema";

    protected final Validator validator;

    protected final MappingIO io;
    protected final List<Check> checks;

    public static MappingVerifier initialize() {
        try (InputStream schema = MappingVerifier.class.getResourceAsStream("/mapping.xsd")) {
            return initialize(schema);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read mapping schema.", e);
        } catch (SAXException e) {
            throw new RuntimeException("Couldn't parse mapping schema.", e);
        }
    }

    protected static MappingVerifier initialize(InputStream schemaSource) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(xsdSchema);

        Schema schema = factory.newSchema(new StreamSource(schemaSource));
        Validator validator = schema.newValidator();
        return new MappingVerifier(validator);
    }

    protected MappingVerifier(Validator validator) {
        this.validator = validator;

        this.io = new MappingIO();
        this.checks = new ArrayList<>();
        this.checks.add(new TypeNamesInTaxonomy());
        this.checks.add(new DefaultNamespaceDeclared());
        this.checks.add(new DeclaredNamespaces());
    }

    public MappingIO getIO() {
        return io;
    }

    public List<Offense> tryParse(InputStream inputStream) throws IOException {
        List<Offense> offenses = new ArrayList<>();

        try {
            validator.validate(new StreamSource(inputStream));
        } catch (SAXException e) {
            offenses.add(new Offense(Grade.ERROR, e.getMessage()));
        }

        return offenses;
    }

    public List<Offense> tryParse(Reader reader) throws IOException {
        List<Offense> offenses = new ArrayList<>();

        try {
            validator.validate(new StreamSource(reader));
        } catch (SAXException e) {
            offenses.add(new Offense(Grade.ERROR, e.getMessage()));
        }

        return offenses;
    }

    public List<Offense> verify(Reader mappingInput) throws JAXBException {
        return verify(io.loadXML(mappingInput));
    }

    public List<Offense> verify(InputStream inputStream) throws JAXBException {
        return verify(io.loadXML(inputStream));
    }

    public List<Offense> verify(Mapping mapping) {
        List<Offense> offenses = new ArrayList<>();
        for (Check check : checks) {
            offenses.addAll(check.verify(mapping));
        }
        return offenses;
    }
}
