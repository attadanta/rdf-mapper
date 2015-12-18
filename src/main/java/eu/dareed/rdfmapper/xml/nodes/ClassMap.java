package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class ClassMap {

	private List<ClassEntity> classList;
	
	public ClassMap() {
		classList = new ArrayList<>();
	}

	
	@XmlElement(name = "class-entity")
	public List<ClassEntity> getClassList() {
		return classList;
	}

	
	public void setClassList(List<ClassEntity> classList) {
		this.classList = classList;
	}
	
}
