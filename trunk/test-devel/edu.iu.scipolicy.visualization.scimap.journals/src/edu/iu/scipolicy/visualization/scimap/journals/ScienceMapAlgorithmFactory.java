package edu.iu.scipolicy.visualization.scimap.journals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class ScienceMapAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String JOURNAL_COLUMN_ID = "journalColumn";
	public static final String DATA_DISPLAY_NAME_ID = "datasetDisplayName";

	@SuppressWarnings("unchecked") // Raw Dictionary
	public Algorithm createAlgorithm(
			Data[] data, Dictionary parameters, CIShellContext context) {
		return new ScienceMapAlgorithm(data, parameters);
	}

	/* Add a drop-down containing the String and Integer type column names
	 * in table, with those seeming mostly like to record journals coming first.
	 */
	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		Table table = (Table) data[0].getData();
		
		ObjectClassDefinition newParameters;		
		newParameters = addJournalColumnDropdownParameter(oldParameters, table);		
		newParameters = addSourceDataFilenameParameter(newParameters, data);
		
		return newParameters;
	}

	private ObjectClassDefinition addSourceDataFilenameParameter(
			ObjectClassDefinition newParameters, Data[] data) {
		String guessedSourceDataFilename = AlgorithmUtilities.guessSourceDataFilename(data[0]);
		
		return MutateParameterUtilities.mutateDefaultValue(
				newParameters, DATA_DISPLAY_NAME_ID, guessedSourceDataFilename);
	}

	private ObjectClassDefinition addJournalColumnDropdownParameter(
			ObjectClassDefinition oldParameters, Table table) {
		List<String> goodColumnNames = new ArrayList<String>();
		
		/* Trying to catch journal names or abbreviations, as well as formatted
		 * ISSNs (like 1234-5678).
		 */
		goodColumnNames.addAll(
				Arrays.asList(TableUtilities.getValidStringColumnNamesInTable(table)));
		
		// Trying to catch purely integral ISSNs (like 12345678).
		goodColumnNames.addAll(
				Arrays.asList(TableUtilities.getValidIntegerColumnNamesInTable(table)));

		// Note descending order (that is, the most journalish names first).
		Collections.sort(
				goodColumnNames,
				Collections.reverseOrder(new Journalishness()));
		
		DropdownMutator mutator = new DropdownMutator();
		mutator.add(JOURNAL_COLUMN_ID, goodColumnNames);

		return mutator.mutate(oldParameters);
	}
	
	
	protected static class Journalishness implements Comparator<String> {
		public int compare(String left, String right) {
			int leftScore = score(left);
			int rightScore = score(right);
			
			if (leftScore < rightScore) {
				return -1;
			} else if (leftScore > rightScore) {
				return +1;
			} else { // leftScore == rightScore, we should hope
				return 0;
			}
		}
		
		/* Greater means more journal-ish.
		 * Naturally the scores are arbitrary and are useful only
		 * in relation to each other.
		 */
		private int score(String columnName) {
			String normalColumnName = columnName.toLowerCase();
			
			if (normalColumnName.contains("journal")) {
				if (normalColumnName.contains("name")
						|| normalColumnName.contains("title")) {
					return 100;
				}
				
				return 4;
			} else if (normalColumnName.contains("issn")) {
				return 50;
			} else if (normalColumnName.contains("period")) { // as in periodical
				return 3;
			} else if (normalColumnName.contains("serial")) {
				return 2;
			} else if (normalColumnName.startsWith("j")) {
				return 1;
			}
			
			return 0;
		}
	}
}