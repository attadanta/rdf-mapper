package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"superClass", "subClass"})
public class SubClassRelation {
	private String superClass;
	private String subClass;
	
	
	public SubClassRelation(){
		
	}
	
	
	public SubClassRelation(String superClass, String subClass) {
		this.superClass = superClass;
		this.subClass = subClass;
	}
	
	
	@XmlElement(name = "super-class")
	public String getSuperClass() {
		return superClass;
	}
	
	
	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}
	
	
	@XmlElement(name = "sub-class")
	public String getSubClass() {
		return subClass;
	}
	
	
	public void setSubClass(String subClass) {
		this.subClass = subClass;
	}
}
