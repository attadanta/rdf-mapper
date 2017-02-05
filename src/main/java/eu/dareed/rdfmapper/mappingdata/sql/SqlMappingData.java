package eu.dareed.rdfmapper.mappingdata.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import eu.dareed.rdfmapper.MappingData;
import eu.dareed.rdfmapper.MappingDataEntity;

public class SqlMappingData implements MappingData {
	private Connection con = null;
	
	private String port = null;
	private String databaseName = null;
	private String host = null;
	private String user = null;
	private String password = null;
	private String databaseType = null;
	private String databaseDriver = null;
	private String url = null;
	
	private List<MappingDataEntity> entityList;
	private List<TableOverview> tables;
	
	
	public SqlMappingData(Properties dbProps) {
		setConnection(dbProps);
		tables = new ArrayList<>();
		entityList = new ArrayList<>();
		
		try {
			Statement tableSt = con.createStatement();
			tableSt.execute("SELECT table_name, table_rows FROM information_schema.tables WHERE engine = 'InnoDB';");
			ResultSet tableResult = tableSt.getResultSet();
			
			while(tableResult.next()){
				TableOverview tableOverview = new TableOverview(tableResult.getString("table_name"), tableResult.getLong("table_rows"));
				Statement columnSt = con.createStatement();
				columnSt.execute("SELECT COLUMN_NAME FROM information_schema.columns WHERE table_name = '" + tableOverview.getTableName() + "'");
				ResultSet columnResult = columnSt.getResultSet();
				while(columnResult.next()){
					tableOverview.addColumnName(columnResult.getString("column_name"));
				}
				tables.add(tableOverview);
				columnSt.close();
			}
			
			tableSt.close();
			
			for(TableOverview to : tables){
				loadTableContent(to);
			}
		} catch (SQLException e) {e.printStackTrace();
		}
		
	}
	

	private void loadTableContent(TableOverview to) throws SQLException {
		if(to.getRowNumbers() > 1000000){
			System.out.println("Warning: Many db entries");
		}
		Statement contentSt = con.createStatement();
		contentSt.execute("SELECT * FROM " + to.getTableName() + ";");
		ResultSet contentResult = contentSt.getResultSet();
		while(contentResult.next()){
			List<String> attributes = new  ArrayList<>();
			for(String column : to.getColumnNames()){
				attributes.add(contentResult.getString(column));
			}
			entityList.add(new SqlMappingDataEntity(to.getTableName(), attributes));
		}
		
		contentSt.close();
	}


	@Override
	public List<MappingDataEntity> getDataEntities() {
		return entityList;
	}
	
	
	private void setConnection(Properties properties) {		
		port = properties.getProperty("port");
		databaseName = properties.getProperty("databaseName");
		host = properties.getProperty("host");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		databaseType = properties.getProperty("databaseType");
		databaseDriver = properties.getProperty("databaseDriver");
		url = "jdbc:"+ databaseType+"://" + host + ":"+ port +"/" + databaseName;
//		System.out.println(url.toString()); // + " " + user + " " + password);
		try {
			Class.forName(databaseDriver);
			this.con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {e.printStackTrace();
		} catch (ClassNotFoundException e) {e.printStackTrace();
		}
	}

}
