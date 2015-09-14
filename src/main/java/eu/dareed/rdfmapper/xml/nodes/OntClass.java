package eu.dareed.rdfmapper.xml.nodes;

public class OntClass extends OntEntity{

	// to prevent instantiation without url
	private OntClass(){
		super(null, null);
	}
	
	public OntClass(String url, String classURL) {
		super(url, classURL);
		this.propertyMap = new PropertyMap();
	}

}
