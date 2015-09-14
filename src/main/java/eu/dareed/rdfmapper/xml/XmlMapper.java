package eu.dareed.rdfmapper.xml;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.model.idd.IDDField;
import eu.dareed.eplus.model.idd.IDDObject;
import eu.dareed.eplus.model.idd.Parameter;
import eu.dareed.rdfmapper.xml.nodes.EntityMap;
import eu.dareed.rdfmapper.xml.nodes.OntClass;
import eu.dareed.rdfmapper.xml.nodes.OntProperty;
import eu.dareed.rdfmapper.xml.nodes.SubClassRelation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class XmlMapper {

    private EntityMap entityMap;

    public EntityMap getEntityMap() {
        return entityMap;
    }

    public void setEntityMap(EntityMap entityMap) {
        this.entityMap = entityMap;
    }

    public void mapIDDToXMLObjects(IDD idd) {
        entityMap = new EntityMap();
        List<OntClass> classList = entityMap.getClassMap().getOntClassList();
        classList.add(new OntClass("ont-class", "ont-class"));
        List<SubClassRelation> subRelList = entityMap.getTaxonomyMap().getSubRelList();

        for (IDDObject iddObj : idd.getAllObjects()) {
            String classURL = buildClassURL(iddObj.getType());
            OntClass ontClass = new OntClass(classURL, "ont-class");
            List<OntProperty> propertyList = ontClass.getPropertyMap().getPropertyList();

            for (IDDField field : iddObj.getFields()) {
                List<Parameter> propList = field.getParameters("field");
                if (propList.size() != 0) {
                    OntProperty ontProperty = new OntProperty(buildPropertyURL(propList.get(0).value()),
                            determinePropertyType(field));
                    ontProperty.setName(field.getName());
                    propertyList.add(ontProperty);
                }
            }
            classList.add(ontClass);

            int relationIndicatorIdx = classURL.indexOf(':');
            if (relationIndicatorIdx > 0) {
                String superURL = buildClassURL(classURL.substring(relationIndicatorIdx + 1));
                subRelList.add(new SubClassRelation(superURL, classURL));
            }
        }
    }

    public void saveXML(File file) throws JAXBException {
        JAXBContext context;
        context = JAXBContext.newInstance(EntityMap.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Marshalling and saving XML to the file.
        m.marshal(entityMap, file);
    }

    public void loadXML(File xmlFile) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(EntityMap.class);
        Unmarshaller um = context.createUnmarshaller();

        // Reading XML from the file and unmarshalling.
        entityMap = (EntityMap) um.unmarshal(xmlFile);
    }

    private String buildClassURL(String type) {
        String className = type.trim().replace(' ', '_').replace(':', '.');
        if (className.contains(".")) {
            int sepIdx = className.lastIndexOf('.');
            return className.substring(sepIdx + 1) + ":"
                    + className.substring(0, sepIdx);
        }
        return className;
    }

    private String buildPropertyURL(String propName) {
        return propName.trim().replace(' ', '_');
    }

    private String determinePropertyType(IDDField field) {
        List<Parameter> typeList = field.getParameters("type");
        if (typeList.size() != 0 && typeList.get(0).value().equals("object-list")) {
            return "object-property";
        }
        return "data-property";
    }
}
