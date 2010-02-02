package edu.iu.scipolicy.database.isi.merge;

import prefuse.data.Tuple;
import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;
import edu.iu.cns.database.merge.generic.maker.MergeCheck;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class IsiJaroMerge implements MergeCheck {
	
	private InterfaceStringMetric metric = new Jaro();

	public boolean shouldMerge(Tuple first, Tuple second) {
		String firstName = first.getString(ISI.UNSPLIT_ABBREVIATED_NAME).toLowerCase();
		String secondName = second.getString(ISI.UNSPLIT_ABBREVIATED_NAME).toLowerCase();
		//TODO: make this a parameter, along with other stuff, like duplicate node detector? Probably.
		return metric.getSimilarity(firstName, secondName) > .85;
	}
}
