package eu.dareed.rdfmapper;

import java.util.Properties;

import eu.dareed.rdfmapper.MappingDataImpl.SqlMappingData;

public class SqlMappingDataTest {
	
	public static void main(String[] args){
		Properties props = new Properties();
		props.put("port", "3306");
		props.put("databaseName", "dareed_db");
		props.put("host", "90.147.11.23");
		props.put("user", "dareeduser");
		props.put("password", "dareed2016");
		props.put("databaseType", "mysql");
		props.put("databaseDriver", "com.mysql.jdbc.Driver");
		
		MappingData mData = new SqlMappingData(props);
		System.out.println("Printing extracted data from dareed_db.\n");
		for(MappingDataEntity ent : mData.getDataEntities()){
			String str = "DB entry from '" + ent.getType() + ": ";
			for(String attr : ent.getAttributes()){
				str = str + "| " + attr + " |\t";
			}
			System.out.println(str);
		}
		
	}
	
}
