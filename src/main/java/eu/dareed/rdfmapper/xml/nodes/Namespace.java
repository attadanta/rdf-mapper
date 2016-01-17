package eu.dareed.rdfmapper.xml.nodes;

import javax.xml.bind.annotation.XmlElement;

public class Namespace {

	private String prefix;
	private String uri;

	public static final String defaultNamespacePrefix = "defaultns";

	private Namespace() {
	}

	public Namespace(String prefix, String uri) {
		this.prefix = prefix;
		this.uri = uri;
	}


	@XmlElement(name = "prefix")
	public String getPrefix() {
		return prefix;
	}


	@XmlElement(name = "uri")
	public String getURI() {
		return uri;
	}


	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Namespace namespace = (Namespace) o;

		if (!prefix.equals(namespace.prefix)) return false;
		return uri.equals(namespace.uri);

	}

	@Override
	public int hashCode() {
		int result = prefix.hashCode();
		result = 31 * result + uri.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Namespace{" +
				"prefix='" + prefix + '\'' +
				", uri='" + uri + '\'' +
				'}';
	}
}
