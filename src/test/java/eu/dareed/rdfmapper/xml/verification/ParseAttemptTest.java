package eu.dareed.rdfmapper.xml.verification;

import eu.dareed.rdfmapper.MappingIO;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.util.Collections;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class ParseAttemptTest {
    @Test
    public void testParseSuccessfully() throws JAXBException {
        Mapping mapping = new MappingIO().loadXML(ParseAttemptTest.class.getResourceAsStream("/fixtures/verification/valid_mapping.xml"));

        ParseAttempt attempt = new ParseAttempt(Collections.<Offense>emptyList(), Collections.<Offense>emptyList(), mapping);

        Assert.assertTrue(attempt.parsingSucceeded());
        Assert.assertFalse(attempt.containsOffenses());
    }
}
