package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "entity-map")
@XmlType(propOrder = {"namespace", "classMap", "taxonomyMap"})
public class EntityMap {
    private String namespace;
    private ClassMap classMap;
    private TaxonomyMap taxonomyMap;
//	private ObjectMap objectMap;

    public EntityMap() {
        classMap = new ClassMap();
        taxonomyMap = new TaxonomyMap();
    }


    public String getNamespace() {
        return namespace;
    }


    public void setNamespace(String namespace) {
        this.namespace = namespace;
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
