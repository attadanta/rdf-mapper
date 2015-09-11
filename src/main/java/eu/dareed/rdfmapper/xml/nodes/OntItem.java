package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

public class OntItem {
	
	private String ontURL;
	
	public OntItem(String url) {
		setOntURL(url);
	}

	@XmlElement(name = "ont-url")
	public String getOntURL(){
		return ontURL;
	}
	
	public void setOntURL(String ontURL){
		this.ontURL = ontURL;
	}
	
}
