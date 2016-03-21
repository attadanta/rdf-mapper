package eu.dareed.rdfmapper.xml.verification;

import eu.dareed.rdfmapper.MappingIO;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
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
    private static MappingIO mappingIO;

    @BeforeClass
    public static void setup() {
        verifier = MappingVerifier.initialize();
        mappingIO = new MappingIO();
    }

    @Test
    public void testValidSchema() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/valid_mapping.xml");
        List<Offense> offenses = verifier.tryParse(inputStream);
        inputStream.close();

        Assert.assertTrue(offenses.isEmpty());
    }

    @Test
    public void testValidEmptySchema() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/valid_mapping_empty.xml");
        List<Offense> offenses = verifier.tryParse(inputStream);
        inputStream.close();

        Assert.assertTrue(offenses.isEmpty());
    }

    @Test
    public void testInvalidSchema() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_no_root.xml");
        List<Offense> offenses = verifier.tryParse(inputStream);
        inputStream.close();

        Assert.assertFalse(offenses.isEmpty());
        Assert.assertEquals(Grade.ERROR, offenses.get(0).grade);
    }

    @Test
    public void testInvalidSchemaRootElement() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_wrong_root.xml");
        List<Offense> offenses = verifier.tryParse(inputStream);
        inputStream.close();

        Assert.assertFalse(offenses.isEmpty());
        Assert.assertEquals(Grade.ERROR, offenses.get(0).grade);
    }

    @Test
    public void testInvalidSchemaInnerElement() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_inner_element.xml");
        List<Offense> offenses = verifier.tryParse(inputStream);
        inputStream.close();

        Assert.assertFalse(offenses.isEmpty());
        Assert.assertEquals(Grade.ERROR, offenses.get(0).grade);
    }

    @Test
    public void testUndeclaredSuperType() throws IOException, JAXBException {
        String fixture = IOUtils.toString(MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_undeclared_taxonomy_element.xml"));
        List<Offense> offenses = verifier.tryParse(new StringReader(fixture));
        Assert.assertTrue(offenses.isEmpty());

        Mapping mapping = mappingIO.loadXML(new StringReader(fixture));
        offenses.addAll(verifier.verify(mapping));

        Assert.assertEquals(1, offenses.size());
        Assert.assertEquals(Grade.ERROR, offenses.iterator().next().grade);
    }

    @Test
    public void testUndeclaredNamespace() throws IOException, JAXBException {
        String fixture = IOUtils.toString(MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/valid_mapping_undeclared_namespace.xml"));
        List<Offense> offenses = verifier.tryParse(new StringReader(fixture));
        Assert.assertTrue(offenses.isEmpty());

        Mapping mapping = mappingIO.loadXML(new StringReader(fixture));
        offenses.addAll(verifier.verify(mapping));

        Assert.assertEquals(4, offenses.size());
        for (Offense offense : offenses) {
            Assert.assertEquals(Grade.WARNING, offense.grade);
        }
    }

    @Test
    public void testUndeclaredDefaultNamespace() throws IOException, JAXBException {
        String fixture = IOUtils.toString(MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/valid_mapping_undeclared_default_namespace.xml"));
        List<Offense> offenses = verifier.tryParse(new StringReader(fixture));
        Assert.assertTrue(offenses.isEmpty());

        Mapping mapping = mappingIO.loadXML(new StringReader(fixture));
        offenses.addAll(verifier.verify(mapping));

        Assert.assertEquals(1, offenses.size());
        Assert.assertEquals(Grade.WARNING, offenses.iterator().next().grade);
    }
}
