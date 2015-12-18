package eu.dareed.rdfmapper.xml;

import eu.dareed.eplus.model.idd.AnnotatedObject;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
enum DataProperty {
    INTEGER(OWL2Datatype.XSD_INTEGER.getIRI().toString()),
    REAL(OWL2Datatype.XSD_FLOAT.getIRI().toString()),
    ALPHA(OWL2Datatype.XSD_STRING.getIRI().toString());

    final String typeURI;

    DataProperty(String typeURI) {
        this.typeURI = typeURI;
    }

    static DataProperty parseDataTypeInField(AnnotatedObject field) {
        return DataProperty.valueOf(field.getParameter("type").value().toUpperCase().trim());
    }
}
