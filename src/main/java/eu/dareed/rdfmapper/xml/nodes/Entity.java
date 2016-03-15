package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(propOrder = {"label", "description", "uri", "name", "properties", "types"})
public class Entity {
    private String uri;
    private String label;
    private String description;
    private String name;
    private List<Property> properties;
    private List<String> types;

    protected Entity() {
        this.types = new ArrayList<>();
        this.properties = new ArrayList<>();
    }

    public Entity(String uri, String entityName) {
        this();
        this.uri = uri;
        this.name = entityName;
    }

    @XmlElement(name = "name", required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "uri", required = true)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @XmlElement(name = "label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlElement(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElementWrapper(name = "properties")
    @XmlElements({
            @XmlElement(name = "dataProperty", type = DataProperty.class),
            @XmlElement(name = "objectProperty", type = ObjectProperty.class)})
    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @XmlElement(name = "type")
    public List<String> getTypes() {
        return types;
    }

    public void addTypeURI(String typeURL) {
        types.add(typeURL);
    }
}
