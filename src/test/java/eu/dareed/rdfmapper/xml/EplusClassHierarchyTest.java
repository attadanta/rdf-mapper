package eu.dareed.rdfmapper.xml;

import eu.dareed.rdfmapper.xml.nodes.SubClassRelation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class EplusClassHierarchyTest {

    private EplusClassHierarchy hierarchy;

    @Before
    public void setup() {
        this.hierarchy = new EplusClassHierarchy();

        hierarchy.extend(new LinkedList<>(Arrays.asList(
                new EplusClass("Site"),
                new EplusClass("Location")
        )));

        hierarchy.extend(new LinkedList<>(Arrays.asList(
                new EplusClass("SizingPeriod"),
                new EplusClass("DesignDay")
        )));
    }

    @Test
    public void testListOfSubclassRelations() {
        List<SubClassRelation> relations = hierarchy.getRelations();

        Assert.assertEquals(2, relations.size());

        SubClassRelation relation = relations.get(0);

        Assert.assertEquals("Site", relation.getSuperClass());
        Assert.assertEquals("Location", relation.getSubClass());

        relation = relations.get(1);

        Assert.assertEquals("SizingPeriod", relation.getSuperClass());
        Assert.assertEquals("DesignDay", relation.getSubClass());
    }
}
