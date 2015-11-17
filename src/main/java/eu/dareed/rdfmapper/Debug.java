package eu.dareed.rdfmapper;

import eu.dareed.eplus.model.idd.IDD;
import eu.dareed.eplus.model.idd.IDDField;
import eu.dareed.eplus.model.idd.IDDObject;
import eu.dareed.eplus.model.idd.Parameter;
import eu.dareed.eplus.parsers.idd.IDDParser;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Outputs the idd structure to a file.
 *
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Debug {
    public static void main(String[] args) throws IOException {
        IDD idd = new IDDParser().parseFile(new FileInputStream(args[0]));

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

        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
        for(String name : classList){
            writer.write(name + "\n");
        }
        writer.close();
    }
}