package edu.iu.iv.modeling.tarl.topic;


/**
 * This interface defines a <code>Topic</code> by defining the necessary functions it should support.  Each <code>Author</code> and <code>Publication</code> possess a <code>Topic</code> as an attribute.<br>
 * All <code>Topics</code> should also support an identification tag that the <code>TopicDatabase</code> provides to enable other classes to distinguish between different <code>Topics</code>.
 *
 * @author Jeegar T Maru
 * @see TopicDatabase
 */
public interface Topic {
	/**
	 * Returns the unique identifier assigned by the <code>TopicDatabaseInterface</code> of the <code>Topic</code> as an integer
	 *
	 * @return the unique identifier of the topic
	 */
	public int getId();

	/**
	 * Initialize the <code>Topic</code> with the given integer as the unique identifier.  This method should be called by a <code>TopicDatabaseInterface</code> just after creating a new <code>Topic</code>.
	 *
	 * @param id Specifies the unique identifier for the topic
	 */
	public void initialize(int id);

	/**
	 * Tests whether the current <code>Topic</code> is the same as the specified one.  This test should be based on the identification tag that the <code>TopicDatabase</code> has assigned to both the <code>Topics</code>.
	 *
	 * @param topicInterface Specifies the topic to be equated against
	 *
	 * @return true, if the current topic is equal to the specified one
	 */
	public boolean equals(Topic topicInterface);
}
