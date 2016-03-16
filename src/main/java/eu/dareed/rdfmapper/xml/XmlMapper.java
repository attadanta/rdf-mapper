package eu.dareed.rdfmapper.xml;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.model.idd.IDDField;
import eu.dareed.eplus.model.idd.IDDObject;
import eu.dareed.rdfmapper.xml.nodes.*;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Maps an energy plus dictionary to a set of mapping entities.
 *
 * @see eu.dareed.rdfmapper.xml.nodes.Mapping
 */
public class XmlMapper {
    private Mapping mapping;

    protected final Namespace namespace;

    /**
     * Constructor.
     *
     * @param namespaceURL the base namespace url.
     */
    public XmlMapper(String namespaceURL) {
        this(new Namespace(Namespace.defaultNamespacePrefix, namespaceURL));
    }

    /**
     * Default constructor.
     *
     * @param namespace the base namespace.
     */
    public XmlMapper(Namespace namespace) {
        this.namespace = namespace;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public void mapIDDToXMLObjects(IDD idd, Map<String, String> namespaceMap) {
        mapping = new Mapping();
        List<Namespace> namespaceList = mapping.getNamespaces();
        List<Entity> classList = mapping.getEntities();
//        classList.add(new ClassEntity("entity-class", "entity-class"));
        List<SubClassRelation> subRelList = mapping.getTaxonomy();

        // add namespaces to mapping
        namespaceList.add(new Namespace(Namespace.defaultNamespacePrefix, "https://energyplus.net/"));
        for (Entry<String, String> entry : namespaceMap.entrySet()) {
            namespaceList.add(new Namespace(entry.getKey(), entry.getValue()));
        }

        // add classes to mapping
        for (IDDObject iddObj : idd.getAllObjects()) {
            String classURI = buildClassURI(iddObj.getType());
            Entity entClass = new Entity(classURI, classURI);
            entClass.setLabel(classURI);
            List<Property> propertyList = entClass.getProperties();

            // add properties of current class to mapping
            List<IDDField> fields = iddObj.getFields();
            for (int i = 0; i < fields.size(); i++) {
                IDDField field = fields.get(i);

                if (field.isSet("field") && containsKnownProperty(field)) {
                    String propertyDescription = field.getParameter("field").value();
                    String propertyURI = buildPropertyURI(propertyDescription);

                    Property property;
                    if (isDataProperty(field)) {
                        property = new eu.dareed.rdfmapper.xml.nodes.DataProperty(propertyURI, DataProperty.parseDataTypeInField(field).typeURI);
                    } else {
                        property = new ObjectProperty(propertyURI);
                    }
                    property.setLabel(propertyDescription.trim().replace(' ', '_'));
                    property.setIdentifier(i);

                    propertyList.add(property);
                }
            }
            classList.add(entClass);

            // Add taxonomy rule for subclass relation if present
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


    /**
     * Transforms an energy plus object designation to a type identifier suitable for urls. This means substituting
     * unsafe characters with safe ones and url-encoding the rest.
     *
     * @param type the energy plus object designation.
     * @return
     */
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

    protected EplusClass processClass(IDDObject object) {
        EplusClass eplusClass = new EplusClass();
        eplusClass.uri = classUrl(object);
        eplusClass.name = className(object);
        eplusClass.label = classLabel(object);
        eplusClass.description = classDescription(object);
        eplusClass.suggestedSuperClasses = suggestedSuperClasses(object);
        return eplusClass;
    }

    protected String classLabel(IDDObject object) {
        return classLabel(object.getType());
    }

    protected String classLabel(String objectType) {
        String[] typeComponents = objectType.split(":");
        String compoundLabel = typeComponents[typeComponents.length - 1];

        return entityLabel(compoundLabel);
    }

    protected String entityLabel(String compoundName) {
        String[] labelComponents = compoundName.split("(?<=[a-z])(?=[A-Z])");
        return StringUtils.join(labelComponents, " ");
    }

    protected String classDescription(IDDObject object) {
        return object.getMemo();
    }

    protected String classUrl(IDDObject object) {
        return classUrl(object.getType());
    }

    protected String classUrl(String objectType) {
        String typeSuffix = objectType.replace(":", "_");
        return namespace.getPrefix() + ":" + typeSuffix;
    }

    protected String className(IDDObject object) {
        return object.getType();
    }

    protected List<EplusClass> suggestedSuperClasses(IDDObject object) {
        String[] types = object.getType().split(":");
        List<EplusClass> result = new ArrayList<>(types.length - 1);

        for (int i = 0; i < types.length - 1; i++) {
            EplusClass superClass = new EplusClass();
            String type = types[i];

            superClass.name = type;
            superClass.label = classLabel(type);
            superClass.uri = classUrl(type);

            result.add(superClass);
        }

        return result;
    }
}
