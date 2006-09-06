/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.analysis.directedknn;

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
public class DirectedKNNAlgorithm extends FortranAnalysisAlgorithm {    
    public static final String ALGORITHM_NAME = "Directed KNN";
    
    /**
     * Creates a new DirectedKNNAlgorithm.
     */
	public DirectedKNNAlgorithm(DataModel dm) {
		super (dm);
		
	    propertyMap.put(AlgorithmProperty.LABEL, ALGORITHM_NAME);
	}
	
	/**
	 * Executes this DirectedKNNAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute(){
        StaticExecutableRunner runner = new StaticExecutableRunner();

        setDefaultParameters();
        saveDataModel(runner, "network", new BasicEdgePersister());
        makeInputFile(runner, "inputfile");

        runner.execute(DirectedKNNPlugin.ID_PLUGIN);
        
	    return true;
	}
}