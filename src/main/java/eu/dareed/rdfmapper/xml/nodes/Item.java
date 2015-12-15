package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

public class Item {
	
	private String uri;
	private String label;
	
	public Item(){
		
	}
	
	public Item(String uri) {
		setURI(uri);
	}

	@XmlElement(name = "uri")
	public String getURI(){
		return uri;
	}
	
	public void setURI(String uri){
		this.uri = uri;
	}

	@XmlElement(name = "label")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
