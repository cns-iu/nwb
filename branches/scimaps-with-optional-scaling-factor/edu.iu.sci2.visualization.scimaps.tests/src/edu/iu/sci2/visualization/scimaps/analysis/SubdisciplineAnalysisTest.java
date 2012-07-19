package edu.iu.sci2.visualization.scimaps.analysis;

import static edu.iu.sci2.visualization.scimaps.analysis.SubdisciplineAnalysis.ID;
import static edu.iu.sci2.visualization.scimaps.analysis.SubdisciplineAnalysis.DESCRIPTION;
import static edu.iu.sci2.visualization.scimaps.analysis.SubdisciplineAnalysis.TOTAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import oim.vivo.scimapcore.journal.Node;
import oim.vivo.scimapcore.journal.Nodes;

import org.junit.BeforeClass;
import org.junit.Test;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

import edu.iu.sci2.visualization.scimaps.analysis.SubdisciplineAnalysis.Subdiscipline;

public class SubdisciplineAnalysisTest {
	private static final ImmutableSortedSet<Subdiscipline> ALL_SUBDISCIPLINES =
			allNodesAsSubdisciplines();
	private static final ImmutableSortedMap<Subdiscipline, Float> TEST_SUBDISCIPLINES =
			createTestSubdisciplineOccurrencesMap();
	private static final int unmappedTotal = 42;
	
	private static Table resultTable;
	
	@BeforeClass
	public static void createAnalysisAndCopyAsTable() {
		resultTable = new SubdisciplineAnalysis(TEST_SUBDISCIPLINES, unmappedTotal).copyAsTable();
	}
	
	@Test
	public void checkRowCountEqualsNumberOfDistinctSubdisciplinesPlusOneUnmappedBin() {
		assertEquals(TEST_SUBDISCIPLINES.size() + 1, resultTable.getRowCount());
	}
	
	@Test
	public void checkRows() {
		@SuppressWarnings("unchecked") // Raw Iterator from Table.tuples()
		Iterator<Tuple> rows = resultTable.tuples();
		
		for (Subdiscipline subdiscipline : ALL_SUBDISCIPLINES) {
			assertTrue("Fewer rows than expected.", rows.hasNext());
			Tuple row = rows.next();
			
			assertEquals("ID mismatch.", subdiscipline.getId(), row.get(ID.getName()));
			assertEquals("Description mismatch.", subdiscipline.getName(), row.get(DESCRIPTION.getName()));
			assertEquals("Total mismatch.", TEST_SUBDISCIPLINES.get(subdiscipline), row.get(TOTAL.getName()));
		}
		
		// We should by now have advanced to the final "Unmapped" row
		assertTrue("Final row is missing.", rows.hasNext());
		Tuple unmappedRow = rows.next();
		
		assertEquals("ID mismatch.", null, unmappedRow.get(ID.getName()));
		assertEquals("Description mismatch.", "Unmapped", unmappedRow.get(DESCRIPTION.getName()));
		assertEquals("Total mismatch.", (float) unmappedTotal, unmappedRow.get(TOTAL.getName()));
		
		
		assertFalse("More rows than expected.", rows.hasNext());
	}


	private static ImmutableSortedSet<Subdiscipline> allNodesAsSubdisciplines() {
		ImmutableSortedSet.Builder<Subdiscipline> allSubdisciplines = ImmutableSortedSet.naturalOrder();
		
		@SuppressWarnings("unchecked")
		Set<Node> allNodes = Nodes.getNodes();
		
		for (Node node : allNodes) {
			allSubdisciplines.add(Subdiscipline.forID(node.getId()));
		}
		
		return allSubdisciplines.build();
	}

	private static ImmutableSortedMap<Subdiscipline, Float> createTestSubdisciplineOccurrencesMap() {
		ImmutableSortedMap.Builder<Subdiscipline, Float> builder = ImmutableSortedMap.<Subdiscipline, Float>naturalOrder();
		
		float occurrences = 1;
		for (Subdiscipline subdiscipline : ALL_SUBDISCIPLINES) {
			builder.put(subdiscipline, occurrences);
			occurrences++;
		}
		
		return builder.build();
	}
}
