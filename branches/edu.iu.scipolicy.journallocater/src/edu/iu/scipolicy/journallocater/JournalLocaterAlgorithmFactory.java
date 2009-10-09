package edu.iu.scipolicy.journallocater;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class JournalLocaterAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String JOURNAL_COLUMN_ID = "journalColumn";

	@SuppressWarnings("unchecked") // Raw Dictionary
	public Algorithm createAlgorithm(
			Data[] data, Dictionary parameters, CIShellContext context) {
		return new JournalLocaterAlgorithm(data, parameters);
	}

	// Add a dropdown containing the column names of string type in the table.
	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		Table table = (Table) data[0].getData();

		String[] stringColumnNames =
			TableUtilities.getValidStringColumnNamesInTable(table);

		DropdownMutator mutator = new DropdownMutator();
		mutator.add(JOURNAL_COLUMN_ID, stringColumnNames);

		return mutator.mutate(oldParameters);
	}
}