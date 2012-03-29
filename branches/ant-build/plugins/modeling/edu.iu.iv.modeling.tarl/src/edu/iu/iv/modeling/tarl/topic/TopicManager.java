package edu.iu.iv.modeling.tarl.topic;

import edu.iu.iv.modeling.tarl.input.TopicParameters;


/**
 * This interface defines all the high-level functions related to <code>Topics</code>.  The <code>TarlExecuter</code> solely uses this interface to deal with the <code>Topic</code> environment.  Some of the functions related to topics are initializing the topics environment, retrieving a random topic from the database and cleaning up the topic environment.
 *
 * @author Jeegar T Maru
 * @see TopicDatabase
 */
public interface TopicManager {
	/**
	 * Initializes the <code>Topic</code> environment to enable various operations on the <code>Topics</code>.  This function should be called before calling any other function in <code>TopicManager</code>.
	 *
	 * @param topicParameters Specifies the model parameters related to topics
	 */
	public void initializeTopics(TopicParameters topicParameters);

	/**
	 * Returns a random <code>Topic</code> from the database
	 *
	 * @return a random topic
	 */
	public Topic getRandomTopic();

	/**
	 * Returns the group of all the <code>Topics</code> in the system
	 *
	 * @return Group of all the topics
	 */
	public TopicGroup getTopics();

	/**
	 * Cleans up the <code>Topic</code> environment.  It is responsible for freeing up resources that the <code>Topic</code> environment has used for its execution.
	 */
	public void cleanUpTopic();
}
