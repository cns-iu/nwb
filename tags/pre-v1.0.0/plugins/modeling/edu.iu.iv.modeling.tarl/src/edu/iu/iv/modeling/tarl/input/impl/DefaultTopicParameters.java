package edu.iu.iv.modeling.tarl.input.impl;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.input.TopicParameters;
import edu.iu.iv.modeling.tarl.topic.impl.DefaultTopicManager;


/**
 * This class defines a default implementation of the <code>TopicParametersInterface</code> interface.  It defines model parameters which are related to <code>Topics</code> and methods to access them.  This class is directly used by the <code>DefaultTopicManager</code> class.
 *
 * @author Jeegar T Maru
 * @see DefaultTopicManager
 * @see DefaultExecuterParameters
 */
public class DefaultTopicParameters implements TopicParameters {
	/**
	 * Stores the number of topics for the Model
	 */
	protected int numTopics;

	/**
	 * Creates a new default instance for <code>TopicParameters</code>.  The default values are the values mentioned in the PNAS paper.
	 */
	public DefaultTopicParameters() {
		numTopics = 3;
	}

	/**
	 * Initializes the <code>TopicParameters</code> with default values
	 */
	public void initializeDefault() {
		this.numTopics = 3;
	}

	/**
	 * Returns the number of topics for the model
	 *
	 * @return the number of topics for the model
	 */
	public int getNumTopics() {
		return numTopics;
	}

	/**
	 * Stores the number of topics for the model
	 *
	 * @param numTopics the number of topics for the Model
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumTopics(int numTopics) throws TarlException {
		if (numTopics > 0)
			this.numTopics = numTopics;
		else
			throw (new TarlException(new String(
			        "Number of topics cannot be zero or negative\n")));
	}
}
