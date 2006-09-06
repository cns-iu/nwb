/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.modeling.barabasialbert;

import edu.iu.informatics.shared.FortranAlgorithm;
import edu.iu.informatics.shared.persisters.BasicEdgePersister;
import edu.iu.iv.core.algorithm.AlgorithmProperty;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.datamodels.DataModelType;
import edu.iu.iv.core.util.staticexecutable.StaticExecutableRunner;

/**
 * Class to implement the Algorithm for this IVC Plug-in.
 * 
 * @author Santo
 */
public class BarabasiAlbertAlgorithm extends FortranAlgorithm {
    public static final String NAME = "Barabasi Albert Modeling Algorithm";

	/**
	 * Creates a new Barabasi Albert Modeling Algorithm.
	 */
	public BarabasiAlbertAlgorithm(DataModel dm) {
        super(dm);
	    propertyMap.put(AlgorithmProperty.LABEL, NAME);
        
        parameterMap.putIntOption("nodes","Number of nodes:","Enter the number of nodes of the network to create.",
                100, null);
        parameterMap.putIntOption("links","Number of links:","Enter the number of new links to add.",
                3, null);
    }

	/**
	 * Executes this Barabasi Albert Modeling Algorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute() {
        StaticExecutableRunner runner = new StaticExecutableRunner();
        
        runner.registerFile("network",
        		           "network file",
        		           DataModelType.NETWORK,
        		           new BasicEdgePersister(parameterMap.getIntValue("nodes")));
        runner.setParentFilename("network");
        
        makeInputFile(runner, "inputfile");
        runner.execute(BarabasiAlbertPlugin.PLUGIN_ID);
        
		return true;
	}
}