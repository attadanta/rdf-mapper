package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.nodes.Mapping;

import javax.xml.bind.*;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class MappingIO {
    public void saveXML(Mapping mapping, File out) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Mapping.class);

        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(mapping, out);
    }

    public void genSchema(File outFile) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(Mapping.class);
        context.generateSchema(new MappingSchemaOutputResolver(outFile.getParent()));
    }

    public Mapping loadXML(File xmlFile) throws JAXBException, FileNotFoundException {
        return loadXML(new FileInputStream(xmlFile));
    }

    public Mapping loadXML(Reader reader) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Mapping.class);
        Unmarshaller um = context.createUnmarshaller();
        return (Mapping) um.unmarshal(reader);
    }

    public Mapping loadXML(InputStream inputStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Mapping.class);
        Unmarshaller um = context.createUnmarshaller();
        return (Mapping) um.unmarshal(inputStream);
    }

    private static final class MappingSchemaOutputResolver extends SchemaOutputResolver {
        final String baseDir;

        MappingSchemaOutputResolver(String baseDir) {
            this.baseDir = baseDir;
        }

        public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
            return new StreamResult(new File(baseDir, suggestedFileName));
        }
    }
}
