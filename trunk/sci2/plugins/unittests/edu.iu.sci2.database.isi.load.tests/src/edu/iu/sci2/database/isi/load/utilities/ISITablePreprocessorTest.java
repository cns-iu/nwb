package edu.iu.sci2.database.isi.load.utilities;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

import prefuse.data.Table;
import prefuse.data.Tuple;

public class ISITablePreprocessorTest {
	/**
	 * The generated hash code must have the prefix that says it's not really
	 * from ISI, and consist only of letters, numbers, and _ (so it's \w in
	 * regex-speak).
	 */
	private static final Pattern ALLOWED = Pattern.compile("^"
			+ ISITablePreprocessor.NON_ISI_UNIQUE_ID_PREFIX + "\\w+$");

	/**
	 * We want the generated hashes to be relatively short
	 */
	private static final int MAX_LENGTH_ALLOWED = 60;

	private static Table createTestTable() {
		Table t = new Table();
		t.addColumn("shoulder", String.class);
		t.addColumn("knee", int.class);
		int rowNum = t.addRow();

		t.setString(rowNum, "shoulder", "yes");
		t.setInt(rowNum, "knee", 3);
		return t;
	}

	@Test
	public void testHashRow() {
		Table t = createTestTable();
		Tuple tup = t.getTuple(0);
		String result = ISITablePreprocessor.hashRow(tup);
		assertTrue(ALLOWED.matcher(result).matches());
		assertTrue(result.length() < MAX_LENGTH_ALLOWED);
	}
	
	/**
	 * Make sure two records with identical field contents but different field names will have 
	 * different hash outputs.
	 * 
	 * Note that it would still be possible to maliciously create an identical hash with another
	 * paper, but I think that this brings it to the point where it won't happen unintentionally. 
	 */
	@Test
	public void testFieldsAreDistinguished() {
		Table t = new Table();
		t.addColumn("firstString", String.class);
		t.addColumn("secondString", String.class);
		
		int rowNum = t.addRow();
		t.setString(rowNum, "firstString", "blah");
		
		rowNum = t.addRow();
		t.setString(rowNum, "secondString", "blah");
		
		
		assertFalse(ISITablePreprocessor.hashRow(t.getTuple(0))
				.equals(ISITablePreprocessor.hashRow(t.getTuple(1))));
	}

}
