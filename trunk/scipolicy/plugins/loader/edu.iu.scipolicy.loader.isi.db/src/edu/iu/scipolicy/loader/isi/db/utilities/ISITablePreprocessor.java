package edu.iu.scipolicy.loader.isi.db.utilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.StringUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.nwb.shared.isiutil.ISITag;

public class ISITablePreprocessor {
	public static Collection<Integer> removeRowsWithDuplicateDocuments(Table table) {
    	Map<String, Integer> isiUniqueArticleIdentifiersToRows = new HashMap<String, Integer>();

    	for (IntIterator rows = table.rows(); rows.hasNext(); ) {
    		int currentRowIndex = rows.nextInt();
    		Tuple currentRow = table.getTuple(currentRowIndex);
    		String isiUniqueArticleIdentifier =
    			currentRow.getString(ISITag.UNIQUE_ID.getColumnName());

    		if (duplicateDocumentExists(
    				isiUniqueArticleIdentifier, isiUniqueArticleIdentifiersToRows)) {
    			int originalRowIndex =
    				isiUniqueArticleIdentifiersToRows.get(isiUniqueArticleIdentifier);

    			int targetRowIndex =
    				determineWhichRowToUseFromDuplicates(table, originalRowIndex, currentRowIndex);
   				isiUniqueArticleIdentifiersToRows.put(isiUniqueArticleIdentifier, targetRowIndex);
    		} else {
    			isiUniqueArticleIdentifiersToRows.put(isiUniqueArticleIdentifier, currentRowIndex);
    		}
    	}

    	return isiUniqueArticleIdentifiersToRows.values();
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