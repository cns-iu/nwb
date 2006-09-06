/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.analysis.wattsclustercoefficient;

import java.net.MalformedURLException;
import java.net.URL;

import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.plugin.AbstractPlugin;
import edu.iu.iv.core.plugin.PluginProperty;
import edu.iu.nwb.core.model.NWBModel;

/**
 * Plugin class for this addition to IVC.
 *
 * @author
 */
public class ClusteringPlugin extends AbstractPlugin {
    //id of this Plugin
    public static final String ID_PLUGIN = "edu.iu.informatics.analysis.wattsclustercoefficient";
    
    //basic Plugin information
    private static final String AUTHOR = "Santo Fortunato (and Duncan Watts, Steve Strogatz)";
    private static final String DESCRIPTION 
      = "it calculates the clustering coefficients of each node of an undirected network and their average";
    private static final String CITATION_STRING = "D. Watts, S. Strogatz, Nature 393, 440-442 (1998)";   
    private static final String UNSUPPORTED_REASON = "Works on Files";
    private static final String DOCUMENTATION_URL = "http://www.nature.com/nature/journal/v393/n6684/full/393440a0_fs.html";
    
    /**
     * Creates a new ClusteringPlugin.
     */
	public ClusteringPlugin() {
	    //add the proper information to this Plugin's PropertyMap
        propertyMap.put(PluginProperty.AUTHOR, AUTHOR);
        propertyMap.put(PluginProperty.CITATION_STRING, CITATION_STRING);
        
        //remove if not providing a Documentation URL
		try {
			propertyMap.put(PluginProperty.DOCUMENTATION_LINK, new URL(DOCUMENTATION_URL)) ;
		} catch (MalformedURLException e) {}	   
	}

	/**
	 * Returns the description of this Plugin.
	 * 
	 * @return the description of this Plugin.
	 */
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * Launches this Plugin.  This method is called if, and only if, the given
     * model has first passed the 'supports' test defined below.  This method should
     * then perform any desired tasks such as loading a GUI and executing an Algorithm.
     * 
     * @param model the data model to be used to launch this Plugin, if needed.
     */
    public void launch(DataModel model) {
        //this template simple creates a ClusteringAlgorithm and executes it,
        //replace as needed.
        //ClusteringAlgorithm algorithm = new ClusteringAlgorithm();
        //algorithm.execute();
        
        ClusteringAlgorithm algorithm = new ClusteringAlgorithm(model);
        algorithm.createGUIandRun(ClusteringAlgorithm.ALGORITHM_NAME, "");   
    }

    /**
     * Determines if this Plugin supports the given data model. This method determines
     * whether or not this Plugin's menu item will be enabled when the given model
     * is selected in IVC.
     * 
     * @param model the data model to check if this Plugin supports
     * @return true if the model is supported, false otherwise.
     */
    public boolean supports(DataModel model) {
        //this template simply returns true, replace as needed.
        return model.getData() instanceof NWBModel;
    }

    /**
     * Returns the reason that the given model is not supported by this Plugin.
     * 
     * @param model the model to determine why it is unsupported
     * @return the reason that the given model is not supported by this Plugin.
     */
    public String unsupportedReason(DataModel model) {
        return UNSUPPORTED_REASON;
    }
}