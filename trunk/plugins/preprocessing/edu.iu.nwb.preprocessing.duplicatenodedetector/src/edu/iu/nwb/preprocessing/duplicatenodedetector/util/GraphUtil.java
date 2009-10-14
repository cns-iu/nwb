package edu.iu.nwb.preprocessing.duplicatenodedetector.util;

import java.util.Iterator;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.util.Sort;

public class GraphUtil {

	public static String[] getColumnNames(Table t) {
		String[] columnNames = new String[t.getColumnCount()];
		for (int ii = 0; ii < t.getColumnCount(); ii++) {
			columnNames[ii] = t.getColumnName(ii);
		}
		return columnNames;
	}

	public static Table copyTable(Table t) {
		Table tCopy = new Table();
		tCopy.addColumns(t.getSchema());

		for (Iterator ii = t.tuples(); ii.hasNext();) {
			Tuple tuple = (Tuple) ii.next();
			tCopy.addTuple(tuple);
		}
		return tCopy;
	}

	public static void printTable(Table t) {
		Iterator ii = t.tuples();
		while (ii.hasNext()) {
			System.out.println(ii.next());
		}
	}

	public static Table getSorted(Table t) {
		return getSortedByColumns(t, getColumnNames(t));
	}

	public static Table getSortedByColumns(Table t, String[] columnNames) {
		for (int ii = 0; ii < columnNames.length; ii++) {
			String columnName = columnNames[ii];
			if (t.getColumn(columnName) == null) {
				System.out.println("Cannot find column " + columnName);
			}
		}
		Sort tSort = new Sort(columnNames);
		Table sortedTable = t.select(ExpressionParser.predicate("TRUE"),
				tSort);
		return sortedTable;
	}

	public static boolean areEqual(Tuple tu1, Tuple tu2) {
		if (tu1.getColumnCount() != tu2.getColumnCount()) {
			return false;
		}

		for (int ii = 0; ii < tu1.getColumnCount(); ii++) {
			Object columnContents1 = tu1.get(ii);

			Object columnContents2 = null;
			boolean foundMatchingColumn = false;
			for (int kk = 0; kk < tu2.getColumnCount(); kk++) {

				if (tu2.getColumnName(kk).equals(tu1.getColumnName(ii))) {
					columnContents2 = tu2.get(kk);
					foundMatchingColumn = true;
					break;
				}
			}

			//TODO: Possibly remove this, since it SHOULD be guaranteed
			//not to happen by a check run before this algorithm
			if (! foundMatchingColumn) {
				return false;
			}

			if (columnContents1 == null && columnContents2 == null) {
				//nulls are equal to each other!
				continue;
			} else if (columnContents1 == null) {
				return false;
			} else if (columnContents2 == null) {
				return false;
			} else if (! columnContents1.equals(columnContents2)) {
				//neither are null, but they are still not equal.
				return false;
			}
		}

		//all column contents are equal.
		return true;
	}
}
