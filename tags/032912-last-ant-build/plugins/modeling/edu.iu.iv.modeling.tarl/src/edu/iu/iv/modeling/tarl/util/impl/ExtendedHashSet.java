package edu.iu.iv.modeling.tarl.util.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;


/**
 * This class defines an extended form of the HashSet - available via the Java Collections Framework.  This class adds a few utility methods in order to make the Set more useful to use.  Some of the utility methods are : <br>
 * <ul>
 * <li> Retrieve a random element from the Set </li>
 * <li> Retrieve n different random elements from the Set </li>
 * <li> Define Set Union </li>
 * <li> Define Subset operation </li>
 * <li> Define equals operation </li>
 * </ul>
 *
 * @author Jeegar T Maru
 * @see HashSet
 */
public class ExtendedHashSet extends HashSet implements Set {
    private static final long serialVersionUID = -1374859868885148547L;

    /**
	 * Creates a new instance of <code>ExtendedHashSet</code> such that it is empty
	 */
	public ExtendedHashSet() {
		super();
	}

	/**
	 * Creates a new instance of <code>ExtendedHashSet</code> such that it contains a specified <code>Collection</code>
	 *
	 * @param collection Specifies the collection to initialize the extended hash set with
	 *
	 * @exception NullPointerException if the specified collection is null
	 */
	public ExtendedHashSet(Collection collection) throws NullPointerException {
		super(collection);

		if (collection == null)
			throw (new NullPointerException(new String(
			        "Extended Hash Set initialized with a null collection\n")));
	}

	/**
	 * Creates a new instance of an empty <code>ExtendedHashSet</code> with the specified initial capacity
	 *
	 * @param initialCapacity Specifies the initial capacity for the extended hash set
	 */
	public ExtendedHashSet(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Creates a new instance of an empty <code>ExtendedHashSet</code> with the specified initial capacity and load factor
	 *
	 * @param initialCapacity Specifies the initial capacity for the extended hash set
	 * @param loadFactor Specifies the load factor for the extended hash set
	 */
	public ExtendedHashSet(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * Returns the element at the specified position in the iteration order.  The method iterates through the <code>ExtendedHashSet</code> until it has iterated the specified number of elements.  It then returns the last element found. <br>
	 * It should be noted that since the iteration order is not fixed, the method does not guarantee of reproducibility of results.  This method is useful in certain situations only, like - retrieving a random element.
	 *
	 * @param index Specifies the desired position in the iteration order starting from 0 to size-1
	 *
	 * @return the element corresponding to the specified position
	 *
	 * @exception NoSuchElementException if the specified position exceeds the size of the set
	 */
	private Object getElementAt(int index) throws NoSuchElementException {
		int i;
		Iterator iterator;

		iterator = this.iterator();

		for (i = 0; i < index; i++)
			iterator.next();

		return ((Object)iterator.next());
	}

	/**
	 * Returns a Random element from the Set. Returns null if the Set if empty.
	 *
	 * @return a random element in the set
	 */
	public Object getRandomElement() {
		Random randomGenerator;

		if (this.isEmpty())
			return null;

		randomGenerator = new Random();

		return (this.getElementAt(randomGenerator.nextInt(this.size())));
	}

	/**
	 * Returns the specified number of different random elements from the Set as an <code>ExtendedHashSet</code>.  If the specified number is larger than the size of the Set, the entire Set is returned.
	 *
	 * @param numElements Specifies the number of elements required
	 *
	 * @return the Set of different random elements
	 */
	public ExtendedHashSet getRandomElements(int numElements) {
		int index;
		int size;
		int numElementsToBeRemoved;
		Object elementToBeRemoved;
		ExtendedHashSet result;

		size = this.size();

		if (numElements >= size)
			return (new ExtendedHashSet(this));

		result = new ExtendedHashSet(this);
		numElementsToBeRemoved = size - numElements;

		for (index = 0; index < numElementsToBeRemoved; index++) {
			elementToBeRemoved = result.getRandomElement();
			result.remove(elementToBeRemoved);
		}

		return result;
	}

	/**
	 * Modifies the Set to be the union of itself and the specified <code>Collection</code>.  It acts like the operation (a = a union b), where a is the current Set and b is the specified <code>Collection</code>
	 *
	 * @param collection Specifies the collection to be unioned with
	 *
	 * @exception NullPointerException if the specified collection is null
	 */
	public void union(Collection collection) throws NullPointerException {
		ExtendedHashSet collExtHashSet;

		if (collection == null)
			throw (new NullPointerException(new String(
			        "Collection specified is null\n")));

		collExtHashSet = new ExtendedHashSet(collection);
		this.addAll(collExtHashSet);
	}

	/**
	 * Returns the union of two specified <code>Collections</code> as an <code>ExtendedHashSet</code>.  Does not change any of the <code>Collections</code>.
	 *
	 * @param collection1 Specifies the first collection
	 * @param collection2 Specifies the second collection
	 *
	 * @return the extended hash set containing the union
	 *
	 * @exception NullPointerException if any of the specified collection is null
	 */
	public static ExtendedHashSet union(Collection collection1,
	    Collection collection2) throws NullPointerException {
		ExtendedHashSet collExtHashSet1;
		ExtendedHashSet collExtHashSet2;
		ExtendedHashSet result;

		if ((collection1 == null) || (collection2 == null))
			throw (new NullPointerException(new String(
			        "Collection specified is null\n")));

		collExtHashSet1 = new ExtendedHashSet(collection1);
		collExtHashSet2 = new ExtendedHashSet(collection2);
		result = new ExtendedHashSet(collExtHashSet1);
		result.addAll(collExtHashSet2);

		return result;
	}

	/**
	 * Modifies the Set to be the intersection of the Set and the specified <code>Collection</code>.  It acts like the operation (a = a intersection b), where a is the current Set and b is the specified <code>Collection</code>
	 *
	 * @param collection Specifies the collection to be intersected with
	 *
	 * @exception NullPointerException if the specified collection is null
	 */
	public void intersection(Collection collection) throws NullPointerException {
		Object element;
		Iterator iterator;
		ExtendedHashSet tempExtHashSet;
		ExtendedHashSet collExtHashSet;

		if (collection == null)
			throw (new NullPointerException(new String(
			        "Collection specified is null\n")));

		tempExtHashSet = new ExtendedHashSet(this);
		collExtHashSet = new ExtendedHashSet(collection);
		iterator = tempExtHashSet.iterator();

		while (iterator.hasNext()) {
			element = (Object)iterator.next();

			if (!collExtHashSet.contains(element))
				this.remove(element);
		}
	}

	/**
	 * Returns the intersection of two specified <code>Collections</code>.  Does not change any of the <code>Collections</code>.
	 *
	 * @param collection1 Specifies the first collection
	 * @param collection2 Specifies the second collection
	 *
	 * @return the extended hash set containing the intersection
	 *
	 * @exception NullPointerException if any of the specified collection is null
	 */
	public static ExtendedHashSet intersection(Collection collection1,
	    Collection collection2) throws NullPointerException {
		Object element;
		ExtendedHashSet result;
		ExtendedHashSet collExtHashSet1;
		ExtendedHashSet collExtHashSet2;
		Iterator iterator;

		if ((collection1 == null) || (collection2 == null))
			throw (new NullPointerException(new String(
			        "Collection specified is null\n")));

		collExtHashSet1 = new ExtendedHashSet(collection1);
		collExtHashSet2 = new ExtendedHashSet(collection2);
		result = new ExtendedHashSet(collExtHashSet1);
		iterator = collExtHashSet1.iterator();

		while (iterator.hasNext()) {
			element = (Object)iterator.next();

			if (!collExtHashSet2.contains(element))
				result.remove(element);
		}

		return result;
	}

	/**
	 * Modifies the Set to be the difference of the current Set and the specified <code>Collection</code>.  It acts like the operation (a = a - b), where a is the current Set and b is the specified <code>Collection</code>
	 *
	 * @param collection Specifies the Collection to be intersected with
	 *
	 * @exception NullPointerException if the specified collection is null
	 */
	public void difference(Collection collection) throws NullPointerException {
		Object element;
		Iterator iterator;
		ExtendedHashSet tempExtHashSet;
		ExtendedHashSet collExtHashSet;

		if (collection == null)
			throw (new NullPointerException(new String(
			        "Collection specified is null\n")));

		tempExtHashSet = new ExtendedHashSet(this);
		collExtHashSet = new ExtendedHashSet(collection);
		iterator = tempExtHashSet.iterator();

		while (iterator.hasNext()) {
			element = (Object)iterator.next();

			if (collExtHashSet.contains(element))
				this.remove(element);
		}
	}

	/**
	 * Returns the difference of the two specified <code>Collections</code>.  Does not change any of the <code>Collections</code>.  The operation can be expressed as (a - b), where a and b are the collections as defined by the first and second argument respectively.
	 *
	 * @param collection1 Specifies the first Collection
	 * @param collection2 Specifies the second Collection
	 *
	 * @return the extended hash set containing the difference
	 *
	 * @exception NullPointerException if the specified collection is null
	 */
	public static ExtendedHashSet difference(Collection collection1,
	    Collection collection2) throws NullPointerException {
		Object element;
		ExtendedHashSet result;
		ExtendedHashSet collExtHashSet1;
		ExtendedHashSet collExtHashSet2;
		Iterator iterator;

		if ((collection1 == null) || (collection2 == null))
			throw (new NullPointerException(new String(
			        "Collection specified is null\n")));

		collExtHashSet1 = new ExtendedHashSet(collection1);
		collExtHashSet2 = new ExtendedHashSet(collection2);
		result = new ExtendedHashSet(collExtHashSet1);
		iterator = collExtHashSet1.iterator();

		while (iterator.hasNext()) {
			element = (Object)iterator.next();

			if (collExtHashSet2.contains(element))
				result.remove(element);
		}

		return result;
	}

	/**
	 * Tests whether the <code>Collection</code> specified by the second argument is a subset of the <code>Collection</code> specified by the first argument
	 *
	 * @param collection1 Specifies the first colletion
	 * @param collection2 Specifies the second colletion
	 *
	 * @return true, if collection2 is a subset of collection1
	 *
	 * @exception NullPointerException if the specified collection is null
	 */
	public static boolean isSubset(Collection collection1,
	    Collection collection2) throws NullPointerException {
		ExtendedHashSet collExtHashSet1;
		ExtendedHashSet collExtHashSet2;

		if ((collection1 == null) || (collection2 == null))
			throw (new NullPointerException(new String(
			        "Collection specified is null\n")));

		collExtHashSet1 = new ExtendedHashSet(collection1);
		collExtHashSet2 = new ExtendedHashSet(collection2);

		return (collExtHashSet1.containsAll(collExtHashSet2));
	}

	/**
	 * Tests whether the two <code>Collections</code> are equal or not in terms of Set Equality.  Two collections are Set Equal if first is the subset of the second and the second is a subset of the first.
	 *
	 * @param collection1 Specifies the first collection
	 * @param collection2 Specifies the second collection
	 *
	 * @return true, if the two collections are set equal
	 *
	 * @exception NullPointerException if any of the specified collections is null
	 */
	public static boolean areSetEqual(Collection collection1,
	    Collection collection2) throws NullPointerException {
		if ((collection1 == null) || (collection2 == null))
			throw (new NullPointerException(new String(
			        "Collection specified is null\n")));

		return ((ExtendedHashSet.isSubset(collection1, collection2))
		&& (ExtendedHashSet.isSubset(collection2, collection1)));
	}

	/**
	 * Partitions the specified Collection into a <code>Collection</code> of subsets each of a fixed specified size.  The elements for the subsets are chosen randomly.  The subsets are mutually exclusive and exhaustive over the entire Set.  Each of the subsets do not contain duplicate elements (as given by the equals method).  Just one subset may have less number of elements than the number specified if the total number of elements is not exactly divisible by the specified number.
	 *
	 * @param collection the collection to be partitioned
	 * @param numElements the number of elements in each subset
	 *
	 * @return the collection of the subsets
	 *
	 * @exception NullPointerException if the collection is null
	 * @exception IllegalArgumentException if the value of numElements is 0 or less
	 */
	public static ExtendedHashSet partitionSet(Collection collection,
	    int numElements) throws NullPointerException, IllegalArgumentException {
		ExtendedHashSet result;
		ExtendedHashSet remainingElements;
		ExtendedHashSet tempSet;

		if (collection == null)
			throw (new NullPointerException(new String(
			        "Collection specified is null\n")));

		if (numElements <= 0)
			throw (new IllegalArgumentException(new String(
			        "numElements specified is not positive\n")));

		result = new ExtendedHashSet();
		remainingElements = new ExtendedHashSet(collection);

		while (remainingElements.size() > numElements) {
			tempSet = remainingElements.getRandomElements(numElements);
			result.add(tempSet);
			remainingElements.difference(tempSet);
		}

		result.add(remainingElements);

		return result;
	}
}
