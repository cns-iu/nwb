package edu.iu.sci2.visualization.geomaps.viz.legend;

import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.utility.Continuum;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory.NumericFormatType;


public class AreaLegend implements Legend<Double, Double> {
	private final VizLegendModel<Double> vizLegendModel;
	private final double dataValueForMidpointArea;
	private final double midpointArea;
	
	public AreaLegend(
			VizLegendModel<Double> generalLegend,
			double dataValueForMidpointArea,
			double midpointArea) {
		this.vizLegendModel = generalLegend;
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
		return vizLegendModel.getScalingLabel();
	}

	public String legendDescription() {
		return vizLegendModel.getLegendDescription();
	}

	public String columnName() {
		return vizLegendModel.getDataColumnName();
	}

	public NumericFormatType numericFormatType() {
		return vizLegendModel.getNumericFormatType();
	}

	@Override
	public Range<Double> getDataRange() {
		return vizLegendModel.getDataRange();
	}

	@Override
	public Continuum<Double> getVizContinuum() {
		return vizLegendModel.getVizContinuum();
	}	
}
