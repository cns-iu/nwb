package edu.iu.sci2.visualization.geomaps.viz.legend;

import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;


public class AreaLegend implements Legend<Double, Double> {
	private final VizLegend<Double> vizLegend;
	private final double dataValueForMidpointArea;
	private final double midpointArea;
	
	public AreaLegend(
			VizLegend<Double> generalLegend,
			double rawMidArea,
			double areaMidrange) {
		this.vizLegend = generalLegend;
		this.dataValueForMidpointArea = rawMidArea;
		this.midpointArea = areaMidrange;
	}

	public double dataValueForOutputMidpoint() {
		return dataValueForMidpointArea;
	}
	
	public double getOutputMidpoint() {
		return midpointArea;
	}
	
	public String getScalingLabel() {
		return vizLegend.scalingLabel();
	}

	public String getLegendDescription() {
		return vizLegend.legendDescription();
	}

	public String getColumnName() {
		return vizLegend.dataColumnName();
	}

	public NumericFormatType getNumericFormatType() {
		return vizLegend.numericFormatType();
	}

	public Range<Double> getDataRange() {
		return vizLegend.dataRange();
	}

	public Range<Double> getVizRange() {
		return vizLegend.vizRange();
	}	
}
