package eu.dareed.rdfmapper.xml.nodes;

public class OntObject extends OntEntity{
	
	// to prevent instantiation without url
	private OntObject(){
		super(null, null);
	}
	
	public OntObject(String url, String classURL) {
		super(url, classURL);
		this.propertyMap = new PropertyMap();
	}
	
}
