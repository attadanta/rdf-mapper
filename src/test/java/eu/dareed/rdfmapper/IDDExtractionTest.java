package eu.dareed.rdfmapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.model.idd.IDDField;
import eu.dareed.eplus.model.idd.IDDObject;
import eu.dareed.eplus.model.idd.Parameter;
import eu.dareed.eplus.parsers.idd.IDDParser;

public class IDDExtractionTest {
    private static IDD idd;

    @BeforeClass
    public static void setup() throws IOException {
        InputStream resource = IDDExtractionTest.class.getResourceAsStream("/Energy+.idd");
        idd = new IDDParser().parseFile(resource);
    }

    @Test
    public void testInitialized() {
        Assert.assertNotNull(idd);
    }

	@Test
    public void testParseWholeDictionary() throws IOException {
        List<String> classList = new LinkedList<>();
        List<String> propertyList = new LinkedList<>();
        for(IDDObject iObj : idd.getAllObjects()){

        	String className = iObj.getType().trim().replace(' ', '_');
//        	classList.add(className);

        	for(IDDField field : iObj.getFields()){
        		List<Parameter> pList = field.getParameters("field");
        		if(pList.size() != 0){
        			String curPropName = pList.get(0).value().trim().replace(' ', '_');
        			boolean inList = false;
        			for(String propName : propertyList){
	        			if(curPropName.equals(propName)){
	        				classList.add(propName + "   ont-class: " + className + "   data-property: " + field.getName());
	        				inList = true;
	        				break;
	        			}
        			}
        			if(!inList){
        				propertyList.add(curPropName);
        			}
        		}

        	}
        }

        Collections.sort(classList);
        System.out.println(propertyList.size());

        BufferedWriter writer = new BufferedWriter(new FileWriter("D:/temp/dareed_multiProperties.txt"));
        for(String name : classList){
        	writer.write(name + "\n");
        }
        writer.close();
    }
}
