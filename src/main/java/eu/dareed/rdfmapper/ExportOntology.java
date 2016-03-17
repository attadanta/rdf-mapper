package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.ontology.OntologyMapper;
import eu.dareed.rdfmapper.xml.nodes.Mapping;

import javax.xml.bind.JAXBException;
import java.io.File;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class ExportOntology {
    public static void main(String[] args) throws JAXBException {
        if (args.length < 2) {
            System.err.println("Usage: mapping.xml out");
            System.exit(1);
        }
        OntologyMapper ontologyMapper = new OntologyMapper();

        Mapping mapping = new MappingIO().loadXML(new File(args[0]));

        ontologyMapper.mapXMLToOntology(mapping);

        ontologyMapper.saveOntologyToFile(args[1]);
    }
}
