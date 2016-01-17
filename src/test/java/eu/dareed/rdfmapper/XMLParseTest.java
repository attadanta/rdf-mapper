package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.XmlMapper;
import eu.dareed.rdfmapper.xml.nodes.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class XMLParseTest {
    private static XmlMapper mapper;

    @BeforeClass
    public static void setup() throws JAXBException, URISyntaxException {
        File xmlMap = Paths.get(XMLParseTest.class.getResource("/fixtures/idd_map.xml").toURI()).toFile();
        mapper = new XmlMapper();
        mapper.loadXML(xmlMap);
    }

    @Test
    public void testNamespaces() {
        Assert.assertEquals(1, mapper.getMapping().getNamespaces().size());
    }

    @Test
    public void testNamespaceProperties() {
        Namespace ns = mapper.getMapping().getNamespaces().get(0);
        Assert.assertEquals("defaultns", ns.getPrefix());
        Assert.assertEquals("https://energyplus.net/", ns.getUri());
    }

    @Test
    public void testEntities() {
        Assert.assertEquals(2, mapper.getMapping().getEntities().size());
    }

    @Test
    public void testEntityProperties() {
        Entity entity = mapper.getMapping().getEntities().get(0);

        Assert.assertEquals("Gebaeude", entity.getLabel());
        Assert.assertEquals("http://dareed.eu/$0$", entity.getUri());
        Assert.assertEquals("Building", entity.getName());
        Assert.assertEquals(1, entity.getTypes().size());
        Assert.assertEquals("http://dareed.eu/Building", entity.getTypes().get(0));
        Assert.assertEquals(2, entity.getProperties().size());
    }

    @Test
    public void testObjectProperty() {
        Property property = mapper.getMapping().getEntities().get(0).getProperties().get(0);
        Assert.assertTrue("Expected an object property", property instanceof ObjectProperty);

        ObjectProperty objectProperty = (ObjectProperty) property;
        Assert.assertEquals(5, objectProperty.getIdentifier());
        Assert.assertEquals("http://dareed.eu/shadowing", objectProperty.getUri());
        Assert.assertEquals("http://dareed.eu/$2$", objectProperty.getObject());
    }

    @Test
    public void testDataProperty() {
        Property property = mapper.getMapping().getEntities().get(0).getProperties().get(1);
        Assert.assertTrue("Expected a data property.", property instanceof DataProperty);

        DataProperty dataProperty = (DataProperty) property;
        Assert.assertEquals(6, dataProperty.getIdentifier());
        Assert.assertEquals("http://dareed.eu/power", dataProperty.getUri());
        Assert.assertEquals("xsd:int", dataProperty.getType());
    }

    @Test
    public void testTaxonomyExistence() {
        Assert.assertEquals(1, mapper.getMapping().getTaxonomy().size());
    }

    @Test
    public void testTaxonomyRelations() {
        SubClassRelation relation = mapper.getMapping().getTaxonomy().get(0);
        Assert.assertEquals("Building", relation.getSuperClass());
        Assert.assertEquals("OfficeBuilding", relation.getSubClass());
    }
}
