package eu.dareed.rdfmapper.energyplus.mapping;

import eu.dareed.eplus.model.idf.IDF;
import eu.dareed.eplus.model.idf.IDFObject;
import eu.dareed.rdfmapper.MappingData;
import eu.dareed.rdfmapper.MappingDataEntity;

import java.util.ArrayList;
import java.util.List;

public class IDFMappingData implements MappingData {
	List<MappingDataEntity> entityList;

	public IDFMappingData(IDF idf) {
		entityList = new ArrayList<>();
		for (IDFObject idfObj : idf.getObjects()) {
			entityList.add(new IDFMappingDataEntity(idfObj));
		}
	}

	@Override
	public List<MappingDataEntity> getDataEntities() {
		return entityList;
	}
}
