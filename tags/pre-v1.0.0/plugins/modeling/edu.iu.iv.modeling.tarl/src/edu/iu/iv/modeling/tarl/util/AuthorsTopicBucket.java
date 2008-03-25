package edu.iu.iv.modeling.tarl.util;

import java.util.Collection;

import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This interface defines the concept of a group of <code>Authors</code> all having the same <code>Topic</code>.  The <code>AuthorsTopicBucket</code> stores a <code>Topic</code> that represents the common <code>Topic</code> of all the <code>Authors</code>.  An <code>Author</code> is added only if his/her <code>Topic</code> is the same as the <code>Topic</code> of the <code>AuthorsTopicBucket</code>.
 *
 * @author Jeegar T Maru
 * @see AuthorGroup
 * @see Topic
 */
public interface AuthorsTopicBucket {
	/**
	 * Initializes the Bucket with <code>Authors</code> from the collection.  It can assume that the collection contains <code>AuthorInterfaces</code>
	 *
	 * @param collection Specifies the collection of authors
	 */
	public void initialize(Collection collection);

	/**
	 * Adds an <code>Author</code> only if his/her <code>Topic</code> is the same as that of the Bucket
	 *
	 * @param author the author to be added to the bucket
	 */
	public void addAuthor(Author author);

	/**
	 * Returns the common <code>Topic</code> of the <code>AuthorGroup</code>
	 *
	 * @return the common topic of the authors
	 */
	public Topic getTopic();

	/**
	 * Returns the <code>AuthorGroup</code> that the <code>AuthorsTopicBucket</code> stores
	 *
	 * @return the group of authors
	 */
	public AuthorGroup getAuthors();

	/**
	 * Returns a <code>Collection</code> of <code>AuthorsTopicBuckets</code> of randomly chosen <code>Authors</code> such that the individual buckets are mutually exclusive and exhaustive (they cover all the authors).  All the buckets are of the fixed specified size except a very few which could hold less number of <code>Authors</code>.
	 *
	 * @param numElements the number of elements in most of the individual buckets
	 *
	 * @return a collection of <code>AuthorsTopicBuckets</code>
	 */
	public Collection partitionBucket(int numElements);
}
