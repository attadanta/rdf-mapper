package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PropertyList {

	private List<String> list;
	
	public PropertyList(){
		setList(new ArrayList<String>());
	}

	@XmlElement(name = "property")
	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
}
