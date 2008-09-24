package edu.iu.iv.modeling.tarl.main;

import java.io.File;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.input.HelperParameters;


/**
 * This interface defines the functions for the <code>TarlHelper</code> which mainly assists the <code>TarlMain</code> in carrying out its functions.  It takes care of the details of initializing the Tarl model, running it, freeing the resources and producing the desired output.<br>
 *
 * @author Jeegar T Maru
 */
public interface TarlHelper {
	/**
	 * Initializes the Tarl model.  This function accepts the <code>HelperParameters</code> to be used for initialization as well as when running the model.  Upon execution, the Tarl model should be initialized to be run in the future.
	 *
	 * @param helperParameters Specifies the input parameters for the model to be used for initialization as well as when the model is run
	 *
	 * @exception TarlException if the model constraints are violated
	 */
	public void initializeModel(HelperParameters helperParameters, File agingFunctionFile)
		throws TarlException;

	/**
	 * Runs the Tarl model.  This function should be called only after initialization.
	 *
	 * @exception TarlException if the model constraints are violated
	 */
	public void runModel() throws TarlException;

	/**
	 * Stores the output of the model on the file system.  The output files should contain information about the results that the model produced.
	 *
	 * @param outputFolderName Specifies the name of the output folder
	 */
	public void writeOutputFiles(String outputFolderName);

	/**
	 * Cleans up the system.  This function frees the resources that the Tarl system used while running the model.  After calling this function, the Tarl model loses all its data and hence is not able to produce any output.  Thus, output data must be gathered before cleaning up the system.
	 */
	public void cleanUpSystem();
	
	/**
	 * @return The executer class that this helper class uses to execute the TARL algorithm.
	 */
	public TarlExecuter getTarlExecuter() ;
}
