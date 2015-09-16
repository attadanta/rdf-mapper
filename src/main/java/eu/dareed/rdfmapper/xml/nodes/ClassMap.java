package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ClassMap {

	private List<ClassEntity> classList;
	
	public ClassMap() {
		classList = new ArrayList<>();
	}

	@XmlElement(name = "entity-class")
	public List<ClassEntity> getClassList() {
		return classList;
	}

	public void setClassList(List<ClassEntity> classList) {
		this.classList = classList;
	}
	
}
