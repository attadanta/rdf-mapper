package eu.dareed.rdfmapper;

import eu.dareed.rdfmapper.xml.verification.MappingVerifier;
import eu.dareed.rdfmapper.xml.verification.Offense;
import eu.dareed.rdfmapper.xml.verification.ParseAttempt;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Verify {
    private final File file;
    private final List<Offense> errors;
    private final List<Offense> warnings;

    private static Verify file(File file, MappingVerifier verifier) throws IOException {
        ParseAttempt parseAttempt = verifier.tryParse(new FileReader(file));
        return new Verify(file, parseAttempt.getErrors(), parseAttempt.getWarnings());
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
