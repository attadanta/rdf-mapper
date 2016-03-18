package eu.dareed.rdfmapper.xml.verification;

import eu.dareed.rdfmapper.MappingIO;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class MappingVerifier {

    static final String xsdSchema = "http://www.w3.org/2001/XMLSchema";

    public List<Offense> tryParse(InputStream inputStream) throws IOException {
        List<Offense> offenses = new ArrayList<>();

        InputStream schemaSource = getClass().getResourceAsStream("/mapping.xsd");

        String mappingInput = IOUtils.toString(inputStream);
        try {
            validate(new StringReader(mappingInput), new InputStreamReader(schemaSource), xsdSchema);
        } catch (SAXException e) {
            offenses.add(new Offense(Grade.ERROR, e.getMessage()));
        }

        if (offenses.isEmpty()) {
            MappingIO mappingIO = new MappingIO();
            try {
                mappingIO.loadXML(new StringReader(mappingInput));
            } catch (JAXBException e) {
                offenses.add(new Offense(Grade.ERROR, e.getMessage()));
            }
        }

        return offenses;
    }

    void validate(Reader mappingInput, Reader schemaInput, String schemaLang) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

        Schema schema = factory.newSchema(new StreamSource(schemaInput));
        Validator validator = schema.newValidator();

        validator.validate(new StreamSource(mappingInput));
    }
}
