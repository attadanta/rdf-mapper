package eu.dareed.rdfmapper;

import com.thaiopensource.util.PropertyMap;
import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.*;
import com.thaiopensource.validate.rng.SAXSchemaReader;
import com.thaiopensource.xml.sax.ErrorHandlerImpl;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class SchemaTest {

    /**
     * Demonstrates the validator - transformer interaction.
     *
     * @throws SAXException
     * @throws IOException
     * @throws IncorrectSchemaException
     */
    @Test
    public void testValidateWithTransformation() throws SAXException, IOException, IncorrectSchemaException, URISyntaxException, TransformerException {
        //we are using Relax NG compact format
        SchemaReader schemaReader = SAXSchemaReader.getInstance();

        //schema can be reused, it's thread safe
        File rng = Paths.get(SchemaTest.class.getResource("/fixtures/simple.rng").toURI()).toFile();
        Schema schema = schemaReader.createSchema(ValidationDriver.fileInputSource(rng), PropertyMap.EMPTY);

        //can use different error handler here (try DraconianErrorHandler http://www.thaiopensource.com/relaxng/api/jing/com/thaiopensource/xml/sax/DraconianErrorHandler.html)
        ErrorHandler seh = new ErrorHandlerImpl();
        PropertyMapBuilder builder = new PropertyMapBuilder();
        builder.put(ValidateProperty.ERROR_HANDLER, seh);

        //Validator is NOT thread safe
        Validator validator = schema.createValidator(builder.toPropertyMap());

        Source source = new StreamSource(SchemaTest.class.getResourceAsStream("/fixtures/simple.xml"));
        SAXResult result = new SAXResult(validator.getContentHandler());
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.transform(source, result);
    }

    @Test
    public void testValidateWithDriver() throws URISyntaxException, IOException, SAXException {
        ValidationDriver validationDriver = new ValidationDriver();
        File rng = Paths.get(SchemaTest.class.getResource("/fixtures/simple.rng").toURI()).toFile();
        validationDriver.loadSchema(ValidationDriver.fileInputSource(rng));

        File xml = Paths.get(SchemaTest.class.getResource("/fixtures/simple.xml").toURI()).toFile();
        Assert.assertTrue(validationDriver.validate(ValidationDriver.fileInputSource(xml)));
    }

    @Test
    public void testInvalidateWithDriver() throws URISyntaxException, IOException, SAXException {
        ValidationDriver validationDriver = new ValidationDriver();
        File rng = Paths.get(SchemaTest.class.getResource("/fixtures/simple.rng").toURI()).toFile();
        validationDriver.loadSchema(ValidationDriver.fileInputSource(rng));

        File xml = Paths.get(SchemaTest.class.getResource("/fixtures/simple_invalid.xml").toURI()).toFile();
        Assert.assertFalse(validationDriver.validate(ValidationDriver.fileInputSource(xml)));
    }
}
