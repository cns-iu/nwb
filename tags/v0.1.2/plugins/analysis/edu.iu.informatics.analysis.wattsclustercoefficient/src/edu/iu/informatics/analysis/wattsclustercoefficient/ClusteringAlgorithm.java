/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.analysis.wattsclustercoefficient;

import edu.iu.informatics.shared.FortranAnalysisAlgorithm;
import edu.iu.informatics.shared.persisters.BasicEdgePersister;
import edu.iu.iv.core.algorithm.AlgorithmProperty;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.util.staticexecutable.StaticExecutableRunner;

/**
 * Class to implement the Algorithm for this IVC Plug-in.
 *
 * @author
 */
public class ClusteringAlgorithm extends FortranAnalysisAlgorithm {    
    public static final String ALGORITHM_NAME = "Clustering Coefficients";
    
    /**
     * Creates a new ClusteringAlgorithm.
     */
	public ClusteringAlgorithm(DataModel dm) {
		super(dm);
		
	    propertyMap.put(AlgorithmProperty.LABEL, ALGORITHM_NAME);
        
//        parameterMap.putIntOption("nodes","Number of Nodes:","Enter the number of nodes in the network",100000,null);
//        parameterMap.putIntOption("edges","Number of Edges:","Enter the number of edges in the network",10,null);
	}
	
	/**
	 * Executes this ClusteringAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute(){
        StaticExecutableRunner runner = new StaticExecutableRunner();

        setDefaultParameters();
        saveDataModel(runner, "network", new BasicEdgePersister());
        makeInputFile(runner, "inputfile");

        runner.execute(ClusteringPlugin.ID_PLUGIN);

        return true;
	}
}