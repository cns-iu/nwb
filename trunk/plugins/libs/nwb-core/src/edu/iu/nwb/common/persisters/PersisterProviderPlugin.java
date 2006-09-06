package edu.iu.nwb.common.persisters;

import edu.iu.iv.provider.PersisterProvider;

/**
 * The main class that will provide persisters in this project
 * to the IVC.
 */
public class PersisterProviderPlugin extends PersisterProvider {
    //The shared instance.
    private static PersisterProviderPlugin plugin;  

    /**
     * The constructor.
     */
    public PersisterProviderPlugin() {
        super("edu.iu.nwb.common.persisters.PersisterProviderPlugin");
        plugin = this;
    }
    
    /**
     * Returns the shared instance.
     */
    public static PersisterProviderPlugin getDefault() {
        return plugin;
    }
}