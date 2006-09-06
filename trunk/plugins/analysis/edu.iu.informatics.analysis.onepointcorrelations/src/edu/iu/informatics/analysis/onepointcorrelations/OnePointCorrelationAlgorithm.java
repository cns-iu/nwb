/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.analysis.onepointcorrelations;

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
public class OnePointCorrelationAlgorithm extends FortranAnalysisAlgorithm {    
    public static final String ALGORITHM_NAME = "One Point Correlation";
    
    /**
     * Creates a new OnePointCorrelationAlgorithm.
     */
	public OnePointCorrelationAlgorithm(DataModel dm) {
		super(dm);
		
	    propertyMap.put(AlgorithmProperty.LABEL, ALGORITHM_NAME);
	    
//      parameterMap.putIntOption("nodes","Number of Nodes:","Enter the number of nodes in the network",100000,null);
//	    parameterMap.putIntOption("edges","Number of Edges:","Enter the number of edges in the network",10,null);
	}
	
	/**
	 * Executes this OnePointCorrelationAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute(){
        StaticExecutableRunner runner = new StaticExecutableRunner();

        setDefaultParameters();
        saveDataModel(runner, "network", new BasicEdgePersister());
        makeInputFile(runner, "inputfile");

        runner.execute(OnePointCorrelationPlugin.ID_PLUGIN);
        
	    return true;
	}
}