package edu.iu.iv.modeling.tarl.author;

import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This interface defines an <code>Author</code> by defining the necessary functions it should support.  The <code>Author</code< is responsible for producing <code>Publications</code>.  The minimal attributes of an <code>Publication</code> are : <br>
 * <ul>
 * <li> a non-null topic that the <code>Author</code> belongs to which has an equivalent <code>TopicInterface</code> representation </li>
 * </ul>
 * <br>
 * All <code>Authors</code> should also support an identification tag that the <code>AuthorDatabase</code> provides to enable other classes to distinguish between different authors. <br>
 * Some other constraints on authors are as follows : <br>
 * <ul>
 * <li> Each <code>Author</code> should deactivate automatically when his age exceeds the maximum_age specified.  The <code>Author</code> keeps track of his age using the {@link #yearEndNotification()} </li>
 * </ul>
 *
 * @author Jeegar T Maru
 * @see AuthorDatabase
 * @see Topic
 */
public interface Author {
	/**
	 * Initializes the <code>Author</code> with the specified identifier and <code>Topic</code>.  Either of the initialization methods should be called before calling any other method of the <code>Author</code>.
	 *
	 * @param id Specifies the unique identifier of the author
	 * @param topic Specifies the topic of the author
	 */
	public void initialize(int id, Topic topic);

	/**
	 * Initializes the <code>Author</code> with the specified identifier, <code>Topic</code> and the maximum age.  Either of the initialization methods should be called before calling any other method of the <code>Author</code>.
	 *
	 * @param id Specifies the unique identifier of the author
	 * @param topic Specifies the topic of the author
	 * @param maximumAge Specifies the maximum age of the author
	 */
	public void initialize(int id, Topic topic, int maximumAge);

	/**
	 * Returns the unique identifier assigned by the <code>AuthorDatabaseInterface</code> of the <code>Author</code> as an integer
	 *
	 * @return the unique identifier of the author
	 */
	public int getId();

	/**
	 * Returns the <code>Topic</code> of the <code>Author</code>
	 *
	 * @return the topic of the author
	 */
	public Topic getTopic();

	/**
	 * Tests whether the current <code>Author</code> is the same as the specified one.  This test should be based on the identification tag that the <code>AuthorDatabase</code> has assigned to both the <code>Authors</code>.
	 *
	 * @param authorInterface Specifies the author to be equated against
	 *
	 * @return true, if the current author is equal to the specified one
	 */
	public boolean equals(Author authorInterface);

	/**
	 * Tests whether the <code>Author</code> is active
	 *
	 * @return true, if the author is active
	 */
	public boolean isActive();

	/**
	 * Deactivates the <code>Author</code> explicitly which stops him/her from producing any more publications.
	 */
	public void deactivate();

	/**
	 * Asks the <code>Author</code> to note the end of year.  The <code>Author</code> needs to update his/her age to reflect the fact.
	 */
	public void yearEndNotification();
}
