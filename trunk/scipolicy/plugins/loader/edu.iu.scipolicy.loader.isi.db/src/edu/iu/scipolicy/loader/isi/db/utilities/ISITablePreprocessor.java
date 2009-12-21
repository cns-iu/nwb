package edu.iu.scipolicy.loader.isi.db.utilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cishell.utilities.StringUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.nwb.shared.isiutil.ISITag;
import edu.iu.scipolicy.loader.isi.db.utilities.parser.IntegerParserWithDefault;

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
    			Tuple originalRow = table.getTuple(originalRowIndex);

    			int timesCited =
    				determineWhichRowToUseFromDuplicates(originalRow, currentRow);
   				isiUniqueArticleIdentifiersToRows.put(isiUniqueArticleIdentifier, timesCited);
    		} else {
    			int timesCited = getTimesCitedFromRow(currentRow);
    			isiUniqueArticleIdentifiersToRows.put(isiUniqueArticleIdentifier, timesCited);
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
			Tuple firstRow, Tuple secondRow) {
		int firstTimesCited = getTimesCitedFromRow(firstRow);
		int secondTimesCited = getTimesCitedFromRow(secondRow);

		return Math.max(firstTimesCited, secondTimesCited);
	}

	private static int getTimesCitedFromRow(Tuple row) {
		return IntegerParserWithDefault.parse(StringUtilities.simpleClean(
			row.getString(ISITag.TIMES_CITED.getColumnName())));
	}
}