package eu.dareed.rdfmapper;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.parsers.idd.IDDParser;
import eu.dareed.rdfmapper.xml.XmlMapper;
import eu.dareed.rdfmapper.xml.nodes.Entity;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
import eu.dareed.rdfmapper.xml.nodes.Property;
import eu.dareed.rdfmapper.xml.nodes.PropertyType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class IDDExtractionTest {
    private static Mapping mapping;

    @BeforeClass
    public static void setup() throws IOException {
        InputStream resource = IDDExtractionTest.class.getResourceAsStream("/fixtures/data_dictionary.idd");
        IDD idd = new IDDParser().parseFile(resource);
        XmlMapper mapper = new XmlMapper("http://energyplus.net/");
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
    public void testEntityNames() {
        List<Entity> entities = mapping.getEntities();
        Assert.assertEquals("Site:Location", entities.get(0).getName());
        Assert.assertEquals("SizingPeriod:DesignDay", entities.get(1).getName());
    }

    @Test
    public void testEntityURIs() {
        List<Entity> entities = mapping.getEntities();
        Assert.assertEquals(":Location", entities.get(0).getUri());
        Assert.assertEquals(":DesignDay", entities.get(1).getUri());
    }

    @Test
    public void testEntityLabels() {
        List<Entity> entities = mapping.getEntities();
        Assert.assertEquals("Location", entities.get(0).getLabel());
        Assert.assertEquals("DesignDay", entities.get(1).getLabel());
    }

    @Test
    public void testLocationProperties() {
        Entity entity = mapping.getEntities().get(0);
        Property name = entity.getProperties().get(0);
        Assert.assertEquals(PropertyType.DATA_PROPERTY, name.getPropertyType());
        Assert.assertEquals("Name", name.getLabel());
        Assert.assertEquals("Name", name.getName());
        Assert.assertEquals("http://energyplus.net/name", name.getUri());
        Assert.assertEquals(0, name.getIdentifier());
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema#string", name.asDataProperty().getType());

        Property lat = entity.getProperties().get(1);
        Assert.assertEquals(PropertyType.DATA_PROPERTY, lat.getPropertyType());
        Assert.assertEquals("Latitude", lat.getName());
        Assert.assertEquals("Latitude", lat.getLabel());
        Assert.assertEquals("http://energyplus.net/latitude", lat.getUri());
        Assert.assertEquals(1, lat.getIdentifier());
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema#double", lat.asDataProperty().getType());
    }

    @Test
    public void testDesignDayProperties() {
        Entity entity = mapping.getEntities().get(1);
        List<Property> properties = entity.getProperties();

        Property name = properties.get(0);
        Assert.assertEquals(PropertyType.DATA_PROPERTY, name.getPropertyType());
        Assert.assertEquals(0, name.getIdentifier());

        Property dayType = properties.get(3);
        Assert.assertEquals(PropertyType.OBJECT_PROPERTY, dayType.getPropertyType());
        Assert.assertEquals(3, dayType.getIdentifier());
    }
}
