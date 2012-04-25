package edu.iu.cns.database.extract.generic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.utilities.DatabaseUtilities;

import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;

public class ExtractTable implements Algorithm {
	private Data parent;

	private String query;
	private String label = "Extracted table";

	public ExtractTable(Data[] data, Dictionary parameters,
			@SuppressWarnings("unused") CIShellContext context) {
		// The context is unused, but included for easy access if a logger for
		// instance was required.
		this.parent = data[0];

		this.query = (String) parameters.get(ExtractTableFactory.QUERY_KEY);
		Object labelObject = parameters.get(ExtractTableFactory.LABEL_KEY);
		if (labelObject != null && labelObject.getClass().isAssignableFrom(String.class)) {
			this.label = String.valueOf(labelObject);
		}
	}

	public Data[] execute() throws AlgorithmExecutionException {

		Database database = (Database) this.parent.getData();
		Connection connection = DatabaseUtilities.connect(database,
				"Unable to communicate with the database.");
		try {
			DatabaseDataSource tableSource = ConnectionFactory
					.getDatabaseConnection(connection);
			Table extractedTable = tableSource.getData(this.query);

			Data outputData = wrapWithMetadata(extractedTable);
			return new Data[] { outputData };
		} catch (SQLException e) {
			throw new AlgorithmExecutionException(
					"Unable to communicate with the selected database.", e);
		} catch (DataIOException e) {
			throw new AlgorithmExecutionException(
					"There was a problem executing the query: "
							+ e.getMessage(), e);
		} finally {
			DatabaseUtilities.closeConnectionQuietly(connection);
		}

	}

	private Data wrapWithMetadata(Table extractedTable) {
		Data outputData = new BasicData(extractedTable, Table.class.getName());
		Dictionary<String, Object> metadata = outputData.getMetadata();
		metadata.put(DataProperty.LABEL, this.label);
		metadata.put(DataProperty.PARENT, this.parent);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		return outputData;
	}
}