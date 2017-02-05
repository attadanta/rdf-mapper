package eu.dareed.rdfmapper.energyplus.mapping;

import eu.dareed.eplus.model.idf.IDFField;
import eu.dareed.eplus.model.idf.IDFObject;
import eu.dareed.rdfmapper.MappingDataEntity;

import java.util.ArrayList;
import java.util.List;

public class IDFMappingDataEntity implements MappingDataEntity {
	String type;
	List<String> attributes;
	
	public IDFMappingDataEntity(IDFObject idfObj) {
		List<IDFField> fields = idfObj.getFields();
		this.attributes = new ArrayList<>(fields.size());
		for (IDFField field : fields) {
			attributes.add(field.getRawValue());
		}
		this.type = idfObj.getType();
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

	@Override
	public boolean containsAttributeWithIndex(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Negative index given.");
		}

		return index < attributes.size();
	}


	@Override
	public String resolveNamedVariable(String variableName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String resolveIndex(int index) {
		return getAttributeByIndex(index);
	}
}
