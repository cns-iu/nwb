package edu.iu.scipolicy.visualization.horizontalbargraph;

import java.util.Dictionary;

import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;

public final class Metadata {
	private String inputDataPath;
	private String datasetName;

	private String labelColumn;
	private String startDateColumn;
	private String endDateColumn;
	private String sizeByColumn;
	private double minimumAmountPerDayForScaling;
	private ScalingFunction scalingFunction;
	private String dateFormat;
	private double yearLabelFontSize;
	private double barLabelFontSize;
	private boolean scaleToFitPage;
	private String colorizedByColumn;

	public Metadata(
			String inputDataPath,
			String datasetName,
			String labelColumn,
			String startDateColumn,
			String endDateColumn,
			String sizeByColumn,
			double minimumAmountPerDayForScaling,
			ScalingFunction scalingFunction,
			String dateFormat,
			double yearLabelFontSize,
			double barLabelFontSize,
			boolean scaleToFitPage,
			String colorizedBy) {
		this.inputDataPath = inputDataPath;
		this.datasetName = datasetName;
		this.labelColumn = labelColumn;
		this.startDateColumn = startDateColumn;
		this.endDateColumn = endDateColumn;
		this.sizeByColumn = sizeByColumn;
		this.minimumAmountPerDayForScaling = minimumAmountPerDayForScaling;
		this.scalingFunction = scalingFunction;
		this.dateFormat = dateFormat;
		this.yearLabelFontSize = yearLabelFontSize;
		this.barLabelFontSize = barLabelFontSize;
		this.scaleToFitPage = scaleToFitPage;
		this.colorizedByColumn = colorizedBy;
	}

	public Metadata(Data inputData, Dictionary<String, Object> parameters) {
		this(
			(String)inputData.getMetadata().get(DataProperty.LABEL),
			FileUtilities.extractFileNameWithExtension(
				(String)inputData.getMetadata().get(DataProperty.LABEL)),
			(String)parameters.get(HorizontalBarGraphAlgorithm.LABEL_FIELD_ID),
        	(String)parameters.get(HorizontalBarGraphAlgorithm.START_DATE_FIELD_ID),
        	(String)parameters.get(HorizontalBarGraphAlgorithm.END_DATE_FIELD_ID),
        	(String)parameters.get(HorizontalBarGraphAlgorithm.SIZE_BY_FIELD_ID),
        	((Double)parameters.get(
        		HorizontalBarGraphAlgorithm.MINIMUM_AMOUNT_PER_DAY_FOR_SCALING_FIELD_ID)).
        			doubleValue(),
        	determineScalingFunction(
        		(String)parameters.get(HorizontalBarGraphAlgorithm.SCALING_FUNCTION_FIELD_ID)),
        	(String)parameters.get(HorizontalBarGraphAlgorithm.DATE_FORMAT_FIELD_ID),
        	((Double)parameters.get(
        		HorizontalBarGraphAlgorithm.YEAR_LABEL_FONT_SIZE_FIELD_ID)).doubleValue(),
        	((Double)parameters.get(
        		HorizontalBarGraphAlgorithm.BAR_LABEL_FONT_SIZE_FIELD_ID)).doubleValue(),
        	((Boolean)parameters.get(
        		HorizontalBarGraphAlgorithm.SCALE_TO_FIT_PAGE_ID)).booleanValue(),
        	(String)parameters.get(HorizontalBarGraphAlgorithm.COLORIZED_BY_FIELD_ID));
	}

	public String getInputDataPath() {
		return this.inputDataPath;
	}

	public String getDatasetName() {
		return this.datasetName;
	}

	public String getLabelColumn() {
		return this.labelColumn;
	}

	public String getStartDateColumn() {
		return this.startDateColumn;
	}

	public String getEndDateColumn() {
		return this.endDateColumn;
	}

	public String getSizeByColumn() {
		return this.sizeByColumn;
	}

	public double getMinimumAmountPerDayForScaling() {
		return this.minimumAmountPerDayForScaling;
	}

	public ScalingFunction getScalingFunction() {
		return this.scalingFunction;
	}

	public String getDateFormat() {
		return this.dateFormat;
	}

	public double getYearLabelFontSize() {
		return this.yearLabelFontSize;
	}

	public double getBarLabelFontSize() {
		return this.barLabelFontSize;
	}

	public boolean scaleToFitPage() {
		return this.scaleToFitPage;
	}
	
	public String getColorizedByColumn() {
		return this.colorizedByColumn;
	}

	private static ScalingFunction determineScalingFunction(String scalingFunctionName) {
		if (ScalingFunction.LINEAR_SCALING_FUNCTION_NAME.equals(scalingFunctionName)) {
			return ScalingFunction.LINEAR;
		// else if (ScalingFunction.LOGARITHMIC_SCALING_FUNCTION_NAME.equals(scalingFunctionName))
		} else {
			return ScalingFunction.LOGARITHMIC;
		}
	}
}