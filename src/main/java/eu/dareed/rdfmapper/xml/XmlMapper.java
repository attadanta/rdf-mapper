package eu.dareed.rdfmapper.xml;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.model.idd.IDDField;
import eu.dareed.eplus.model.idd.IDDObject;
import eu.dareed.rdfmapper.xml.nodes.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class XmlMapper {
    private Mapping mapping;

    public Mapping getMapping() {
        return mapping;
    }


    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }


    public void mapIDDToXMLObjects(IDD idd, Map<String, String> namespaceMap) {
        mapping = new Mapping();
        List<Namespace> namespaceList = mapping.getNamespaceMap().getNamespaceList();
        List<ClassEntity> classList = mapping.getClassMap().getClassList();
//        classList.add(new ClassEntity("entity-class", "entity-class"));
        List<SubClassRelation> subRelList = mapping.getTaxonomyMap().getSubRelList();

        // add namespaces to mapping
		namespaceList.add(new Namespace(Namespace.defaultNamespacePrefix, "https://energyplus.net/"));
        for (Entry<String, String> entry : namespaceMap.entrySet()) {
        	namespaceList.add(new Namespace(entry.getKey(), entry.getValue()));
        }

        // add classes to mapping
        for (IDDObject iddObj : idd.getAllObjects()) {
            String classURI = buildClassURI(iddObj.getType());
            ClassEntity entClass = new ClassEntity(classURI, classURI);
            entClass.setLabel(classURI);
//            entClass.getclassURLList().add("entity-class");
            List<ClassProperty> propertyList = entClass.getPropertyMap().getPropertyList();

            // add properties of current class to mapping
            for (IDDField field : iddObj.getFields()) {
                if (field.isSet("field") && containsKnownProperty(field)) {
                    String propertyDescription = field.getParameter("field").value();

                    ClassProperty entProperty = new ClassProperty(buildPropertyURI(propertyDescription));
                    entProperty.setLabel(propertyDescription.trim().replace(' ', '_'));
                    entProperty.setPropertyType(getPropertyType(field));
                    entProperty.setIdentifier(getIdentifier(fixPropertyName(field.getName())));
                    if (isDataProperty(field)) {
                        entProperty.setDataType(DataProperty.parseDataTypeInField(field).typeURI);
                    }

                    propertyList.add(entProperty);
                }
            }
            classList.add(entClass);

            // Add taxonomy rule for sublass relation if present
            int relationIndicatorIdx = classURI.indexOf("--");
            if (relationIndicatorIdx > 0) {
                String superURI = buildClassURI(classURI.substring(relationIndicatorIdx + 2));
                subRelList.add(new SubClassRelation(superURI, classURI));
            }
        }
    }


    public void saveXML(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Mapping.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Marshalling and saving XML to the file.
        m.marshal(mapping, file);
    }


    public void loadXML(File xmlFile) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Mapping.class);
        Unmarshaller um = context.createUnmarshaller();

        // Reading XML from the file and unmarshalling.
        mapping = (Mapping) um.unmarshal(xmlFile);
    }


    private String buildClassURI(String type) {
        String className = type.trim().replace(' ', '_').replace(':', '.');
        if (className.contains(".")) {
            int sepIdx = className.lastIndexOf('.');
            return className.substring(sepIdx + 1) + "--"
                    + className.substring(0, sepIdx);
        }
        return className;
    }


    private String buildPropertyURI(String propName) {
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


	private int getIdentifier(String fixPropertyName) {
		try{
			return Integer.parseInt(fixPropertyName);
		} catch (NumberFormatException e){
            throw new RuntimeException("Expected an integer: `" + fixPropertyName + "'");
        }
	}
}
