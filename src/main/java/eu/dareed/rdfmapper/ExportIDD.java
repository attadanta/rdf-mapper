package eu.dareed.rdfmapper;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.parsers.idd.IDDParser;
import eu.dareed.rdfmapper.xml.XmlMapper;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Runs {@link XmlMapper} on an input IDD and saves the results in a file.
 *
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class ExportIDD {
    public static void main(String[] args) throws JAXBException, IOException {
        if (args.length < 2) {
            System.err.println("Usage: export-idd [idd] [path]");
            System.exit(1);
        }
        FileInputStream iddStream = new FileInputStream(new File(args[0]));
        IDD idd = new IDDParser().parseFile(iddStream);

        XmlMapper mapper = new XmlMapper();
        mapper.mapIDDToXMLObjects(idd);
        mapper.saveXML(new File(args[1]));
    }
}
