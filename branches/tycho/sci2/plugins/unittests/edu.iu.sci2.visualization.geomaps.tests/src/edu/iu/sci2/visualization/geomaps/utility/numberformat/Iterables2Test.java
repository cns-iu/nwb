package edu.iu.sci2.visualization.geomaps.utility.numberformat;

import java.util.List;

import junit.framework.TestCase;

import com.google.common.base.Equivalences;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import edu.iu.sci2.visualization.geomaps.utility.Iterables2;

public class Iterables2Test extends TestCase {
	public static void testOmitConsecutiveDuplicatesHasSizeZero() {
		assertEquals(0, Iterables.size(
				deduplicateOnEquality(ImmutableList.of())));
	}
	
	public static void testOmitConsecutiveDuplicatesHasSizeOne() {
		assertEquals(1, Iterables.size(
				deduplicateOnEquality(ImmutableList.of(1, 1, 1, 1))));
	}
	
	public static void testOmitConsecutiveDuplicatesHasSizeTwo() {
		assertEquals(2, Iterables.size(
				deduplicateOnEquality(ImmutableList.of(1, 1, 2, 2))));
	}
	
	public static void testOmitConsecutiveDuplicatesHasSizeThree() {
		assertEquals(3, Iterables.size(
				deduplicateOnEquality(ImmutableList.of(1, 1, 2, 1, 1))));
	}
	
	public static void testOmitConsecutiveDuplicatesHasSizeFive() {
		assertEquals(5, Iterables.size(
				deduplicateOnEquality(ImmutableList.of(1, 1, 2, 1, 1, 2, 2, 2, 1))));
	}
	
	public static void testSplitZeroToOne() {
		assertEquals(
				ImmutableList.of(
						),
				splitOnInequality(ImmutableList.of()));
	}
	
	public static void testSplitFourToOne() {
		assertEquals(
				ImmutableList.of(
						ImmutableList.of(1, 1, 1, 1)),
				splitOnInequality(ImmutableList.of(1, 1, 1, 1)));
	}
	
	public static void testSplitFourToTwo() {
		assertEquals(
				ImmutableList.of(
						ImmutableList.of(1, 1),
						ImmutableList.of(2, 2)),
				splitOnInequality(ImmutableList.of(1, 1, 2, 2)));
	}
	
	public static void testSplitFiveToThree() {
		assertEquals(
				ImmutableList.of(
						ImmutableList.of(1, 1),
						ImmutableList.of(2),
						ImmutableList.of(1, 1)),
				splitOnInequality(ImmutableList.of(1, 1, 2, 1, 1)));
	}
	
	public static void testSplitNineToFive() {
		assertEquals(
				ImmutableList.of(
						ImmutableList.of(1, 1),
						ImmutableList.of(2),
						ImmutableList.of(1, 1),
						ImmutableList.of(2, 2, 2),
						ImmutableList.of(1)),
				splitOnInequality(ImmutableList.of(1, 1, 2, 1, 1, 2, 2, 2, 1)));
	}
	
	
	private static <E> Iterable<E> deduplicateOnEquality(Iterable<? extends E> iterable) {
		return Iterables2.omitConsecutiveDuplicates(iterable, Equivalences.equals());
	}

	private static <E> List<List<E>> splitOnInequality(Iterable<? extends E> iterable) {
		return Iterables2.split(iterable, Equivalences.equals());
	}
}
