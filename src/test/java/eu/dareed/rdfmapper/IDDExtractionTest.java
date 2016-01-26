package eu.dareed.rdfmapper;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.parsers.idd.IDDParser;
import eu.dareed.rdfmapper.xml.XmlMapper;
import eu.dareed.rdfmapper.xml.nodes.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class IDDExtractionTest {
    private static Mapping mapping;

    @BeforeClass
    public static void setup() throws IOException {
        InputStream resource = IDDExtractionTest.class.getResourceAsStream("/fixtures/data_dictionary.idd");
        IDD idd = new IDDParser().parseFile(resource);
        XmlMapper mapper = new XmlMapper();
        mapper.mapIDDToXMLObjects(idd, Collections.<String, String>emptyMap());
        mapping = mapper.getMapping();
    }

    @Test
    public void testInitialized() {
        Assert.assertNotNull(mapping);
    }

    @Test
    public void testNumberOfEntities() {
        Assert.assertEquals(2, mapping.getEntities().size());
    }

    @Test
    public void testLocationProperties() {
        Entity entity = mapping.getEntities().get(0);
        Property name = entity.getProperties().get(0);
        Assert.assertEquals(PropertyType.DATA_PROPERTY, name.getPropertyType());
        Assert.assertEquals("Name", name.getLabel());
        Assert.assertEquals(0, name.getIdentifier());
        Assert.assertEquals("http://www.w3.org/2002/07/owl#DataProperty", ((DataProperty) name).getType());
    }
}
