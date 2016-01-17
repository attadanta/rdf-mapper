package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "mapping")
@XmlType(propOrder = {"namespaceMap", "classMap", "taxonomyMap"})
public class Mapping {
    private NamespaceMap namespaceMap;
    private ClassMap classMap;
    private TaxonomyMap taxonomyMap;

    public Mapping() {
    	namespaceMap = new NamespaceMap();
        classMap = new ClassMap();
        taxonomyMap = new TaxonomyMap();
    }


//    @XmlAttribute(name = "xmlns")
//    public String getXmlnsAttribute() {
//		return xmlnsAttribute;
//	}
//
//
//	public void setXmlnsAttribute(String xmlnsAttribute) {
//		this.xmlnsAttribute = xmlnsAttribute;
//	}


	@XmlElement(name = "namespace-map")
    public NamespaceMap getNamespaceMap() {
        return namespaceMap;
    }


    public void setNamespace(NamespaceMap namespaceMap) {
        this.namespaceMap = namespaceMap;
    }


    @XmlElement(name = "class-map")
    public ClassMap getClassMap() {
        return classMap;
    }


    public void setClassMap(ClassMap classMap) {
        this.classMap = classMap;
    }


    @XmlElement(name = "taxonomy-map")
    public TaxonomyMap getTaxonomyMap() {
        return taxonomyMap;
    }


    public void setTaxonomyMap(TaxonomyMap taxonomyMap) {
        this.taxonomyMap = taxonomyMap;
    }


//	@XmlElement(name = "object-map")
//	public ObjectMap getObjectMap() {
//		if(objectMap == null){
//			objectMap = new ObjectMap();
//		}
//		return objectMap;
//	}
//
//	
//	public void setObjectMap(ObjectMap objectMap) {
//		this.objectMap = objectMap;
//	}

}
