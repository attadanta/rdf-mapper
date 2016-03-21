package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.nodes.Mapping;

import javax.xml.bind.*;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class MappingIO {
    protected final JAXBContext context;
    protected final Marshaller marshaller;
    protected final Unmarshaller unmarshaller;

    public MappingIO() {
        try {
            this.context = JAXBContext.newInstance(Mapping.class);
            this.marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            this.unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveXML(Mapping mapping, File out) throws JAXBException {
        marshaller.marshal(mapping, out);
    }

    public void genSchema(File outFile) throws JAXBException, IOException {
        context.generateSchema(new MappingSchemaOutputResolver(outFile.getParent()));
    }

    public Mapping loadXML(File xmlFile) throws JAXBException, FileNotFoundException {
        return loadXML(new FileInputStream(xmlFile));
    }

    public Mapping loadXML(Reader reader) throws JAXBException {
        return (Mapping) unmarshaller.unmarshal(reader);
    }

    public Mapping loadXML(InputStream inputStream) throws JAXBException {
        return (Mapping) unmarshaller.unmarshal(inputStream);
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
