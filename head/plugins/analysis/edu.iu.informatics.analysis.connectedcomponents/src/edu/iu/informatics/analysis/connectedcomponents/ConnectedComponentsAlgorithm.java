/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.analysis.connectedcomponents;

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
public class ConnectedComponentsAlgorithm extends FortranAnalysisAlgorithm {    
    public static final String ALGORITHM_NAME = "Connected Components";
    
    /**
     * Creates a new ConnectedComponentsAlgorithm.
     */
	public ConnectedComponentsAlgorithm(DataModel dm) {
		super(dm);
		
	    propertyMap.put(AlgorithmProperty.LABEL, ALGORITHM_NAME);
	}
	
	/**
	 * Executes this ConnectedComponentsAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute(){
        StaticExecutableRunner runner = new StaticExecutableRunner();

        setDefaultParameters();
        saveDataModel(runner, "network", new BasicEdgePersister());
        makeInputFile(runner, "inputfile");

        runner.execute(ConnectedComponentsPlugin.ID_PLUGIN);
        
	    return true;
	}
}