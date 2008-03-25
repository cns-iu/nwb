package edu.iu.iv.modeling.tarl.topic;

import java.util.Collection;
import java.util.Iterator;


/**
 * This interface defines the functions to represent a group of <code>Topics</code>.  It should support various operations on a group of <code>Topics</code> like returning an iterator over the group, adding a <code>Topic</code> to the group, testing whether a <code>Topic</code> is a member of the group, retrieving random <code>Topic</code>, etc. <br>
 * A major constraint on the <code>TopicGroup</code> is to include only <code>Topics</code> as its members.
 *
 * @author Jeegar T Maru
 * @see Topic
 */
public interface TopicGroup {
	/**
	 * Initializes the group of <code>Topics</code> with the specified <code>Collection</code>
	 *
	 * @param collection Specifies the collection of topics
	 */
	public void initialize(Collection collection);

	/**
	 * Returns the <code>Iterator</code> over the Group of <code>Topics</code>.  The <code>Iterator</code> should iterate only over the non-duplicate members of the <code>TopicGroup</code>.  The order of iteration does not matter as long as it covers all the non-duplicate members of the Group.
	 *
	 * @return the iterator over the group of topics
	 */
	public Iterator getIterator();

	/**
	 * Returns all the <code>Topics</code> in the group as a <code>Collection</code>
	 *
	 * @return the collection of all topics in the group
	 */
	public Collection getTopics();

	/**
	 * Returns the number of <code>Topics</code> in the Group
	 *
	 * @return the number of topics in the group
	 */
	public int size();

	/**
	 * Adds a <code>Topic</code> to the group
	 *
	 * @param topic Specifies the topic to be added
	 */
	public void addTopic(Topic topic);

	/**
	 * Removes all the <code>Topic</code> from the Group
	 */
	public void removeAllTopics();

	/**
	 * Tests whether the Group contains the <code>Topic</code> or not
	 *
	 * @param topic Specifies the topic to be tested
	 *
	 * @return true, if the topic is contained in the group
	 */
	public boolean containsTopic(Topic topic);

	/**
	 * Returns a random <code>Topic</code> from the Group
	 *
	 * @return a random topic
	 */
	public Topic getRandomTopic();
}
