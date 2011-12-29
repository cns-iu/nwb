package edu.iu.iv.modeling.tarl.author;

import java.util.NoSuchElementException;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This interface defines the <code>AuthorDatabase</code> which is used to store all the <code>Authors</code> ever produced in the system.  The interface is responsible for operations like initializing the database, adding a new <code>Author</code> to the database, iterating through each <code>Author</code> of the database, removing all the <code>Authors</code> from the database, etc. <br>
 * Apart from these functions, the major responsibility of the interface is to tag each <code>Author</code> with a unique identifier which distinguishes it from other <code>Authors</code>. <br>
 *
 * @author Jeegar T Maru
 * @see AuthorGroup
 * @see AuthorManager
 */
public interface AuthorDatabase {
	/**
	 * Adds a new <code>Author</code> to the database with the specified <code>Topic</code>.  The system should store the <code>Author</code> for retreival in the future.  The maximum age of the author is assumed to be infinite.
	 *
	 * @param topic Specifies the topic that the author belongs to
	 *
	 * @exception TarlException if the specified parameters are insufficient to construct an author
	 */
	public void addAuthor(Topic topic) throws TarlException;

	/**
	 * Adds a new <code>Author</code> to the database with the specified <code>Topic</code> and maximum age.  The system should store the <code>Author</code> for retreival in the future.
	 *
	 * @param topic Specifies the topic that the author belongs to
	 * @param maximumAge Specifies the maximum age of the author
	 *
	 * @exception TarlException if the specified parameters are insufficient to construct an author
	 */
	public void addAuthor(Topic topic, int maximumAge)
		throws TarlException;

	/**
	 * Returns all the <code>Authors</code> in the database
	 *
	 * @return the group of authors in the database
	 */
	public AuthorGroup getAuthors();

	/**
	 * Returns the number of <code>Authors</code> in the database
	 *
	 * @return the number of authors in the database
	 */
	public int size();

	/**
	 * Resets the Search Index to the start of the list of <code>Authors</code>.  This function is called just before iterating through the <code>Authors</code>.
	 */
	public void resetSearchIndex();

	/**
	 * Tests whether the database has more <code>Authors</code> for iteration
	 *
	 * @return true, if the database has more authors to visit
	 */
	public boolean hasMoreAuthors();

	/**
	 * Returns the next <code>Author</code> in the database while iteration
	 *
	 * @return the next author in the database
	 *
	 * @exception NoSuchElementException if the database has no more authors
	 */
	public Author getNextAuthor() throws NoSuchElementException;

	/**
	 * Removes all the <code>Authors</code> from the database
	 */
	public void removeAll();
}
