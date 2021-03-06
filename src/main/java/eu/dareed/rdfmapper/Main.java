package eu.dareed.rdfmapper;

import eu.dareed.eplus.model.idf.IDF;
import eu.dareed.eplus.parsers.idf.IDFParser;
import eu.dareed.rdfmapper.energyplus.mapping.IDFMappingData;
import eu.dareed.rdfmapper.rdf.RDFMapper;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
import org.apache.jena.rdf.model.Model;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Usage: Main [map.xml] [input.idf] [out.ttl]
 *
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Main {
    public static void main(String[] args) throws JAXBException, IOException {
        File xmlInput = new File(args[0]);
        Mapping mapping = new MappingIO().loadXML(xmlInput);

        IDF idf = new IDFParser().parseFile(new FileInputStream(new File(args[1])));

        MappingData mappingData = new IDFMappingData(idf);
        Model model = new RDFMapper().mapToRDF(mappingData, mapping);
        FileOutputStream outputStream = new FileOutputStream(args[2]);
        model.write(outputStream, "TTL", null);
        outputStream.flush();
        outputStream.close();
    }
}
