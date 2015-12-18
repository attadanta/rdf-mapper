package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.nodes.Item;
import eu.dareed.rdfmapper.xml.nodes.NamespaceMap;

public class URIBuilder {

	private NamespaceMap namespaceMap;

	public URIBuilder(NamespaceMap namespaceMap) {
		this.namespaceMap = namespaceMap;
	}

	
	public String buildURIString(String uriPart){
		if(!uriPart.contains(":")){
			return uriPart;
		}
		
		int prefixIdx = uriPart.indexOf(':');
		String prefix = uriPart.substring(0, prefixIdx);
		if(prefix.equals("http") || prefix.equals("https")){
			return prefix + ":" + uriPart.substring(prefixIdx + 1);
		}else{
			return buildURIString(findNamespace(prefix) + uriPart.substring(prefixIdx + 1));
		}
	}
	
	
	public String buildURIString(String uriPart, String addPrefix){
		String innerURI = buildURIString(uriPart);
		if(addPrefix != null){
			return buildURIString(addPrefix + ":" + innerURI);
		}else{
			return innerURI;
		}
		
	}


	private String findNamespace(String prefix) {
		for(Item ns : namespaceMap.getNamespaceList()){
			if(ns.getLabel().equals(prefix)){
				return ns.getURI();
			}
		}
		System.out.println("Error: namespace with prefix: " + prefix + " not found!");
		return null;
	}
	
}
