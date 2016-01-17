package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class Entity {
	private String uri;
	private String label;
	private String entityName;
	private List<String> classURIList;

	public Entity(String uri, String entityName) {
		this.uri = uri;
		this.entityName = entityName;
		this.classURIList = new ArrayList<>();
	}

	@XmlElement(name = "name")
	public String getEntityName() {
		return entityName;
	}

	@XmlElement(name = "uri")
	public String getUri() {
		return uri;
	}

	@XmlElement(name = "label")
	public String getLabel() {
		return label;
	}

	@XmlElement(name = "type")
	public List<String> getClassURIList() {
		return classURIList;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void addTypeURI(String typeURL) {
		classURIList.add(typeURL);
	}
}
