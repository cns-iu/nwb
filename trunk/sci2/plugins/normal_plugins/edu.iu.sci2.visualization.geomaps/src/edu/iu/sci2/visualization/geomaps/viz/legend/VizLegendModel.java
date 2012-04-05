package edu.iu.sci2.visualization.geomaps.viz.legend;

import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.utility.Continuum;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory.NumericFormatType;

public class VizLegendModel<U> {
	private final Range<Double> dataRange;
	private final Continuum<U> vizContinuum;
	private final String scalingLabel;
	private final String legendDescription;
	private final String dataColumnName;
	private final NumericFormatType numericFormatType;

	public VizLegendModel(Range<Double> dataRange, Continuum<U> vizContinuum, String scalingLabel,
			String legendDescription, String dataColumnName, NumericFormatType numericFormatType) {
		this.dataRange = dataRange;
		this.vizContinuum = vizContinuum;
		this.scalingLabel = scalingLabel;
		this.legendDescription = legendDescription;
		this.dataColumnName = dataColumnName;
		this.numericFormatType = numericFormatType;
	}

	public Range<Double> getDataRange() {
		return dataRange;
	}

	public Continuum<U> getVizContinuum() {
		return vizContinuum;
	}

	public String getScalingLabel() {
		return scalingLabel;
	}

	public String getLegendDescription() {
		return legendDescription;
	}

	public String getDataColumnName() {
		return dataColumnName;
	}

	public NumericFormatType getNumericFormatType() {
		return numericFormatType;
	}
}