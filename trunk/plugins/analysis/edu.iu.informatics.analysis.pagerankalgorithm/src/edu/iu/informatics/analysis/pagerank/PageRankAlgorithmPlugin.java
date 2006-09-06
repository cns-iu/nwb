/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.analysis.pagerank;

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
public class PageRankAlgorithmPlugin extends AbstractPlugin {
    //id of this Plugin
    public static final String PLUGIN_ID = "edu.iu.informatics.analysis.pagerank";
    
    //basic Plugin information
    private static final String AUTHOR = "Implemented by Santo Fortunato.";
    private static final String DESCRIPTION = "Computes rank of nodes given an edge list using the PageRank algorithm";
    private static final String CITATION_STRING = "S. Brin & L. Page (1998), \"The Anatomy of a Large-Scale Hypertextual Web Search Engine\", Computer Networks, {\bf 30}: 107-117.";   
    private static final String UNSUPPORTED_REASON = "Works on Files";
    private static final String DOCUMENTATION_URL = "http://citeseer.ist.psu.edu/cache/papers/cs/13017/http:zSzzSzwww-db.stanford.eduzSzpubzSzpaperszSzgoogle.pdf/brin98anatomy.pdf";
    
    /**
     * Creates a new PageRankAlgorithmPlugin.
     */
	public PageRankAlgorithmPlugin() {
	    //add the proper information to this Plugin's PropertyMap
        propertyMap.put(PluginProperty.AUTHOR, AUTHOR);
        propertyMap.put(PluginProperty.CITATION_STRING, CITATION_STRING);
//        
//        //remove if not providing a Documentation URL
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
        //this template simple creates a PageRankAlgorithm and executes it,
        //replace as needed.
        PageRankAlgorithm algorithm = new PageRankAlgorithm(model);
        algorithm.createGUIandRun(PageRankAlgorithm.NAME, "");        
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