package edu.iu.sci2.visualization.geomaps.viz.legend;

import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory.NumericFormatType;

public class VizLegendModel<U> {
	private final Range<Double> dataRange;
	private final Range<U> vizRange;
	private final String scalingLabel;
	private final String legendDescription;
	private final String dataColumnName;
	private final NumericFormatType numericFormatType;

	/**
	 * @param dataRange
	 * @param vizRange
	 * @param scalingLabel
	 * @param legendDescription
	 * @param dataColumnName
	 * @param numericFormatType
	 */
	public VizLegendModel(Range<Double> dataRange, Range<U> vizRange, String scalingLabel,
			String legendDescription, String dataColumnName, NumericFormatType numericFormatType) {
		this.dataRange = dataRange;
		this.vizRange = vizRange;
		this.scalingLabel = scalingLabel;
		this.legendDescription = legendDescription;
		this.dataColumnName = dataColumnName;
		this.numericFormatType = numericFormatType;
	}

	public Range<Double> getDataRange() {
		return dataRange;
	}

	public Range<U> getVizRange() {
		return vizRange;
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