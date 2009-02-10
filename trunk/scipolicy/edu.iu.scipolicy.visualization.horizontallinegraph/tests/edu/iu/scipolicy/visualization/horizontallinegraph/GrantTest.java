package edu.iu.scipolicy.visualization.horizontallinegraph;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GrantTest {
	private Grant firstGrant;
	private Grant secondGrant;
	private Grant thirdGrant;
	
	@Before
	public void setUp() throws Exception {
		this.firstGrant =
			new Grant("grant1", new Date(1984, 0, 1), new Date(1984, 0, 2), 0.0f);
		
		this.secondGrant =
			new Grant("grant2", new Date(1984, 0, 1), new Date(1984, 0, 3), 0.1f);
		
		this.thirdGrant =
			new Grant("grant3", new Date(1984, 0, 2), new Date(1984, 0, 3), 0.0f);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test public void testCompareToNotEqual() {
		int compareToResultFirstToThird =
			this.firstGrant.compareTo(this.thirdGrant);
		
		int compareToResultThirdToFirst =
			this.thirdGrant.compareTo(this.firstGrant);
		
		if ((compareToResultFirstToThird >= 0) ||
			(compareToResultThirdToFirst <= 0))
		{
			fail();
		}
	}
	
	@Test public void testCompareToEqual() {
		int compareToResultFirstToSecond =
			this.firstGrant.compareTo(this.secondGrant);
		
		int compareToResultSecondToFirst =
			this.secondGrant.compareTo(this.firstGrant);
		
		if ((compareToResultFirstToSecond != 0) ||
			(compareToResultSecondToFirst != 0))
		{
			fail();
		}
	}
}
