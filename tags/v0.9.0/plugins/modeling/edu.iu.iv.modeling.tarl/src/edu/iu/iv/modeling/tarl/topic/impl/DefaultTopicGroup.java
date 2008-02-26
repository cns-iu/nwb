package edu.iu.iv.modeling.tarl.topic.impl;

import java.util.Collection;
import java.util.Iterator;

import edu.iu.iv.modeling.tarl.topic.Topic;
import edu.iu.iv.modeling.tarl.topic.TopicGroup;
import edu.iu.iv.modeling.tarl.util.impl.ExtendedHashSet;


/**
 * This class defines the Group of Topics.  It provides facilities for duplicate detection, and for iterating over the group.  It also provides some sophisticated Group operations (borrowed from ExtendedHashSet) such as union, intersection, random subgroup extraction, etc.
 *
 * @author Jeegar T Maru
 * @see DefaultTopic
 * @see ExtendedHashSet
 * @see TopicGroup
 */
public class DefaultTopicGroup implements TopicGroup {
	/**
	 * Stores a group of Topics
	 */
	protected ExtendedHashSet topics;

	/**
	 * Creates a new instance of an empty <code>TopicGroup</code>
	 */
	public DefaultTopicGroup() {
		topics = new ExtendedHashSet();
	}

	/**
	 * Creates a new instance for a <code>TopicGroup</code> as an exact replica of the specified <code>TopicGroup</code> object
	 *
	 * @param topics Specifies the topic group to be replicated
	 *
	 * @exception NullPointerException if the parameter topics is null
	 */
	public DefaultTopicGroup(TopicGroup topics)
		throws NullPointerException {
		if (topics == null)
			throw (new NullPointerException(new String(
			        "TopicGroup cannot be initialized with a null topicgroup\n")));

		this.topics = new ExtendedHashSet(topics.getTopics());
	}

	/**
	 * Initializes the group of <code>Topics</code> with the specified <code>Collection</code>
	 *
	 * @param collection Specifies the collection of topics
	 */
	public void initialize(Collection collection) {
		topics = new ExtendedHashSet(collection);
	}

	/**
	 * Returns the Group of <code>Topics</code>
	 *
	 * @return the group of topics
	 */
	public Collection getTopics() {
		return (topics);
	}

	/**
	 * Returns the <code>Iterator</code> over the Group of <code>Topics</code>
	 *
	 * @return the iterator over the group of topics
	 */
	public Iterator getIterator() {
		return (topics.iterator());
	}

	/**
	 * Returns the number of topics in the Group
	 *
	 * @return the number of topics in the group
	 */
	public int size() {
		return (topics.size());
	}

	/**
	 * Adds a <code>Topic</code> to the group only if it is not already present in the group
	 *
	 * @param topic Specifies the topic to be added
	 */
	public void addTopic(Topic topic) {
		topics.add((DefaultTopic)topic);
	}

	/**
	 * Tests whether the Group contains the <code>Topic</code> or not
	 *
	 * @param topic Specifies the topic to be tested
	 *
	 * @return true if the topic is contained in the group
	 */
	public boolean containsTopic(Topic topic) {
		return (topics.contains((DefaultTopic)topic));
	}

	/**
	 * Removes the <code>Topic</code> from the Group
	 *
	 * @param topic Specifies the topic to be removed
	 *
	 * @return true if the topic was successfully found and removed
	 */
	public boolean removeTopic(Topic topic) {
		return (topics.remove((DefaultTopic)topic));
	}

	/**
	 * Removes all the <code>Topics</code> from the Group
	 */
	public void removeAllTopics() {
		topics.clear();
	}

	/**
	 * Returns a Random <code>Topic</code> from the <code>TopicGroup</code>
	 *
	 * @return a random topic from the topic group
	 */
	public Topic getRandomTopic() {
		return ((DefaultTopic)topics.getRandomElement());
	}

	/**
	 * Returns the specified number of different random <code>Topics</code> from the <code>TopicGroup</code> as a <code>TopicGroup</code>.  If the specified number is larger than the size of the <code>TopicGroup</code>, the entire <code>TopicGroup</code> is returned.
	 *
	 * @param num_elements Specifies the number of topics required
	 *
	 * @return the topic group of different random topics
	 */
	public TopicGroup getRandomTopics(int num_elements) {
		TopicGroup result;
		Iterator iterator;
		ExtendedHashSet ext_hash_set;

		result = new DefaultTopicGroup();
		ext_hash_set = topics.getRandomElements(num_elements);
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addTopic((DefaultTopic)iterator.next());

		return (result);
	}

	/**
	 * Modifies the <code>TopicGroup</code> to be the union of itself and the specified <code>TopicGroup</code>.  It acts like the operation (a = a union b), where a is the current <code>TopicGroup</code> and b is the specified <code>TopicGroup</code>
	 *
	 * @param topic_group Specifies the topic group to be unioned with
	 */
	public void union(TopicGroup topic_group) {
		topics.union(topic_group.getTopics());
	}

	/**
	 * Returns the union of two specified <code>TopicGroups</code> as a <code>TopicGroup</code>.  Does not change any of the <code>AuthroGroups</code>.
	 *
	 * @param topic_group1 Specifies the first topic group
	 * @param topic_group2 Specifies the second topic group
	 *
	 * @return the topicgroup containing the union
	 */
	public static TopicGroup union(TopicGroup topic_group1,
	    TopicGroup topic_group2) {
		ExtendedHashSet ext_hash_set;
		TopicGroup result;
		Iterator iterator;

		result = new DefaultTopicGroup();
		ext_hash_set =
			ExtendedHashSet.union(topic_group1.getTopics(),
			    topic_group2.getTopics());
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addTopic((DefaultTopic)iterator.next());

		return result;
	}

	/**
	 * Modifies the <code>TopicGroup</code> to be the intersection of the Group and the specified <code>TopicGroup</code>.  It acts like the operation (a = a intersection b), where a is the current <code>TopicGroup</code> and b is the specified <code>TopicGroup</code>
	 *
	 * @param topic_group Specifies the topic group to be intersected with
	 */
	public void intersection(TopicGroup topic_group) {
		topics.intersection(topic_group.getTopics());
	}

	/**
	 * Returns the intersection of two specified <code>TopicGroups</code>.  Does not change any of the <code>TopicGroups</code>.
	 *
	 * @param topic_group1 Specifies the first topic group
	 * @param topic_group2 Specifies the second topic group
	 *
	 * @return the topic group containing the intersection
	 */
	public static TopicGroup intersection(
	    TopicGroup topic_group1, TopicGroup topic_group2) {
		ExtendedHashSet ext_hash_set;
		TopicGroup result;
		Iterator iterator;

		result = new DefaultTopicGroup();
		ext_hash_set =
			ExtendedHashSet.intersection(topic_group1.getTopics(),
			    topic_group2.getTopics());
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addTopic((DefaultTopic)iterator.next());

		return result;
	}

	/**
	 * Modifies the Group to be the difference of the current <code>TopicGroup</code> and the specified <code>TopicGroup</code>.  It acts like the operation (a = a - b), where a is the current <code>TopicGroup and b is the specified <code>TopicGroup</code>
	 *
	 * @param topic_group Specifies the topic group to be intersected with
	 */
	public void difference(TopicGroup topic_group) {
		topics.difference(topic_group.getTopics());
	}

	/**
	 * Returns the difference of the two specified <code>TopicGroups</code>.  Does not change any of the <code>TopicGroups</code>.  The operation can be expressed as (a - b), where a and b are the <code>TopicGroups</code> as defined by the first and second argument respectively.
	 *
	 * @param topic_group1 Specifies the first topic group
	 * @param topic_group2 Specifies the second topic group
	 *
	 * @return the topic group containing the difference
	 */
	public static TopicGroup difference(
	    TopicGroup topic_group1, TopicGroup topic_group2) {
		ExtendedHashSet ext_hash_set;
		TopicGroup result;
		Iterator iterator;

		result = new DefaultTopicGroup();
		ext_hash_set =
			ExtendedHashSet.difference(topic_group1.getTopics(),
			    topic_group2.getTopics());
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addTopic((DefaultTopic)iterator.next());

		return result;
	}

	/**
	 * Tests whether the <code>TopicGroup<code> specified by the second argument is a subset of the <code>TopicGroup</code> specified by the first argument
	 *
	 * @param topic_group1 Specifies the first topic group
	 * @param topic_group2 Specifies the second topic group
	 *
	 * @return true, if topic_group2 is a subset of topic_group1
	 */
	public static boolean isSubset(TopicGroup topic_group1,
	    TopicGroup topic_group2) {
		return ((topic_group1.getTopics()).containsAll(topic_group2.getTopics()));
	}

	/**
	 * Tests whether the two <code>TopicGroups</code> are equal or not in terms of Set Equality.  Two topicgroups are Group Equal if first is the subgroup of the second and the second is a subgroup of the first.
	 *
	 * @param topic_group1 Specifies the first topic group
	 * @param topic_group2 Specifies the second topic group
	 *
	 * @return true, if the two topic groups are set equal
	 */
	public static boolean areSetEqual(TopicGroup topic_group1,
	    TopicGroup topic_group2) {
		return ((DefaultTopicGroup.isSubset(topic_group1, topic_group2))
		&& (DefaultTopicGroup.isSubset(topic_group2, topic_group1)));
	}

	/**
	 * Returns the details of the <code>TopicGroup</code> as a <code>String</code>
	 *
	 * @return a string describing the topic group
	 */
	public String toString() {
		StringBuffer sb;
		Iterator iterator;

		sb = new StringBuffer();

		iterator = getIterator();
		sb.append(new String(" [ "));

		while (iterator.hasNext()) {
			sb.append(((DefaultTopic)iterator.next()).getId());
			sb.append(new String("\t"));
		}

		sb.append(new String(" ]\n"));

		return (sb.toString());
	}
}
