package eu.dareed.rdfmapper.xml.verification;

import eu.dareed.rdfmapper.xml.nodes.Entity;
import eu.dareed.rdfmapper.xml.nodes.Mapping;
import eu.dareed.rdfmapper.xml.nodes.SubClassRelation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
final class TypeNamesInTaxonomy implements Check {
    @Override
    public List<Offense> verify(Mapping context) {
        List<Offense> offenses = new ArrayList<>();
        Set<String> entities = collectEntityNames(context);
        for (SubClassRelation relation : context.getTaxonomy()) {
            if (!entities.contains(relation.getSubClass())) {
                offenses.add(new Offense(Grade.ERROR, "Subclass not declared: `" + relation.getSubClass() + "'."));
            }

            if (!entities.contains(relation.getSuperClass())) {
                offenses.add(new Offense(Grade.ERROR, "Superclass not declared: `" + relation.getSuperClass() + "'."));
            }
        }
        return offenses;
    }

    private Set<String> collectEntityNames(Mapping context) {
        List<Entity> entities = context.getEntities();
        Set<String> names = new HashSet<>(entities.size());
        for (Entity entity : entities) {
            names.add(entity.getName());
        }
        return names;
    }
}
