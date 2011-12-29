package edu.iu.sci2.visualization.geomaps.legend;

import edu.iu.sci2.visualization.geomaps.utility.Range;

public class NullLegendComponent implements LegendComponent {

	public Range<Double> getRawRange() {
		return new Range<Double>(0.0, 0.0);
	}

	public String toPostScript() {
		return "";
	}
}
