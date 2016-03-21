package eu.dareed.rdfmapper.xml.verification;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */

import eu.dareed.rdfmapper.xml.nodes.Mapping;

import java.util.List;

interface Check {
    List<Offense> verify(Mapping context);
}
