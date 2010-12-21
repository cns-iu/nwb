package edu.iu.iv.modeling.tarl.input.impl;

import edu.iu.iv.modeling.tarl.input.ExecuterParameters;
import edu.iu.iv.modeling.tarl.input.HelperParameters;
import edu.iu.iv.modeling.tarl.main.impl.DefaultTarlHelper;
import edu.iu.iv.modeling.tarl.util.YearInformation;


/**
 * This class defines a default implementation of the <code>HelperParametersInterface</code> interface.  It defines model parameters which are needed by the <code>TarlHelper</code> and methods to access them.
 *
 * @author Jeegar T Maru
 * @see DefaultTarlHelper
 * @see DefaultExecuterParameters
 */
public class DefaultHelperParameters implements HelperParameters {
	/**
	 * Stores the <code>ExecuterParameters</code> for the model
	 */
	protected ExecuterParameters executerParameters;

	/**
	 * Creates a new default instance of an <code>HelperParameters</code>.  It initializes the parameters using default values as described in the PNAS paper.
	 */
	public DefaultHelperParameters() {
		executerParameters = new DefaultExecuterParameters();
	}

	/**
	 * Initializes the <code>HelperParameters</code> with the specified values.
	 */
	public void initializeDefault() {
		executerParameters.initializeDefault();
	}

	/**
	 * Returns the <code>YearInformation</code> for the model
	 *
	 * @return the year information for the model
	 */
	public YearInformation getYearInformation() {
		return executerParameters.getYearInformation();
	}

	/**
	 * Returns the <code>ExecuterParameters</code> for the model
	 *
	 * @return the executer parameters for the model
	 */
	public ExecuterParameters getExecuterParameters() {
		return executerParameters;
	}

	/**
	 * Stores the <code>ExecuterParameters</code> for the model
	 *
	 * @param executerParameters the executer parameters for the model
	 */
	public void setExecuterParameters(
	    ExecuterParameters executerParameters) {
		this.executerParameters = executerParameters;
	}
}
