package edu.iu.iv.modeling.tarl.input;


/**
 * This interface defines the Parameters used by the Main module of Tarl.  There are no parameters that Main uses directly.  They are used between the Helper, Executer and the Manager modules of Tarl. <br>
 * The behavior of the model depends on these parameters.  For example, the parameter "number of authors at start" determines the number of authors that the system starts with.  These parameters are to be read from the GUI or the script-file.
 *
 * @author Jeegar T Maru
 */
public interface MainParameters {
	/**
	 * Initializes the <code>MainParameters</code> with default values
	 */
	public void initializeDefault();

	/**
	 * Returns the module containing Parameters for the Helper module for Tarl
	 *
	 * @return the module containing Parameters for the Helper module for Tarl
	 */
	public HelperParameters getHelperParameters();
}
