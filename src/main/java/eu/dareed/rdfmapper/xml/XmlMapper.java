package eu.dareed.rdfmapper.xml;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.model.idd.IDDField;
import eu.dareed.eplus.model.idd.IDDObject;
import eu.dareed.eplus.model.idd.Parameter;
import eu.dareed.rdfmapper.xml.nodes.EntityMap;
import eu.dareed.rdfmapper.xml.nodes.ClassEntity;
import eu.dareed.rdfmapper.xml.nodes.ClassProperty;
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
        List<ClassEntity> classList = entityMap.getClassMap().getClassList();
        classList.add(new ClassEntity("entity-class", "entity-class"));
        List<SubClassRelation> subRelList = entityMap.getTaxonomyMap().getSubRelList();

        for (IDDObject iddObj : idd.getAllObjects()) {
            String classURL = buildClassURL(iddObj.getType());
            ClassEntity entClass = new ClassEntity(classURL, "entity-class");
            List<ClassProperty> propertyList = entClass.getPropertyMap().getPropertyList();

            for (IDDField field : iddObj.getFields()) {
                List<Parameter> propList = field.getParameters("field");
                if (propList.size() != 0) {
                    ClassProperty entProperty = new ClassProperty(buildPropertyURL(propList.get(0).value()));
                    entProperty.setPropertyType(determineTypes(field, entProperty));
                    entProperty.setName(field.getName());
                    propertyList.add(entProperty);
                }
            }
            classList.add(entClass);

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

    private String determineTypes(IDDField field, ClassProperty entProperty) {
        List<Parameter> typeList = field.getParameters("type");
        if(typeList.size() != 0){
        	String dataType = typeList.get(0).value();
        	if (dataType.equals("object-list")) {
        		entProperty.setDataType("object-url");
        		return "object-property";
        	}
        	entProperty.setDataType(dataType);
        }
        return "data-property";
    }
}
