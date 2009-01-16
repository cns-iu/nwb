package edu.iu.scipolicy.filtering.topn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;

import javax.sql.DataSource;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

//TODO: This algorithm needs to be able to enforce a stronger contract. Specifically, that the input DB is a single table.
//TODO: Also make metadata note that this alg modifies its input.
public class TopNAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new TopNAlgorithm(data, parameters, context);
	}

	private static final String COLUMN_TO_SORT_BY_ID = "columnToSortBy"; // must agree with id in METADATA.XML

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		// Mutating parameters to allow user to choose which column they want to sort the table rows by

		ObjectClassDefinition oldDefinition = parameters;

		// create a new empty object class definition
		BasicObjectClassDefinition newDefinition;
		try {
			newDefinition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(),
					oldDefinition.getDescription(), oldDefinition.getIcon(16));
		} catch (IOException e) {
			newDefinition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(),
					oldDefinition.getDescription(), null);
		}

		// fill the new object class definition with the old definition's attributes
		// as we pass old attributes into the new definition, modify as desired

		// TODO: Determine good way to know things about DataSource, such as names of tables (using metadata?)
		// determine the names of all the columns in the table of this DataSource
		DataSource tableDB = (DataSource) data[0].getData();
		try {
			// TODO: Confirm this is correct
			Connection tableDBConnection = tableDB.getConnection();
			String[] tableColumnNames = getTableColumnNames(tableDBConnection);

			AttributeDefinition[] definitions = oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

			for (int ii = 0; ii < definitions.length; ii++) {

				AttributeDefinition attribute = definitions[ii];
				String id = attribute.getID();
				if (COLUMN_TO_SORT_BY_ID.equals(id)) {
					newDefinition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, new BasicAttributeDefinition(
							id, attribute.getName(), attribute.getDescription(), AttributeDefinition.STRING,
							tableColumnNames, tableColumnNames));
				} else {
					newDefinition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, attribute);
				}
			}

			// Determine the name of the DB's table by inspecting metadata.
			// DatabaseMetaData tableDBMetadata = tableDBConnection.getMetaData();
			// ResultSet tableDescriptions = tableDBMetadata.getTables(null, null, null, null); //get all tables
			// System.out.println(tableDescriptions.toString()); //TODO: START HERE LATER (Make DB real also)
		} catch (SQLException e) {
			// TODO: This is not so good. We need to have mutateParameters throw a special caught exception
//			throw new Exception("Could not establish connection to database.", e);
			e.printStackTrace();
		}

		return newDefinition;
	}

	private String[] getTableColumnNames(Connection tableDBConnection) {
		try {
			// TODO: figure this out
			DatabaseMetaData tableDBMetadata = tableDBConnection.getMetaData();
			ResultSet columnsDescription = tableDBMetadata.getColumns(null, null, null, null); // get all columns with
																								// no filters
			return new String[] { columnsDescription.toString() };
		} catch (Exception e) {
			return null;
		}
	}

	private String getTableName() {
		// TODO: Determine metadata standard for getting the name of a table
		return null;
	}
}