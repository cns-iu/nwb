package edu.iu.nwb.analysis.burst;

import prefuse.data.Table;

public class DocumentRetrieverFactory {

	public static DocumentRetriever createForColumn(String documentColumn) {
		if(BurstFactory.NO_DOCUMENT_COLUMN.equals(documentColumn)) {
			return new DocumentRetriever() {
				public Object retrieve(Table data, int row, String documentColumn) {
					return null;
				}};
		} else {
			return new DocumentRetriever() {
				public Object retrieve(Table data, int row, String documentColumn) {
					return data.get(row, documentColumn);
				}};
		}
	}
}