package edu.iu.sci2.visualization.geomaps.utility;

import java.util.Iterator;

import junit.framework.TestCase;

import com.google.common.base.Equivalences;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

import edu.iu.sci2.visualization.geomaps.utility.Iterators2;

public class Iterators2Test extends TestCase {
	public static void testOmitConsecutiveDuplicatesGivesSizeZero() {
		assertEquals(0, Iterators.size(
				deduplicateOnEquality(ImmutableList.of().iterator())));
	}
	
	public static void testOmitConsecutiveDuplicatesGivesSizeOne() {
		assertEquals(1, Iterators.size(
				deduplicateOnEquality(ImmutableList.of(1, 1, 1, 1).iterator())));
	}
	
	public static void testOmitConsecutiveDuplicatesGivesSizeTwo() {
		assertEquals(2, Iterators.size(
				deduplicateOnEquality(ImmutableList.of(1, 1, 2, 2).iterator())));
	}
	
	public static void testOmitConsecutiveDuplicatesGivesSizeThree() {
		assertEquals(3, Iterators.size(
				deduplicateOnEquality(ImmutableList.of(1, 1, 2, 1, 1).iterator())));
	}
	
	public static void testOmitConsecutiveDuplicatesGivesSizeFive() {
		assertEquals(5, Iterators.size(
				deduplicateOnEquality(ImmutableList.of(1, 1, 2, 1, 1, 2, 2, 2, 1).iterator())));
	}
	
	
	private static <E> Iterator<E> deduplicateOnEquality(Iterator<? extends E> source) {
		return Iterators2.omitConsecutiveDuplicates(source,	Equivalences.equals());
	}
}
