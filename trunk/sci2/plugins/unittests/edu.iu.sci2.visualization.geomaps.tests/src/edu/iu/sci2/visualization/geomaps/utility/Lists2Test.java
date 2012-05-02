package edu.iu.sci2.visualization.geomaps.utility;

import java.util.List;

import junit.framework.TestCase;

import com.google.common.base.Equivalences;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import edu.iu.sci2.visualization.geomaps.utility.Lists2;

public class Lists2Test extends TestCase {
	public static void testLastIndexFromEmptyMisses() {
		assertEquals(-1, lastIndexOfPositive(ImmutableList.<Integer>of()));
	}
	
	public static void testLastIndexFromFourMisses() {
		assertEquals(-1, lastIndexOfPositive(ImmutableList.of(-1, -1, -1, -1)));
	}
	
	public static void testLastIndexFromFourIsAtThree() {
		assertEquals(3, lastIndexOfPositive(ImmutableList.of(-1, -1, -1, 1)));
	}
	
	public static void testLastIndexFromFourIsAtThree2() {
		assertEquals(3, lastIndexOfPositive(ImmutableList.of(-1, 1, -1, 1)));
	}
	
	public static void testLastIndexFromFourIsAtTwo() {
		assertEquals(2, lastIndexOfPositive(ImmutableList.of(-1, -1, 1, -1)));
	}
	
	public static void testLastIndexFromFourIsAtOne() {
		assertEquals(1, lastIndexOfPositive(ImmutableList.of(-1, 1, -1, -1)));
	}
	
	public static void testLastIndexFromFourIsAtZero() {
		assertEquals(0, lastIndexOfPositive(ImmutableList.of(1, -1, -1, -1)));
	}
	
	
	public static void testOmitConsecutiveDuplicatesHasSizeZero() {
		assertEquals(0, deduplicateOnEquality(ImmutableList.of()).size());
	}
	
	public static void testOmitConsecutiveDuplicatesHasSizeOne() {
		assertEquals(1, deduplicateOnEquality(ImmutableList.of(1, 1, 1, 1)).size());
	}
	
	public static void testOmitConsecutiveDuplicatesHasSizeTwo() {
		assertEquals(2, deduplicateOnEquality(ImmutableList.of(1, 1, 2, 2)).size());
	}
	
	public static void testOmitConsecutiveDuplicatesHasSizeThree() {
		assertEquals(3, deduplicateOnEquality(ImmutableList.of(1, 1, 2, 1, 1)).size());
	}
	
	public static void testOmitConsecutiveDuplicatesHasSizeFive() {
		assertEquals(5, deduplicateOnEquality(ImmutableList.of(1, 1, 2, 1, 1, 2, 2, 2, 1)).size());
	}
	
	
	private static <E> List<E> deduplicateOnEquality(List<? extends E> iterable) {
		return Lists2.omitConsecutiveDuplicates(iterable, Equivalences.equals());
	}
	
	private static int lastIndexOfPositive(List<Integer> list) {
		return Lists2.lastIndexOf(list, PositiveIntegerPredicate.INSTANCE);
	}
	

	private enum PositiveIntegerPredicate implements Predicate<Integer> {
		INSTANCE;
	
		@Override
		public boolean apply(Integer i) {
			return i.intValue() > 0;
		}		
	}
}
