package edu.iu.iv.modeling.tarl.input;

import edu.iu.iv.modeling.tarl.util.YearInformation;


/**
* This interface defines the Parameters for the Helper module of Tarl.  These parameters are used only by the Helper module.
*
* @author Jeegar T Maru
* @see MainParameters
*/
public interface HelperParameters {
	/**
	 * Initializes the <code>HelperParameters</code> with default values
	 */
	public void initializeDefault();

	/**
	  * Returns the <code>YearInformation</code> for the model
	  *
	  * @return the year information for the model
	  */
	public YearInformation getYearInformation();

	/**
	 * Returns the ParametersInterface for the Executer module of Tarl
	 *
	 * @return the parameters interface for the executer module of tarl
	 */
	public ExecuterParameters getExecuterParameters();
}
