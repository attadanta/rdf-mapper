# Guide for Contributions

## General Recommendations

 * Follow the [semver conventions](http://semver.org) when introducing changes to the code.
 * Feature additions should be accompanied by tests which exercise them.
 * Features should be well-documented, both in their public API as well as in the project documentation.

## Suggestions for Further Development

 * Provide escape sequences for property value placeholders.
 * Provide implementations for CSV and/or SQL sources.
 * Provide configuration options for `XMLMapper` to exclude some types or to export just a specific set to make the consequent spec editing more convenient.
 * Indicate the locations of the violations in offenses.
 * Extend the mapping schema with syntactic checks on element values.
 * Extend the identifier field with non-integer values so that loosely structured sources could be used.