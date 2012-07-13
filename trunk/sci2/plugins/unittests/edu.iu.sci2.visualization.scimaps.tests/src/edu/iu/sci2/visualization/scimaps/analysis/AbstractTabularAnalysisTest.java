package edu.iu.sci2.visualization.scimaps.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import edu.iu.sci2.visualization.scimaps.analysis.table.Column;
import edu.iu.sci2.visualization.scimaps.analysis.table.Row;

public class AbstractTabularAnalysisTest {	
	private static final Column<Integer> ID = Column.create(int.class, "id");
	private static final ImmutableList<Integer> PRIMARY_IDS = ImmutableList.of(0, 1, 2, 3);
	private static final ImmutableList<Integer> ADDITIONAL_IDS = ImmutableList.of(4, 5);
	
	private static Table table;
	
	@BeforeClass
	public static void createTestAnalysisAndCopyAsTable() {
		table = new TestTabularAnalysis().copyAsTable();
	}
	
	@Test
	public void checkRowCountEqualsTotalIDCount() {
		assertEquals(PRIMARY_IDS.size() + ADDITIONAL_IDS.size(), table.getRowCount());
	}
	
	@Test
	public void checkRows() {
		@SuppressWarnings("unchecked") // Raw Iterator from table.tuples()
		Iterator<Tuple> rows = table.tuples();
		
		for (Integer id : Iterables.concat(PRIMARY_IDS, ADDITIONAL_IDS)) {
			assertTrue("Fewer rows than IDs", rows.hasNext());
			Tuple row = rows.next();
			
			assertTrue(
					String.format(
							"Can't get objects of type %s from column %s",
							ID.getClazz(), ID.getName()),
					row.canGet(ID.getName(), ID.getClazz()));
			
			assertEquals("ID mismatch", id, row.get(ID.getName()));
		}
		
		assertFalse("More rows than IDs", rows.hasNext());
	}
	
	/**
	 * A one-column "analysis" of two short lists of integer IDs. The value of the column for each
	 * row is simply the ID.
	 */
	static class TestTabularAnalysis extends AbstractTabularAnalysis<Integer> {
		protected TestTabularAnalysis() {
			super(ImmutableSet.of(ID));
		}

		@Override
		protected Iterable<Integer> getElements() {
			return PRIMARY_IDS;
		}

		@Override
		protected Row createRowFor(Integer id) {
			return new Row().put(ID, id);
		}
		
		@Override
		protected Iterable<Row> createAdditionalRows() {
			ImmutableList.Builder<Row> additionalRows = ImmutableList.builder();
			
			for (Integer id : ADDITIONAL_IDS) {
				additionalRows.add(new Row().put(ID, id));
			}
			
			return additionalRows.build();
		}
	}
}
