package eu.dareed.rdfmapper.xml;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.model.idd.IDDField;
import eu.dareed.eplus.model.idd.IDDObject;
import eu.dareed.rdfmapper.xml.nodes.ClassEntity;
import eu.dareed.rdfmapper.xml.nodes.ClassProperty;
import eu.dareed.rdfmapper.xml.nodes.EntityMap;
import eu.dareed.rdfmapper.xml.nodes.SubClassRelation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

            ClassEntity entClass = new ClassEntity(classURL, classURL);
            entClass.setLabel(classURL);
            entClass.addTypeURL("entity-class");
            List<ClassProperty> propertyList = entClass.getPropertyMap().getPropertyList();

            for (IDDField field : iddObj.getFields()) {
                if (field.isSet("field") && containsKnownProperty(field)) {
                    String propertyDescription = field.getParameter("field").value();

                    ClassProperty entProperty = new ClassProperty(buildPropertyURL(propertyDescription));
                    entProperty.setLabel(propertyDescription.trim());
                    entProperty.setPropertyType(getPropertyType(field));
                    entProperty.setIdentifier(fixPropertyName(field.getName()));
                    if (isDataProperty(field)) {
                        entProperty.setDataType(DataProperty.parseDataTypeInField(field).typeURL);
                    }

                    propertyList.add(entProperty);
                }
            }
            classList.add(entClass);

            int relationIndicatorIdx = classURL.indexOf("--");
            if (relationIndicatorIdx > 0) {
                String superURL = buildClassURL(classURL.substring(relationIndicatorIdx + 2));
                subRelList.add(new SubClassRelation(superURL, classURL));
            }
        }
    }


    public void saveXML(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(EntityMap.class);
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
            return className.substring(sepIdx + 1) + "--"
                    + className.substring(0, sepIdx);
        }
        return className;
    }


    private String buildPropertyURL(String propName) {
//        propName = propName.replaceAll("%", "percent");
//        propName = propName.replaceAll("#", "no.");
//        propName = propName.replaceAll(": ", "--");
//        propName = propName.replaceAll(":", "--");
//        propName = propName.replaceAll(",", "");
//        propName = propName.replaceAll("/", "_");
        propName = propName.trim().replace(' ', '_');
        try {
            propName = URLEncoder.encode(propName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return propName;
    }

    /**
     * Determines if a field contains a type declaration which is mappable.
     *
     * @param field the field to check.
     * @return {@code true} if a type parameter is declared in the {@link IDDField}, it is known and mappable, and {@code false} otherwise.
     */
    private boolean containsKnownProperty(IDDField field) {
        if (field.isSet("type")) {
            String type = field.getParameter("type").value().trim();
            try {
                PropertyType.parseTypeParameter(type);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDataProperty(IDDField field) {
        return getPropertyType(field).equals("data-property");
    }

    protected String getPropertyType(IDDField field) {
        String fieldType = field.getParameter("type").value();
        return PropertyType.parseTypeParameter(fieldType).propertyType;
    }

    private String fixPropertyName(String name) {
//		name = name.replaceAll("%", "percent");

        int newLineIdx = name.lastIndexOf("\n");
        if (newLineIdx >= 0) {
            name = name.substring(newLineIdx + 1);
        }

        return name.trim().replace(' ', '_');
    }
}
