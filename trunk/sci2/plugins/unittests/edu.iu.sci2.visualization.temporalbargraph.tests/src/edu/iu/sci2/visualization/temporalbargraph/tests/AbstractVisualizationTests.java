package edu.iu.sci2.visualization.temporalbargraph.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.temporalbargraph.common.AbstractVisualization;

public class AbstractVisualizationTests extends TestCase {

	@Test
	public static void testDecimateMaxNumberGreaterThanCollectionLength() {
		Collection<Integer> collection = new ArrayList<Integer>();
		collection.add(1);
		collection.add(2);
		boolean caught = false;
		try {
			AbstractVisualization.decimate(collection,
					new Comparator<Integer>() {
						@Override
						public int compare(Integer int1, Integer int2) {
							return Integer.compare(int1, int2);
						}
					}, collection.size() + 2);
		} catch (IllegalArgumentException e) {
			caught = true;
		}

		assertTrue(caught);

	}

	@Test
	public static void testDecimateMaxNumberGreater2() {
		Collection<Integer> collection = new ArrayList<Integer>();
		collection.add(0);
		collection.add(1);
		collection.add(2);
		collection.add(3);
		collection.add(4);
		boolean caught = false;
		try {
			List<Integer> decimatedList = AbstractVisualization.decimate(
					collection, new Comparator<Integer>() {
						@Override
						public int compare(Integer int1, Integer int2) {
							return Integer.compare(int1, int2);
						}
					}, 1);
		} catch (IllegalArgumentException e) {
			caught = true;

		}

		assertTrue(caught);
	}

	@Test
	public static void testDecimateNone() {
		Collection<Integer> collection = new ArrayList<Integer>();
		collection.add(0);
		collection.add(1);
		collection.add(2);
		collection.add(3);
		collection.add(4);

		List<Integer> decimatedList = AbstractVisualization.decimate(
				collection, Ordering.natural(), collection.size());

		assertEquals(collection.size(), decimatedList.size());
		assertTrue(Ordering.natural().isOrdered(decimatedList));
	}
	
	@Test
	public static void testDecimateMax() {
		Collection<Integer> collection = new ArrayList<Integer>();
		collection.add(1);
		collection.add(3);
		collection.add(5);
		collection.add(2);
		collection.add(0);
		collection.add(7);
		collection.add(4);
		collection.add(6);
		collection.add(2);
		collection.add(0);
		collection.add(7);
		collection.add(4);
		collection.add(6);
		collection.add(6);
		
		List<Integer> decimatedList = AbstractVisualization.decimate(
				collection, Ordering.natural(), 5);
		assertTrue( "Expected 5 elements but had " + decimatedList.size(), decimatedList.size() == 5);
		assertTrue(Ordering.natural().isOrdered(decimatedList));
	}
	
	@Test
	public static void testDecimateLessThanMax() {
		Collection<Integer> collection = new ArrayList<Integer>();
		collection.add(1);
		collection.add(3);
		collection.add(5);
		collection.add(2);
		collection.add(0);
		collection.add(7);
		collection.add(4);
		collection.add(6);
		collection.add(2);
		collection.add(0);
		collection.add(7);
		collection.add(4);
		collection.add(6);
		
		List<Integer> decimatedList = AbstractVisualization.decimate(
				collection, Ordering.natural(), 5);
		assertTrue( "Expected less than 5 elements but had " + decimatedList.size(), decimatedList.size() < 5);
		assertTrue(Ordering.natural().isOrdered(decimatedList));
	}
}
