package eu.dareed.rdfmapper;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class GenSchema {
    public static void main(String[] args) throws JAXBException, IOException {
        new MappingIO().genSchema(new File(args[0]));
    }
}
