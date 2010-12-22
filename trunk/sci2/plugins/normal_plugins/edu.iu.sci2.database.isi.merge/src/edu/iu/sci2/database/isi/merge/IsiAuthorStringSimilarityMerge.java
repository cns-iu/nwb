package edu.iu.sci2.database.isi.merge;

import prefuse.data.Tuple;
import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;
import edu.iu.cns.database.merge.generic.maker.MergeCheck;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class IsiAuthorStringSimilarityMerge implements MergeCheck {
	
	private final InterfaceStringMetric metric;

	public IsiAuthorStringSimilarityMerge(InterfaceStringMetric metric) {
		this.metric = metric;
	}

	public boolean shouldMerge(Tuple first, Tuple second) {
		String firstName = first.getString(ISI.UNSPLIT_ABBREVIATED_NAME).toLowerCase();
		String secondName = second.getString(ISI.UNSPLIT_ABBREVIATED_NAME).toLowerCase();
		//TODO: make this a parameter, along with other stuff, like duplicate node detector? Probably.
		return metric.getSimilarity(firstName, secondName) > .85;
	}
}
