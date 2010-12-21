package edu.iu.iv.modeling.tarl.author.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.author.AuthorDatabase;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.topic.Topic;


/**
 * This class defines a default implementation for <code>AuthorDatabaseInterface</code>.  It stores a list of <code>Authors</code> as an instance of <code>DefaultAuthorGroup</code>.
 *
 * @author Jeegar T Maru
 * @see DefaultAuthorGroup
 * @see AuthorDatabase
 */
public class DefaultAuthorDatabase implements AuthorDatabase {
	/**
	 * Stores the list of <code>Authors</code> as a <code>AuthorGroup</code>
	 */
	protected AuthorGroup authors;

	/**
	 * Stores the iterator to be used for the list of Authors
	 */
	protected Iterator iterator;

	/**
	 * Creates a new instance for a <code>AuthorDatabase</code>
	 */
	public DefaultAuthorDatabase() {
		authors = new DefaultAuthorGroup();
		iterator = authors.getIterator();
	}

	/**
	 * Adds an <code>Author</code> to the list
	 *
	 * @param topic Specifies the topic of the author
	 *
	 * @exception TarlException if the new author cannot be initialized
	 */
	public void addAuthor(Topic topic) throws TarlException {
		Author author;

		author = new DefaultAuthor();
		author.initialize(authors.size(), topic);
		authors.addAuthor(author);
	}

	/**
	 * Adds an <code>Author</code> to the list
	 *
	 * @param topic Specifies the topic of the author
	 * @param maximum_age Specifies the maximum age of the author
	 *
	 * @exception TarlException if the new author cannot be initialized
	 */
	public void addAuthor(Topic topic, int maximum_age)
		throws TarlException {
		Author author;

		author = new DefaultAuthor();
		author.initialize(authors.size(), topic, maximum_age);
		authors.addAuthor(author);
	}

	/**
	 * Removes all the <code>Authors</code> from the database
	 */
	public void removeAll() {
		authors.removeAllAuthors();
	}

	/**
	 * Returns the list of <code>Authors</code> in the Database
	 *
	 *@return the list of authors in the database
	 */
	public AuthorGroup getAuthors() {
		return (authors);
	}

	/**
	 * Returns the number of <code>Authors</code> in the Database
	 *
	 * @return the number of authors in the Database
	 */
	public int size() {
		return (authors.size());
	}

	/**
	 * Resets the Search Index so the Database can be searched iteratively
	 */
	public void resetSearchIndex() {
		iterator = authors.getIterator();
	}

	/**
	 * Tests whether the Database has more authors for iteration
	 *
	 * @return true if the database has more authors to iterate over
	 */
	public boolean hasMoreAuthors() {
		return (iterator.hasNext());
	}

	/**
	 * Returns the next <code>Author</code> in the database
	 *
	 * @return the next author of the iteration
	 *
	 * @exception NoSuchElementException if the iteration has no more authors
	 */
	public Author getNextAuthor() throws NoSuchElementException {
		return ((DefaultAuthor)iterator.next());
	}

	/**
	 * Returns the details of the <code>AuthorDatabase</code> as a String
	 *
	 * @return a string describing the author database
	 */
	public String toString() {
		return (authors.toString());
	}
}
