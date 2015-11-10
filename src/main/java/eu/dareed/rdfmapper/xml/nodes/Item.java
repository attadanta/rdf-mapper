package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

public class Item {
	
	private String url;
	private String label;
	
	public Item(String url) {
		setURL(url);
	}

	@XmlElement(name = "url")
	public String getURL(){
		return url;
	}
	
	public void setURL(String url){
		this.url = url;
	}

	@XmlElement(name = "label")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
