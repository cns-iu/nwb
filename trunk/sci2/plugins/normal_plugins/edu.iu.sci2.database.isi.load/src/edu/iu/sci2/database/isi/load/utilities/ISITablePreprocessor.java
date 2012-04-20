package edu.iu.sci2.database.isi.load.utilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.StringUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import edu.iu.nwb.shared.isiutil.ISITag;

public class ISITablePreprocessor {
	public static final String NON_ISI_UNIQUE_ID_PREFIX = "sci2-generated-id:";

	/// Side-effects originalTable.
	public static void generateMissingUniqueIDs(Table table) {
		for (IntIterator rows = table.rows(); rows.hasNext();) {
			int currentRowIndex = rows.nextInt();
    		Tuple currentRow = table.getTuple(currentRowIndex);
    		String isiUniqueArticleIdentifier = StringUtilities.simpleClean(
    			currentRow.getString(ISITag.UNIQUE_ID.getColumnName()));

    		if (StringUtilities.isNull_Empty_OrWhitespace(isiUniqueArticleIdentifier)) {
    			String hash = hashRow(currentRow);
    			currentRow.set(ISITag.UNIQUE_ID.getColumnName(), hash);
    		}
		}
	}

	public static Collection<Integer> removeRowsWithDuplicateDocuments(Table table) {
    	Map<String, Integer> isiUniqueArticleIdentifiersToRows = new HashMap<String, Integer>();

    	for (IntIterator rows = table.rows(); rows.hasNext(); ) {
    		int currentRowIndex = rows.nextInt();
    		Tuple currentRow = table.getTuple(currentRowIndex);
    		String isiUniqueArticleIdentifier = StringUtilities.simpleClean(
    			currentRow.getString(ISITag.UNIQUE_ID.getColumnName()));

   			if (duplicateDocumentExists(
   					isiUniqueArticleIdentifier, isiUniqueArticleIdentifiersToRows)) {
				int originalRowIndex =
					isiUniqueArticleIdentifiersToRows.get(isiUniqueArticleIdentifier);

				int targetRowIndex = determineWhichRowToUseFromDuplicates(
					table, originalRowIndex, currentRowIndex);
				isiUniqueArticleIdentifiersToRows.put(
					isiUniqueArticleIdentifier, targetRowIndex);
			} else {
				isiUniqueArticleIdentifiersToRows.put(
					isiUniqueArticleIdentifier, currentRowIndex);
			}
    	}

    	return isiUniqueArticleIdentifiersToRows.values();
    }

	// default visibility just for testing
	static String hashRow(Tuple row) {
		Hasher hasher = Hashing.md5().newHasher();

		for (int ii = 0; ii < row.getColumnCount(); ii++) {
			// Need to put column name so that a record that's just
			// title:"SomeRandomData"
			// is different from one that's just
			// abstract:"SomeRandomData"
			hasher.putString(row.getColumnName(ii));
			
			String cellValue = StringUtilities.emptyStringIfNull(row.get(ii));
			hasher.putString(cellValue);
		}

		return NON_ISI_UNIQUE_ID_PREFIX + hasher.hash().toString();
	}
	
	private static boolean duplicateDocumentExists(
			String isiUniqueArticleIdentifier,
			Map<String, Integer> isiUniqueArticleIdentifiersToRows) {
		return isiUniqueArticleIdentifiersToRows.containsKey(isiUniqueArticleIdentifier);
	}

	private static int determineWhichRowToUseFromDuplicates(
			Table table, int firstRowIndex, int secondRowIndex) {
		Tuple firstRow = table.getTuple(firstRowIndex);
		Tuple secondRow = table.getTuple(secondRowIndex);
		int firstTimesCited = getTimesCitedFromRow(firstRow);
		int secondTimesCited = getTimesCitedFromRow(secondRow);

		if (firstTimesCited >= secondTimesCited) {
			return firstRowIndex;
		} else {
			return secondRowIndex;
		}
	}

	private static int getTimesCitedFromRow(Tuple row) {
		return IntegerParserWithDefault.parse(StringUtilities.simpleClean(
			row.getString(ISITag.TIMES_CITED.getColumnName())));
	}
}