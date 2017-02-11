package eu.dareed.rdfmapper.xml;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.model.idd.IDDField;
import eu.dareed.eplus.model.idd.IDDObject;
import eu.dareed.eplus.model.idd.Parameter;
import eu.dareed.rdfmapper.Context;
import eu.dareed.rdfmapper.VariableResolver;
import eu.dareed.rdfmapper.xml.nodes.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Maps an energy plus dictionary to a set of mapping entities.
 *
 * @see eu.dareed.rdfmapper.xml.nodes.Mapping
 */
public class XmlMapper {
    protected final Namespace namespace;

    /**
     * Shorthand constructor.
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

    public Mapping mapIDDToXMLObjects(IDD idd) {
        Mapping mapping = new Mapping();
        List<Namespace> namespaceList = mapping.getNamespaces();

        List<Entity> classList = mapping.getEntities();
        EplusClassHierarchy hierarchy = new EplusClassHierarchy();

        // add namespaces to mapping
        namespaceList.add(namespace);

        Set<EplusClass> processedClasses = new HashSet<>(idd.getAllObjects().size() * 2);
        // add classes to mapping
        for (IDDObject iddObj : idd.getAllObjects()) {
            EplusClass eplusClass = processClass(iddObj);

            processedClasses.add(eplusClass);
            hierarchy.extend(eplusClass.getAncestry());

            Entity entClass = eplusClass.toEntity();
            List<Property> propertyList = entClass.getProperties();

            // add properties of current class to mapping
            List<IDDField> fields = iddObj.getFields();
            for (int i = 0; i < fields.size(); i++) {
                IDDField field = fields.get(i);

                if (field.isSet("field") && containsKnownProperty(field)) {
                    EplusProperty eplusProperty = processProperty(field);

                    Property property;
                    if (isDataProperty(field)) {
                        property = new eu.dareed.rdfmapper.xml.nodes.DataProperty(eplusProperty.uri, DataProperty.parseDataTypeInField(field).typeURI, String.format("%s{%d}", Context.indexVariable, i));
                    } else {
                        property = new ObjectProperty(eplusProperty.uri, namespace.getPrefix() + ":");
                    }
                    property.setLabel(eplusProperty.label);
                    property.setDescription(eplusProperty.description);

                    propertyList.add(property);
                }
            }
            classList.add(entClass);
        }

        for (EplusClass eplusClass : hierarchy.traverseHierarchy()) {
            if (!processedClasses.contains(eplusClass)) {
                classList.add(eplusClass.toEntity());
            }
        }
        return mapping;
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

    protected String classDescription(IDDObject object) {
        return object.getMemo();
    }

    protected String classUrl(IDDObject object) {
        return classUrl(object.getType());
    }

    protected String classUrl(String objectType) {
        String typeSuffix = objectType.replaceAll(":", "_").replaceAll(" ", "_");
        return namespace.getPrefix() + ":" + typeSuffix;
    }

    protected String className(IDDObject object) {
        return object.getType();
    }

    protected List<EplusClass> suggestedSuperClasses(IDDObject object) {
        return suggestedSuperClasses(object.getType());
    }

    protected List<EplusClass> suggestedSuperClasses(String compoundTypeLabel) {
        List<String> types = Arrays.asList(compoundTypeLabel.split(":"));
        List<EplusClass> result = new ArrayList<>(types.size() - 1);

        for (int i = 0; i < types.size() - 1; i++) {
            EplusClass superClass = new EplusClass();
            String type = types.get(i);
            String typeName = StringUtils.join(types.subList(0, i + 1), ":");

            superClass.name = typeName;
            superClass.label = classLabel(type);
            superClass.uri = classUrl(typeName);

            result.add(superClass);
        }

        return result;
    }

    protected EplusProperty processProperty(IDDField field) {
        EplusProperty property = new EplusProperty();

        String propertyName = field.getParameter("field").value();
        String uriSuffix = propertyName.replaceAll(" ", "_");

        property.uri = namespace.getPrefix() + ":" + uriSuffix;
        property.label = propertyName;

        List<Parameter> parameters = field.getParameters("note");
        List<String> notes = new ArrayList<>(parameters.size());

        for (Parameter parameter : parameters) {
            notes.add(parameter.value());
        }

        property.description = StringUtils.join(notes, "; ");

        return property;
    }

    protected String entityLabel(String compoundName) {
        String[] labelComponents = compoundName.split("(?<=[a-z])(?=[A-Z])");
        return StringUtils.join(labelComponents, " ");
    }
}
