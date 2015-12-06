package eu.dareed.rdfmapper.xml;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.model.idd.IDDField;
import eu.dareed.eplus.model.idd.IDDObject;
import eu.dareed.eplus.model.idd.Parameter;
import eu.dareed.rdfmapper.xml.nodes.EntityMap;
import eu.dareed.rdfmapper.xml.nodes.ClassEntity;
import eu.dareed.rdfmapper.xml.nodes.ClassProperty;
import eu.dareed.rdfmapper.xml.nodes.SubClassRelation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class XmlMapper {

    private EntityMap entityMap;

    public EntityMap getEntityMap() {
        return entityMap;
    }

    
    public void setEntityMap(EntityMap entityMap) {
        this.entityMap = entityMap;
    }

    
    public void mapIDDToXMLObjects(IDD idd) {
        entityMap = new EntityMap();
        List<ClassEntity> classList = entityMap.getClassMap().getClassList();
        classList.add(new ClassEntity("entity-class", "entity-class"));
        List<SubClassRelation> subRelList = entityMap.getTaxonomyMap().getSubRelList();

        for (IDDObject iddObj : idd.getAllObjects()) {
            String classURL = buildClassURL(iddObj.getType());
            ClassEntity entClass = new ClassEntity(classURL, classURL);
            entClass.setLabel(classURL);
            entClass.getclassURLList().add("entity-class");
            List<ClassProperty> propertyList = entClass.getPropertyMap().getPropertyList();

            for (IDDField field : iddObj.getFields()) {
                List<Parameter> propList = field.getParameters("field");
                if (propList.size() != 0) {
                    ClassProperty entProperty = new ClassProperty(buildPropertyURL(propList.get(0).value()));
                    entProperty.setLabel(propList.get(0).value().trim().replace(' ', '_'));
                    entProperty.setPropertyType(determineTypes(field, entProperty)); // also sets datatype (see determineTypes())
                    entProperty.setIdentifier(fixPropertyName(field.getName()));
                    propertyList.add(entProperty);
                }
            }
            classList.add(entClass);

            int relationIndicatorIdx = classURL.indexOf("--");
            if (relationIndicatorIdx > 0) {
                String superURL = buildClassURL(classURL.substring(relationIndicatorIdx + 2));
                subRelList.add(new SubClassRelation(superURL, classURL));
            }
        }
    }

    
    public void saveXML(File file) throws JAXBException {
        JAXBContext context;
        context = JAXBContext.newInstance(EntityMap.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Marshalling and saving XML to the file.
        m.marshal(entityMap, file);
    }

    
    public void loadXML(File xmlFile) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(EntityMap.class);
        Unmarshaller um = context.createUnmarshaller();

        // Reading XML from the file and unmarshalling.
        entityMap = (EntityMap) um.unmarshal(xmlFile);
    }

    
    private String buildClassURL(String type) {
        String className = type.trim().replace(' ', '_').replace(':', '.');
        if (className.contains(".")) {
            int sepIdx = className.lastIndexOf('.');
            return className.substring(sepIdx + 1) + "--"
                    + className.substring(0, sepIdx);
        }
        return className;
    }

    
    private String buildPropertyURL(String propName) {
//        propName = propName.replaceAll("%", "percent");
//        propName = propName.replaceAll("#", "no.");
//        propName = propName.replaceAll(": ", "--");
//        propName = propName.replaceAll(":", "--");
//        propName = propName.replaceAll(",", "");
//        propName = propName.replaceAll("/", "_");
    	propName = propName.trim().replace(' ', '_');
    	try {
			propName = URLEncoder.encode(propName, "UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();
		}
    	return propName;
    }

    
    private String determineTypes(IDDField field, ClassProperty entProperty) {
        List<Parameter> typeList = field.getParameters("type");
        if(typeList.size() != 0){
        	String dataType = typeList.get(0).value().trim();
        	if (dataType.equals("object-list")) {
        		entProperty.setDataType("object-url");
        		return "object-property";
        	}
        	
            if (dataType.equals("integer")) {
            	entProperty.setDataType(OWL2Datatype.XSD_INTEGER.getIRI().toString());
//            	entProperty.setDataType(XSDDatatype.XSDinteger.getURI().toString());
            } else if (dataType.equals("real")) {
            	entProperty.setDataType(OWL2Datatype.XSD_FLOAT.getIRI().toString());
//            	entProperty.setDataType(XSDDatatype.XSDfloat.getURI().toString());
            } else {
            	entProperty.setDataType(OWL2Datatype.XSD_STRING.getIRI().toString());
//            	entProperty.setDataType(XSDDatatype.XSDstring.getURI().toString());
            }
        }else{
        	entProperty.setDataType(null);
//        	entProperty.setDataType("unknown");
        }
        return "data-property";
    }

    
	private String fixPropertyName(String name) {
//		name = name.replaceAll("%", "percent");
		
		int newLineIdx = name.lastIndexOf("\n");
		if(newLineIdx >= 0){
			name = name.substring(newLineIdx + 1); 
		}
		
		return name.trim().replace(' ', '_');
	}
}
