package edu.iu.iv.modeling.tarl.publication;

import java.util.Collection;
import java.util.Iterator;


/**
 * This interface defines the functions to represent a group of <code>Publications</code>.  It should support various operations on a group of <code>Publications</code> like returning an iterator over the group, adding a <code>Publication</code> to the group, testing whether a <code>Publication</code> is a member of the group, retrieving random <code>Publications</code>, unioning with another <code>PublicationGroup</code>, etc. <br>
 * A major constraint on the <code>PublicationGroup</code> is to include only <code>Publications</code> as its members.
 *
 * @author Jeegar T Maru
 * @see Publication
 */
public interface PublicationGroup {
	/**
	 * Initializes the group of <code>Publications</code> with the specified <code>Collection</code>
	 *
	 * @param collection Specifies the collection of publications
	 */
	public void initialize(Collection collection);

	/**
	 * Returns the <code>Collection</code> of <code>Publications</code> in the Group
	 *
	 * @return the collection of publications in the group
	 */
	public Collection getPublications();

	/**
	 * Returns the <code>Iterator</code> over the Group of <code>Publications</code>.  The <code>Iterator</code> should iterate only over the non-duplicate members of the <code>PublicationGroup</code>.  The order of iteration does not matter as long as it covers all the non-duplicate members of the Group.
	 *
	 * @return the iterator over the group of publications
	 */
	public Iterator getIterator();

	/**
	 * Returns the number of <code>Publications</code> in the Group
	 *
	 * @return the number of publications in the group
	 */
	public int size();

	/**
	 * Adds a <code>Publication</code> to the group
	 *
	 * @param publication Specifies the publication to be added
	 */
	public void addPublication(Publication publication);

	/**
	 * Removes all the <code>Publication</code> from the Group
	 */
	public void removeAllPublications();

	/**
	 * Tests whether the Group contains the <code>Publication</code> or not
	 *
	 * @param publication Specifies the publication to be tested
	 *
	 * @return true, if the publication is contained in the group
	 */
	public boolean containsPublication(Publication publication);

	/**
	 * Returns a Random <code>Publication</code> from the <code>PublicationGroup</code>
	 *
	 * @return a random publication from the publication group
	 */
	public Publication getRandomPublication();

	/**
	 * Returns the specified number of distinct random <code>Publications</code> from the <code>PublicationGroup</code> as a <code>PublicationGroup</code>.  If the specified number is larger than the size of the <code>PublicationGroup</code>, the entire <code>PublicationGroup</code> is returned.
	 *
	 * @param numElements Specifies the number of publications required
	 *
	 * @return the publication group of different random publications
	 */
	public PublicationGroup getRandomPublications(int numElements);

	/**
	 * Modifies the <code>PublicationGroup</code> to be the union of itself and the specified <code>PublicationGroup</code>.  It acts like the operation (a = a union b), where a is the current <code>PublicationGroup</code> and b is the specified <code>PublicationGroup</code>
	 *
	 * @param publicationGroupInterface Specifies the publication group to be unioned with
	 */
	public void union(PublicationGroup publicationGroupInterface);

	/**
	 * Tests whether the specified <code>PublicationGroup<code> is a subset of the current one
	 *
	 * @param publication_group Specifies the publication group to be tested
	 *
	 * @return true, if publication_group is a subset of the current publication
	 */
	public boolean isSubset(PublicationGroup publication_group);
}
