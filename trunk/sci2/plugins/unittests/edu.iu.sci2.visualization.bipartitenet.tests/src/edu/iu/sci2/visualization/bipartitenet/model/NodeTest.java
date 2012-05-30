package edu.iu.sci2.visualization.bipartitenet.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {

	
	@Test
	public void testNumericInterpreter() {
		assertEquals(Double.valueOf(3.1), Node.EXTRACT_DOUBLE_VALUE.apply("3.1"));
		assertEquals(Double.valueOf(3.1), Node.EXTRACT_DOUBLE_VALUE.apply("asdf jkl 3.1"));
		assertEquals(Double.valueOf(3.1), Node.EXTRACT_DOUBLE_VALUE.apply("asdf jkl 3.1 hahad"));
		assertEquals(Double.valueOf(3.1), Node.EXTRACT_DOUBLE_VALUE.apply("0.31e1"));
		assertEquals(Double.valueOf(3.1), Node.EXTRACT_DOUBLE_VALUE.apply("0.31e+1"));
		assertEquals(Double.valueOf(3.1), Node.EXTRACT_DOUBLE_VALUE.apply("31.0e-1"));
		assertEquals(Double.valueOf(3.1), Node.EXTRACT_DOUBLE_VALUE.apply("blah31.0e-1"));
		assertEquals(Double.valueOf(3), Node.EXTRACT_DOUBLE_VALUE.apply("3"));
		assertEquals(Double.valueOf(3), Node.EXTRACT_DOUBLE_VALUE.apply("3 blah"));
		assertEquals(Double.valueOf(3), Node.EXTRACT_DOUBLE_VALUE.apply("ahsdfkj 3 blah"));
		
		// null, not an exception
		assertEquals(null, Node.EXTRACT_DOUBLE_VALUE.apply("no numbers here"));
	}
	
	@Test
	public void testNumericOrdering() {
		int LESS = -1, EQUAL = 0, GREATER = 1;
		
		// 2 comes before 100
		assertEquals(GREATER, Node.NUMERIC_STRING_ORDERING.compare("100", "2"));
		
		// a comes before b
		assertEquals(GREATER, Node.NUMERIC_STRING_ORDERING.compare("b", "a"));
		
		// negative before positive
		assertEquals(LESS, Node.NUMERIC_STRING_ORDERING.compare("-1", "1"));
		
		// if numbers are equal, order on other stuff...
		// I'm not sure how to get this exactly right, so I'm just testing one case
		// that I think is fairly obvious.  There are lots of edge cases where I'm
		// not sure that the current behavior is the best.
		assertEquals(EQUAL, Node.NUMERIC_STRING_ORDERING.compare("1 blah", "1 blah"));
		assertEquals(LESS, Node.NUMERIC_STRING_ORDERING.compare("1a", "1b"));
	}

	
}
