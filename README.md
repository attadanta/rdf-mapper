# RDF Mapper

This library facilitates the extraction of RDF graphs from table-based
sources. It proposes a way for specifying the URLs of resources and
their property assignments in an XML file. Once a specification is
written, a graph can be extracted by processing the input schemas or
tables with an implementation of two interfaces which instruct how to
access the data items and their properties.

The assumptions on the data sources are designed to be broad and should
be applicable to common table-based format where each item is
represented by a row in some table. These assumptions are namely:

 * Schemas are associated with a names which identifies item types
   uniquely.
 * Item properties are accessed by integers which identify property
   values.

These requirements are stated in the interfaces which have to be
implemented in order to make the data sources for the mapping available
to the library:

 * `MappingData`: represents a collection of entities.
 * `MappingDataEntity`: represents a particular named entity that
 possesses a list of properties.

## Creating a Mapping

A mapping specification is comprised of three main elements:

 * Entity Maps: Assign each mapped object a resource URL and,
   optionally, one or more types.
 * Property Maps: Assign to each mapped object's field a property URL.
 * Subtype Map: Organizes entities in a taxonomy.

For convenience's sake, URL prefixes can be reused after being declared
once at the top of the mapping spec.

### Namespace Declarations

The namespaces are declared as prefix-uri pairs in a `namespaces`
element:

    <namespaces xmlns="http://imi.kit.edu/rdfMapping">
        <ns>
            <prefix>defaultns</prefix>
            <uri>https://energyplus.net/</uri>
        </ns>
        <ns>
            <prefix>geo</prefix>
            <uri>http://www.w3.org/2003/01/geo/wgs84_pos#</uri>
        </ns>
        <ns>
            <prefix>rdfs</prefix>
            <uri>http://www.w3.org/2000/01/rdf-schema#</uri>
        </ns>
        <ns>
            <prefix>xsd</prefix>
            <uri>http://www.w3.org/2001/XMLSchema#</uri>
        </ns>
    </namespaces>

The prefix `defaultns` is reserved and denotes a base URL.

Given that a prefix `p` is declared in the mapping, a URL with the form
`p:suffix` is resolved by substituting `p:` with the full URL assigned
to `p` in the namespace map. The default prefix can be used by leaving
`p` empty. Thus, following the above example, `:Energy` will resolve to
`https://energyplus.net/Energy` and `geo:lat` will resolve to
`http://www.w3.org/2003/01/geo/wgs84_pos#lat`.

### Property Value Expansion

The mapping utilizes the concept of property value placeholders, much
like the URI patterns defined in [RFC 6570](https://tools.ietf.org/html/rfc6570).

Placeholders are delimited by the dollar character `$` on both sides and
enclose an integer. The integer is a zero-based index of a property
value in the entity in the scope.

This convention is used both for building typed literals and URLs. The
prefixed URL notation and the property value expansion can thus be
mixed. The character sequence `:$2$` will resolve to a URL using the
default URL prefix and append the property value of the third property
in the entity under consideration.

### Entity Map

An entity mapping specifies a naming scheme for resources. For the sake
of convenience, type assignments are also given in this section instead
of using property mappings. This imitates the shortcut employed in
TURTLE whereby the `rdfs:type` predicate is aliased.

Entity maps are defined by four elements:

 * `uri`: the URL scheme. In order to uniquely identify each resource,
   value expansion can be leveraged here.
 * `name`: a string identifier of the entity's type. The mapper is using
   this to identify the mapping with which to process an object from the
   data set.
 * `type`: a URL of a class resource. This element can be used multiple
   times.
 * `properties`: a property map attached to this entity map. See below.

### Property Map

The property map is a sequence of data property and object property
definitions. Both types should give a URL pattern in an `uri` element
and an `id` that references the index of the mapped property.

Values are encoded differently in data properties and object properties.
Data property mappings _may_ indicate a `type` URL in order to build a
[typed literal](https://www.w3.org/TR/rdf11-concepts/#section-Graph-Literal)
with the value. Object properties on the other hand _should_ give the
object's URL pattern in an `object` element.

### Taxonomy Map

The taxonomy map can be used to construct a type hierarchy separately
from entities' `type` assignments. Each subtype relation is represented
by a `subClassRelation` element. It has two children:

 * `superClass`: the _name_ of the abstract entity.
 * `subClass`: the _name_ of the specialization.

The two name references _should_ be defined in type entities of the
spec in order to resolve entity names.

This feature feature is obviously incompatible with entity maps that use
URL patterns. It is only used for deriving an ontology within the DAREED
project (see below).

### Full Example

A full example demonstrating the usage of the concepts explained above
is given below:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <mapping xmlns="http://imi.kit.edu/rdfMapping">
        <namespaces>
            <ns>
                <prefix>defaultns</prefix>
                <uri>https://energyplus.net/</uri>
            </ns>
            <ns>
                <prefix>geo</prefix>
                <uri>http://www.w3.org/2003/01/geo/wgs84_pos#</uri>
            </ns>
            <ns>
                <prefix>rdfs</prefix>
                <uri>http://www.w3.org/2000/01/rdf-schema#</uri>
            </ns>
            <ns>
                <prefix>xsd</prefix>
                <uri>http://www.w3.org/2001/XMLSchema#</uri>
            </ns>
        </namespaces>
        <entities>
            <entity>
                <uri>http://dareed.eu/simulation/buildings/$0$</uri>
                <name>Building</name>
                <type>:Building</type>
                <properties>
                    <dataProperty>
                        <uri>rdfs:label</uri>
                        <id>0</id>
                        <type>xsd:string</type>
                    </dataProperty>
                    <objectProperty>
                        <uri>:terrain</uri>
                        <id>2</id>
                        <object>:$2$</object>
                    </objectProperty>
                </properties>
            </entity>
        </entities>
    </mapping>

## Usage

The library supports two main operations: the verification of a mapping
and the graph generation given a mapping spec and a data set. Their
respective APIs are described below.

### Extracting an RDF Graph

Mapping specs can be processed in two ways; the direct way uses a
`MappingIO` object and the recommended way performs a check on the input
to verify if the input is valid. The instruction on how to perform the
validation are in the next section. Below, we use the direct approach.

First, obtain a `Mapping` instance by using `MappingIO#loadXML()`. This
method accepts standard inputs like Streams and Readers.

The next step is to prepare your data sources. You should obtain a
`MappingData` instance which collects the objects you are interested in
exposing as RDF.

Finally, use `RDFMapper#mapToRDF()` method by passing it the data source
and the mapping. This should give you a `Model` which you can process
further with the facilities provided by
[Jena](https://jena.apache.org/).

### Verification of a Mapping Spec

The verification API resides in `eu.dareed.rdfmapper.xml.verification`.

A mapping verification requires a `Profile` which defines what
constitutes a correct spec. Minimally, mapping specs should conform to
the mapping XML schema. The profile may also include other checks which
make sure that some logical constraints are fulfilled in order to
extract the RDF graph correctly.

The library provides a default `Profile` implementation. You can obtain
it by calling `Profile::defaultProfile()`. The default profile performs
the following checks:

 * All entity references in the subtype map are also defined in entity
   maps. Because the entity URLs cannot be resolved otherwise, each
   violation of this check is graded as an `ERROR`.
 * The mapping should declare a default namespace. Otherwise, the
   profile emits a `WARNING`.
 * Every namespace prefix in the mapping's entity maps should be defined
   in the mapping's `namespaces` element. Otherwise, the profile emits a
   `WARNING` for each undeclared prefix.

Once a `Profile` is constructed, initialize a `MappingVerifier`
instance with it. The verifier delegates to a `MappingIO` to read the
contents of an input. The result of the read methods is encoded in a
`ParseAttempt` object. Interrogate it to figure out if there are
violations which render the spec invalid or in a sound state.

In particular, a `ParseAttempt` exposes a list of warnings and errors.
If the attempt contains errors, the spec should be deemed invalid.
However, you may try obtaining the spec by asking
`ParseAttempt#parsingSucceeded()` although this should be used as a
last resort measure. Warnings on the other hand signify that the spec is
sound, but the user may want to look over and edit their mappings
beforehand. Those warnings wouldn't prevent the mapper to construct a
valid RDF graph.

You can display errors and warnings by looking at the `Offense`s
descriptions. Ideally, they would indicate the position in the input
where the violation has been detected.

Finally, the parsed spec is exposed via `ParseAttempt#getMapping()`.

#### Writing custom verifiers

A `Profile` accepts a list of `Check`s next to the schema validator. You
can write custom checks by implementing that interface. It accepts a
valid (according to some schema) mapping it can inspect and returns a
list of `Offense`s. It is the `MappingVerifier`s job to sort the errors
from the warnings. Like stated above, any `ERROR`-graded offenses you
include in the result signifies that the mapping is invalid and hence a
graph cannot (or shouldn't) be constructed.

#### Command line usage

The library provides a utility for processing input mappings with the
default verification profile. You can use it directly from the source
root in the following way:

    mvn exec:java -Dexec.mainClass="eu.dareed.rdfmapper.Verify" \
                  -Dexec.args="mapping1.xml mapping2.xml ..."

It will process each given input in the args list and output any
offenses detected on the standard output.

## Special Usages in the DAREED Project

In DAREED, the library is used to extract a linked-data view of the
EnergyPlus' simulation input (IDF) files. Here, we leverage the
`eu.dareed.eplus` library in order to gain an object oriented view of
the contents of those files. Each `IDFObject` is wrapped as a
`MappingDataEntity` that exposes its type and the raw values (strings)
of its properties. The `MappingData` wrapper simply collects each
converted `IDFObject` from an `IDF` instance. Thus, exposing the E+ data
to the mapper is trivial.

The IDF files realistically contain a lot of objects which makes writing
specs manually a difficult task. That's why the library provides a
facility which derives a spec automatically from the data dictionary of
the EnergyPlus distribution. It produces user-friendly labels for each
object template and uses heuristics to determine the property type of
each attribute. Additionally, it tries to detect entities which are
implied in the naming convention of the data dictionary's objects.

The derived spec can then be edited manually to fine-tune the mapping or
exclude some objects or properties. The library also has an OWL ontology
exporter which transforms the spec into an OWL2 document. Here, the
library adopts different semantics for the conversion of the mapping
spec, however. Instead of mapping entities to URLs from the OWL2
namespace, the converter maps the mapper's elements themselves to OWL
entities. The spec is regarded as an ontology. Each entity is thereby
converted to a OWL class. Labels and descriptions are asserted on that
class, but the type assignments are ignored. The properties are asserted
in the ontology without creating any restrictions beside range axioms on
data properties, where data types are given. The taxonomy is trivially
converted to a series of subclass axioms.

The inconsistency affords us to handle both the mapping of IDF files and
IDD files with with a single specification.

## Acknowledgement

This work originated in the [DAREED](http://dareed.eu) project. DAREED has
received funding from the the Seventh framework programme of the European
union (EU) under grant agreement 609082.