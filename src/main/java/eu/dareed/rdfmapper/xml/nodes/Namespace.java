package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

public class Namespace extends Item {

	private String testAttribute;
	
	private Namespace(){
		super(null);
	}
	
	
	public Namespace(String uri) {
		super(uri);
	}


	@XmlElement(name = "test-attribute")
	public String getTestAttribute() {
		return testAttribute;
	}


	public void setTestAttribute(String testAttribute) {
		this.testAttribute = testAttribute;
	}

}
