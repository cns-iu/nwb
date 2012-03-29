package edu.iu.cns.database.merge.generic.analyze.mergetable;

import static edu.iu.cns.database.merge.generic.prepare.plain.CreateMergingTable.MERGE_GROUP_IDENTIFIER_COLUMN;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.ColumnProjection;
import prefuse.data.util.NamedColumnProjection;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;

import edu.iu.cns.database.merge.generic.perform.EntityGroup;
import edu.iu.cns.database.merge.generic.perform.EntityGroup.MergingErrorException;
public final class MergeTableAnalyzer {
	
	private MergeTableAnalyzer() {
		assert false;
	}
	
	/**
	 * This will write the analysis for the merge table to the output stream
	 * specified. Only the column name given will be used in the report. It will
	 * thrown an AnalysisException if there was a problem analyzing the table.
	 */
	public static void writeAnalysis(OutputStream outputStream,
			Table mergeTable, String columnName) throws AnalysisException {
		writeAnalysis(outputStream, mergeTable, new NamedColumnProjection(columnName,
				true));
	}
	
	/**
	 * This will write the analysis for the merge table to the output stream
	 * specified. Only the column projection given will be used in the report.
	 * It will thrown an AnalysisException if there was a problem analyzing the
	 * table.
	 */
	public static void writeAnalysis(OutputStream outputStream, Table mergeTable,
			ColumnProjection columnFilter) throws AnalysisException {

		PrintWriter printWriter = new PrintWriter(outputStream);

		Map<String, EntityGroup> entityGroups = getEntityGroups(mergeTable,
				columnFilter);

		/*
		 * SOMEDAY The sorting here does not work well if the strings being
		 * sorted are really numbers or have numbers in them. If this becomes a
		 * big deal There is a solution here:
		 * http://www.davekoelle.com/alphanum.html More discussion here:
		 * http://stackoverflow
		 * .com/questions/104599/sort-on-a-string-that-may-contain-a-number
		 */

		List<String> groupIdentifiers = new ArrayList<String>(
				entityGroups.keySet());
		final Collator collator = Collator.getInstance();
		collator.setStrength(Collator.TERTIARY);

		Collections.sort(groupIdentifiers, collator);
		for (String groupIdentifier : groupIdentifiers) {
			analyzeEntityGroup(entityGroups.get(groupIdentifier), printWriter);
		}

		printWriter.flush();
		printWriter.close();
	}

	/*
	 * Example of what we're trying to achieve:
	 * 
	 * ===== Begin Group 1011 =====
	 * | Merge: 'FULL_TITLE->(empty),TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION->EUR PHYS J B', 'FULL_TITLE->EUROPEAN PHYSICAL JOURNAL B,TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION->EUR PHYS J B'
	 * | Use 'FULL_TITLE->EUROPEAN PHYSICAL JOURNAL B,TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION->EUR PHYS J B' to represent this merged group.
	 * ===== End Group 1011 =====
	 */
	private static void analyzeEntityGroup(EntityGroup entityGroup,
			PrintWriter printWriter) throws AnalysisException {
		String newline = System.getProperty("line.separator");
		printWriter.write("===== Begin Group ");
		printWriter.write(entityGroup.getGroupIdentifier());
		printWriter.write(" =====");
		printWriter.write(newline);
		printWriter.write("| Merge: ");

		MapJoiner entityJoiner = Joiner.on(",").withKeyValueSeparator("->")
				.useForNull("(empty)");

		try {
			List<Map<String, Object>> entities = entityGroup.getAllEntities();
			List<String> entityStrings = new ArrayList<String>();

			for (Map<String, Object> entity : entities) {
				StringBuilder entityString = new StringBuilder();
				entityString.append("'");

				assert entity.keySet().size() != 0;

				if (entity.keySet().size() == 1) {
					for (Object value : entity.values()) {
						entityString.append(value);
					}
				} else {
					entityString.append(entityJoiner.join(entity));
				}

				entityString.append("'");
				entityStrings.add(entityString.toString());
			}
			printWriter.write(Joiner.on(", ").join(entityStrings));
		} catch (MergingErrorException e) {
			throw new AnalysisException(
					"The merge table given to be analyzed was malformed."
							+ e.getMessage());
		}
		printWriter.write(newline);
		printWriter.write("| Use '");
		Map<String, Object> primaryEntity = entityGroup.getPrimaryEntity();
		if (primaryEntity.keySet().size() == 1) {
			for (Object value : primaryEntity.values()) {
				printWriter.write(value.toString());
			}
		} else {
			printWriter.write(entityJoiner.join(primaryEntity));
		}

		printWriter.write("' to represent this merged group.");
		printWriter.write(newline);
		printWriter.write("===== End Group ");
		printWriter.write(entityGroup.getGroupIdentifier());
		printWriter.write(" =====");
		printWriter.write(newline);
		printWriter.write(newline);

		printWriter.flush();
	}

	private static Map<String, EntityGroup> getEntityGroups(Table mergeTable,
			ColumnProjection columnFilter) throws AnalysisException {
		Map<String, EntityGroup> mergeGroupIDToEntityGroup = new HashMap<String, EntityGroup>();

		for (Iterator<?> rows = mergeTable.tuples(); rows.hasNext();) {
			Tuple row = (Tuple) rows.next();
			String groupID = row.getString(MERGE_GROUP_IDENTIFIER_COLUMN);
			if (!mergeGroupIDToEntityGroup.containsKey(groupID)) {
				mergeGroupIDToEntityGroup.put(groupID, new EntityGroup(groupID,
						columnFilter));
			}
			EntityGroup entityGroup = mergeGroupIDToEntityGroup.get(groupID);
			try {
				entityGroup.addRecord(row);
			} catch (MergingErrorException e) {
				throw new AnalysisException("Row " + row.getRow()
						+ " in the merge table was invalid:"
						+ System.getProperty("line.separator") + e.getMessage());
			}
			
		}

		return mergeGroupIDToEntityGroup;
	}

	public static class AnalysisException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1912428826934368182L;

		public AnalysisException(String message) {
			super(message);
		}
	}
}
