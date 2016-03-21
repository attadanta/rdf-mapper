package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.verification.Grade;
import eu.dareed.rdfmapper.xml.verification.MappingVerifier;
import eu.dareed.rdfmapper.xml.verification.Offense;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Verify {
    private final File file;
    private final List<Offense> errors;
    private final List<Offense> warnings;

    private static Verify file(File file, MappingVerifier verifier) throws IOException {
        String mappingInput;

        try (FileInputStream input = new FileInputStream(file)) {
            mappingInput = IOUtils.toString(input);
        }

        List<Offense> errors = new ArrayList<>();
        List<Offense> warnings = new ArrayList<>();

        StringReader validationInput = new StringReader(mappingInput);
        for (Offense offense : verifier.tryParse(validationInput)) {
            if (offense.grade == Grade.ERROR) {
                errors.add(offense);
            } else if (offense.grade == Grade.WARNING) {
                warnings.add(offense);
            }
        }
        validationInput.close();

        if (errors.isEmpty()) {
            StringReader verificationInput = new StringReader(mappingInput);
            try {
                for (Offense offense : verifier.verify(verifier.getIO().loadXML(verificationInput))) {
                    if (offense.grade == Grade.ERROR) {
                        errors.add(offense);
                    } else if (offense.grade == Grade.WARNING) {
                        warnings.add(offense);
                    }
                }
            } catch (JAXBException e) {
                errors.add(new Offense(Grade.ERROR, e.getMessage()));
            }
            verificationInput.close();
        }

        return new Verify(file, errors, warnings);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: verify [mapping] ...");
            System.exit(1);
        }
        MappingVerifier verifier = MappingVerifier.initialize();
        for (String path : args) {
            try {
                Verify verify = Verify.file(new File(path), verifier);
                verify.report(System.out);
            } catch (IOException e) {
                System.err.println("Could not verify " + path + ": " + e.getMessage());
            }
        }
    }

    private void report(PrintStream out) {
        out.println(file.getName() + ": " + errors.size() + " error(s), " + warnings.size() + " warning(s)");
        for (Offense error : errors) {
            reportOffense(error, out);
        }
        for (Offense warning : warnings) {
            reportOffense(warning, out);
        }
    }

    private void reportOffense(Offense offense, PrintStream out) {
        out.printf("%s: [%s] %s\n", file.getName(), offense.grade.name(), offense.description);
    }

    private Verify(File file, List<Offense> errors, List<Offense> warnings) {
        this.file = file;
        this.errors = errors;
        this.warnings = warnings;
    }
}
