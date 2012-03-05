package edu.iu.sci2.visualization.geomaps.viz.legend;

import java.awt.Color;

import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;


public class ColorLegend implements Legend<Double, Color> {
	private final VizLegendModel<Color> vizLegendModel;
	private final double dataValueForColorMidpoint;
	

	public ColorLegend(
			VizLegendModel<Color> generalLegend,
			double dataValueForOutputMidpoint) {
		this.vizLegendModel = generalLegend;
		this.dataValueForColorMidpoint = dataValueForOutputMidpoint;
	}

	public double getDataValueForOutRangeMidpoint() {
		return dataValueForColorMidpoint;
	}

	
	@Override
	public Range<Double> getDataRange() {
		return vizLegendModel.getDataRange();
	}

	public String getScalingLabel() {
		return vizLegendModel.getScalingLabel();
	}

	@Override
	public Range<Color> getVizRange() {
		return vizLegendModel.getVizRange();
	}

	public String getLegendDescription() {
		return vizLegendModel.getLegendDescription();
	}

	public String getColumnName() {
		return vizLegendModel.getDataColumnName();
	}

	public NumericFormatType getNumericFormatType() {
		return vizLegendModel.getNumericFormatType();
	}
}
