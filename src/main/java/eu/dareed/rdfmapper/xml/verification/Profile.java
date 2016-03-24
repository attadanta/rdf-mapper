package eu.dareed.rdfmapper.xml.verification;

import javax.xml.validation.Validator;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Profile {
    protected final Validator validator;
    protected final List<Check> checkList;

    public Profile(Validator validator, List<Check> checkList) {
        this.validator = validator;
        this.checkList = checkList == null ? Collections.<Check>emptyList() : checkList;
    }
}
