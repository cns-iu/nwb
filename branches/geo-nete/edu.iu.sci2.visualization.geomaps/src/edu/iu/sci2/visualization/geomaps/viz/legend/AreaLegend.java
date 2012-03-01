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

	public double getDataValueForOutputMidpoint() {
		return dataValueForMidpointArea;
	}
	
	public double getMidpointArea() {
		return midpointArea;
	}
	
	
	public String scalingLabel() {
		return vizLegend.getScalingLabel();
	}

	public String legendDescription() {
		return vizLegend.getLegendDescription();
	}

	public String columnName() {
		return vizLegend.getDataColumnName();
	}

	public NumericFormatType numericFormatType() {
		return vizLegend.getNumericFormatType();
	}

	@Override
	public Range<Double> getDataRange() {
		return vizLegend.getDataRange();
	}

	@Override
	public Range<Double> getVizRange() {
		return vizLegend.getVizRange();
	}	
}
