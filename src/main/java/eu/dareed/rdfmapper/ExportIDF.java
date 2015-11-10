package eu.dareed.rdfmapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import eu.dareed.eplus.model.idf.IDF;
import eu.dareed.eplus.parsers.idf.IDFParser;
import eu.dareed.rdfmapper.rdf.RDFMapper;
import eu.dareed.rdfmapper.xml.XmlMapper;

public class ExportIDF {
    public static void main(String[] args) throws JAXBException, IOException {
        if (args.length < 3) {
            System.err.println("Usage: export-idf [idf] [xml] [path]");
            System.exit(1);
        }
        FileInputStream idfStream = new FileInputStream(new File(args[0]));
        IDF idf = new IDFParser().parseFile(idfStream);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.loadXML(new File(args[1]));
        
        RDFMapper rdfMapper = new RDFMapper();
        rdfMapper.mapIDFToRDF(idf, xmlMapper.getEntityMap());
        
        rdfMapper.getModel().write(new FileOutputStream(args[2]));
    }
}
