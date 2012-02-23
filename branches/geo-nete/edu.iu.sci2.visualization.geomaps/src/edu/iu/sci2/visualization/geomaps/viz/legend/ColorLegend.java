package edu.iu.sci2.visualization.geomaps.viz.legend;

import java.awt.Color;

import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;


public class ColorLegend implements Legend<Double, Color> {
	private final VizLegend<Color> vizLegend;
	private final double dataValueForColorMidpoint;
	

	public ColorLegend(
			VizLegend<Color> generalLegend,
			double dataValueForOutputMidpoint) {
		this.vizLegend = generalLegend;
		this.dataValueForColorMidpoint = dataValueForOutputMidpoint;
	}

	public double getDataValueForOutRangeMidpoint() {
		return dataValueForColorMidpoint;
	}

	
	@Override
	public Range<Double> getDataRange() {
		return vizLegend.dataRange();
	}

	public String getScalingLabel() {
		return vizLegend.scalingLabel();
	}

	@Override
	public Range<Color> getVizRange() {
		return vizLegend.vizRange();
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
}
