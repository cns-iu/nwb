package edu.iu.sci2.visualization.scimaps;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.collect.ImmutableMultiset;

import edu.iu.sci2.visualization.scimaps.analysis.DisciplineAnalysis;
import edu.iu.sci2.visualization.scimaps.analysis.SubdisciplineAnalysis;
import edu.iu.sci2.visualization.scimaps.journals.JournalDataset;

public class MapOfScienceTest {
	private static final JournalDataset JOURNAL_DATASET = JournalDataset.forJournals(
			ImmutableMultiset.<JournalDataset.Journal>builder()
					.addCopies(JournalDataset.Journal.forName("African Invertebrates"), 2)
					.addCopies(JournalDataset.Journal.forName("Nature"), 3)
					.addCopies(JournalDataset.Journal.forName("Science"), 5)
					.addCopies(JournalDataset.Journal.forName("!@#$%^"), 7)					
					.build());
	private static MapOfScience mapOfScience;
	
	@BeforeClass
	public static void createJournalDatasetAndMapOfScience() {
		mapOfScience = new MapOfScience("dataColumnName", JOURNAL_DATASET.copyAsIdentifierCountMap());
	}
	
	@Test
	public void testTotalDisciplineAnalysisCountEqualsTotalJournalCount() {
		Table resultTable = mapOfScience.createDisciplineAnalysis(JOURNAL_DATASET).copyAsTable();
		
		int total = 0;
		
		// Raw Iterator from Table.tuples()
		for (@SuppressWarnings("unchecked") Iterator<Tuple> rows = resultTable.tuples(); rows.hasNext();) {
			Tuple row = rows.next();
			
			total += (Integer) row.get(DisciplineAnalysis.TOTAL.getName());
		}
		
		assertEquals(JOURNAL_DATASET.size(), total);
	}
	
	@Test
	public void testTotalSubdisciplineAnalysisCountEqualsTotalJournalCount() {
		Table resultTable = mapOfScience.createSubdisciplineAnalysis(JOURNAL_DATASET).copyAsTable();
		
		float total = 0.0f;
		
		// Raw Iterator from Table.tuples()
		for (@SuppressWarnings("unchecked") Iterator<Tuple> rows = resultTable.tuples(); rows.hasNext();) {
			Tuple row = rows.next();
			
			total += (Float) row.get(SubdisciplineAnalysis.TOTAL.getName());
		}
		
		assertEquals(JOURNAL_DATASET.size(), total, 0.01);
	}
}
