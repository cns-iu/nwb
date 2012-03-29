package edu.iu.cns.database.merge.generic.prepare.plain;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.DataValidator;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.cishell.utilities.DatabaseUtilities;
import org.cishell.utilities.database.DatabaseTable;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

public class CreateMergingTableFactory implements AlgorithmFactory,
		ParameterMutator, DataValidator {
	public static final String TABLE_PARAMETER = "table";

	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext context) {
		return new CreateMergingTable(data, parameters);
	}

	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {

		Database database = (Database) data[0].getData();

		String[] tableNames;
		try {
			tableNames = availableTableNames(database);
		} catch (SQLException e) {
			throw new RuntimeException(
					"There was a problem connecting to the database! "
							+ e.getMessage(), e);
		}

		if (tableNames.length == 0) {
			throw new RuntimeException(
					"There are no eligible tables, despite passing validation!");
		}

		// TODO: if all schema are the same, don't show any schema
		return makeDropdown(parameters, tableNames);
	}

	public String validate(Data[] data) {
		try {
			String[] availableTableNames = availableTableNames((Database) data[0]
					.getData());
			if (availableTableNames.length == 0) {
				return "No tables with primary keys exist in the database. "
						+ "Merging tables can only be made for tables with primary keys.";
			}
		} catch (SQLException e) {
			return "Unable to connect to this database.";
		}
		return null;
	}

	private static ObjectClassDefinition makeDropdown(
			ObjectClassDefinition parameters, String[] tableNames) {
		DropdownMutator mutator = new DropdownMutator();
		String[] prefixes = determinePrefixes(tableNames);
		String prefix = prefixes[0];
		boolean allPrefixesTheSame = allPrefixesTheSame(prefixes, prefix);
		if (allPrefixesTheSame) {
			String[] displayNames = determineDisplayNames(tableNames, prefix);
			mutator.add(TABLE_PARAMETER, displayNames, tableNames);
		} else {
			mutator.add(TABLE_PARAMETER, tableNames);
		}
		return mutator.mutate(parameters);
	}

	private static String[] determineDisplayNames(String[] tableNames,
			String prefix) {
		String[] displayNames = new String[tableNames.length];
		for (int ii = 0; ii < displayNames.length; ii++) {
			displayNames[ii] = tableNames[ii].substring(prefix.length());
		}
		return displayNames;
	}

	private static boolean allPrefixesTheSame(String[] prefixes, String prefix) {
		boolean allPrefixesTheSame = true;
		for (int ii = 1; ii < prefixes.length; ii++) {
			if (!prefixes[ii].equals(prefix)) {
				allPrefixesTheSame = false;
			}
		}
		return allPrefixesTheSame;
	}

	private static String[] determinePrefixes(String[] tableNames) {
		String[] prefixes = new String[tableNames.length];
		for (int ii = 0; ii < prefixes.length; ii++) {
			String[] parts = tableNames[ii].split("\\.");
			String prefix = "";
			for (int jj = 0; jj < parts.length - 1; jj++) {
				prefix += parts[jj] + ".";
			}
			prefixes[ii] = prefix;
		}
		return prefixes;
	}

	private static String[] availableTableNames(Database database)
			throws SQLException {
		String[] tableNames;
		Connection connection = null;
		try {
			connection = database.getConnection();
			tableNames = availableTableNames(connection);
		} finally {
			DatabaseUtilities.closeConnectionQuietly(connection);
		}
		return tableNames;
	}

	private static String[] availableTableNames(Connection connection)
			throws SQLException {
		DatabaseTable[] tables = DatabaseTable.availableTables(connection);
		if (tables.length == 0) {
			return new String[] {};
		}

		List<String> tableNames = new ArrayList<String>();
		for (int ii = 0; ii < tables.length; ii++) {
			if (tables[ii].hasPrimaryKey(connection)) {
				// TODO tables[ii].name might be a little more
				// refactor-friendly.
				tableNames.add(tables[ii].toString());
			}
		}

		/*
		 * TODO This is ultimately used in ways which would equally well befit a
		 * List. If you're worried about the raw types in DropdownMutator, I'd
		 * say don't; they'll get changed eventually.
		 */
		return tableNames.toArray(new String[] {});
	}
}