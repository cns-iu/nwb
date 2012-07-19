package edu.iu.sci2.visualization.scimaps.analysis;

import static edu.iu.sci2.visualization.scimaps.analysis.DisciplineAnalysis.NAME;
import static edu.iu.sci2.visualization.scimaps.analysis.DisciplineAnalysis.TOTAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import oim.vivo.scimapcore.journal.Discipline;
import oim.vivo.scimapcore.journal.Disciplines;

import org.junit.BeforeClass;
import org.junit.Test;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multisets;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

public class DisciplineAnalysisTest {
	@SuppressWarnings("unchecked")
	private static final ImmutableSortedSet<Discipline> ALL_DISCIPLINES =
			ImmutableSortedSet.copyOf(Disciplines.getDisciplines());
	private static final SortedMultiset<Discipline> TEST_DISCIPLINES =
			createTestDisciplineMultiset();
	
	private static Table resultTable;
	
	@BeforeClass
	public static void createAnalysisAndCopyAsTable() {
		resultTable = new DisciplineAnalysis(TEST_DISCIPLINES).copyAsTable();
	}
	
	@Test
	public void checkRowCountEqualsNumberOfDistinctDisciplines() {
		assertEquals(TEST_DISCIPLINES.elementSet().size(), resultTable.getRowCount());
	}
	
	@Test
	public void checkRows() {
		@SuppressWarnings("unchecked") // Raw Iterator from Table.tuples()
		Iterator<Tuple> rows = resultTable.tuples();
		
		for (Discipline discipline : ALL_DISCIPLINES) {
			assertTrue("Fewer rows than disciplines.", rows.hasNext());
			Tuple row = rows.next();
			
			assertEquals("Name mismatch.", discipline.getName(), row.get(NAME.getName()));
			assertEquals("Count mismatch.", TEST_DISCIPLINES.count(discipline), row.get(TOTAL.getName()));
		}
		
		assertFalse("More rows than disciplines.", rows.hasNext());
	}


	private static SortedMultiset<Discipline> createTestDisciplineMultiset() {
		SortedMultiset<Discipline> disciplines = TreeMultiset.create();
		
		int occurrences = 1;
		for (Discipline discipline : ALL_DISCIPLINES) {
			disciplines.add(discipline, occurrences);
			occurrences++;
		}
		
		return Multisets.unmodifiableSortedMultiset(disciplines);
	}
}
