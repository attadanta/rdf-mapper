package eu.dareed.rdfmapper;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.parsers.idd.IDDParser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class IDDExtractionTest {
    private static IDD idd;

    @BeforeClass
    public static void setup() throws IOException {
        InputStream resource = IDDExtractionTest.class.getResourceAsStream("/Energy+.idd");
        idd = new IDDParser().parseFile(resource);
    }

    @Test
    public void testInitialized() {
        Assert.assertNotNull(idd);
    }
}
