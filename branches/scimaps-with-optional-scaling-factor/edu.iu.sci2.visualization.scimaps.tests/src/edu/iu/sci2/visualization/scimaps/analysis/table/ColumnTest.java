package edu.iu.sci2.visualization.scimaps.analysis.table;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import prefuse.data.Schema;

import com.google.common.collect.ImmutableSet;

public class ColumnTest {
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void createWithNullClass() {
		thrown.expect(NullPointerException.class);
		Column.create(null, "");
	}
	
	@Test
	public void createWithNullName() {
		thrown.expect(NullPointerException.class);
		Column.create(Object.class, null);
	}
	
	@Test
	public void hashCodesEqualWhenNameIsSame() {
		Column<Integer> c1 = Column.create(Integer.class, "Name");
		Column<String> c2 = Column.create(String.class, "Name");
		
		assertEquals(c1.hashCode(), c2.hashCode());
	}
	
	@Test
	public void hashCodesDifferentWhenNameIsDifferent() {
		Column<Object> c1 = Column.create(Object.class, "1");
		Column<Object> c2 = Column.create(Object.class, "2");

		assertThat(c1.hashCode(), is(not(equalTo(c2.hashCode()))));
	}
	
	@Test
	public void equalWhenNameIsSame() {
		Column<Integer> c1 = Column.create(Integer.class, "Name");
		Column<String> c2 = Column.create(String.class, "Name");
		
		assertEquals(c1, c2);
	}
	
	@Test
	public void notEqualWhenNameIsDifferent() {
		Column<Object> c1 = Column.create(Object.class, "1");
		Column<Object> c2 = Column.create(Object.class, "2");
		
		assertThat(c1, is(not(c2)));
	}
	
	@Test
	public void comparesLessThanUsingName() {
		Column<Object> c1 = Column.create(Object.class, "1");
		Column<Object> c2 = Column.create(Object.class, "2");
		
		assertTrue(c1.compareTo(c2) < 0);
	}
	
	@Test
	public void comparesEqualUsingName() {
		Column<Object> c1 = Column.create(Object.class, "1");
		Column<Object> c2 = Column.create(Object.class, "1");
		
		assertEquals(0, c1.compareTo(c2));
	}
	
	@Test
	public void buildSchemaFailsOnNullSet() {
		thrown.expect(NullPointerException.class);
		Column.buildSchemaFor(null);
	}
	
	@Test
	public void buildSchemaResultIsLocked() {
		Schema schema = Column.buildSchemaFor(ImmutableSet.of(Column.create(Object.class, "")));
		
		assertTrue(schema.isLocked());
	}
}
