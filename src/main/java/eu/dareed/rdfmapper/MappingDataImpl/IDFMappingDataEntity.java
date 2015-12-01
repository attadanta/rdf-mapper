package eu.dareed.rdfmapper.MappingDataImpl;

import java.util.ArrayList;
import java.util.List;

import eu.dareed.eplus.model.idf.IDFField;
import eu.dareed.eplus.model.idf.IDFObject;
import eu.dareed.rdfmapper.MappingDataEntity;

public class IDFMappingDataEntity implements MappingDataEntity {
	String type;
	List<String> attributes;
	
	public IDFMappingDataEntity(IDFObject idfObj) {
		this.type = idfObj.getType();
		attributes = new ArrayList<>();
		for (IDFField field : idfObj.getFields()) {
			attributes.add(field.getRawValue());
		}
	}
	
	private IDFMappingDataEntity() {
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
