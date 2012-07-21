package edu.iu.sci2.visualization.scimaps.parameters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.service.metatype.AttributeDefinition;

public class ScalingFactorAttributeDefinitionTest {
	private static AttributeDefinition ad;
	
	@BeforeClass
	public static void prepareAD() {
		ad = new ScalingFactorAttributeDefinition("id", "name", "description", "defaultValue");
	}
	
//	@Test
//	public void testParentWithNoValidation() {
//		// TODO Don't use "ad", make one with a mock parent that has no validation
//	}
//	
//	@Test
//	public void testParentThatFails() {
//		// TODO Don't use "ad", make one with a mock parent that has no validation
//	}
//	
//	@Test
//	public void testParentThatSucceeds() {
//		// TODO Don't use "ad", make one with a mock parent that has no validation
//	}
	
	@Test
	public void testValidateWithAutoTokenSucceeds() {
		assertFalse(ValidationResult.isFailure(ad.validate(ScalingFactorAttributeDefinition.AUTO_TOKEN)));
	}
	
	@Test
	public void testValidateWithEmptyStringFails() {
		assertTrue(ValidationResult.isFailure(ad.validate("")));
	}
	
	@Test
	public void testValidateWithGarbageStringFails() {
		assertTrue(ValidationResult.isFailure(ad.validate("!@#$*&^")));
	}
	
	@Test
	public void testNegativeOneFails() {
		assertTrue(ValidationResult.isFailure(ad.validate("-1")));
	}
	
	@Test
	public void testZeroFails() {
		assertTrue(ValidationResult.isFailure(ad.validate("0")));
	}
	
	@Test
	public void testIntegerOneSucceeds() {
		assertFalse(ValidationResult.isFailure(ad.validate("1")));
	}
	
	@Test
	public void testOnePointZeroSucceeds() {
		assertFalse(ValidationResult.isFailure(ad.validate("1.0")));
	}
}
