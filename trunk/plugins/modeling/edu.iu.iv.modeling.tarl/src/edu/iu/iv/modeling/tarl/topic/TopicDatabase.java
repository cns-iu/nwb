package edu.iu.iv.modeling.tarl.topic;

import java.util.NoSuchElementException;


/**
 * This interface defines the <code>TopicDatabase</code> which is used to store all the <code>Topics</code> ever produced in the system.  The functions are responsible for actions like initializing the database, adding a new <code>Topic</code> to the database, iterating through each <code>Topic</code> of the database, removing all the <code>Topics</code> from the database, etc. <br>
 * Apart from these functions, the major responsibility of the database is to tag each <code>Topic</code> with a unique identifier which distinguishes it from other <code>Topics</code>. <br>
 *
 * @author Jeegar T Maru
 * @see TopicGroup
 * @see TopicManager
 */
public interface TopicDatabase {
	/**
	 * Adds a new default <code>Topic</code> to the database.  The system should store the <code>Topic</code> for retreival in the future.
	 */
	public void addTopic();

	/**
	 * Returns all the <code>Topics</code> in the database
	 *
	 * @return the group of topics in the database
	 */
	public TopicGroup getTopics();

	/**
	 * Returns the number of <code>Topics</code> in the database
	 *
	 * @return the number of topics in the database
	 */
	public int size();

	/**
	 * Resets the Search Index to the start of the list of <code>Topics</code>.  This function is called just before iterating through the <code>Topics</code>.
	 */
	public void resetSearchIndex();

	/**
	 * Tests whether the database has more <code>Topics</code> for iteration
	 *
	 * @return true, if the database has more topics to visit
	 */
	public boolean hasMoreTopics();

	/**
	 * Returns the next <code>Topic</code> in the database while iteration
	 *
	 * @return the next topic in the database
	 *
	 * @exception NoSuchElementException if the database has no more topics
	 */
	public Topic getNextTopic() throws NoSuchElementException;

	/**
	 * Removes all the <code>Topics</code> from the database
	 */
	public void removeAll();
}
