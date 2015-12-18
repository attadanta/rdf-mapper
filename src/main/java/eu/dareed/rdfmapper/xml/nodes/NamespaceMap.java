package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class NamespaceMap {

	private List<Namespace> namespaceList;
	
	public NamespaceMap() {
		namespaceList = new ArrayList<>();
	}
	
	
	@XmlElement(name = "namespace")
	public List<Namespace> getNamespaceList() {
		return namespaceList;
	}

	
	public void setNamespaceList(List<Namespace> namespaceList) {
		this.namespaceList = namespaceList;
	}
}
