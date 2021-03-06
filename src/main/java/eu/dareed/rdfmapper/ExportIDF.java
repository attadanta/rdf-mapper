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

public class ExportIDF {
    public static void main(String[] args) throws JAXBException, IOException {
        if (args.length < 3) {
            System.err.println("Usage: export-idf [idf] [xml] [path]");
            System.exit(1);
        }

        FileInputStream idfStream = new FileInputStream(new File(args[0]));
        IDF idf = new IDFParser().parseFile(idfStream);

        MappingIO mappingIO = new MappingIO();
        Mapping mapping = mappingIO.loadXML(new File(args[1]));

        RDFMapper rdfMapper = new RDFMapper();

        Model model = rdfMapper.mapToRDF(new IDFMappingData(idf), mapping);
        model.write(new FileOutputStream(args[2]));
    }
}
