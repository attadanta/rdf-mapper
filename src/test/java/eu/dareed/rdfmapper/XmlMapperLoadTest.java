package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.XmlMapper;
import eu.dareed.rdfmapper.xml.nodes.ClassEntity;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class XmlMapperLoadTest {

    @Test
    public void xmlImport() throws URISyntaxException, JAXBException {
        File xmlMap = Paths.get(getClass().getResource("/dareed_entity_map3.xml").toURI()).toFile();

        XmlMapper mapper = new XmlMapper();
        mapper.loadXML(xmlMap);
        for (ClassEntity ontClass : mapper.getEntityMap().getClassMap().getClassList()) {
            System.out.println(ontClass.getURL());
        }
    }
}
