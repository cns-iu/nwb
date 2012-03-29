package edu.iu.iv.modeling.tarl.input.impl;

import edu.iu.iv.modeling.tarl.input.HelperParameters;
import edu.iu.iv.modeling.tarl.input.MainParameters;


/**
 * This class defines a default implementation of the <code>MainParametersInterface</code> interface.  It defines the various parameters on which the behavior of the model depends and defines methods to access them.  This class is directly used by the <code>DefaultTarlMain</code> class and is passed subsequently to other classes.
 *
 * @author Jeegar T Maru
 * @see MainParameters
 */
public class DefaultMainParameters implements MainParameters {
	/**
	 * Stores the <code>HelperParameters</code> for the model
	 */
	protected HelperParameters helperParameters;

	/**
	 * Creates a new default instance of a <code>MainParameters</code>.  It initializes the parameters using default values as described in the PNAS paper.
	 */
	public DefaultMainParameters() {
		helperParameters = new DefaultHelperParameters();
	}

	/**
	 * Initializes the <code>MainParameters</code> with default values
	 */
	public void initializeDefault() {
		helperParameters.initializeDefault();
	}

	/**
	 * Returns the <code>HelperParameters</code> for the model
	 *
	 * @return the helper parameters for the model
	 */
	public HelperParameters getHelperParameters() {
		return helperParameters;
	}

	/**
	 * Stores the <code>HelperParameters</code> for the model
	 *
	 * @param helperParameters Specifies the helper parameters for the model
	 */
	public void setHelperParameters(HelperParameters helperParameters) {
		this.helperParameters = helperParameters;
	}
}
