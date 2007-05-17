package edu.iu.iv.modeling.tarl.input;

import edu.iu.iv.modeling.tarl.TarlException;


/**
 * This interface defines the Parameters for the <code>TopicManager</code> module of Tarl.  These parameters are used only by the <code>TopicManager</code> module.
 *
 * @author Jeegar T Maru
 * @see ExecuterParameters
 */
public interface TopicParameters {
	/**
	 * Initializes the <code>TopicParameters</code> with default values
	 */
	public void initializeDefault();

	/**
	 * Returns the number of <code>Topics</code> for the Model
	 *
	 * @return the number of topics for the Model
	 */
	public int getNumTopics();

	/**
	 * Stores the number of <code>Topics</code> for the Model
	 *
	 * @param numTopics the number of topics for the Model
	 *
	 * @exception TarlException if the field is set with an invalid value
	 */
	public void setNumTopics(int numTopics) throws TarlException;
}
