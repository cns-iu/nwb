package edu.iu.informatics.shared;

import edu.iu.iv.provider.PersisterProvider;

/**
 * Implementation of a persister plug-in
 */
public class PersisterProviderPlugin extends PersisterProvider {
    //The shared instance.
    private static PersisterProviderPlugin plugin;  

    /**
     * The constructor for the persister plugin
     */
    public PersisterProviderPlugin() {
        super("edu.iu.informatics.shared.PersisterProviderPlugin");
        plugin = this;
    }
    
    /**
     * Returns the shared instance.
     * @return Persister plugin
     */
    public static PersisterProviderPlugin getDefault() {
        return plugin;
    }
}