package edu.iu.nwb.analysis.isidupremover.tuplecomparison;

import prefuse.data.Tuple;
import edu.iu.nwb.analysis.isidupremover.GraphUtil;

public class MainPubComparer implements ISIPubComparer {

	private TotalCitationComparer citationComparer = new TotalCitationComparer();
	private TotalLengthComparer lengthComparer = new TotalLengthComparer();
	
	public int compare(Tuple tu1, Tuple tu2, StringBuffer log) {
		
		boolean areIdentical = GraphUtil.areEqual(tu1, tu2);
		if (areIdentical) {

			log.append("Publications completely identical\r\n");
			return 0;
		} else {
			
			int citationDifference = citationComparer.compare(tu1, tu2, log);
			if (citationDifference > 0) {
				log.append("First has " + citationDifference +
						" more citations than second\r\n");
				return citationDifference;
			} else if (citationDifference < 0) {
				log.append("Second has " + (-citationDifference) +
				" more citations than the first\r\n");
				return citationDifference;
			} else {
				log.append("Both have same number of citations\r\n");
			}
			
			int lengthDifference = lengthComparer.compare(tu1, tu2, log);
			if (lengthDifference > 0) {
				log.append("First is " + 
						lengthDifference + " character(s) longer than the second\r\n");
				return lengthDifference;
			} else if (lengthDifference < 0) {
				log.append("Second is " +
						(-lengthDifference) + " character(s) longer than the first\r\n");
				return lengthDifference;
			} else {
				log.append("Both have the same length\r\n");
			}
			
			log.append("Unclear which should be kept\r\n");
			return 0;
		}
	}

}
