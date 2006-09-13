/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.modeling.erdosrandom;

import edu.iu.informatics.shared.FortranAlgorithm;
import edu.iu.informatics.shared.persisters.BasicEdgePersister;
import edu.iu.iv.core.algorithm.AlgorithmProperty;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.datamodels.DataModelType;
import edu.iu.iv.core.util.staticexecutable.StaticExecutableRunner;

/**
 * Class to implement the Algorithm for this IVC Plug-in.
 *
 * @author
 */
public class ErdosRandomAlgorithm extends FortranAlgorithm {    
    public static final String ALGORITHM_NAME = "Erdos Random Graph";
    
    /**
     * Creates a new RandomGraphAlgorithm.
     */
	public ErdosRandomAlgorithm(DataModel dm) {
		super(dm);
	    propertyMap.put(AlgorithmProperty.LABEL, ALGORITHM_NAME);
	    
        parameterMap.putIntOption("nodes", "Number of Nodes:",
				"Enter the number of nodes in the network", 100, null);
        parameterMap.putDoubleOption("link_probability", "Link Probability:",
				"Enter the link probability", 0.01, null);
        /*
		parameterMap.putIntOption("edges", "Number of Edges:",
				"Enter the number of edges in the network", 10, null);
		parameterMap.putDoubleOption("dampingFactor", "Dampening Factor",
				"Enter the dampening factor to use", 0.001, new Validator() {

					public boolean isValid(Object value) {
						double val = ((Double) value).doubleValue();

						return val >= 0.0 && val <= 1.0;
					}
				});
		*/
	}
	
	/**
	 * Executes this RandomGraphAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute(){
	    StaticExecutableRunner runner = new StaticExecutableRunner();

		runner.registerFile("network", 
				           "network file", 
				           DataModelType.NETWORK,
				           new BasicEdgePersister(parameterMap.getIntValue("nodes")));
		runner.setParentFilename("network");
		
		makeInputFile(runner, "inputfile");
		runner.execute(ErdosRandomPlugin.ID_PLUGIN);

		return true;
	}
}