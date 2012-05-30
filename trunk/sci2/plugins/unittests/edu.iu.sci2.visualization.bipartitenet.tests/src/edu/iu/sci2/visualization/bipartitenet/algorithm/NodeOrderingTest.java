package edu.iu.sci2.visualization.bipartitenet.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class NodeOrderingTest {
	@Test
	public void testAnyOptionsExist() {
		ImmutableList<String> identifiers = NodeOrderingOption.getIdentifiers();
		assertNotNull(identifiers);
		assertTrue(identifiers.size() > 0);
	}

	@Test
	public void testOptionsAreGettable() {
		ImmutableList<String> identifiers = NodeOrderingOption.getIdentifiers();
		for (String id : identifiers) {
			assertNotNull(NodeOrderingOption.getOrdering(id));
		}
	}
	
	@Test
	public void testIsEnum() {
		assertTrue(Enum.class.isAssignableFrom(NodeOrderingOption.class));
	}
}
