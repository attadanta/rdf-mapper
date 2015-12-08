package eu.dareed.rdfmapper.xml;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
enum PropertyType {
    CHOICE                  ("object-property"),
    REFERENCE               ("object-property"),
    REFERENCE_CLASS_NAME    ("object-property"),
    EXTERNAL_LIST           ("object-property"),
    NODE                    ("object-property"),
    INTEGER                 ("data-property"),
    REAL                    ("data-property"),
    ALPHA                   ("data-property");

    final String propertyType;

    PropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    static PropertyType parseTypeParameter(String typeDeclaration) {
        return PropertyType.valueOf(typeDeclaration.trim().toUpperCase().replaceAll("-", "_"));
    }
}
