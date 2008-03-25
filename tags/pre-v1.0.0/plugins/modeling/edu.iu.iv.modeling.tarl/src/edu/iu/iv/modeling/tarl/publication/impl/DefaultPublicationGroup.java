package edu.iu.iv.modeling.tarl.publication.impl;

import java.util.Collection;
import java.util.Iterator;

import edu.iu.iv.modeling.tarl.publication.Publication;
import edu.iu.iv.modeling.tarl.publication.PublicationGroup;
import edu.iu.iv.modeling.tarl.util.impl.ExtendedHashSet;


/**
 * This class defines a default implementation of the <code>PublicationGroupInterface</code>.  It stores the group of <code>Publications</code> using a <code>DefaultPublicationGroup</code>.
 *
 * @author Jeegar T Maru
 * @see ExtendedHashSet
 * @see PublicationGroup
 */
public class DefaultPublicationGroup implements PublicationGroup {
	/**
	 * Stores a set of Publications
	 */
	protected ExtendedHashSet publications;

	/**
	 * Creates a new instance of an empty <code>PublicationGroup</code>
	 */
	public DefaultPublicationGroup() {
		publications = new ExtendedHashSet();
	}

	/**
	 * Creates a new instance for a <code>PublicationGroup</code> as an exact replica of the specified <code>PublicationGroup</code> object
	 *
	 * @param publications Specifies the publication group to be replicated
	 *
	 * @exception NullPointerException if the parameter publications is null
	 */
	public DefaultPublicationGroup(PublicationGroup publications)
		throws NullPointerException {
		this.publications = new ExtendedHashSet(publications.getPublications());
	}

	/**
	 * Initializes the group of <code>Publications</code> with the specified <code>Collection</code>
	 *
	 * @param collection Specifies the collection of publications
	 */
	public void initialize(Collection collection) {
		publications = new ExtendedHashSet(collection);
	}

	/**
	 * Returns the Group of <code>Publications</code>
	 *
	 * @return the group of publications
	 */
	public Collection getPublications() {
		return (publications);
	}

	/**
	 * Returns the <code>Iterator</code> over the Group of <code>Publications</code>
	 *
	 * @return the iterator over the group of publications
	 */
	public Iterator getIterator() {
		return (publications.iterator());
	}

	/**
	 * Returns the number of publications in the Group
	 *
	 * @return the number of publications in the group
	 */
	public int size() {
		return (publications.size());
	}

	/**
	 * Adds a <code>Publication</code> to the group only if it is not already present in the group
	 *
	 * @param publication Specifies the publication to be added
	 */
	public void addPublication(Publication publication) {
		publications.add((DefaultPublication)publication);
	}

	/**
	 * Tests whether the Group contains the <code>Publication</code> or not
	 *
	 * @param publication Specifies the publication to be tested
	 *
	 * @return true if the publication is contained in the group
	 */
	public boolean containsPublication(Publication publication) {
		return (publications.contains((DefaultPublication)publication));
	}

	/**
	 * Removes the <code>Publication</code> from the Group
	 *
	 * @param publication Specifies the publication to be removed
	 *
	 * @return true if the publication was successfully found and removed
	 */
	public boolean removePublication(Publication publication) {
		return (publications.remove((DefaultPublication)publication));
	}

	/**
	 * Removes all the <code>Publications</code> from the Group
	 */
	public void removeAllPublications() {
		publications.clear();
	}

	/**
	 * Returns a Random <code>Publication</code> from the <code>PublicationGroup</code>
	 *
	 * @return a random publication from the publication group
	 */
	public Publication getRandomPublication() {
		return ((DefaultPublication)publications.getRandomElement());
	}

	/**
	 * Returns the specified number of different random <code>Publications</code> from the <code>PublicationGroup</code> as a <code>PublicationGroup</code>.  If the specified number is larger than the size of the <code>PublicationGroup</code>, the entire <code>PublicationGroup</code> is returned.
	 *
	 * @param num_elements Specifies the number of publications required
	 *
	 * @return the publication group of different random publications
	 */
	public PublicationGroup getRandomPublications(int num_elements) {
		PublicationGroup result;
		Iterator iterator;
		ExtendedHashSet ext_hash_set;

		result = new DefaultPublicationGroup();
		ext_hash_set = publications.getRandomElements(num_elements);
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addPublication((DefaultPublication)iterator.next());

		return (result);
	}

	/**
	 * Modifies the <code>PublicationGroup</code> to be the union of itself and the specified <code>PublicationGroup</code>.  It acts like the operation (a = a union b), where a is the current <code>PublicationGroup</code> and b is the specified <code>PublicationGroup</code>
	 *
	 * @param publication_group Specifies the publication group to be unioned with
	 */
	public void union(PublicationGroup publication_group) {
		publications.union(publication_group.getPublications());
	}

	/**
	 * Returns the union of two specified <code>PublicationGroups</code> as a <code>PublicationGroup</code>.  Does not change any of the <code>PublicationGroups</code>.
	 *
	 * @param publication_group1 Specifies the first publication group
	 * @param publication_group2 Specifies the second publication group
	 *
	 * @return the publication group containing the union
	 */
	public static PublicationGroup union(
	    PublicationGroup publication_group1,
	    PublicationGroup publication_group2) {
		ExtendedHashSet ext_hash_set;
		PublicationGroup result;
		Iterator iterator;

		result = new DefaultPublicationGroup();
		ext_hash_set =
			ExtendedHashSet.union(publication_group1.getPublications(),
			    publication_group2.getPublications());
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addPublication((DefaultPublication)iterator.next());

		return result;
	}

	/**
	 * Modifies the <code>PublicationGroup</code> to be the intersection of the Group and the specified <code>PublicationGroup</code>.  It acts like the operation (a = a intersection b), where a is the current <code>PublicationGroup</code> and b is the specified <code>PublicationGroup</code>
	 *
	 * @param publication_group Specifies the publication group to be intersected with
	 */
	public void intersection(PublicationGroup publication_group) {
		publications.intersection(publication_group.getPublications());
	}

	/**
	 * Returns the intersection of two specified <code>PublicationGroups</code>.  Does not change any of the <code>PublicationGroups</code>.
	 *
	 * @param publication_group1 Specifies the first publicationgroup
	 * @param publication_group2 Specifies the second publicationgroup
	 *
	 * @return the publication group containing the intersection
	 */
	public static PublicationGroup intersection(
	    PublicationGroup publication_group1,
	    PublicationGroup publication_group2) {
		ExtendedHashSet ext_hash_set;
		PublicationGroup result;
		Iterator iterator;

		result = new DefaultPublicationGroup();
		ext_hash_set =
			ExtendedHashSet.intersection(publication_group1.getPublications(),
			    publication_group2.getPublications());
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addPublication((DefaultPublication)iterator.next());

		return result;
	}

	/**
	 * Modifies the Group to be the difference of the current <code>PublicationGroup</code> and the specified <code>PublicationGroup</code>.  It acts like the operation (a = a - b), where a is the current <code>PublicationGroup and b is the specified <code>PublicationGroup</code>
	 *
	 * @param publication_group Specifies the publication group to be intersected with
	 */
	public void difference(PublicationGroup publication_group) {
		publications.difference(publication_group.getPublications());
	}

	/**
	 * Returns the difference of the two specified <code>PublicationGroups</code>.  Does not change any of the <code>PublicationGroups</code>.  The operation can be expressed as (a - b), where a and b are the <code>PublicationGroups</code> as defined by the first and second argument respectively.
	 *
	 * @param publication_group1 Specifies the first publication group
	 * @param publication_group2 Specifies the second publication group
	 *
	 * @return the publication group containing the difference
	 */
	public static PublicationGroup difference(
	    PublicationGroup publication_group1,
	    PublicationGroup publication_group2) {
		ExtendedHashSet ext_hash_set;
		PublicationGroup result;
		Iterator iterator;

		result = new DefaultPublicationGroup();
		ext_hash_set =
			ExtendedHashSet.difference(publication_group1.getPublications(),
			    publication_group2.getPublications());
		iterator = ext_hash_set.iterator();

		while (iterator.hasNext())
			result.addPublication((DefaultPublication)iterator.next());

		return result;
	}

	/**
	 * Tests whether the specified <code>PublicationGroup<code> is a subset of the current one
	 *
	 * @param publication_group Specifies the publication group to be tested
	 *
	 * @return true, if publication_group is a subset of the current publication
	 */
	public boolean isSubset(PublicationGroup publication_group) {
		return (publications.containsAll(publication_group.getPublications()));
	}

	/**
	 * Tests whether the <code>PublicationGroup<code> specified by the second argument is a subset of the <code>PublicationGroup</code> specified by the first argument
	 *
	 * @param publication_group1 Specifies the first publication group
	 * @param publication_group2 Specifies the second publication group
	 *
	 * @return true, if publication_group2 is a subset of publication_group1
	 */
	public static boolean isSubset(
	    PublicationGroup publication_group1,
	    PublicationGroup publication_group2) {
		return ((publication_group1.getPublications()).containsAll(publication_group2
		    .getPublications()));
	}

	/**
	 * Tests whether the two <code>PublicationGroups</code> are equal or not in terms of Set Equality.  Two publicationgroups are Set Equal if first is the subset of the second and the second is a subset of the first.
	 *
	 * @param publication_group1 Specifies the first publication group
	 * @param publication_group2 Specifies the second publication group
	 *
	 * @return true, if the two publication groups are set equal
	 */
	public static boolean areSetEqual(
	    PublicationGroup publication_group1,
	    PublicationGroup publication_group2) {
		return ((DefaultPublicationGroup.isSubset(publication_group1,
		    publication_group2))
		&& (DefaultPublicationGroup.isSubset(publication_group2,
		    publication_group1)));
	}

	/**
	 * Returns the details of the <code>PublicationGroup</code> as a <code>String</code>
	 *
	 * @return a string describing the publication group
	 */
	public String toString() {
		StringBuffer sb;
		Iterator iterator;

		sb = new StringBuffer();

		iterator = getIterator();
		sb.append(new String(" [ "));

		while (iterator.hasNext()) {
			sb.append(((DefaultPublication)iterator.next()).getId());
			sb.append(new String("\t"));
		}

		sb.append(new String(" ]\n"));

		return (sb.toString());
	}
}
