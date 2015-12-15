package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class NamespaceMap {

	private List<Item> namespaceList;
	
	public NamespaceMap() {
		namespaceList = new ArrayList<>();
	}
	
	
	@XmlElement(name = "namespace")
	public List<Item> getNamespaceList() {
		return namespaceList;
	}

	
	public void setNamespaceList(List<Item> namespaceList) {
		this.namespaceList = namespaceList;
	}
}
