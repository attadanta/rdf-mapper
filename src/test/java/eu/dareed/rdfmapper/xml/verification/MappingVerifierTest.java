package eu.dareed.rdfmapper.xml.verification;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class MappingVerifierTest {
    private static MappingVerifier verifier;

    @BeforeClass
    public static void setup() {
        verifier = MappingVerifier.initialize();
    }

    @Test
    public void testValidSchema() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/valid_mapping.xml");
        List<Offense> offenses = verifier.tryParse(inputStream).allOffenses();
        inputStream.close();

        Assert.assertTrue(offenses.isEmpty());
    }

    @Test
    public void testValidEmptySchema() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/valid_mapping_empty.xml");
        ParseAttempt parseAttempt = verifier.tryParse(inputStream);
        inputStream.close();

        Assert.assertTrue(parseAttempt.getErrors().isEmpty());
    }

    @Test
    public void testInvalidSchema() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_no_root.xml");
        List<Offense> offenses = verifier.tryParse(inputStream).allOffenses();
        inputStream.close();

        Assert.assertFalse(offenses.isEmpty());
        Assert.assertEquals(Grade.ERROR, offenses.get(0).grade);
    }

    @Test
    public void testInvalidSchemaRootElement() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_wrong_root.xml");
        List<Offense> offenses = verifier.tryParse(inputStream).allOffenses();
        inputStream.close();

        Assert.assertFalse(offenses.isEmpty());
        Assert.assertEquals(Grade.ERROR, offenses.get(0).grade);
    }

    @Test
    public void testInvalidSchemaInnerElement() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_inner_element.xml");
        List<Offense> offenses = verifier.tryParse(inputStream).allOffenses();
        inputStream.close();

        Assert.assertFalse(offenses.isEmpty());
        Assert.assertEquals(Grade.ERROR, offenses.get(0).grade);
    }

    @Test
    public void testUndeclaredSuperType() throws IOException, JAXBException {
        String fixture = IOUtils.toString(MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_undeclared_taxonomy_element.xml"));
        ParseAttempt parseAttempt = verifier.tryParse(new StringReader(fixture));

        Assert.assertTrue(parseAttempt.parsingSucceeded());
        Assert.assertTrue(parseAttempt.containsErrors());
        Assert.assertFalse(parseAttempt.containsWarnings());

        List<Offense> errors = parseAttempt.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals(Grade.ERROR, errors.iterator().next().grade);
    }

    @Test
    public void testUndeclaredNamespace() throws IOException, JAXBException {
        String fixture = IOUtils.toString(MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/valid_mapping_undeclared_namespace.xml"));
        ParseAttempt parseAttempt = verifier.tryParse(new StringReader(fixture));

        Assert.assertTrue(parseAttempt.getErrors().isEmpty());

        List<Offense> offenses = parseAttempt.allOffenses();
        Assert.assertEquals(4, offenses.size());
        for (Offense offense : offenses) {
            Assert.assertEquals(Grade.WARNING, offense.grade);
        }
    }

    @Test
    public void testUndeclaredDefaultNamespace() throws IOException, JAXBException {
        String fixture = IOUtils.toString(MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/valid_mapping_undeclared_default_namespace.xml"));
        ParseAttempt parseAttempt = verifier.tryParse(new StringReader(fixture));

        Assert.assertTrue(parseAttempt.getErrors().isEmpty());

        List<Offense> offenses = parseAttempt.allOffenses();
        Assert.assertEquals(1, offenses.size());
        Assert.assertEquals(Grade.WARNING, offenses.iterator().next().grade);
    }
}
