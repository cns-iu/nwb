/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.modeling.smallworld;

import edu.iu.informatics.shared.FortranAlgorithm;
import edu.iu.informatics.shared.persisters.BasicEdgePersister;
import edu.iu.iv.common.parameter.Validator;
import edu.iu.iv.core.algorithm.AlgorithmProperty;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.datamodels.DataModelType;
import edu.iu.iv.core.util.staticexecutable.StaticExecutableRunner;

/**
 * Class to implement the Algorithm for this IVC Plug-in.
 *
 * @author
 */
public class SmallWorldAlgorithm extends FortranAlgorithm {    
    public static final String ALGORITHM_NAME = "Small World";
    
    /**
     * Creates a new SmallWorldAlgorithm.
     */
	public SmallWorldAlgorithm(DataModel dm) {
		super(dm);
	    propertyMap.put(AlgorithmProperty.LABEL, ALGORITHM_NAME);
	    
        parameterMap.putIntOption("nodes", "Number of Nodes:",
				"Enter the number of nodes in the network", 100, null);
		parameterMap.putIntOption("neighbors", "number of neighbors in the initial ordered lattice:",
				"Enter the number of neighbors in the initial ordered lattice:", 10, null);
		parameterMap.putDoubleOption("rewiring", "Rewiring Probability",
				"Enter the rewiring probability", 0.001, new Validator() {

					public boolean isValid(Object value) {
						double val = ((Double) value).doubleValue();

						return val >= 0.0 && val <= 1.0;
					}
				});
	}
	
	/**
	 * Executes this SmallWorldAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute(){
	    StaticExecutableRunner runner = new StaticExecutableRunner();

		runner.registerFile("network", "network file", DataModelType.NETWORK,
				new BasicEdgePersister(parameterMap.getIntValue("nodes")));

		makeInputFile(runner, "inputfile");
		runner.execute(SmallWorldPlugin.ID_PLUGIN);

		return true;
	}
}