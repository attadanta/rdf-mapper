package eu.dareed.rdfmapper.xml.verification;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class MappingVerifierTest {
    @Test
    public void testValidSchema() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/valid_mapping_empty.xml");
        MappingVerifier verifier = new MappingVerifier();
        List<Offense> offenses = verifier.tryParse(inputStream);

        Assert.assertTrue(offenses.isEmpty());
    }

    @Test
    public void testValidEmptySchema() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/valid_mapping_empty.xml");
        MappingVerifier verifier = new MappingVerifier();
        List<Offense> offenses = verifier.tryParse(inputStream);

        Assert.assertTrue(offenses.isEmpty());
    }

    @Test
    public void testInvalidSchema() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_no_root.xml");
        MappingVerifier verifier = new MappingVerifier();
        List<Offense> offenses = verifier.tryParse(inputStream);

        Assert.assertFalse(offenses.isEmpty());
        Assert.assertEquals(Grade.ERROR, offenses.get(0).grade);
    }

    @Test
    public void testInvalidSchemaRootElement() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_wrong_root.xml");
        MappingVerifier verifier = new MappingVerifier();
        List<Offense> offenses = verifier.tryParse(inputStream);

        Assert.assertFalse(offenses.isEmpty());
        Assert.assertEquals(Grade.ERROR, offenses.get(0).grade);
    }

    @Test
    public void testInvalidSchemaInnerElement() throws IOException {
        InputStream inputStream = MappingVerifierTest.class.getResourceAsStream("/fixtures/verification/invalid_mapping_inner_element.xml");
        MappingVerifier verifier = new MappingVerifier();
        List<Offense> offenses = verifier.tryParse(inputStream);

        Assert.assertFalse(offenses.isEmpty());
        Assert.assertEquals(Grade.ERROR, offenses.get(0).grade);
    }
}
