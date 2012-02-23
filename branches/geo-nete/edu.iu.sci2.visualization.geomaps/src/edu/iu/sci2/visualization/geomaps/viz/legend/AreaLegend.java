package edu.iu.sci2.visualization.geomaps.viz.legend;

import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;


public class AreaLegend implements Legend<Double, Double> {
	private final VizLegend<Double> vizLegend;
	private final double dataValueForMidpointArea;
	private final double midpointArea;
	
	public AreaLegend(
			VizLegend<Double> generalLegend,
			double dataValueForMidpointArea,
			double midpointArea) {
		this.vizLegend = generalLegend;
		this.dataValueForMidpointArea = dataValueForMidpointArea;
		this.midpointArea = midpointArea;
	}

	public double dataValueForOutputMidpoint() {
		return dataValueForMidpointArea;
	}
	
	public double getMidpointArea() {
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

	@Override
	public Range<Double> getDataRange() {
		return vizLegend.dataRange();
	}

	@Override
	public Range<Double> getVizRange() {
		return vizLegend.vizRange();
	}	
}
