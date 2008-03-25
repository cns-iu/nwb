package edu.iu.iv.modeling.tarl.topic.impl;

import java.util.Collection;

import edu.iu.iv.modeling.tarl.author.impl.DefaultAuthor;
import edu.iu.iv.modeling.tarl.publication.impl.DefaultPublication;
import edu.iu.iv.modeling.tarl.topic.Topic;
import edu.iu.iv.modeling.tarl.util.impl.ExtendedHashSet;


/**
 * This class provides a default implementation of the <code>TopicInterface</code>.  It represents the identifier of the topic as an integer; it associates a name with each <code>Topic</code> and it associates a list of keywords as an <code>ExtendedHashSet</code>.
 *
 * @author Jeegar T Maru
 * @see DefaultAuthor
 * @see DefaultPublication
 * @see Topic
 */
public class DefaultTopic implements Topic {
	/**
	 * Stores the unique id of the Topic
	 */
	protected int id;

	/**
	 * Stores the generic name of the Topic, as a <code>String</code>
	 */
	protected String name;

	/**
	 * Stores the list of keywords pertaining to the Topic, as an <code>ExtendedHashSet</code>
	 */
	protected ExtendedHashSet keywords;

	/**
	 * Creates a new instance for a default <code>Topic</code>.
	 */
	public DefaultTopic() {
		name = new String("default");
		keywords = null;
	}

	/**
	 * Creates a new instance for a <code>Topic</code> with the specified id.  A <code>Topic</code> must have an id at the very least.
	 *
	 * @param id Specifies the unique id for the newly instantiated topic
	 */
	public DefaultTopic(int id) {
		this.id = id;
		name = new String("default");
		keywords = null;
	}

	/**
	 * Creates a new instance for a <code>Topic</code> with the specified id, and the specified name
	 *
	 * @param id Specifies the unique id for the newly instantiated topic
	 * @param name Specifies the name of the newly instantiated topic
	 *
	 * @exception NullPointerException if the specified name is null
	 */
	public DefaultTopic(int id, String name) throws NullPointerException {
		this.id = id;

		if (name == null)
			throw (new NullPointerException(new String(
			        "Parameter name needs to be non-null\n")));

		this.name = new String(name);
		keywords = null;
	}

	/**
	 * Creates a new instance for a <code>DefaultTopic</code> with the specified id, name and <code>Topic</code>
	 *
	 * @param id Specifies the unique id for the newly instantiated topic
	 * @param name Specifies the name of the newly instantiated topic
	 * @param keywords Specifies the extended hashset of keywords for the newly instantiated topic
	 *
	 * @exception NullPointerException if the specified name is null
	 */
	public DefaultTopic(int id, String name, ExtendedHashSet keywords)
		throws NullPointerException {
		this.id = id;

		if (name == null)
			throw (new NullPointerException(new String(
			        "Parameter name needs to be non-null\n")));

		this.name = new String(name);

		try {
			this.keywords = new ExtendedHashSet(keywords);
		} catch (NullPointerException npe) {
			this.keywords = null;
		}
	}

	/**
	 * Initializes the <code>Topic</code> with the given integer identification
	 *
	 * @param id Specifies the integer id used for identification
	 */
	public void initialize(int id) {
		this.id = id;
	}

	/**
	 * Returns the id of the <code>Topic</code>
	 *
	 * @return the id of the topic as an integer
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the <code>Topic</code>
	 *
	 * @return the name of the topic as a string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the keywords of the <code>Topic</code>
	 *
	 * @return the keywords of the topic as a extended hash set
	 */
	public ExtendedHashSet getKeywords() {
		return keywords;
	}

	/**
	 * Stores the name of the <code>Topic</code> as a <code>String</code>
	 *
	 * @param name Specifies the name of the topic
	 */
	public void setName(String name) {
		if (name != null)
			this.name = new String(name);
	}

	/**
	 * Stores the list of keywords of the <code>Topic</code>
	 *
	 * @param keywords Specifies the collection of keywords
	 */
	public void setKeywords(Collection keywords) {
		if (keywords != null)
			this.keywords = new ExtendedHashSet(keywords);
	}

	/**
	 * Tests whether the current <code>Topic</code> is the same as the <code>Topic</code> given by the parameter
	 *
	 * @param topic Specifies the topic to be equated against
	 *
	 * @return true if the current topic and the specified topic are equal
	 */
	public boolean equals(Topic topic) {
		if (topic == null)
			return false;

		return (id == topic.getId());
	}

	/**
	 * Return the details of the <code>Topic</code> as a <code>String</code>
	 *
	 * @return a string describing the topic
	 */
	public String toString() {
		StringBuffer sb;

		sb = new StringBuffer();
		sb.append(new String("Id : "));
		sb.append(id);
		sb.append(new String("\n"));

		return (sb.toString());
	}
}
