package eu.dareed.rdfmapper;

import java.util.List;

public interface MappingDataEntity {
	String getType();
	List<String> getAttributes();
	String getAttributeByIndex(int idx);
	boolean containsAttributeWithIndex(int index);
}
