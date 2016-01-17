package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class NamespaceMap {

	private List<Namespace> namespaceList;
	
	public NamespaceMap() {
		namespaceList = new ArrayList<>();
	}
	
	
	@XmlElement(name = "ns")
	public List<Namespace> getNamespaceList() {
		return namespaceList;
	}

	
	public void setNamespaceList(List<Namespace> namespaceList) {
		this.namespaceList = namespaceList;
	}
}
