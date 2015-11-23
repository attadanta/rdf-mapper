package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class PropertyMap {
    private List<ClassProperty> propertyList;

    public PropertyMap() {
        propertyList = new ArrayList<>();
    }

    @XmlElement(name = "class-property")
    public List<ClassProperty> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<ClassProperty> propertyList) {
        this.propertyList = propertyList;
    }
}
