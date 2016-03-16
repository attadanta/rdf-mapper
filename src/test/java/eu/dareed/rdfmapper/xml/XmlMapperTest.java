package eu.dareed.rdfmapper.xml;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.model.idd.IDDField;
import eu.dareed.eplus.model.idd.IDDObject;
import eu.dareed.eplus.parsers.idd.IDDParser;
import eu.dareed.rdfmapper.xml.nodes.Namespace;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class XmlMapperTest {
    private static IDD idd;
    private static Namespace namespace;
    private static XmlMapper xmlMapper;

    @BeforeClass
    public static void setup() throws IOException {
        namespace = new Namespace(Namespace.defaultNamespacePrefix, "http://energyplus.net/");
        xmlMapper = new XmlMapper(namespace);

        InputStream resource = XmlMapperTest.class.getResourceAsStream("/fixtures/data_dictionary.idd");
        idd = new IDDParser().parseFile(resource);
    }

    @Test
    public void testProcessLocation() {
        IDDObject iddObject = idd.getAllObjects().get(0);

        EplusClass eplusClass = xmlMapper.processClass(iddObject);
        Assert.assertEquals(iddObject.getMemo(), eplusClass.description);
        Assert.assertEquals(iddObject.getType(), eplusClass.name);
        Assert.assertEquals(namespace.getPrefix() + ":Site_Location", eplusClass.uri);
        Assert.assertEquals("Location", eplusClass.label);

        List<EplusClass> suggestedSuperClasses = eplusClass.suggestedSuperClasses;
        Assert.assertEquals(1, suggestedSuperClasses.size());
        Assert.assertEquals("Site", suggestedSuperClasses.get(0).label);
        Assert.assertEquals(namespace.getPrefix() + ":Site", suggestedSuperClasses.get(0).uri);
    }

    @Test
    public void testLocationProperties() {
        IDDField iddField = idd.getAllObjects().get(0).getFields().get(3);
        EplusProperty property = xmlMapper.processProperty(iddField);

        Assert.assertEquals("Time Zone", property.label);
        Assert.assertEquals(namespace.getPrefix() + ":Time_Zone", property.uri);
        Assert.assertEquals("basic these limits on the WorldTimeZone Map (2003);  Time relative to GMT. Decimal hours.", property.description);
    }

    @Test
    public void testDesignDay() {
        IDDObject iddObject = idd.getAllObjects().get(1);

        EplusClass eplusClass = xmlMapper.processClass(iddObject);
        Assert.assertEquals("Design Day", eplusClass.label);

        List<EplusClass> suggestedSuperClasses = eplusClass.suggestedSuperClasses;
        Assert.assertEquals(1, suggestedSuperClasses.size());
        Assert.assertEquals("Sizing Period", suggestedSuperClasses.get(0).label);
    }
}
