package eu.dareed.rdfmapper.rdf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import eu.dareed.eplus.model.idf.IDF;
import eu.dareed.eplus.model.idf.IDFObject;
import eu.dareed.rdfmapper.xml.nodes.ClassEntity;
import eu.dareed.rdfmapper.xml.nodes.ClassProperty;
import eu.dareed.rdfmapper.xml.nodes.EntityMap;

public class RDFMapper {

	Model model;
	
	public RDFMapper(){
		model = ModelFactory.createDefaultModel();
	}


	public Model getModel() {
		return model;
	}


	public void setModel(Model model) {
		this.model = model;
	}


	public void mapIDFToRDF(IDF idf, EntityMap entityMap){
		
		List<ClassEntity> classList = entityMap.getClassMap().getClassList();
		Map<String, Integer> classIndexMap = buildClassIndexMap(classList);
		
		for(IDFObject idfObj : idf.getObjects()){
			// get class entity from entity map
			if(classIndexMap.get(idfObj.getType()) == null){
				System.out.println("Class " + idfObj.getType() + " not found in entity-map.");
				continue;
			}
			ClassEntity clsEnt = classList.get(classIndexMap.get(idfObj.getType()));
			
			// add triples for 'is instance of class xy'
			Resource subject = model.createResource(clsEnt.getURL());
			for(String classURL : clsEnt.getclassURLList()){
//				com.hp.hpl.jena.vocabulary
				subject.addProperty(RDF.type, model.createResource(completeURL(classURL, idfObj)));
			}
			
			// add triples for object properties
			for(ClassProperty classProperty : clsEnt.getPropertyMap().getPropertyList()){
				int fieldIndex = Integer.parseInt(classProperty.getName());
				String objectString = idfObj.getField(fieldIndex).getRawValue();
				
				if(classProperty.getPropertyType().equals("data-property")){
					subject.addProperty(model.getProperty(classProperty.getURL()), objectString);
					
				}else if(classProperty.getPropertyType().equals("object-property")){
					subject.addProperty(model.getProperty(classProperty.getURL()), model.getResource(objectString));
				}
				
			}
		}
	}
	
	
	private Map<String, Integer> buildClassIndexMap(List<ClassEntity> classList) {
		Map<String, Integer> indexMap = new HashMap<String, Integer>();
		for(int i=0; i<classList.size(); i++){
			ClassEntity clsEnt = classList.get(i);
			indexMap.put(clsEnt.getEntityName(), i);
		}
		return indexMap;
	}


	private String completeURL(String url, IDFObject idfObj){
		String[] splittedURL = url.split("$");
		for(int i=1; i<splittedURL.length; i+=2){
			int idx = Integer.parseInt(splittedURL[i]);
			splittedURL[i] = idfObj.getField(idx).getRawValue();
		}
		return splittedURL.toString();
	}
	
}
