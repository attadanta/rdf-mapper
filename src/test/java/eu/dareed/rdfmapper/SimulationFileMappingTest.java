package eu.dareed.rdfmapper;

import com.hp.hpl.jena.rdf.model.Model;
import eu.dareed.eplus.model.idf.IDF;
import eu.dareed.eplus.parsers.idf.IDFParser;
import eu.dareed.rdfmapper.energyplus.mapping.IDFMappingData;
import eu.dareed.rdfmapper.rdf.RDFMapper;
import eu.dareed.rdfmapper.xml.nodes.Entity;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class SimulationFileMappingTest {

    private static Mapping mapping;
    private static MappingData mappingData;

    @BeforeClass
    public static void setUp() throws URISyntaxException, JAXBException, IOException {
        File xmlInput = Paths.get(SimulationFileMappingTest.class.getResource("/fixtures/simulation_map.xml").toURI()).toFile();
        mapping = new MappingIO().loadXML(xmlInput);

        IDF idf = new IDFParser().parseFile(SimulationFileMappingTest.class.getResourceAsStream("/fixtures/simulation.idf"));
        mappingData = new IDFMappingData(idf);
    }

    @Test
    public void testMapping() {
        List<Entity> entities = mapping.getEntities();
        for (int i = 0; i < entities.size(); i++) {
            Assert.assertNotNull("Entity " + (i + 1) + " has no name assigned.", entities.get(i).getName());
            Assert.assertNotNull("No uri for entity `" + entities.get(i).getName() + "'.", entities.get(i).getUri());
            Assert.assertFalse("No declared types for entity `" + entities.get(i).getName() + "'.", entities.get(i).getTypes().isEmpty());
        }
        Assert.assertEquals(13, entities.size());
    }

    @Test
    public void testPrintOut() throws Exception {
        Model model = new RDFMapper().mapToRDF(mappingData, mapping);
        model.write(System.out, "TTL", null);
    }
}
