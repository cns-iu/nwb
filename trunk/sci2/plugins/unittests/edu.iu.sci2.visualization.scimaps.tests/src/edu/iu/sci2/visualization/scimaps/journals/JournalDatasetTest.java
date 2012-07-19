package edu.iu.sci2.visualization.scimaps.journals;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import prefuse.data.Schema;
import prefuse.data.Table;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;

import edu.iu.sci2.visualization.scimaps.journals.JournalDataset.Journal;

public class JournalDatasetTest {
	private static final ImmutableMultiset<Journal> NO_JOURNALS =
			ImmutableMultiset.<JournalDataset.Journal>of();
	
	private static final Journal NATURE = JournalDataset.Journal.forName("Nature");
	private static final Journal SCIENCE = JournalDataset.Journal.forName("Science");
	private static final ImmutableMultiset<JournalDataset.Journal> JOURNALS = ImmutableMultiset
			.<JournalDataset.Journal>builder()
			.addCopies(NATURE, 3)
			.addCopies(SCIENCE, 5)
			.build();
	
	private static JournalDataset journalDataset;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void prepareTestJournalDataset() {
		journalDataset = JournalDataset.forJournals(JOURNALS);
	}
	
	@Test
	public void testConstructionFromMultisetFailsForNull() {
		thrown.expect(NullPointerException.class);
		JournalDataset.forJournals(null);
	}
	
	@Test
	public void testConstructionFromMultiset() {
		assertEquals(JOURNALS.size(), journalDataset.size());
	}
	
	@Test
	public void testConstructionFromTableFailsForNullTable() {
		thrown.expect(NullPointerException.class);
		JournalDataset.fromTable(null, "", null);
	}
	
	@Test
	public void testConstructionFromTableFailsForNullColumnName() {
		thrown.expect(NullPointerException.class);
		JournalDataset.fromTable(new Table(), null, null);
	}
	
	@Test
	public void testConstructionFromTableSucceedsForNullLogger() {
		Schema schema = new Schema(1);
		schema.addColumn("journal", String.class);
		Table table = schema.instantiate();
		
		assertEquals(0, JournalDataset.fromTable(table, "journal", null).size());
	}
	

	@Test
	public void testIsEmpty() {
		assertTrue(JournalDataset.forJournals(NO_JOURNALS).isEmpty());
	}
	
	@Test
	public void testIsNotEmpty() {
		assertFalse(journalDataset.isEmpty());
	}
	
	@Test
	public void testSizeIsZero() {
		assertEquals(0, JournalDataset.forJournals(NO_JOURNALS).size());
	}
	
	@Test
	public void testSizeIsNotZero() {
		assertThat(journalDataset.size(), is(not(0)));
	}
	
	@Test
	public void testCountOfNatureIsThree() {
		assertThat(journalDataset.count(NATURE), is(3));
	}
	
	@Test
	public void testCountOfAbsentJournalIsZero() {
		assertThat(journalDataset.count(JournalDataset.Journal.forName("absent")), is(0));
	}
	
	@Test
	public void testTotalCountOfNullFails() {
		thrown.expect(NullPointerException.class);
		journalDataset.totalCount(null);
	}
	
	@Test
	public void testTotalCountOfNatureAndScienceIsEight() {
		assertThat(journalDataset.totalCount(ImmutableSet.of(NATURE, SCIENCE)), is(8));
	}
	
	@Test
	public void testTotalCountOfAbsentJournalsIsZero() {
		assertThat(journalDataset.totalCount(ImmutableSet.of(
					JournalDataset.Journal.forName("absent1"),
					JournalDataset.Journal.forName("absent2"))),
				is(0));
	}
	
	@Test
	public void testCopyAsIdentifierCountMap() {
		assertEquals(
				ImmutableMap.of(
						NATURE.getIdentifier(), 3,
						SCIENCE.getIdentifier(), 5),
				journalDataset.copyAsIdentifierCountMap());
	}
	
	@Test
	public void testEqual() {
		assertThat(
				JournalDataset.forJournals(ImmutableMultiset.of(NATURE)),
				is(JournalDataset.forJournals(ImmutableMultiset.of(NATURE))));
	}
	
	@Test
	public void testNotEqualWhenCountsDiffer() {
		assertThat(
				JournalDataset.forJournals(ImmutableMultiset.of(NATURE, NATURE)),
				is(not(JournalDataset.forJournals(ImmutableMultiset.of(NATURE)))));
	}
	
	@Test
	public void testNotEqualWhenCountsSameButForDifferentJournals() {
		assertThat(
				JournalDataset.forJournals(ImmutableMultiset.of(NATURE)),
				is(not(JournalDataset.forJournals(ImmutableMultiset.of(SCIENCE)))));
	}
	
	@Test
	public void testHashCodesEqual() {
		assertThat(
				JournalDataset.forJournals(ImmutableMultiset.of(NATURE)).hashCode(),
				is(JournalDataset.forJournals(ImmutableMultiset.of(NATURE)).hashCode()));
	}
	
	@Test
	public void testHashCodesNotEqualWhenCountsDiffer() {
		assertThat(
				JournalDataset.forJournals(ImmutableMultiset.of(NATURE, NATURE)).hashCode(),
				is(not(JournalDataset.forJournals(ImmutableMultiset.of(NATURE)).hashCode())));
	}
	
	@Test
	public void testHashCodesNotEqualWhenCountsSameButForDifferentJournals() {
		assertThat(
				JournalDataset.forJournals(ImmutableMultiset.of(NATURE)).hashCode(),
				is(not(JournalDataset.forJournals(ImmutableMultiset.of(SCIENCE)).hashCode())));
	}
}
