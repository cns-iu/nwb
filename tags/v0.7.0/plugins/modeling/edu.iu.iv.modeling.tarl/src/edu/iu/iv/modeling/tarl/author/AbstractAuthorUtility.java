package edu.iu.iv.modeling.tarl.author;

import java.util.Collection;

import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This abstract class defines the functions in order to filter
 * <code>Authors</code> based on their attributes. Some of the functions that
 * it fulfils are filtering based on the <code>Topic</code> and whether he/she
 * is active.
 *
 * @author Jeegar T Maru
 * @see Author
 * @see AuthorGroup
 */
public abstract class AbstractAuthorUtility {
	/**
	 * Filters based on the <code>Topic</code> of <code>Author</code> on the
	 * conditions of equality with the specified <code>Topic</code>. It
	 * returns a group of all the <code>Authors</code> which have the
	 * specified <code>Topic</code>.
	 *
	 * @param authorGroup
	 *            Specifies the group of authors on which to apply the filter
	 * @param topic
	 *            Specifies the topic of author to be equated against
	 *
	 * @return the group of authors which belong to the specified topic
	 */
	public static AuthorGroup filterOnTopicEquality(
	    AuthorGroup authorGroup, Topic topic) {
		return null;
	}

	/**
	 * Filters out <code>Authors</code> that are deactivated. It returns a
	 * group of all the <code>Authors</code> which are active.
	 *
	 * @param authorGroup
	 *            Specifies the group of authors on which to apply the filter
	 *
	 * @return the group of all the authors in the group that are active
	 */
	public static AuthorGroup getAllActiveAuthors(
	    AuthorGroup authorGroup) {
		return null;
	}

	/**
	 * Partitions the <code>Authors</code> on the basis of <code>Topic</code>.
	 * The method returns a <code>Collection</code> of
	 * <code>AuthorsTopicBuckets</code> each with a diferent
	 * <code>Topic</code>.
	 *
	 * @return the collection of authors-topic buckets each with a different
	 *         topic
	 */
	public static Collection partitionOnTopic(AuthorGroup authorGroup) {
		return null;
	}
}
