package edu.iu.iv.modeling.tarl.input;

import java.io.File;


/**
 * This interface is used to read in the Model Parameters for the <code>TarlMainInterface</code> from the specified input script file on the file system
 *
 * @author Jeegar T Maru
 * @see MainParameters
 */
public interface InputReader {
	/**
	 * Initializes the model parameters with values from the given file
	 *
	 * @param inputFile Specifies the input file name to read the parameters from
	 */
	public void initialize(File inputFile);

	/**
	 * Returns the Model Parameters for the model to run.
	 *
	 * @return all the model parameters for the model to run
	 */
	public MainParameters getModelParameters();
}
