package edu.iu.iv.modeling.tarl.author;

import java.util.Collection;
import java.util.Iterator;


/**
 * This interface defines the functions to represent a group of <code>Authors</code>.  It should support various operations on a group of <code>Authors</code> like returning an iterator over the group, adding an <code>Author</code> to the group, testing whether an <code>Author</code> is a member of the group, retrieving random <code>Authors</code>, unioning with another <code>AuthorGroup</code>, etc. <br>
 * A major constraint on the <code>AuthorGroup</code> is to include only <code>Authors</code> as its members.
 *
 * @author Jeegar T Maru
 * @see Author
 */
public interface AuthorGroup {
	/**
	 * Initializes the group of <code>Authors</code> with the specified <code>Collection</code>
	 *
	 * @param collection Specifies the collection of authors
	 */
	public void initialize(Collection collection);

	/**
	 * Returns the <code>Collection</code> of <code>Authors</code> in the Group
	 *
	 * @return the collection of authors in the group
	 */
	public Collection getAuthors();

	/**
	 * Returns a random <code>Author</code> from the group
	 *
	 * @return a random author from the group
	 */
	public Author getRandomAuthor();

	/**
	 * Returns the <code>Iterator</code> over the Group of <code>Authors</code>.  The <code>Iterator</code> should iterate only over the non-duplicate members of the <code>AuthorGroup</code>.  The order of iteration does not matter as long as it covers all the non-duplicate members of the Group.
	 *
	 * @return the iterator over the group of authors
	 */
	public Iterator getIterator();

	/**
	 * Returns the number of <code>Authors</code> in the Group
	 *
	 * @return the number of authors in the group
	 */
	public int size();

	/**
	 * Adds an <code>Author</code> to the group
	 *
	 * @param author Specifies the author to be added
	 */
	public void addAuthor(Author author);

	/**
	 * Removes all the <code>Author</code> from the Group
	 */
	public void removeAllAuthors();

	/**
	 * Tests whether the Group contains the <code>Author</code> or not
	 *
	 * @param author Specifies the author to be tested
	 *
	 * @return true, if the author is contained in the group
	 */
	public boolean containsAuthor(Author author);
}
