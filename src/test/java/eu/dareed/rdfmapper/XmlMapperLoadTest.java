package eu.dareed.rdfmapper;

import org.junit.Test;

import eu.dareed.rdfmapper.xml.XmlMapper;
import eu.dareed.rdfmapper.xml.nodes.OntClass;

public class XmlMapperLoadTest {

	@Test
	public void xmlImport(){
		XmlMapper mapper = new XmlMapper();
		mapper.loadXMLFromFile("D:/temp/dareed_entity_map3.xml");
		for(OntClass ontClass : mapper.getEntityMap().getClassMap().getOntClassList()){
			System.out.println(ontClass.getOntURL());
		}
	}
}
