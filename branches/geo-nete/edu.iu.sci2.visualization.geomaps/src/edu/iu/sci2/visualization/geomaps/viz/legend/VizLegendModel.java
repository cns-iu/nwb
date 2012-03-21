package edu.iu.sci2.visualization.geomaps.viz.legend;

import edu.iu.sci2.visualization.geomaps.utility.Continuum;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory.NumericFormatType;

public class VizLegendModel<U> {
	private final Continuum<Double> dataRange;
	private final Continuum<U> vizRange;
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
	public VizLegendModel(Continuum<Double> dataRange, Continuum<U> vizRange, String scalingLabel,
			String legendDescription, String dataColumnName, NumericFormatType numericFormatType) {
		this.dataRange = dataRange;
		this.vizRange = vizRange;
		this.scalingLabel = scalingLabel;
		this.legendDescription = legendDescription;
		this.dataColumnName = dataColumnName;
		this.numericFormatType = numericFormatType;
	}

	public Continuum<Double> getDataRange() {
		return dataRange;
	}

	public Continuum<U> getVizRange() {
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