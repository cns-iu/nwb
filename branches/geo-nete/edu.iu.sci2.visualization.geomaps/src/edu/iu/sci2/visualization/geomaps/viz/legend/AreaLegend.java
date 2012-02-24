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
	
	public double midpointArea() {
		return midpointArea;
	}
	
	
	public String scalingLabel() {
		return vizLegend.scalingLabel();
	}

	public String legendDescription() {
		return vizLegend.legendDescription();
	}

	public String columnName() {
		return vizLegend.dataColumnName();
	}

	public NumericFormatType numericFormatType() {
		return vizLegend.numericFormatType();
	}

	@Override
	public Range<Double> dataRange() {
		return vizLegend.dataRange();
	}

	@Override
	public Range<Double> vizRange() {
		return vizLegend.vizRange();
	}	
}
