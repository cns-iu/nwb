package edu.iu.sci2.medline.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import edu.iu.sci2.medline.common.MedlineField;
import edu.iu.sci2.medline.common.MedlineRecord;
import edu.iu.sci2.testutilities.TestLogService;

public class TestMedlineRecord {

	@SuppressWarnings("static-method")
	@Test
	public void testNull() {
		try {
			new MedlineRecord(null, null);
			fail("A null medline record string should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// This is the desired result
		}
	}

	@SuppressWarnings("static-method")
	@Test
	public void testEmpty() {
		MedlineRecord record = new MedlineRecord("", new TestLogService());
		assertTrue("An empty record should be empty.", record.getFields()
				.isEmpty());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testTagParsingSeparation() {
		List<String> goodSeparators = new ArrayList<String>();
		goodSeparators.add("-");
		goodSeparators.add(" - ");
		goodSeparators.add(" -");
		goodSeparators.add("- ");
		goodSeparators.add("  -  ");
		goodSeparators.add("\t-\t");
		goodSeparators.add("\t- ");

		for (String separation : goodSeparators) {
			MedlineField medlineField = MedlineField.ABSTRACT;
			String tag = medlineField.getField();
			String value = "This is an value.";
			MedlineRecord record = new MedlineRecord(tag + separation + value, new TestLogService());
			assertFalse(
					"Some fields should have been found when using seperator '"
							+ separation + "'.", record.getFields().isEmpty());
			assertTrue(
					"The abstract field should have been parsed out when using seperator '"
							+ separation + "'.",
					record.getFields().contains(medlineField));
			assertTrue(
					"The value should have been saved when using seperator '"
							+ separation + "'." + record,
					record.getValue(medlineField).equals(value));
		}
	}

	
	@SuppressWarnings({ "static-method"})
	@Test
	public void testTagParsingField() {
		List<MedlineField> fields = ImmutableList.of(MedlineField.ABSTRACT, MedlineField.BOOK_TITLE, MedlineField.AUTHOR);
		for (MedlineField medlineField : fields) {
			String tag = medlineField.getField();
			String value = Long.toString(Math.abs(new Random().nextLong()));
			MedlineRecord record = new MedlineRecord(tag + "-" + value, new TestLogService());
			assertFalse("A field should have been found.", record.getFields().isEmpty());
			assertTrue("The field should have been found", record.getFields().contains(medlineField));
			assertTrue("The value should have been found.", record.getValue(medlineField).equals(value));
		}
	}
	
	@SuppressWarnings({ "static-method", "serial" })
	@Test
	public void testTagParsingFields() {
		List<MedlineField> fields = new ArrayList<MedlineField>() {{ add(MedlineField.ABSTRACT); add(MedlineField.BOOK_TITLE); add(MedlineField.AUTHOR); }};
		Map<MedlineField, String> fieldValue = new HashMap<MedlineField, String>();
		
		StringBuilder recordString = new StringBuilder();
		for (MedlineField field : fields) {
			fieldValue.put(field, Long.toString(Math.abs(new Random().nextLong())));
			recordString.append(field.getField() + "-" + fieldValue.get(field) + "\n");
		}
		
		MedlineRecord record = new MedlineRecord(recordString.toString(), new TestLogService());
		
		assertFalse("A field should have been found.", record.getFields().isEmpty());
		assertTrue("All fields should have been found.", record.getFields().containsAll(fields));
		for (MedlineField field : fields) {
			assertTrue("All field values should match.", record.getValue(field).equals(fieldValue.get(field)));
		}
	}
	
	@SuppressWarnings({ "static-method", "serial" })
	@Test
	public void testMultiValueFields() {
		MedlineField field = MedlineField.AUTHOR;
		StringBuilder recordString = new StringBuilder();
		List<String> values = new ArrayList<String>(){{ add("Author1"); add("Author2"); }};
		recordString.append(field.getField() + "-" + values.get(0) + "\n");
		recordString.append(field.getField() + "-" + values.get(1) + "\n");
		
		MedlineRecord record = new MedlineRecord(recordString.toString(), new TestLogService());
		assertTrue("There should be multipe values in the right format.", record.getValue(field).equals(Joiner.on(MedlineField.MEDLINE_MULTI_VALUE_SEPERATOR).join(values)));
		assertTrue("The values should be found when parsed.", record.getValues(field).containsAll(values));
	}
}
