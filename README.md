# RDF Mapper

This library facilitates the extraction of RDF graphs from table-based
sources. It proposes a way for specifying the urls of resources and
their property assignments in an XML file. Once a specification is
written, a graph can be extracted by processing the input schemas or
tables with an implementation of two interfaces which instruct how to
access the data items and their properties.

The assumptions on the data sources are designed to be broad and should
be applicable to common table-based format where each item is
represented by a row in some table. These assumptions are namely:

 * Schemas are associated with a names which identifies item types
   uniquely.
 * Item properties are accessed by integers which identify a cell value.

These requirements are stated in the interfaces which have to be
implemented in order to make the data sources for the mapping available
to the library:

 * `MappingData`: represents a collection of entities.
 * `MappingDataEntity`: represents a particular named entity that
 possesses a list of properties.

## Creating a Mapping

A mapping specification is comprised of three elements:

 * Entity Maps: Assigns to each mapped object a resource URL and,
   optionally, one or more types.
 * Property Maps: Assign to each mapped object's field a property URL.
 * Subtype Map: Organizes entities in a taxonomy.

## Usage

The library supports two main operations: the verification of a mapping
and the graph generation given a mapping spec and a data set. Their
respective APIs are described below.

### Verification of a Mapping Spec

The verification API resides in `eu.dareed.rdfmapper.xml.verification`.

A mapping verification requires a `Profile` which defines what
constitutes a correct spec. Minimally, mapping specs should conform to
the mapping XML schema. The profile may also include other checks which
make sure that some logical constraints are fulfilled in order to
extract the RDF graph correctly.

The library provides a default `Profile` implementation. You can obtain
it by calling `Profile.defaultProfile()`. The default profile performs
the following checks:

 * All entity references in the subtype map are also defined in entity
   maps. Because the entity urls cannot be resolved otherwise, each
   violation of this check is graded as an `ERROR`.
 * The mapping should declare a default namespace. Otherwise, the
   profile emits a `WARNING`.
 * Every namespace prefix in the mapping's entity maps should be defined
   in the mapping's `namespaces` element. Otherwise, the profile emits a
   `WARNING` for each undeclared prefix.

Once a `Profile` is constructed, initialize a `MappingVerifier`
instance with it. The verifier provides read functionality over basic IO
objects like Streams or Readers. The results of those read operations
are encoded in a `ParseAttempt` objects. Interrogate them to figure out
if there are violations which render the spec invalid or in a sound
state.

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
valid mapping it can inspect and returns a list of `Offense`s. It is the
`MappingVerifier`s job to sort the errors from the warnings. Like stated
above, any `ERROR`-graded offenses you include in the result signifies
that the mapping is invalid and hence a graph cannot (or shouldn't) be
constructed.

#### Command line usage

The library provides a utility for processing input mappings with the
default verification profile. You can use it directly from the source
root in the following way:

    mvn exec:java -Dexec.mainClass="eu.dareed.rdfmapper.Verify" \
                  -Dexec.args="mapping1.xml mapping2.xml ..."

It will process each given input in the args list and output any
offenses detected on the standard output.

### Extracting an RDF Graph

## Special usages in the DAREED Project