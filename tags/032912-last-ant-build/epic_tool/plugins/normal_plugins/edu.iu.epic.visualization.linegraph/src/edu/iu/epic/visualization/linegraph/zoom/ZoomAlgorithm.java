package edu.iu.epic.visualization.linegraph.zoom;

import prefuse.data.Table;
import edu.iu.epic.visualization.linegraph.model.TimestepBounds;

public interface ZoomAlgorithm {
	
	TimestepBounds getSubsetBounds(
			Table table, 
			String lineColumnName, 
			String timeStepColumnName);

}
