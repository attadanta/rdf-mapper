package eu.dareed.rdfmapper;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Environment {
    private final NamespaceResolver namespaceResolver;
    private final Context context;

    public Environment(NamespaceResolver namespaceResolver) {
        this(namespaceResolver, null);
    }

    Environment(NamespaceResolver namespaceResolver, Context context) {
        this.namespaceResolver = namespaceResolver;
        this.context = context;
    }

    public Environment augment(MappingDataEntity entity) {
        return new Environment(this.namespaceResolver, context == null ? new Context(entity) : context.augment(entity));
    }

    public NamespaceResolver getNamespaceResolver() {
        return namespaceResolver;
    }

    public Context getContext() {
        return context;
    }
}
