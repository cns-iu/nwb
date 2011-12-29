package edu.iu.iv.modeling.tarl.publication;

import java.util.NoSuchElementException;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This interface defines the <code>PublicationDatabase</code> which is used to store all the <code>Publications</code> ever produced in the system.  The interface is responsible for actions like initializing the database, adding a new <code>Publication</code> to the database, iterating through each <code>Publication</code> of the database, removing all the <code>Publications</code> from the database, etc. <br>
 * Apart from these functions, the major responsibility of the interface is to tag each <code>Publication</code> with a unique identifier which distinguishes it from other <code>Publications</code>. <br>
 *
 * @author Jeegar T Maru
 * @see PublicationGroup
 * @see PublicationManager
 */
public interface PublicationDatabase {
	/**
	 * Adds a new <code>Publication</code> to the database with the specified year of publication, <code>Topic</code> and the group of <code>Authors</code>.  The system should store the <code>Publication</code> for retreival in the future.  It is assumed that the <code>Publication</code> has no citations at all.
	 *
	 * @param year Specifies the year of publication
	 * @param topic Specifies the topic that the publication belongs to
	 * @param authors Specifies the group of authors who have collaborated for the publication
	 *
	 * @exception TarlException if the specified parameters are insufficient to construct a publication
	 */
	public void addPublication(int year, Topic topic,
	    AuthorGroup authors) throws TarlException;

	/**
	 * Adds a new <code>Publication</code> to the database with the specified year of publication, <code>Topic</code> and the group of <code>Authors</code> and the group of <code>Publications</code>. The system should store the <code>Publication</code> for retreival in the future.
	 *
	 * @param year Specifies the year of publication
	 * @param topic Specifies the topic that the publication belongs to
	 * @param authors Specifies the group of authors who have collaborated for the publication
	 * @param citations Specifies the group of citations for the publication
	 *
	 * @exception TarlException if the specified parameters are insufficient to construct a publication
	 */
	public void addPublication(int year, Topic topic,
	    AuthorGroup authors, PublicationGroup citations)
		throws TarlException;

	/**
	 * Returns all the <code>Publications</code> in the database
	 *
	 * @return the group of publications in the database
	 */
	public PublicationGroup getPublications();

	/**
	 * Returns the number of <code>Publications</code> in the database
	 *
	 * @return the number of publications in the database
	 */
	public int size();

	/**
	 * Resets the Search Index to the start of the list of <code>Publications</code>.  This function is called just before iterating through the <code>Publications</code>.
	 */
	public void resetSearchIndex();

	/**
	 * Tests whether the database has more <code>Publications</code> for iteration
	 *
	 * @return true, if the database has more publications to visit
	 */
	public boolean hasMorePublications();

	/**
	 * Returns the next <code>Publication</code> in the database while iteration
	 *
	 * @return the next publication in the database
	 *
	 * @exception NoSuchElementException if the database has no more publications
	 */
	public Publication getNextPublication()
		throws NoSuchElementException;

	/**
	 * Removes all the <code>Publications</code> from the database
	 */
	public void removeAll();
}
