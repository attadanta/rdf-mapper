package eu.dareed.rdfmapper;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.parsers.idd.IDDParser;
import eu.dareed.rdfmapper.xml.XmlMapper;

public class XmlMapperTest {

	@Test
	public void export() throws IOException{
		InputStream resource = IDDExtractionTest.class.getResourceAsStream("/Energy+.idd");
        IDD idd = new IDDParser().parseFile(resource);
        Assert.assertNotNull(idd);
        
        XmlMapper mapper = new XmlMapper();
        mapper.mapIDDToXMLObjects(idd);
        Assert.assertNotNull(mapper.getEntityMap());
        
        mapper.saveXMLToFile("D:/temp/dareed_entity_map3.xml");
	}
}
