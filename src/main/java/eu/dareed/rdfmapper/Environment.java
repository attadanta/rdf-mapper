package eu.dareed.rdfmapper;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Kiril Tonev</a>
 */
public class Environment {
    private final Model model;
    private final NamespaceResolver namespaceResolver;
    private final Context context;

    public Environment(Model model, NamespaceResolver namespaceResolver) {
        this(model, namespaceResolver, null);
    }

    Environment(Model model, NamespaceResolver namespaceResolver, Context context) {
        this.model = model;
        this.namespaceResolver = namespaceResolver;
        this.context = context;
    }

    public Environment augment(MappingDataEntity entity) {
        return new Environment(this.model, this.namespaceResolver, context.augment(entity));
    }

    public NamespaceResolver getNamespaceResolver() {
        return namespaceResolver;
    }

    public Context getContext() {
        return context;
    }

    public Model getModel() {
        return model;
    }
}
