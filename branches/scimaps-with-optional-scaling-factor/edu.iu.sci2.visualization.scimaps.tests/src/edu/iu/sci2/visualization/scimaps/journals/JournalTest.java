package edu.iu.sci2.visualization.scimaps.journals;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JournalTest {
	private static final JournalDataset.Journal NATURE_NORMAL =
			JournalDataset.Journal.forName("Nature");
	private static final JournalDataset.Journal NATURE_WEIRD =
			JournalDataset.Journal.forName("  nATure ");
	private static final JournalDataset.Journal SCIENCE =
			JournalDataset.Journal.forName("Science");
	
	@Test
	public void testIdentifierIsNormalized() {
		assertThat(NATURE_WEIRD.getIdentifier(), is(NATURE_NORMAL.getIdentifier()));
	}
	
	@Test
	public void testNormalizedEqualsWeird() {
		assertThat(NATURE_WEIRD, is(NATURE_NORMAL));
	}
	
	@Test
	public void testNormalizedHashCodeEqualsWeirdHashCode() {
		assertThat(NATURE_WEIRD.hashCode(), is(NATURE_NORMAL.hashCode()));
	}
	
	@Test
	public void testDifferentJournalsNotEqual() {
		assertThat(NATURE_NORMAL, is(not(SCIENCE)));
	}
	
	@Test
	public void testDifferentJournalsHaveDifferentHashCodes() {
		assertThat(NATURE_NORMAL.hashCode(), is(not(SCIENCE.hashCode())));
	}
}
