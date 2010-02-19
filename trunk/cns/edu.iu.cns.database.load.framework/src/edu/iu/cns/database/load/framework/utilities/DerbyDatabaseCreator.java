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

import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.DatabaseUtilities;
import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.Schema.Field;

public class DerbyDatabaseCreator {
	public static final int MAX_VARCHAR_LENGTH = 32000;
	public static final String NULL_VALUE = "null";

	public static final float PERCENTAGE_OF_PROGRESS_FOR_CREATING_TABLES = 0.04f;
	public static final float PERCENTAGE_OF_PROGRESS_FOR_INSERTING_VALUES = 0.85f;
	public static final float PERCENTAGE_OF_PROGRESS_FOR_ADDING_FOREIGN_KEYS = 0.01f;

	/**
	 * dataType should be the human-readable display name of the type of originating data,
	 *  e.g. ISI or NSF.
	 */
	public static Database createFromModel(
			DatabaseService databaseProvider,
			DatabaseModel model,
			String dataType,
			ProgressMonitor progressMonitor,
			int workUnitCount)
			throws AlgorithmCanceledException, DatabaseCreationException, SQLException {
		Database database = databaseProvider.createNewDatabase();
		Connection databaseConnection = database.getConnection();

		try {
			createEmptyTablesFromModel(model, databaseConnection, progressMonitor, workUnitCount);
			fillTablesFromModel(model, databaseConnection, progressMonitor);
			addForeignKeysToTablesFromModel(model, databaseConnection, progressMonitor);
		} finally {
			DatabaseUtilities.closeConnectionQuietly(databaseConnection);
		}

		return database;
	}

	public static void createEmptyTablesFromModel(
			DatabaseModel model,
			Connection databaseConnection,
			ProgressMonitor progressMonitor,
			int workUnitCount)
			throws AlgorithmCanceledException, SQLException {
		progressMonitor.describeWork("Creating empty database tables for ISI data.");
		Statement createTableStatement = databaseConnection.createStatement();

		try {
			for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
				if (progressMonitor.isCanceled()) {
					throw new AlgorithmCanceledException();
				}

				while (progressMonitor.isPaused()) { }

				createEmptyTable(databaseConnection, itemContainer, createTableStatement);

				// TODO: work done.
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			createTableStatement.close();
		}
	}

	public static void fillTablesFromModel(
			DatabaseModel model, Connection databaseConnection, ProgressMonitor progressMonitor)
			throws AlgorithmCanceledException, SQLException {
		progressMonitor.describeWork("Inserting ISI data into database tables.");

		for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			if (progressMonitor.isCanceled()) {
				throw new AlgorithmCanceledException();
			}

			while (progressMonitor.isPaused()) { }

			Collection<? extends RowItem<?>> items = itemContainer.getItems();

			if (items.size() == 0) {
				continue;
			}

			fillTable(databaseConnection, itemContainer);

			// TODO: work done.
		}
	}

	public static void addForeignKeysToTablesFromModel(
			DatabaseModel model, Connection databaseConnection, ProgressMonitor progressMonitor)
			throws AlgorithmCanceledException, SQLException {
		progressMonitor.describeWork("Adding foreign key constraints to the database tables.");
		Statement addForeignKeysStatement = databaseConnection.createStatement();

		for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			if (progressMonitor.isCanceled()) {
				throw new AlgorithmCanceledException();
			}

			while (progressMonitor.isPaused()) { }

			addForeignKeysToTable(databaseConnection, itemContainer, addForeignKeysStatement);

			// TODO: work done.
		}
	}

	public static void createEmptyTable(
			Connection connection,
			RowItemContainer<? extends RowItem<?>> itemContainer,
			Statement createTableStatement)
			throws SQLException {
		String tableName = itemContainer.getDatabaseTableName();
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();

		// TODO: Refactor this to not use StringBuffer (since it doesn't need to anymore)?
		StringBuffer fieldNamesForQuery = new StringBuffer();
		fieldNamesForQuery.append(schemaToFieldsForCreateTableQueryString(schema));
		fieldNamesForQuery.append(schemaToPrimaryKeysForCreateTableQueryString(schema));

		String createTableQuery =
			"CREATE TABLE " + tableName + "(" + fieldNamesForQuery.toString() + ")";
		createTableStatement.execute(createTableQuery);
	}

	public static void addForeignKeysToTable(
			Connection connection,
			RowItemContainer<? extends RowItem<?>> itemContainer,
			Statement addForeignKeysStatement)
			throws SQLException {
		String tableName = itemContainer.getDatabaseTableName();
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();

		for (Schema.ForeignKey foreignKey : schema.getForeignKeys()) {
			String addForeignKeyQuery =
				"ALTER TABLE " +
				tableName +
				" ADD CONSTRAINT \"" +
				foreignKey.getFieldName() + "_CONSTRAINT" +
				"\" FOREIGN KEY (\"" +
				foreignKey.getFieldName() +
				"\") REFERENCES " +
				foreignKey.getReferenceTo_TableName() +
				" (\"" +
				Schema.PRIMARY_KEY +
				"\")";
			addForeignKeysStatement.execute(addForeignKeyQuery);
		}
	}

	/**
 	 * Given all of the entities of the current entity type, form an SQL query that
	 *  constructs the corresponding entity table in the database, and then run
	 *  that query.
 	 */
	public static void fillTable(
			Connection connection,
			RowItemContainer<? extends RowItem<?>> itemContainer)
			throws SQLException {
		
		PreparedStatement insertStatement = createInsertStatement(itemContainer, connection);	

		try {
			if (!itemContainer.shouldInsertInBatches()) {
				fillEntireTable(insertStatement, itemContainer);
			} else {
				fillTableInBatches(insertStatement, itemContainer);
			}
		} finally {
			insertStatement.close();
		}
	}

	// TODO: New name?
	private static void fillEntireTable(
			PreparedStatement insertStatement,
			RowItemContainer<? extends RowItem<?>> itemContainer)
			throws SQLException {
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();
		Collection<? extends RowItem<?>> items = itemContainer.getItems();

		for (RowItem<?> item : items) {
			//(PreparedStatement uses 1-based indexing)
			int fieldIndex = 1;
			Dictionary<String, Object> attributes = item.getAttributesForInsertion();

			for (Field field : schema.getFields()) {
				Object value = attributes.get(field.getName());

				if (value != null) {
					insertStatement.setObject(fieldIndex, value);
				} else {
					insertStatement.setNull(fieldIndex, field.getType().getSQLType());
				}
		
				fieldIndex++;
			}

			insertStatement.addBatch();
		}

		long beforeBatch = System.currentTimeMillis();
		insertStatement.executeBatch();
		long afterBatch = System.currentTimeMillis();
		long insertTime = afterBatch - beforeBatch;

		System.err.println(
			"Time to insert into " + itemContainer.getDatabaseTableName() + ": " + insertTime);
	}

	private static void fillTableInBatches(
			PreparedStatement insertStatement,
			RowItemContainer<? extends RowItem<?>> itemContainer)
			throws SQLException {
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();

		final int batchSize = itemContainer.getBatchSize();
		Collection<? extends RowItem<?>> items = itemContainer.getItems();
		Iterator<? extends RowItem<?>> iterator = items.iterator();
		final int totalItemCount = items.size();
		int itemsInsertedCount = 0;

		while (iterator.hasNext()) {
			for (int ii = 0; iterator.hasNext() && (ii < batchSize); ii++, itemsInsertedCount++) {
				RowItem<?> item = iterator.next();
				int fieldIndex = 1;
				Dictionary<String, Object> attributes = item.getAttributesForInsertion();

				for (Field field : schema.getFields()) {
					Object value = attributes.get(field.getName());

					if (value != null) {
						insertStatement.setObject(fieldIndex, value);
					} else {
						insertStatement.setNull(fieldIndex, field.getType().getSQLType());
					}

					fieldIndex++;
				}

				insertStatement.addBatch();
			}

			long beforeBatch = System.currentTimeMillis();
			insertStatement.executeBatch();
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
		String placeholderContents =
			StringUtilities.multiplyWithSeparator("?", ", ",
					itemContainer.getSchema().getFields().size());
		String placeholder = "(" + placeholderContents + ")";
	
		String insertQuery = "INSERT INTO " + 
			itemContainer.getDatabaseTableName() + " VALUES " + placeholder;
		PreparedStatement insertStatement = databaseConnection.prepareStatement(insertQuery);
		return insertStatement;
	}


	// TODO: New name.
	public static String schemaToFieldsForCreateTableQueryString(
			Schema<? extends RowItem<?>> schema) {
		// TODO: Use PreparedStatement.
		List<Field> fields = schema.getFields();
		int fieldCount = fields.size();

		if (fieldCount == 0) {
			return "";
		}

		StringBuffer fieldsForCreateTableQueryString = new StringBuffer();

		for (Field field : schema.getFields()) {
			fieldsForCreateTableQueryString.append(
				"\"" + field.getName() + "\" " +
				field.getType().getDerbyQueryStringRepresentation() +
				", ");
		}

		// To remove the last ", ".
		fieldsForCreateTableQueryString.deleteCharAt(fieldsForCreateTableQueryString.length() - 1);
		fieldsForCreateTableQueryString.deleteCharAt(fieldsForCreateTableQueryString.length() - 1);

		return fieldsForCreateTableQueryString.toString();
	}

	public static String schemaToPrimaryKeysForCreateTableQueryString(
			Schema<? extends RowItem<?>> schema) {
		List<Schema.PrimaryKey> primaryKeys = schema.getPrimaryKeys();

		if (primaryKeys.size() == 0) {
			return "";
		}

		List<String> primaryKeyStrings = new ArrayList<String>();

		for (Schema.PrimaryKey primaryKey : primaryKeys) {
			primaryKeyStrings.add("\"" + primaryKey.getFieldName() + "\"");
		}

		return ", PRIMARY KEY (" + StringUtilities.implodeList(primaryKeyStrings, ", ") + ")";
	}

	public static String schemaToFieldsForInsertQueryString(Schema<? extends RowItem<?>> schema) {
		List<String> fieldNames = new ArrayList<String>();

		for (Schema.Field field : schema.getFields()) {
			fieldNames.add(field.getName());
		}

		return StringUtilities.implodeList(fieldNames, ", ");
	}

	public static String createAttributesStringAccordingToSchemaForInsertQuery(
			Schema<? extends RowItem<?>> schema, Dictionary<String, Comparable<?>> attributes) {
		List<String> attributeValues = new ArrayList<String>();

		for (Schema.Field field : schema.getFields()) {
			attributeValues.add(valueFormattingForField(attributes.get(field.getName()), field));
		}

		return "(" + StringUtilities.implodeList(attributeValues, ", ") + ")";
	}

	public static String valueFormattingForField(Object toString, Field field) {
		String value = StringUtilities.emptyStringIfNull(toString);
		DerbyFieldType type = field.getType();

		if (type == DerbyFieldType.PRIMARY_KEY) {
			return "" + IntegerParserWithDefault.parse(value);
		} else if (type == DerbyFieldType.FOREIGN_KEY) {
			if ("".equals(value)) {
				return NULL_VALUE;
			} else {
				return "" + IntegerParserWithDefault.parse(value);
			}
		} else if (type == DerbyFieldType.TEXT) {
			if (value == null) {
				return NULL_VALUE;
			} else {
				return "\'" + value + "\'";
			}
		} else if (type == DerbyFieldType.INTEGER) {
			if ("".equals(value)) {
				return NULL_VALUE;
			} else {
				return "" + IntegerParserWithDefault.parse(value);
			}
		} else {
			// TODO: Error?  Just default to INT for now.
			if ("".equals(value)) {
				return NULL_VALUE;
			} else {
				return "" + IntegerParserWithDefault.parse(value);
			}
		}
	}
}