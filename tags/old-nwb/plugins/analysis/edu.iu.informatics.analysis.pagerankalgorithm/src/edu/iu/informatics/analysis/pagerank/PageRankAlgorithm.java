/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.analysis.pagerank;

import edu.iu.informatics.shared.FortranAnalysisAlgorithm;
import edu.iu.informatics.shared.persisters.BasicEdgePersister;
import edu.iu.iv.common.parameter.Validator;
import edu.iu.iv.core.algorithm.AlgorithmProperty;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.util.staticexecutable.StaticExecutableRunner;

/**
 * Class to implement the Algorithm for this IVC Plug-in.
 * 
 * @author
 */
public class PageRankAlgorithm extends FortranAnalysisAlgorithm {
    public static final String NAME = "Page Rank Algorithm";

	/**
	 * Creates a new PageRankAlgorithm.
	 */
	public PageRankAlgorithm(DataModel dm) { 
        super(dm);
	    propertyMap.put(AlgorithmProperty.LABEL, NAME);
        
        parameterMap.putDoubleOption("dampingFactor", "Dampening Factor",
				"Enter the dampening factor to use", 0.001, new Validator() {

					public boolean isValid(Object value) {
						double val = ((Double) value).doubleValue();

						return val >= 0.0 && val <= 1.0;
					}
				});
	}

	/**
	 * Executes this PageRankAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute() {
        StaticExecutableRunner runner = new StaticExecutableRunner();

//        setDefaultParameters();
        saveDataModel(runner, "network", new BasicEdgePersister());
        makeInputFile(runner, "inputfile", getDefaultParameters(), true);

        runner.execute(PageRankAlgorithmPlugin.PLUGIN_ID);
        
		return true;
	}
}