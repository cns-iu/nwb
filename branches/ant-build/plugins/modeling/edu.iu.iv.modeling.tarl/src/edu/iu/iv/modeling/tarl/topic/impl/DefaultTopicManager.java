package edu.iu.iv.modeling.tarl.topic.impl;

import edu.iu.iv.modeling.tarl.input.TopicParameters;
import edu.iu.iv.modeling.tarl.topic.Topic;
import edu.iu.iv.modeling.tarl.topic.TopicDatabase;
import edu.iu.iv.modeling.tarl.topic.TopicGroup;
import edu.iu.iv.modeling.tarl.topic.TopicManager;


/**
 * This class defines a default implementation of the <code>TopicManagerInterface</code>.  It uses the default implementations of the standard interfaces for its cause.
 *
 * @author Jeegar T Maru
 * @see DefaultTopicDatabase
 * @see TopicManager
 */
public class DefaultTopicManager implements TopicManager {
	/**
	 * Stores all the <code>Topics</code> involved in the system
	 */
	protected TopicDatabase topics;

	/**
	 * Creates a new instance of the <code>TopicManager</code>
	 */
	public DefaultTopicManager() {
		topics = null;
	}

	/**
	 * Initializes the <code>Topic</code> environment to enable various operations on the <code>Topics</code>.  Creates new topics based on the model parameters of the topic environment.
	 *
	 * @param topicParameters Specifies the model parameters related to topics
	 */
	public void initializeTopics(TopicParameters topicParameters) {
		int i;
		int numTopics;

		topics = new DefaultTopicDatabase();
		numTopics = topicParameters.getNumTopics();

		for (i = 0; i < numTopics; i++)
			topics.addTopic();
	}

	/**
	 * Returns a random <code>Topic</code> from the database
	 *
	 * @return a random topic
	 */
	public Topic getRandomTopic() {
		return (((DefaultTopicGroup)topics.getTopics()).getRandomTopic());
	}

	/**
	 * Returns the group of all the <code>Topics</code> in the system
	 *
	 * @return Group of all the topics
	 */
	public TopicGroup getTopics() {
		return topics.getTopics();
	}

	/**
	 * Removes every <code>Topic</code> from its database
	 */
	public void cleanUpTopic() {
		topics.removeAll();
	}
}
