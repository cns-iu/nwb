package edu.iu.cns.database.load.framework.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.DatabaseUtilities;
import org.cishell.utilities.ProgressMonitorUtilities;
import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.Schema;

public class DerbyDatabaseCreator {
	public static final int MAX_VARCHAR_LENGTH = 32000;
	public static final String NULL_VALUE = "null";

	public static final double PERCENTAGE_OF_PROGRESS_FOR_MODEL_CREATION = 0.05;
	public static final double PERCENTAGE_OF_PROGRESS_FOR_DATABASE_CREATION = 0.95;

	/**
	 * dataType should be the human-readable display name of the type of originating data,
	 *  e.g. ISI or NSF.
	 */
	public static Database createFromModel(
			DatabaseService databaseProvider,
			DatabaseModel model,
			String dataType,
			ProgressMonitor progressMonitor,
			double totalWork)
			throws AlgorithmCanceledException, DatabaseCreationException, SQLException {
		Database database = databaseProvider.createNewDatabase();
		Connection databaseConnection = database.getConnection();

		try {
			createEmptyTablesFromModel(model, databaseConnection, progressMonitor);
			fillTablesFromModel(model, databaseConnection, progressMonitor, totalWork);
			addForeignKeysToTablesFromModel(model, databaseConnection, progressMonitor);
		} finally {
			DatabaseUtilities.closeConnectionQuietly(databaseConnection);
		}

		return database;
	}

	private static void createEmptyTablesFromModel(
			DatabaseModel model,
			Connection databaseConnection,
			ProgressMonitor progressMonitor)
			throws AlgorithmCanceledException, SQLException {
		Statement createTableStatement = databaseConnection.createStatement();

		try {
			for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
				ProgressMonitorUtilities.handleCanceledOrPausedAlgorithm(progressMonitor);

				createEmptyTable(itemContainer, createTableStatement);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			createTableStatement.close();
		}
	}

	private static void fillTablesFromModel(
			DatabaseModel model,
			Connection databaseConnection,
			ProgressMonitor progressMonitor,
			double totalWork)
			throws AlgorithmCanceledException, SQLException {
		double totalFillTablesWork = (totalWork * PERCENTAGE_OF_PROGRESS_FOR_DATABASE_CREATION);
		double unitsWorked = (totalWork * PERCENTAGE_OF_PROGRESS_FOR_MODEL_CREATION);
		double totalRowItemCount = calculateTotalRowItemCount(model);

		for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			ProgressMonitorUtilities.handleCanceledOrPausedAlgorithm(progressMonitor);

			Collection<? extends RowItem<?>> items = itemContainer.getItems();

			if (items.size() == 0) {
				continue;
			}

			double workPercentageForThisTable =
				(itemContainer.getItems().size() / totalRowItemCount);
			double unitsOfWorkForThisTable = (workPercentageForThisTable * totalFillTablesWork);
			fillTable(
				databaseConnection,
				itemContainer,
				progressMonitor,
				unitsWorked,
				unitsOfWorkForThisTable);
			unitsWorked += unitsOfWorkForThisTable;
		}
	}

	private static void addForeignKeysToTablesFromModel(
			DatabaseModel model, Connection databaseConnection, ProgressMonitor progressMonitor)
			throws AlgorithmCanceledException, SQLException {
		progressMonitor.describeWork("Adding foreign key constraints to the database tables.");
		Statement addForeignKeysStatement = databaseConnection.createStatement();

		for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			ProgressMonitorUtilities.handleCanceledOrPausedAlgorithm(progressMonitor);

			addForeignKeysToTable(itemContainer, addForeignKeysStatement);
		}
	}

	private static void createEmptyTable(
			RowItemContainer<? extends RowItem<?>> itemContainer,
			Statement createTableStatement) throws SQLException {
		String tableName = itemContainer.getDatabaseTableName();
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();

		String fieldNamesForQuery =
			schemaToFieldsForCreateTableQueryString(schema) +
			schemaToPrimaryKeysForCreateTableQueryString(schema);

		String createTableQuery =
			String.format("CREATE TABLE \"%s\" (%s)", tableName, fieldNamesForQuery);
		createTableStatement.execute(createTableQuery);
	}

	private static void addForeignKeysToTable(
			RowItemContainer<? extends RowItem<?>> itemContainer,
			Statement addForeignKeysStatement) throws SQLException {
		String tableName = itemContainer.getDatabaseTableName();
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();

		for (Schema.ForeignKey foreignKey : schema.getForeignKeys()) {
			String addForeignKeyQuery = String.format(
				"ALTER TABLE \"%s\" ADD CONSTRAINT \"%s_CONSTRAINT_%s\" FOREIGN KEY (\"%s\") " +
					"REFERENCES \"%s\" (\"%s\")",
				tableName,
				foreignKey.getFieldName(),
				UUID.randomUUID(),
				foreignKey.getFieldName(),
				foreignKey.getReferenceTo_TableName(),
				Schema.PRIMARY_KEY);
			addForeignKeysStatement.execute(addForeignKeyQuery);
		}
	}

	private static double calculateTotalRowItemCount(DatabaseModel model) {
		double totalRowItemCount = 0;

		for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			totalRowItemCount += itemContainer.getItems().size();
		}

		return totalRowItemCount;
	}

	/**
 	 * Given all of the entities of the current entity type, form an SQL query that
	 *  constructs the corresponding entity table in the database, and then run
	 *  that query.
 	 */
	private static void fillTable(
			Connection connection,
			RowItemContainer<? extends RowItem<?>> itemContainer,
			ProgressMonitor progressMonitor,
			double previousUnitsWorked,
			double unitsWorkedForThisTable)
			throws SQLException {
		
		PreparedStatement insertStatement = createInsertStatement(itemContainer, connection);	

		try {
			if (!itemContainer.shouldInsertInBatches()) {
				fillEntireTable(
					insertStatement,
					itemContainer,
					progressMonitor,
					previousUnitsWorked,
					unitsWorkedForThisTable);
			} else {
				fillTableInBatches(
					insertStatement,
					itemContainer,
					progressMonitor,
					previousUnitsWorked,
					unitsWorkedForThisTable);
			}
		} finally {
			insertStatement.close();
		}
	}

	// TODO: New name?
	private static void fillEntireTable(
			PreparedStatement insertStatement,
			RowItemContainer<? extends RowItem<?>> itemContainer,
			ProgressMonitor progressMonitor,
			double previousUnitsWorked,
			double unitsWorkedForThisTable)
			throws SQLException {
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();
		Collection<? extends RowItem<?>> items = itemContainer.getItems();

		for (RowItem<?> item : items) {
			// PreparedStatement uses 1-based indexing.
			int fieldIndex = 1;
			Dictionary<String, Object> attributes = item.getAttributesForInsertion();

			for (DBField field : schema.getFields()) {
				Object value = attributes.get(field.name());

				if (value != null) {
					insertStatement.setObject(fieldIndex, value);
				} else {
					insertStatement.setNull(fieldIndex, field.type().getSQLType());
				}
		
				fieldIndex++;
			}

			insertStatement.addBatch();
		}

		long beforeBatch = System.currentTimeMillis();
		insertStatement.executeBatch();
		progressMonitor.worked(previousUnitsWorked + unitsWorkedForThisTable);
		long afterBatch = System.currentTimeMillis();
		long insertTime = afterBatch - beforeBatch;

		System.err.println(
			"Time to insert into " + itemContainer.getDatabaseTableName() + ": " + insertTime);
	}

	private static void fillTableInBatches(
			PreparedStatement insertStatement,
			RowItemContainer<? extends RowItem<?>> itemContainer,
			ProgressMonitor progressMonitor,
			double previousUnitsWorked,
			double unitsWorkedForThisTable)
			throws SQLException {
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();

		final int batchSize = itemContainer.getBatchSize();
		Collection<? extends RowItem<?>> items = itemContainer.getItems();
		Iterator<? extends RowItem<?>> iterator = items.iterator();
		final int totalItemCount = items.size();
		int itemsInsertedCount = 0;
		double workUnitsDone = 0;

		while (iterator.hasNext()) {
			for (int ii = 0; iterator.hasNext() && (ii < batchSize); ii++) {
				RowItem<?> item = iterator.next();
				int fieldIndex = 1;
				Dictionary<String, Object> attributes = item.getAttributesForInsertion();

				for (DBField field : schema.getFields()) {
					Object value = attributes.get(field.name());

					if (value != null) {
						insertStatement.setObject(fieldIndex, value);
					} else {
						insertStatement.setNull(fieldIndex, field.type().getSQLType());
					}

					fieldIndex++;
				}

				insertStatement.addBatch();
				itemsInsertedCount++;
			}

			workUnitsDone =
				((double) itemsInsertedCount / (double) totalItemCount) * unitsWorkedForThisTable;

			long beforeBatch = System.currentTimeMillis();
			insertStatement.executeBatch();
			progressMonitor.worked(previousUnitsWorked + workUnitsDone);
			long afterBatch = System.currentTimeMillis();
			long insertTime = afterBatch - beforeBatch;

			System.err.println(
				"Time to insert batch of size " + batchSize +
				" into " + itemContainer.getDatabaseTableName() +
				": " + insertTime +
				"; inserted " + itemsInsertedCount + " of " + totalItemCount + ".");
		}
	}

	private static PreparedStatement createInsertStatement(
			RowItemContainer<? extends RowItem<?>> itemContainer,
			Connection databaseConnection) throws SQLException {
		String placeholderContents = StringUtilities.multiplyWithSeparator(
			"?", ", ", itemContainer.getSchema().getFields().size());
		String placeholder = "(" + placeholderContents + ")";
	
		String insertQuery = String.format(
			"INSERT INTO \"%s\" VALUES %s", itemContainer.getDatabaseTableName(), placeholder);
		PreparedStatement insertStatement = databaseConnection.prepareStatement(insertQuery);

		return insertStatement;
	}


	private static String schemaToFieldsForCreateTableQueryString(
			Schema<? extends RowItem<?>> schema) {
		int fieldCount = schema.getFields().size();

		if (fieldCount == 0) {
			return "";
		}

		List<String> fieldsForSchema = new ArrayList<String>();

		for (DBField field : schema.getFields()) {
			String fieldForSchema = String.format(
				"\"%s\" %s",
				field.name(),
				field.type().getDerbyQueryStringRepresentation());
			fieldsForSchema.add(fieldForSchema);
		}

		return StringUtilities.implodeItems(fieldsForSchema, ", ");
	}

	private static String schemaToPrimaryKeysForCreateTableQueryString(
			Schema<? extends RowItem<?>> schema) {
		List<Schema.PrimaryKey> primaryKeys = schema.getPrimaryKeys();

		if (primaryKeys.size() == 0) {
			return "";
		}

		List<String> primaryKeyStrings = new ArrayList<String>();

		for (Schema.PrimaryKey primaryKey : primaryKeys) {
			primaryKeyStrings.add("\"" + primaryKey.getFieldName() + "\"");
		}

		String primaryKeysForQuery = String.format(
			", PRIMARY KEY (%s)", StringUtilities.implodeItems(primaryKeyStrings, ", "));

		return primaryKeysForQuery;
	}

}