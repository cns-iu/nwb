package edu.iu.iv.modeling.tarl.publication;

import java.io.File;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.input.PublicationParameters;
import edu.iu.iv.modeling.tarl.util.AuthorsTopicBucket;


/**
 * This interface defines all the high-level functions related to <code>Publications</code>.  The <code>TarlExecuter</code> solely uses this interface to deal with the <code>Publication</code> environment.  Some of these functions are initializing the publications environment, producing a publication, terminating the current year, retreiving the publication database, computing the co-citation graph and cleaning up the publication environment. <br>
 * The <code>PublicationManager</code> should keep track of current year throughout its existence.  The function {@link #terminateCurrentYear()} should be used for such a task.
 */
public interface PublicationManager {
	/**
	 * Initializes the <code>Publication</code> environment to enable various operations on the <code>Publications</code>.  This function should be called before calling any other function in <code>PublicationManager</code>.
	 *
	 * @param publicationParameters Specifies the input parameters related to publications
	 */
	public void initializePublications(
	    PublicationParameters publicationParameters, File agingFunctionFile);

	/**
	 * Produces the <code>Publications</code> for the specified authors and topic at the very start of the model
	 *
	 * @param authorsTopicBucket Specifies the authors and topic for the new publications
	 *
	 * @exception TarlException if the new publications cannot be initialized
	 */
	public void producePublicationsAtStart(
	    AuthorsTopicBucket authorsTopicBucket)
		throws TarlException;

	/**
	 * Produces <code>Publications</code> for the specified <code>Authors</code>.  The function should choose the new publication based on the initialization values it was supplied with.
	 *
	 * @param authorsTopicBucket Specifies the bucket of authors and their topic who wish to collaborate for the publication
	 *
	 * @exception TarlException if the specified information is insufficient to produce a publication
	 */
	public void producePublications(
	    AuthorsTopicBucket authorsTopicBucket)
		throws TarlException;

	/**
	 * Terminates the current year for the <code>Publication</code> environment.  The function should take the necessary steps to indicate that the current year has ended.
	 */
	public void terminateCurrentYear();

	/**
	 * Returns the group of <code>Publications</code>.  This function can be used to generate the Author-Publication graph at the <code>TarlExecuter</code> level where it has access to the graph of <code>Authors</code> and <code>Publications</code>.
	 *
	 * @return the group of publications in the system
	 */
	public PublicationGroup getPublications();

	/**
	 * Cleans up the <code>Publication</code> environment.  It is responsible for freeing up resources that the <code>Publication</code> environment has used for its execution.
	 */
	public void cleanUpPublication();
}
