package edu.iu.nwb.analysis.isidupremover.tuplecomparison;

import prefuse.data.Tuple;

public class TotalLengthComparer implements ISIPubComparer {
	
	public int compare(Tuple tu1, Tuple tu2,  StringBuffer log) {
		
		int tu1Length = getTotalLengthOfStringFields(tu1);
		int tu2Length = getTotalLengthOfStringFields(tu2);
		
		return tu1Length - tu2Length;
	}
	
	private int getTotalLengthOfStringFields(Tuple tu) {
		int lengthOfStringFieldsSoFar = 0;
		
		final int numColumns = tu.getColumnCount();
		for (int ii = 0; ii < numColumns; ii++) {
			Object field = tu.get(ii);
			
			if (field instanceof String) {
				String stringField = (String) field;
				lengthOfStringFieldsSoFar += stringField.length();
			}
		}
		
		int totalLengthOfStringFields = lengthOfStringFieldsSoFar;
		return totalLengthOfStringFields;
	}
}
