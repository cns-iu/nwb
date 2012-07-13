package edu.iu.sci2.visualization.scimaps.analysis.table;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.collect.ImmutableSet;

public class RowTest {
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void newRowIsEmpty() {
		assertEquals(0, new Row().size());
	}
	
	@Test
	public void putNullColumnFails() {
		thrown.expect(NullPointerException.class);
		new Row().put(null, "");
	}
	
	@Test
	public void putNullValueFailsForPrimitiveType() {
		thrown.expect(NullPointerException.class);
		new Row().put(Column.create(int.class, "primitive int column"), null);
	}
	
	@Test
	public void putNullValueSucceedsForReferenceType() {
		new Row().put(Column.create(Integer.class, "Integer column"), null);
	}
	
	@Test
	public void putFailsIfColumnAlreadySet() {
		Column<Integer> column = Column.create(Integer.class, "Integer column");
		
		thrown.expect(IllegalStateException.class);
		new Row().put(column, 0).put(column, 1);
	}
	
	@Test
	public void sizeIncrementsIfPutSucceeds() {
		Column<Integer> column = Column.create(Integer.class, "Integer column");
		
		Row row = new Row();
		
		int initialSize = row.size();
		
		row.put(column, 0);
		
		assertEquals(initialSize + 1, row.size());
	}
	
	@Test
	public void copyIntoTupleFailsIfTupleCannotSetValue() {
		Column<Integer> column = Column.create(int.class, "int column");
		Schema schema = Column.buildSchemaFor(ImmutableSet.of(column));

		Tuple mockTuple = EasyMock.createMock(Tuple.class);
		expect(mockTuple.getSchema())
			.andReturn(schema);
		expect(mockTuple.canSet(EasyMock.anyObject(String.class), EasyMock.anyObject(Class.class)))
			.andReturn(false);
		replay(mockTuple);
		
		thrown.expect(IllegalArgumentException.class);
		new Row().put(column, 42).copyIntoTuple(mockTuple);
	}
	
	@Test
	public void copyIntoTupleFailsIfRowHasExtraColumns() {
		Tuple mockTuple = EasyMock.createMock(Tuple.class);
		expect(mockTuple.getSchema())
			.andReturn(new Schema()); // The target schema lacks the column set for our row
		expect(mockTuple.canSet(EasyMock.anyObject(String.class), EasyMock.anyObject(Class.class)))
			.andReturn(true); // TODO pointless?
		replay(mockTuple);
		
		thrown.expect(IllegalStateException.class);
		new Row().put(Column.create(int.class, "int column"), 42).copyIntoTuple(mockTuple);
	}
	
	@Test
	public void copyIntoTupleFailsIfRowIsMissingColumns() {
		Schema schema = Column.buildSchemaFor(ImmutableSet.of(Column.create(int.class, "int column")));
		
		Tuple mockTuple = EasyMock.createMock(Tuple.class);
		expect(mockTuple.getSchema())
			.andReturn(schema); // The target schema includes a column not set on our row
		expect(mockTuple.canSet(EasyMock.anyObject(String.class), EasyMock.anyObject(Class.class)))
			.andReturn(true); // TODO pointless?
		replay(mockTuple);
		
		thrown.expect(IllegalStateException.class);
		new Row().copyIntoTuple(mockTuple);
	}
	
	@Test
	public void copyIntoTupleSucceeds() {
		Column<Integer> column = Column.create(int.class, "int column");
		Schema schema = Column.buildSchemaFor(ImmutableSet.of(column));
		Table table = schema.instantiate();
		Tuple tuple = table.getTuple(table.addRow());

		final int testInt = 42;
		
		new Row().put(column, testInt).copyIntoTuple(tuple);
		
		// TODO OK to do both of these?
		assertEquals(1, table.getRowCount());
		assertEquals(testInt, tuple.get(column.getName()));
	}
	
	@Test
	public void addAsNewRowToTableSucceeds() {
		Column<Integer> column = Column.create(int.class, "int column");
		Schema schema = Column.buildSchemaFor(ImmutableSet.of(column));
		Table table = schema.instantiate();

		final int testInt = 42;
		
		new Row().put(column, testInt).addAsNewRowToTable(table);
		
		// TODO OK to do both of these?
		assertEquals(1, table.getRowCount());
		assertEquals(testInt, table.get(0, column.getName()));
	}
}
