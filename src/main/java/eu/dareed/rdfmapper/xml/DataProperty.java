package eu.dareed.rdfmapper.xml;

import org.semanticweb.owlapi.vocab.OWL2Datatype;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
enum DataProperty {
    INTEGER(OWL2Datatype.XSD_INTEGER.getIRI().toString()),
    REAL(OWL2Datatype.XSD_FLOAT.getIRI().toString()),
    ALPHA(OWL2Datatype.XSD_STRING.getIRI().toString());

    final String typeURL;

    DataProperty(String typeURL) {
        this.typeURL = typeURL;
    }
}
