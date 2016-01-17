package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.XmlMapper;
import eu.dareed.rdfmapper.xml.nodes.ClassEntity;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class XmlMapperLoadTest {
private static XmlMapper mapper;
    @BeforeClass
    public static void setup() throws JAXBException, URISyntaxException {
        File xmlMap = Paths.get(XmlMapperLoadTest.class.getResource("/idd_map_to_parse.xml").toURI()).toFile();

        mapper = new XmlMapper();
        mapper.loadXML(xmlMap);
    }

    @Test
    public void testNamespaces() {
        Assert.assertEquals(1, mapper.getMapping().getNamespaceMap().getNamespaceList().size());
    }

    @Test
    public void testClasses() {
        Assert.assertEquals(1, mapper.getMapping().getClassMap().getClassList().size());
    }
}
