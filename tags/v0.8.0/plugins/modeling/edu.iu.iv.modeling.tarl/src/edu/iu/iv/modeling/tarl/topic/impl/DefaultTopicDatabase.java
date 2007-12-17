package edu.iu.iv.modeling.tarl.topic.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.iu.iv.modeling.tarl.topic.Topic;
import edu.iu.iv.modeling.tarl.topic.TopicDatabase;
import edu.iu.iv.modeling.tarl.topic.TopicGroup;


/**
 * This class defines a default implementation of <code>TopicDatabaseInterface</code>.  It uses an instance of  <code>DefaultTopicGroup</code> to store all the topics.
 *
 * @author Jeegar T Maru
 * @see DefaultTopic
 * @see DefaultTopicGroup
 */
public class DefaultTopicDatabase implements TopicDatabase {
	/**
	 * Stores the list of Topics as a <code>TopicGroup</code>
	 */
	protected TopicGroup topics;

	/**
	 * Stores the iterator to be used for the list of Topics
	 */
	protected Iterator iterator;

	/**
	 * Creates a new instance for a <code>TopicDatabase</code>
	 */
	public DefaultTopicDatabase() {
		topics = new DefaultTopicGroup();
		iterator = topics.getIterator();
	}

	/**
	 * Adds a <code>Topic</code> to the list
	 */
	public void addTopic() {
		Topic topic;

		topic = new DefaultTopic();
		topic.initialize(topics.size());
		topics.addTopic(topic);
	}

	/**
	 * Removes all the <code>Topics</code> from the database
	 */
	public void removeAll() {
		topics.removeAllTopics();
	}

	/**
	 * Returns the list of <code>Topics</code> in the Database
	 *
	 *@return the list of topics in the database
	 */
	public TopicGroup getTopics() {
		return topics;
	}

	/**
	 * Returns the number of <code>Topics</code> in the Database
	 *
	 * @return the number of topics in the database
	 */
	public int size() {
		return (topics.size());
	}

	/**
	 * Resets the Search Index so the Database can be searched iteratively
	 */
	public void resetSearchIndex() {
		iterator = topics.getIterator();
	}

	/**
	 * Tests whether the Database has more <code>Topics</code> for iteration
	 *
	 * @return true if the database has more topics to iterate over
	 */
	public boolean hasMoreTopics() {
		return (iterator.hasNext());
	}

	/**
	 * Returns the next <code>Topic</code> in the database
	 *
	 * @return the next topic of the iteration
	 *
	 * @exception NoSuchElementException if the iteration has no more topics
	 */
	public Topic getNextTopic() throws NoSuchElementException {
		return ((DefaultTopic)iterator.next());
	}

	/**
	 * Returns the details of the <code>TopicDatabase</code> as a <code>String</code>
	 *
	 * @return a string describing the topicdatabase
	 */
	public String toString() {
		return (topics.toString());
	}
}
