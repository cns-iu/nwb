package edu.iu.iv.modeling.tarl.author.impl;

import java.util.Random;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This class defines a default implementation for the <code>AuthorInterface</code>.  The implementation stores the following properties of the <code>Author</code> : <br>
 * <ul>
 * <li> a unique identifier as an integer </li>
 * <li> an active status as a boolean </li>
 * <li> a topic as an instance of <code>TopicInterface</code> </li>
 * <li> the maximum age of the <code>Author</code> as an integer </li>
 * <li> the age of the <code>Author</code> as an integer </li>
 * </ul>
 * <br>
 * Extension : If the model is to be extended to include Agent-based modeling, the Author class would need to be modified/extended to make an Author capable of reasoning and making decisions (basically, making it an Agent).
 * @author Jeegar T Maru
 * @see Topic
 * @see Author
 */
public class DefaultAuthor implements Author {
	/**
	 * Stores the unique id of the Author as an integer
	 */
	protected int id;

	/**
	 * Stores whether the Author is active or not
	 */
	protected boolean active_status;

	/**
	 * Stores the <code>Topic</code> that the Author is interested in
	 */
	protected Topic topic;

	/**
	 * Stores the maximum age uptil which the Author can remain active
	 */
	protected int maximum_age;

	/**
	 * Stores the age of the Author starting from 0
	 */
	protected int age;

	/**
	 * Creates a new instance for an <code>Author</code> with the specified id and <code>Topic</code>.  An Author should have an id and a Topic at the very least.  Assumes that the maximum age of the <code>Author</code> is infinity.
	 */
	public DefaultAuthor() {
		topic = null;
	}

	/**
	 * Creates a new instance for an <code>Author</code> with the specified id and <code>Topic</code>.  An Author should have an id and a Topic at the very least.  Assumes that the maximum age of the <code>Author</code> is infinity.
	 *
	 * @param id Specifies the unique id for the newly instantiated author
	 * @param topic Specifies the topic for the newly instantiated author
	 *
	 * @exception TarlException if the specified topic is null
	 */
	public DefaultAuthor(int id, Topic topic)
		throws TarlException {
		if (topic == null)
			throw (new TarlException(new String(
			        "Authors need to have a valid topic\n")));

		this.id = id;
		this.topic = topic;
	}

	/**
	 * Creates a new instance for an <code>Author</code> with the specified id, <code>Topic</code> and maximum age
	 *
	 * @param id Specifies the unique id for the newly instantiated author
	 * @param topic Specifies the topic for the newly instantiated author
	 * @param maximum_age Specifies the maximum age for the newly instantiated author
	 *
	 * @exception TarlException if the specified topic is null
	 */
	public DefaultAuthor(int id, Topic topic, int maximum_age)
		throws TarlException {
		if (topic == null)
			throw (new TarlException(new String(
			        "Authors need to have a valid topic\n")));

		this.id = id;
		this.topic = topic;
		this.maximum_age = maximum_age;
	}

	/**
	 * Initializes the <code>Author</code> with the specified identifier and <code>Topic</code>
	 *
	 * @param id Specifies the unique identifier of the author
	 * @param topic Specifies the topic of the author
	 */
	public void initialize(int id, Topic topic) {
		this.id = id;
		this.topic = topic;
		active_status = true;
		maximum_age = -10;
		age = 0;
	}

	/**
	 * Initializes the <code>Author</code> with the specified identifier, <code>Topic</code> and the maximum age
	 *
	 * @param id Specifies the unique identifier of the author
	 * @param topic Specifies the topic of the author
	 * @param maximum_age Specifies the maximum age of the author
	 */
	public void initialize(int id, Topic topic, int maximum_age) {
		Random randomGenerator;

		this.id = id;
		this.topic = topic;
		this.maximum_age = maximum_age;
		active_status = true;

		randomGenerator = new Random();

		if (maximum_age > 0)
			age = randomGenerator.nextInt(maximum_age);
		else
			age = 0;
	}

	/**
	 * Returns the id of the <code>Author</code>
	 *
	 * @return the id of the author as an integer
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the <code>Topic</code> of the <code>Author</code
	 *
	 * @return the topic of the author
	 */
	public Topic getTopic() {
		return topic;
	}

	/**
	 * Returns the maximum age of the <code>Author</code
	 *
	 * @return the maximum age of the author as an integer
	 */
	public int getMaximumAge() {
		return maximum_age;
	}

	/**
	 * Returns the age of the <code>Author</code>
	 *
	 * @return the age of the author as an integer
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Tests whether the current <code>Author</code> is the same as the <code>Author</code> given by the parameter
	 *
	 * @param author Specifies the author to be equated against
	 *
	 * @return true, if the current author and the specified author are equal
	 */
	public boolean equals(Author author) {
		if (author == null)
			return false;

		return (id == author.getId());
	}

	/**
	 * Tests whether the <code>Author</code> has infinite age (that is, whether the <code>Author</code> is supposed to remain active throughout the lifetime of the system)
	 *
	 * @return true, if the author has infinite age
	 */
	private boolean hasInfiniteAge() {
		return (maximum_age < 0);
	}

	/**
	 * Tests whether the <code>Author</code> is active for the current year
	 *
	 * @return true if the author is active
	 */
	public boolean isActive() {
		return (active_status);
	}

	/**
	 * Notifies the <code>Author</code> of deactivation.  Asks the <code>Author</code> to not produce any <code>Publications</code> henceforth.
	 */
	public void deactivate() {
		active_status = false;
	}

	/**
	 * Notifies the <code>Author</code> that the current year has terminated.  Allows the <code>Author</code> to increment his/her age or do book-keeping activities.
	 */
	public void yearEndNotification() {
		if ((this.isActive()) && (!this.hasInfiniteAge())) {
			age++;

			if (age >= maximum_age)
				active_status = false;
		}
	}

	/**
	 * Returns the details of the <code>Author</code> as a <code>String</code>
	 *
	 * @return a string describing the author
	 */
	public String toString() {
		StringBuffer sb;

		sb = new StringBuffer();
		sb.append(new String("Id : "));
		sb.append(id);
		sb.append(new String("\nTopic : "));
		sb.append(topic);
		sb.append(new String("Maximum Age : "));
		sb.append(maximum_age);
		sb.append(new String("\nAge : "));
		sb.append(age);
		sb.append(new String("\nActive Status : "));
		sb.append(active_status);
		sb.append(new String("\n"));

		return (sb.toString());
	}
}
