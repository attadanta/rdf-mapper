package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

public class Item {
	
	private String url;
	
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
	
}
