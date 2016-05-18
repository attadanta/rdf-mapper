package eu.dareed.rdfmapper.MappingDataImpl;

import java.util.List;

import eu.dareed.rdfmapper.MappingDataEntity;

public class SqlMappingDataEntity implements MappingDataEntity {
	String type;
	List<String> attributes;
	
	
	
	public SqlMappingDataEntity(String type, List<String> attributes) {
		this.type = type;
		this.attributes = attributes;
	}

	private SqlMappingDataEntity() {
	}
	
	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public List<String> getAttributes() {
		return attributes;
	}

	@Override
	public String getAttributeByIndex(int idx){
		return attributes.get(idx);
	}


}
