package edu.iu.sci2.visualization.geomaps.utility.numberformat;

import org.cishell.utilities.ToCaseFunction;

import junit.framework.TestCase;

import com.google.common.base.Predicate;

import edu.iu.sci2.visualization.geomaps.utility.StringPredicates;

public class StringPredicatesTest extends TestCase {
	private static Predicate<String> testcharactersequenceSubstringPredicate;
	private static Predicate<String> normalizedTestcharactersequenceSubstringPredicate;
	
	@Override
	protected void setUp() throws Exception {
		StringPredicatesTest.testcharactersequenceSubstringPredicate =
				StringPredicates.substringOf("testcharactersequence");
		StringPredicatesTest.normalizedTestcharactersequenceSubstringPredicate =
				StringPredicates.substringOf("TeStChArAcTeRsEqUeNcE", ToCaseFunction.LOWER);
	}
	
	public static void testEmptyIsSubstring() {
		assertTrue(testcharactersequenceSubstringPredicate.apply(""));
	}
	
	public static void testActIsSubstring() {
		assertTrue(testcharactersequenceSubstringPredicate.apply("act"));
	}
	
	public static void testThinkIsNotSubstring() {
		assertFalse(testcharactersequenceSubstringPredicate.apply("think"));
	}
	
	public static void testTerseIsSubstring() {
		assertTrue(testcharactersequenceSubstringPredicate.apply("terse"));
	}
	
	public static void testVerboseIsNotSubstring() {
		assertFalse(testcharactersequenceSubstringPredicate.apply("verbose"));
	}
		
	public static void testCaseMismatchIsNotSubstring() {
		assertFalse(testcharactersequenceSubstringPredicate.apply("ACT"));
	}
	
	
	public static void testEmptyIsNormalizedSubstring() {
		assertTrue(normalizedTestcharactersequenceSubstringPredicate.apply(""));
	}
	
	public static void testActIsNormalizedSubstring() {
		assertTrue(normalizedTestcharactersequenceSubstringPredicate.apply("act"));
	}
	
	public static void testThinkIsNotNormalizedSubstring() {
		assertFalse(normalizedTestcharactersequenceSubstringPredicate.apply("think"));
	}
	
	public static void testTerseIsNormalizedSubstring() {
		assertTrue(normalizedTestcharactersequenceSubstringPredicate.apply("terse"));
	}
	
	public static void testVerboseIsNotNormalizedSubstring() {
		assertFalse(normalizedTestcharactersequenceSubstringPredicate.apply("verbose"));
	}
	
	public static void testCaseMismatchIsSubstring() {
		assertTrue(normalizedTestcharactersequenceSubstringPredicate.apply("ACT"));
	}
}
