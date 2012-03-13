package edu.iu.sci2.visualization.temporalbargraph.print;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithmFactory;

public class TemporalBarGraphAlgorithmFactory extends
		AbstractTemporalBarGraphAlgorithmFactory {
	public static final double PAGE_LONG_DIMENTION = 11;
	public static final double PAGE_SHORT_DIMENTION = 8.5;

	private static final String QUERY_ID = "query";

	/**
	 * Pages can be landscapes or portrait. They are fixed at letter size for
	 * now, but all of the code correctly uses the height and width for
	 * arbitrary page sizes if this needs to be changed later.
	 * 
	 */
	public enum PageOrientation {
		LANDSCAPE(TemporalBarGraphAlgorithmFactory.PAGE_SHORT_DIMENTION,
				TemporalBarGraphAlgorithmFactory.PAGE_LONG_DIMENTION),
		PORTRAIT(TemporalBarGraphAlgorithmFactory.PAGE_LONG_DIMENTION,
				TemporalBarGraphAlgorithmFactory.PAGE_SHORT_DIMENTION);
		private final double height;
		private final double width;

		PageOrientation(double height, double width) {
			this.height = height;
			this.width = width;
		}

		public String getLabel() {

			String label = this.toString();
			String firstLetter = label.substring(0, 1);
			String labelWithoutFirstLetter = label.toLowerCase().substring(1,
					label.length());
			firstLetter.toUpperCase();

			return firstLetter + labelWithoutFirstLetter;

		}

		public String getValue() {
			return this.toString();
		}

		public double getHeight() {
			return this.height;
		}

		public double getWidth() {
			return this.width;
		}
	}

	@Override
	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		Data inputData = data[0];
		Table inputTable = (Table) inputData.getData();
		LogService logger = (LogService) ciShellContext
				.getService(LogService.class.getName());
		String labelColumn = parameters.get(LABEL_FIELD_ID).toString();
		String startDateColumn = parameters.get(START_DATE_FIELD_ID).toString();
		String endDateColumn = parameters.get(END_DATE_FIELD_ID).toString();
		String sizeByColumn = parameters.get(SIZE_BY_FIELD_ID).toString();
		String startDateFormat = (String) parameters.get(DATE_FORMAT_FIELD_ID);
		String endDateFormat = (String) parameters.get(DATE_FORMAT_FIELD_ID);
		String pageOrientation = PageOrientation.LANDSCAPE.toString();
		String categoryColumn = (String) parameters.get(CATEGORY_FIELD_ID);

		String query = (String) parameters.get(QUERY_ID);
		boolean shouldScaleOutput = ((Boolean) parameters
				.get(SHOULD_SCALE_OUTPUT_FIELD_ID)).booleanValue();

		PageOrientation orientation = PageOrientation.valueOf(pageOrientation);

		Double pageWidth = orientation.getWidth();
		Double pageHeight = orientation.getHeight();

		return new TemporalBarGraphAlgorithm(inputData, inputTable, logger,
				labelColumn, startDateColumn, endDateColumn, sizeByColumn,
				startDateFormat, endDateFormat, query, pageWidth, pageHeight,
				shouldScaleOutput, categoryColumn);
	}

	@SuppressWarnings("unused")
	// Removed the portrait version. See svn or mutateParameters for hints of
	// how to enable it.
	private static Collection<String> formOrientationLabels() {
		Collection<String> orientationLabels = new ArrayList<String>(
				PageOrientation.values().length);
		for (PageOrientation orientation : PageOrientation.values()) {
			orientationLabels.add(orientation.getLabel());
		}
		return orientationLabels;
	}

	@SuppressWarnings("unused")
	// Removed the portrait version. See svn or mutateParameters for hints of
	// how to enable it.
	private static Collection<String> formOrientationValues() {
		Collection<String> orientationValues = new ArrayList<String>(
				PageOrientation.values().length);
		for (PageOrientation orientation : PageOrientation.values()) {
			orientationValues.add(orientation.getValue());
		}
		return orientationValues;
	}

	@Override
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		Data inputData = data[0];
		Table table = (Table) inputData.getData();

		BasicObjectClassDefinition newParameters = MutateParameterUtilities
				.createNewParameters(oldParameters);

		AttributeDefinition[] oldAttributeDefinitions = oldParameters
				.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

			if (oldAttributeDefinitionID.equals(LABEL_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities
						.formLabelAttributeDefinition(oldAttributeDefinition,
								table);
			} else if (oldAttributeDefinitionID.equals(START_DATE_FIELD_ID)
					|| oldAttributeDefinitionID.equals(END_DATE_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities
						.formDateAttributeDefinition(oldAttributeDefinition,
								table);
			} else if (oldAttributeDefinitionID.equals(SIZE_BY_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities
						.formNumberAttributeDefinition(oldAttributeDefinition,
								table);
			} else if (oldAttributeDefinitionID.equals(PAGE_ORIENTATION_ID)) {
				/*
				 * To enable the Portrait version again, look in svn. If you are
				 * lazy, here is an overview...
				 * 
				 * put <AD name="Page Orientation" id="page_orientation"
				 * type="String" description="The orientation of the page."
				 * default="" /> into the metadata
				 * 
				 * add this newAttributeDefinition = MutateParameterUtilities
				 * .cloneToDropdownAttributeDefinition( oldAttributeDefinition,
				 * formOrientationLabels(), formOrientationValues());
				 * 
				 * here
				 */
			} else if (oldAttributeDefinitionID.equals(DATE_FORMAT_FIELD_ID)) {
				Collection<String> dateFormatLabels = formDateFormatOptionLabels();
				Collection<String> dateFormatOptions = formDateFormatOptionValues();
				newAttributeDefinition = MutateParameterUtilities
						.cloneToDropdownAttributeDefinition(
								oldAttributeDefinition, dateFormatLabels,
								dateFormatOptions);
			} else if (oldAttributeDefinitionID.equals(CATEGORY_FIELD_ID)) {
				List<String> additionalOptions = new ArrayList<String>();
				additionalOptions.add(DO_NOT_PROCESS_CATEGORY_VALUE);

				newAttributeDefinition = MutateParameterUtilities
						.formLabelAttributeDefinition(oldAttributeDefinition,
								table, additionalOptions);
				MutateParameterUtilities.mutateDefaultValue(oldParameters,
						CATEGORY_FIELD_ID, DO_NOT_PROCESS_CATEGORY_VALUE);
			}

			/*
			 * This can take optional ADs and mutate them needlessly into
			 * required ones, so be careful.
			 */
			newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, newAttributeDefinition);
		}

		return newParameters;
	}

}
