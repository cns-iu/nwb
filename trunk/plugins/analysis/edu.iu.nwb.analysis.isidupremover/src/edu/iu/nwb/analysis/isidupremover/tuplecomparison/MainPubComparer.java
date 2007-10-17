package edu.iu.nwb.analysis.isidupremover.tuplecomparison;

import prefuse.data.Tuple;
import edu.iu.nwb.analysis.isidupremover.GraphUtil;

public class MainPubComparer implements ISIPubComparer {

	private TotalCitationComparer citationComparer = new TotalCitationComparer();
	private TotalLengthComparer lengthComparer = new TotalLengthComparer();
	
	public int compare(Tuple tu1, Tuple tu2, StringBuilder log) {
		
		boolean areIdentical = GraphUtil.areEqual(tu1, tu2);
		if (areIdentical) {

			log.append("The publications are identical in every way\r\n");
			return 0;
		} else {
			
			int citationDifference = citationComparer.compare(tu1, tu2, log);
			if (citationDifference > 0) {
				log.append("The first publication record has " + citationDifference +
						" more citations than the second\r\n");
				return citationDifference;
			} else if (citationDifference < 0) {
				log.append("The second publication record has " + (-citationDifference) +
				" more citations than the first\r\n");
				return citationDifference;
			} else {
				log.append("Both publication records have the same number of citation references\r\n");
			}
			
			int lengthDifference = lengthComparer.compare(tu1, tu2, log);
			if (lengthDifference > 0) {
				log.append("The first publication record is " + 
						lengthDifference + " characters longer than the second\r\n");
				return lengthDifference;
			} else if (lengthDifference < 0) {
				log.append("The second publication record is " +
						(-lengthDifference) + " character longer than the first\r\n");
				return lengthDifference;
			} else {
				log.append("Both publication records have the same length\r\n");
			}
			
			log.append("It is unclear which publication record should be kept\r\n");
			return 0;
		}
	}

}
