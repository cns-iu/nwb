package edu.iu.iv.modeling.tarl.author.impl;

import java.util.Collection;
import java.util.Iterator;

import edu.iu.iv.modeling.tarl.author.AbstractAuthorUtility;
import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.topic.Topic;
import edu.iu.iv.modeling.tarl.util.AuthorsTopicBucket;
import edu.iu.iv.modeling.tarl.util.impl.DefaultAuthorsTopicBucket;
import edu.iu.iv.modeling.tarl.util.impl.ExtendedHashSet;


/**
 * This class extends the abstract class <code>AbstractAuthorUtility</code>.  It uses the default representations of the standard interfaces to execute the methods.
 *
 * @author Jeegar T Maru
 * @see Author
 * @see AuthorGroup
 */
public class DefaultAuthorUtility extends AbstractAuthorUtility {
	/**
	 * Filters based on the <code>Topic</code> of <code>Author</code> on the conditions of equality with the specified <code>Topic</code>.  It returns a group of all the <code>Authors</code> which have the specified <code>Topic</code>.
	 *
	 * @param authorGroup Specifies the group of authors on which to apply the filter
	 * @param topic Specifies the topic of author to be equated against
	 *
	 * @return the group of authors which belong to the specified topic
	 */
	public static AuthorGroup filterOnTopicEquality(
	    AuthorGroup authorGroup, Topic topic) {
		Iterator iterator;
		Author author;
		AuthorGroup result;

		result = new DefaultAuthorGroup();
		iterator = authorGroup.getIterator();

		while (iterator.hasNext()) {
			author = (DefaultAuthor)iterator.next();

			if (topic.equals(author.getTopic()))
				result.addAuthor(author);
		}

		return result;
	}

	/**
	 * Filters out <code>Authors</code> that are deactivated.  It returns a group of all the <code>Authors</code> which are active.
	 *
	 * @param authorGroup Specifies the group of authors on which to apply the filter
	 *
	 * @return the group of all the authors in the group that are active
	 */
	public static AuthorGroup getAllActiveAuthors(
	    AuthorGroup authorGroup) {
		Iterator iterator;
		Author author;
		AuthorGroup result;

		result = new DefaultAuthorGroup();
		iterator = authorGroup.getIterator();

		while (iterator.hasNext()) {
			author = (DefaultAuthor)iterator.next();

			if (author.isActive())
				result.addAuthor(author);
		}

		return result;
	}

	/**
	 * Partitions the <code>Authors</code> on the basis of <code>Topic</code>.  The method returns a <code>Collection</code> of <code>AuthorsTopicBuckets</code> each with a diferent <code>Topic</code>.
	 *
	 * @return the collection of authors-topic buckets each with a different topic
	 */
	public static Collection partitionOnTopic(AuthorGroup authorGroup) {
		boolean topicFound;
		Iterator iterator1;
		Iterator iterator2;
		ExtendedHashSet extHashSet;
		Author author;
		AuthorsTopicBucket authorsTopicBucket;

		extHashSet = new ExtendedHashSet();
		iterator1 = authorGroup.getIterator();

		while (iterator1.hasNext()) {
			author = (DefaultAuthor)iterator1.next();
			iterator2 = extHashSet.iterator();
			topicFound = false;

			while ((iterator2.hasNext()) && (!topicFound)) {
				authorsTopicBucket =
					(DefaultAuthorsTopicBucket)iterator2.next();

				if ((author.getTopic()).equals(authorsTopicBucket.getTopic())) {
					topicFound = true;
					authorsTopicBucket.addAuthor(author);
				}
			}

			if (!topicFound) {
				authorsTopicBucket = new DefaultAuthorsTopicBucket();
				authorsTopicBucket.addAuthor(author);
				extHashSet.add(authorsTopicBucket);
			}
		}

		return extHashSet;
	}
}
