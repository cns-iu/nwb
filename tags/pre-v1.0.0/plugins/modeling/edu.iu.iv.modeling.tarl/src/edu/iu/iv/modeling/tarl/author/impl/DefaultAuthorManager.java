package edu.iu.iv.modeling.tarl.author.impl;

import java.util.Collection;
import java.util.Iterator;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.author.AuthorDatabase;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.author.AuthorManager;
import edu.iu.iv.modeling.tarl.input.AuthorParameters;
import edu.iu.iv.modeling.tarl.topic.Topic;
import edu.iu.iv.modeling.tarl.util.AuthorsTopicBucket;
import edu.iu.iv.modeling.tarl.util.impl.DefaultAuthorsTopicBucket;
import edu.iu.iv.modeling.tarl.util.impl.ExtendedHashSet;


/**
 * This class defines a default implementation to the <code>AuthorManagerInterface</code>.  It utilizes the default implementations of the standard interfaces to achieve its cause.
 *
 * @author Jeegar T Maru
 * @see DefaultAuthorDatabase
 * @see DefaultAuthorUtility
 * @see AuthorManager
 */
public class DefaultAuthorManager implements AuthorManager {
	/**
	 * Stores the database of <code>Authors</code> for the system
	 */
	protected AuthorDatabase authors;

	/**
	 * Stores the model parameters concerning the <code>Author</code> environment
	 */
	protected AuthorParameters authorParameters;

	/**
	 * Stores the offset from the start year
	 */
	protected int offsetYear;

	/**
	 * Creates a new instance of the <code>AuthorManager</code>
	 */
	public DefaultAuthorManager() {
		authors = null;
		authorParameters = null;
	}

	/**
	 * Initializes the <code>Author</code> environment to enable various operations on the <code>Authors</code>
	 *
	 * @param authorParameters Specifies the model parameters related to authors
	 */
	public void initializeAuthors(AuthorParameters authorParameters) {
		authors = new DefaultAuthorDatabase();
		this.authorParameters = authorParameters;
		offsetYear = 0;
	}

	/**
	 * Adds an <code>Author</code> to the environment with the specified <code>Topic</code>.
	 *
	 * @param topic Specifies the topic for the author
	 *
	 * @exception TarlException if the specified parameters are insufficient to construct an author
	 */
	public void addAuthor(Topic topic) throws TarlException {
		authors.addAuthor(topic, authorParameters.getMaximumAge());
	}

	/**
	 * Returns the <code>Collection</code> of <code>AuthorsTopicBuckets</code>.
	 *
	 * @return the collection of author groups
	 */
	public Collection partitionActiveAuthors() {
		Iterator iterator;
		Collection authorPartition;
		ExtendedHashSet result;
		AuthorGroup activeAuthors;
		AuthorsTopicBucket authorsTopicBucket;

		activeAuthors =
			DefaultAuthorUtility.getAllActiveAuthors(authors.getAuthors());
		authorPartition = DefaultAuthorUtility.partitionOnTopic(activeAuthors);
		result = new ExtendedHashSet();
		iterator = authorPartition.iterator();

		while (iterator.hasNext()) {
			authorsTopicBucket = (DefaultAuthorsTopicBucket)iterator.next();
			result.union(authorsTopicBucket.partitionBucket(authorParameters
			        .getNumCoAuthors() + 1));
		}

		return result;
	}

	/**
	 * Terminates the current year for the <code>Author</code> environment.  The function should take the necessary steps that should be taken when the current year ends (for example, notifying authors about year termination).
	 */
	public void terminateCurrentYear() {
		int i;
		int numDeactivationAuthors;
		Author author;
		AuthorGroup authorGroup;

		authors.resetSearchIndex();

		while (authors.hasMoreAuthors()) {
			author = authors.getNextAuthor();
			author.yearEndNotification();
		}

		offsetYear++;

		if ((offsetYear % (authorParameters.getNumDeactivationYears())) == 0) {
			numDeactivationAuthors =
				authorParameters.getNumDeactivationAuthors();

			for (i = 0; i < numDeactivationAuthors; i++) {
				authorGroup =
					DefaultAuthorUtility.getAllActiveAuthors(authors.getAuthors());
				author = authorGroup.getRandomAuthor();

				if (author == null)
					break;

				author.deactivate();
			}
		}
	}

	/**
	 * Returns the group of <code>Authors</code>.
	 *
	 * @return the group of authors in the system
	 */
	public AuthorGroup getAuthors() {
		return authors.getAuthors();
	}

	/**
	 * Cleans up the <code>Author</code> environment.  It is responsible for freeing up resources that the <code>Author</code> environment has used for its execution.
	 */
	public void cleanUpAuthor() {
		authors.removeAll();
	}
}
