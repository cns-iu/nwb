package edu.iu.iv.modeling.tarl.author;

import java.util.Collection;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.input.AuthorParameters;
import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This interface defines all the high-level functions related to the <code>Author</code> environment.  The <code>TarlExecuter</code> solely uses this interface to deal with the <code>Author</code> environment.  Some of these functions are initializing the <code>Authors</code> environment, producing an <code>Author</code>, terminating the current year, retreiving the author database, computing the co-authorship graph and cleaning up the <code>Author</code> environment.
 *
 * @author Jeegar T Maru
 * @see AuthorParameters
 */
public interface AuthorManager {
	/**
	 * Initializes the <code>Author</code> environment to enable various operations on the <code>Authors</code>.  This function should be called before calling any other function in <code>AuthorManager</code>.
	 *
	 * @param authorParameters Specifies the model parameters related to authors
	 */
	public void initializeAuthors(AuthorParameters authorParameters);

	/**
	 * Adds an <code>Author</code> to the environment with the specified <code>Topic</code>.
	 *
	 * @param topic Specifies the topic for the author
	 *
	 * @exception TarlException if the specified parameters are insufficient to construct an author
	 */
	public void addAuthor(Topic topic) throws TarlException;

	/**
	 * Returns the <code>Collection</code> of <code>AuthorsTopicBuckets</code>.  Each <code>AuthorsTopicBucket</code> contains a fixed number of randomly chosen <code>Authors</code>.  <code>AuthorsTopicBuckets</code> are mutually exclusive and exhaustive of the entire <code>AuthorDatabase</code> in the system.  The number of <code>Authors</code> in each <code>AuthorsTopicBucket</code> should be (number of co-authors + 1).  The number of <code>Authors</code> in a few <code>AuthorsTopicBuckets</code> could be less than (number of co-authors + 1).
	 *
	 * @return the collection of author groups
	 */
	public Collection partitionActiveAuthors();

	/**
	 * Terminates the current year for the <code>Author</code> environment.  The function should take the necessary steps that should be taken when the current year ends (for example, notifying authors about year termination).
	 */
	public void terminateCurrentYear();

	/**
	 * Returns the group of <code>Authors</code>.  This function can be used to generate the Author-Publication graph at the <code>TarlExecuter</code> level where it has access to the group of <code>Authors</code> and <code>Publications</code>.
	 *
	 * @return the group of authors in the system
	 */
	public AuthorGroup getAuthors();

	/**
	 * Cleans up the <code>Author</code> environment.  It is responsible for freeing up resources that the <code>Author</code> environment has used for its execution.
	 */
	public void cleanUpAuthor();
}
