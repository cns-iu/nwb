package edu.iu.epic.visualization.linegraph.zoom;

import java.util.Iterator;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.epic.visualization.linegraph.model.TimestepBounds;

/**
 * This algorithm is the naive way of finding the cut point. It assumes the 
 * subset start point is always first timestep and subset end point belongs to 
 * the tuple whose value diffres from the preceding value for the first time.
 * 
 * @author cdtank
 */
public class NaiveSubsetAlgorithm implements ZoomAlgorithm {
	
	/**
	 * This method makes certain reasonable assumptions,
	 * 		1. The stream value which is used for finding the cut point will 
	 * always be a number - double/int. And be readily cast-able into double.
	 * 		2. The timestep values will always be of type "Integer"
	 * 
	 * @param table
	 * @param lineColumnName
	 * @param timeStepColumnName
	 * @return
	 */
	public TimestepBounds getSubsetBounds(Table table, 
											String lineColumnName, 
											String timeStepColumnName) {
		
		int thresholdTimestep = Integer.MAX_VALUE;

		/*
		 * We start from the last tuple in the complete stream.
		 * */
		Iterator<Tuple> tuplesReversed = table.tuplesReversed();
		
		int index = 0;
		double previousStreamValue = 0;
		
		while (tuplesReversed.hasNext()) {
			
			Tuple currentTuple = tuplesReversed.next();
			
			double currentStreamValue = currentTuple.getDouble(lineColumnName);
			
			if (index == 0) {
				previousStreamValue = currentStreamValue;
			} 
			
			if (currentStreamValue != previousStreamValue) {
				
				thresholdTimestep = currentTuple.getInt(timeStepColumnName);
				break;
			}
			
			index++;
		}
		
		return new TimestepBounds(null, thresholdTimestep);
	}

}
