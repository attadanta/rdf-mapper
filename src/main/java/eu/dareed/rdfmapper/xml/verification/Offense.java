package eu.dareed.rdfmapper.xml.verification;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Offense {
    public final Grade grade;
    public final String description;

    public Offense(Grade grade, String description) {
        this.grade = grade;
        this.description = description;
    }
}
