package eu.dareed.rdfmapper.mappingdata.sql;

import java.util.ArrayList;
import java.util.List;

public class TableOverview {
	private String tableName;
	private long rowNumbers;
	private List<String> columnNames;
	
	public TableOverview(String tableName, long rowNumbers) {
		this.tableName = tableName;
		this.rowNumbers = rowNumbers;
		columnNames = new ArrayList<>();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public long getRowNumbers() {
		return rowNumbers;
	}

	public void setRowNumbers(long rowNumbers) {
		this.rowNumbers = rowNumbers;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}
	
	public void addColumnName(String columnName){
		columnNames.add(columnName); 
	}
}
