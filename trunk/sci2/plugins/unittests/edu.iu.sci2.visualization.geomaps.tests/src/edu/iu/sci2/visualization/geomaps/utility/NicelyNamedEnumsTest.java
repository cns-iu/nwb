package edu.iu.sci2.visualization.geomaps.utility;

import junit.framework.TestCase;

import com.google.common.collect.ImmutableSet;

import edu.iu.sci2.visualization.geomaps.utility.NicelyNamedEnums;
import edu.iu.sci2.visualization.geomaps.utility.NicelyNamedEnums.NicelyNamed;

public class NicelyNamedEnumsTest extends TestCase {
	private static enum GoodEnum implements NicelyNamed {
		BLINKY("Blinky"),
		PINKY("Pinky"),
		INKY("Inky"),
		CLYDE("Clyde");
		
		private final String niceName;

		private GoodEnum(String niceName) {
			this.niceName = niceName;
		}

		@Override
		public String getNiceName() {
			return niceName;
		}		
	}
	
	private static enum BadEnum implements NicelyNamed {
		BOB("Robert"),
		BOBBY("Robert"),
		FRANK("Frank");
		
		private final String niceName;

		private BadEnum(String niceName) {
			this.niceName = niceName;
		}

		@Override
		public String getNiceName() {
			return niceName;
		}		
	}
	
	
	public static void testMissingLookupFailsInConformingEnum() {
		try {
			NicelyNamedEnums.getConstantNamed(GoodEnum.class, "Ain't 'fraid of no ghost");
			fail("Missing lookup did not signal IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			return;
		}
	}	
	
	public static void testGoodAllNiceNamesOf() {
		assertEquals(
				ImmutableSet.of(
						GoodEnum.BLINKY.getNiceName(),
						GoodEnum.PINKY.getNiceName(), 
						GoodEnum.INKY.getNiceName(), 
						GoodEnum.CLYDE.getNiceName()), 
				NicelyNamedEnums.allNiceNamesOf(GoodEnum.class));
	}
	
	public static void testGoodGetConstantNamed() {
		assertEquals(
				GoodEnum.INKY,
				NicelyNamedEnums.getConstantNamed(GoodEnum.class, GoodEnum.INKY.getNiceName()));
	}

	public static void testBadAllNiceNamesOf() {
		try {
			NicelyNamedEnums.allNiceNamesOf(BadEnum.class);
			fail("BadEnum has non-unique nice names and should throw IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			return;
		}
	}

	public static void testBadGetConstantNamedGettingADuplicate() {
		try {
			NicelyNamedEnums.getConstantNamed(BadEnum.class, "Robert");
			fail("Didn't detect that we're looking up a non-unique name.");
		} catch (IllegalArgumentException e) {
			return;
		}
	}
	
	public static void testBadGetConstantNamedGettingANonDuplicate() {
		try {
			NicelyNamedEnums.getConstantNamed(BadEnum.class, "Frank");
			fail("Though the looked up name is not itself a duplicate, other enum constants are " +
					"colliding and the tested method is meant to fail fast.");
		} catch (IllegalArgumentException e) {
			return;
		}
	}
}
