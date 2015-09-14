package eu.dareed.rdfmapper.xml.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class TaxonomyMap {
	private List<SubClassRelation> subRelList;
	
	public TaxonomyMap() {
		subRelList = new ArrayList<>();
	}

	@XmlElement(name = "subclass-relation")
	public List<SubClassRelation> getSubRelList() {
		return subRelList;
	}

	public void setSubRelList(List<SubClassRelation> subRelList) {
		this.subRelList = subRelList;
	}
}
