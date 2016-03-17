package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.nodes.Mapping;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class MappingIO {

    public void saveXML(Mapping mapping, File out) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Mapping.class);

        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(mapping, out);
    }

    public Mapping loadXML(File xmlFile) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Mapping.class);
        Unmarshaller um = context.createUnmarshaller();
        return (Mapping) um.unmarshal(xmlFile);
    }
}
