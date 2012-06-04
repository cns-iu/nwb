package edu.iu.sci2.visualization.scimaps.journals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.ColumnNotFoundException;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
//SOMEDAY this and the FieldsMapAlgorithmFactory are very similar.  Combine them.
public class JournalsMapAlgorithmFactory implements AlgorithmFactory,
		ParameterMutator {
	public static final String JOURNAL_COLUMN_ID = "journalColumn";
	public static final String SUBTITLE_ID = "subtitle";
	public static final String SCALING_FACTOR_ID = "scalingFactor";
	public static final String WEB_VERSION_ID = "webVersion";
	public static final String SHOW_EXPORT_WINDOW = "showWindow";
	public static final String DEFAULT_SUBTITLE_PREFIX = "Generated from ";

	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext context) {
		String journalColumnName = (String) parameters.get(JOURNAL_COLUMN_ID);
		float scalingFactor = ((Float) parameters.get(SCALING_FACTOR_ID))
				.floatValue();
		String dataDisplayName = (String) parameters.get(SUBTITLE_ID);
		boolean webVersion = ((Boolean) parameters.get(WEB_VERSION_ID))
				.booleanValue();
		boolean showWindow = ((Boolean) parameters.get(SHOW_EXPORT_WINDOW))
				.booleanValue();
		LogService logger = (LogService) context.getService(LogService.class
				.getName());
		return new JournalsMapAlgorithm(data, journalColumnName, scalingFactor,
				dataDisplayName, webVersion, showWindow, logger);
	}

	/**
	 * Mutate the parameters to add a dropdown list of possible journal names
	 * and a suggested filename.
	 */
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		Table table = (Table) data[0].getData();

		ObjectClassDefinition paramsWithJournal = addJournalColumnDropdownParameter(
				oldParameters, table);

		ObjectClassDefinition paramsWithJournalAndFilename = addSourceDataFilenameParameter(
				paramsWithJournal, data);

		return mutateSubtitleParameter(paramsWithJournalAndFilename, data);
	}

	/**
	 * Add the possible columns that contain the Journal Title to a drop down in
	 * the order of their likelihood of being the correct column.
	 */
	private static ObjectClassDefinition addJournalColumnDropdownParameter(
			ObjectClassDefinition oldParameters, Table table) {
		List<String> goodColumnNames = new ArrayList<String>();

		boolean hasStringColumn = true;
		try {
			/*
			 * Journal names or abbreviations
			 */
			goodColumnNames.addAll(Arrays.asList(TableUtilities
					.getValidStringColumnNamesInTable(table)));
		} catch (ColumnNotFoundException e) {
			hasStringColumn = false;
		}

		if (hasStringColumn || !goodColumnNames.isEmpty()) {
			// Note descending order (that is, the most journal-ish names
			// first).
			Collections.sort(goodColumnNames,
					Collections.reverseOrder(new Journalishness()));

			DropdownMutator mutator = new DropdownMutator();
			mutator.add(JOURNAL_COLUMN_ID, goodColumnNames);

			return mutator.mutate(oldParameters);
		}
		String message = "Table contains no string or integer columns, "
				+ "so there is no candidate for a column containing journal identifiers.";
		throw new AlgorithmCreationFailedException(message);

	}

	/**
	 * Guess the filename for the data and add it as a suggested name.
	 */
	private static ObjectClassDefinition addSourceDataFilenameParameter(
			ObjectClassDefinition newParameters, Data[] data) {
		String guessedSourceDataFilename = AlgorithmUtilities
				.guessSourceDataFilename(data[0]);

		return MutateParameterUtilities.mutateDefaultValue(newParameters,
				SUBTITLE_ID, guessedSourceDataFilename);
	}
	
	/**
	 * Generate a default subtitle.
	 */
	private static ObjectClassDefinition mutateSubtitleParameter(
			ObjectClassDefinition newParameters, Data[] data) {
		// Generate default subtitle from the dataset label
		String defaultSubtitle = DEFAULT_SUBTITLE_PREFIX 
				+ data[0].getMetadata().get(DataProperty.LABEL);
		
		return MutateParameterUtilities
				.mutateDefaultValue(newParameters, SUBTITLE_ID,defaultSubtitle);
	}

	/**
	 * Compare two titles of columns to determine which is more likely to be a
	 * column for journal titles.
	 * 
	 */
	protected static class Journalishness implements Comparator<String> {
		/**
		 * Compare two column titles to determine which is more likely to be a
		 * column containing journal titles.
		 */
		public int compare(String left, String right) {
			return new Integer(score(left)).compareTo(score(right));
		}

		/*
		 * Greater means more journal-ish. Naturally the scores are arbitrary
		 * and are useful only in relation to each other.
		 */
		private static int score(String columnName) {
			String normalColumnName = columnName.toLowerCase();

			if (normalColumnName.contains("journal")) {
				if (normalColumnName.contains("name")
						|| normalColumnName.contains("title")) {
					if (normalColumnName.contains("full")) {
						return 100;
					}
					return 50;
				}

				return 4;
			} else if (normalColumnName.contains("period")) { // as in
																// periodical
				return 3;
			} else if (normalColumnName.startsWith("j")) {
				return 1;
			}

			return 0;
		}
	}
}