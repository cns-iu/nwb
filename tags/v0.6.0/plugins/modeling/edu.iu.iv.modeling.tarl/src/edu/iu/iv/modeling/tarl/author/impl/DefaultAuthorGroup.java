package edu.iu.iv.modeling.tarl.author.impl;

import java.util.Collection;
import java.util.Iterator;

import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.util.impl.ExtendedHashSet;


/**
 * This class defines a default implementation of <code>AuthorGroupInterface</code> using the <code>ExtendedHashSet</code>.
 *
 * @author Jeegar T Maru
 * @see ExtendedHashSet
 * @see AuthorGroup
 */
public class DefaultAuthorGroup implements AuthorGroup {
	/**
	 * Stores a set of authors
	 */
	protected ExtendedHashSet authors;

	/**
	 * Creates a new instance of an empty <code>AuthorGroup</code>
	 */
	public DefaultAuthorGroup() {
		authors = new ExtendedHashSet();
	}

	/**
	 * Creates a new instance for a <code>AuthorGroup</code> as an exact replica of the specified <code>AuthorGroup</code> object
	 *
	 * @param authors Specifies the author set to be replicated
	 *
	 * @exception NullPointerException if the parameter authors is null
	 */
	public DefaultAuthorGroup(AuthorGroup authors)
		throws NullPointerException {
		if (authors == null)
			throw (new NullPointerException(new String(
			        "AuthorGroup cannot be initialized with a null authorgroup\n")));

		this.authors = new ExtendedHashSet(authors.getAuthors());
	}

	/**
	 * Initializes the group of <code>Authors</code> with the specified <code>Collection</code>
	 *
	 * @param collection Specifies the collection of authors
	 */
	public void initialize(Collection collection) {
		authors = new ExtendedHashSet(collection);
	}

	/**
	 * Returns the Group of <code>Authors</code>
	 *
	 * @return the group of authors
	 */
	public Collection getAuthors() {
		return (authors);
	}

	/**
	 * Returns the <code>Iterator</code> over the Group of <code>Authors</code>
	 *
	 * @return the iterator over the group of authors
	 */
	public Iterator getIterator() {
		return (authors.iterator());
	}

	/**
	 * Returns the number of authors in the Group
	 *
	 * @return the number of authors in the group
	 */
	public int size() {
		return (authors.size());
	}

	/**
	 * Adds an <code>Author</code> to the group only if it is not already present in the group
	 *
	 * @param author Specifies the author to be added
	 */
	public void addAuthor(Author author) {
		authors.add((DefaultAuthor)author);
	}

	/**
	 * Tests whether the Group contains the <code>Author</code> or not
	 *
	 * @param author Specifies the author to be tested
	 *
	 * @return true if the author is contained in the set
	 */
	public boolean containsAuthor(Author author) {
		return (authors.contains((DefaultAuthor)author));
	}

	/**
	 * Removes the <code>Author</code> from the Group
	 *
	 * @param author Specifies the author to be removed
	 *
	 * @return true if the author was successfully found and removed
	 */
	public boolean removeAuthor(Author author) {
		return (authors.remove((DefaultAuthor)author));
	}

	/**
	 * Removes all the <code>Authors</code> from the Group
	 */
	public void removeAllAuthors() {
		authors.clear();
	}

	/**
	 * Returns a Random <code>Author</code> from the <code>AuthorGroup</code>
	 *
	 * @return a random author from the author group
	 */
	public Author getRandomAuthor() {
		return ((DefaultAuthor)authors.getRandomElement());
	}

	/**
	 * Returns the specified number of different random <code>Authors</code> from the <code>AuthorGroup</code> as an <code>AuthorGroup</code>.  If the specified number is larger than the size of the <code>AuthorGroup</code>, the entire <code>AuthorGroup</code> is returned.
	 *
	 * @param num_elements Specifies the number of authors required
	 *
	 * @return the author group of different random authors
	 */
	public AuthorGroup getRandomAuthors(int num_elements) {
		AuthorGroup result;
		Iterator iterator;
		ExtendedHashSet ext_hash_set;

		result = new DefaultAuthorGroup();
		ext_hash_set = authors.getRandomElements(num_elements);
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addAuthor((DefaultAuthor)iterator.next());

		return (result);
	}

	/**
	 * Modifies the <code>AuthorGroup</code> to be the union of itself and the specified <code>AuthorGroup</code>.  It acts like the operation (a = a union b), where a is the current <code>AuthorGroup</code> and b is the specified <code>AuthorGroup</code>
	 *
	 * @param author_group Specifies the author group to be unioned with
	 */
	public void union(AuthorGroup author_group) {
		authors.union(author_group.getAuthors());
	}

	/**
	 * Returns the union of two specified <code>AuthorGroups</code> as an <code>AuthorGroup</code>.  Does not change any of the <code>AuthroGroups</code>.
	 *
	 * @param author_group1 Specifies the first author group
	 * @param author_group2 Specifies the second author group
	 *
	 * @return the author group containing the union
	 */
	public static AuthorGroup union(
	    AuthorGroup author_group1, AuthorGroup author_group2) {
		ExtendedHashSet ext_hash_set;
		AuthorGroup result;
		Iterator iterator;

		result = new DefaultAuthorGroup();
		ext_hash_set =
			ExtendedHashSet.union(author_group1.getAuthors(),
			    author_group2.getAuthors());
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addAuthor((DefaultAuthor)iterator.next());

		return result;
	}

	/**
	 * Modifies the <code>AuthorGroup</code> to be the intersection of the Group and the specified <code>AuthorGroup</code>.  It acts like the operation (a = a intersection b), where a is the current <code>AuthorGroup</code> and b is the specified <code>AuthorGroup</code>
	 *
	 * @param author_group Specifies the author group to be intersected with
	 */
	public void intersection(AuthorGroup author_group) {
		authors.intersection(author_group.getAuthors());
	}

	/**
	 * Returns the intersection of two specified <code>AuthorGroups</code>.  Does not change any of the <code>AuthorGroups</code>.
	 *
	 * @param author_group1 Specifies the first author group
	 * @param author_group2 Specifies the second author group
	 *
	 * @return the author group containing the intersection
	 */
	public static AuthorGroup intersection(
	    AuthorGroup author_group1, AuthorGroup author_group2) {
		ExtendedHashSet ext_hash_set;
		AuthorGroup result;
		Iterator iterator;

		result = new DefaultAuthorGroup();
		ext_hash_set =
			ExtendedHashSet.intersection(author_group1.getAuthors(),
			    author_group2.getAuthors());
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addAuthor((DefaultAuthor)iterator.next());

		return result;
	}

	/**
	 * Modifies the Group to be the difference of the current <code>AuthorGroup</code> and the specified <code>AuthorGroup</code>.  It acts like the operation (a = a - b), where a is the current <code>AuthorGroup and b is the specified <code>AuthorGroup</code>
	 *
	 * @param author_group Specifies the author group to be intersected with
	 */
	public void difference(AuthorGroup author_group) {
		authors.difference(author_group.getAuthors());
	}

	/**
	 * Returns the difference of the two specified <code>AuthorGroups</code>.  Does not change any of the <code>AuthorGroups</code>.  The operation can be expressed as (a - b), where a and b are the <code>AuthorGroups</code> as defined by the first and second argument respectively.
	 *
	 * @param author_group1 Specifies the first author group
	 * @param author_group2 Specifies the second author group
	 *
	 * @return the author group containing the difference
	 */
	public static AuthorGroup difference(
	    AuthorGroup author_group1, AuthorGroup author_group2) {
		ExtendedHashSet ext_hash_set;
		AuthorGroup result;
		Iterator iterator;

		result = new DefaultAuthorGroup();
		ext_hash_set =
			ExtendedHashSet.difference(author_group1.getAuthors(),
			    author_group2.getAuthors());
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addAuthor((DefaultAuthor)iterator.next());

		return result;
	}

	/**
	 * Tests whether the <code>AuthorGroup<code> specified by the second argument is a subset of the <code>AuthorGroup</code> specified by the first argument
	 *
	 * @param author_group1 Specifies the first author group
	 * @param author_group2 Specifies the second author group
	 *
	 * @return true, if author_group2 is a subset of author_group1
	 */
	public static boolean isSubset(AuthorGroup author_group1,
	    AuthorGroup author_group2) {
		return ((author_group1.getAuthors()).containsAll(author_group2
		    .getAuthors()));
	}

	/**
	 * Tests whether the two <code>AuthorGroups</code> are equal or not in terms of Set Equality.  Two authorgroups are Set Equal if first is the subset of the second and the second is a subset of the first.
	 *
	 * @param author_group1 Specifies the first author group
	 * @param author_group2 Specifies the second author group
	 *
	 * @return true, if the two author groups are Set equal
	 */
	public static boolean areSetEqual(AuthorGroup author_group1,
	    AuthorGroup author_group2) {
		return ((DefaultAuthorGroup.isSubset(author_group1, author_group2))
		&& (DefaultAuthorGroup.isSubset(author_group2, author_group1)));
	}

	/**
	 * Returns the details of the <code>AuthorGroup</code> as a <code>String</code>
	 *
	 * @return a string describing the author group
	 */
	public String toString() {
		StringBuffer sb;
		Iterator iterator;

		sb = new StringBuffer();

		iterator = getIterator();
		sb.append(new String(" [ "));

		while (iterator.hasNext()) {
			sb.append(((DefaultAuthor)iterator.next()).getId());
			sb.append(new String("\t"));
		}

		sb.append(new String(" ]\n"));

		return (sb.toString());
	}
}
