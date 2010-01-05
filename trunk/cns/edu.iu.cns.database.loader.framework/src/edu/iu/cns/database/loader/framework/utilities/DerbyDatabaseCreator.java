package edu.iu.cns.database.loader.framework.utilities;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;

import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.database.loader.framework.Schema;

public class DerbyDatabaseCreator {
	public static final int MAX_VARCHAR_LENGTH = 32000;

	// TODO: Better exception message?
	/*public static final String EXCEPTION_MESSAGE =
		"A problem occurred when reading ISI data.\n" +
		"Try reinstalling the application or contacting the Help Desk.";*/

	/**
	 * dataType should be the human-readable display name of the type of originating data,
	 *  e.g. ISI or NSF.
	 */
	public static Database createFromModel(
			DatabaseService databaseProvider, DatabaseModel model, String dataType)
			throws DatabaseCreationException, SQLException {
		Database isiDatabase = databaseProvider.createNewDatabase();
		Connection databaseConnection = isiDatabase.getConnection();

		for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			createEmptyTable(databaseConnection, itemContainer);
			addForeignKeysToTable(databaseConnection, itemContainer);
			fillTable(databaseConnection, itemContainer);
		}

		databaseConnection.close();

		return isiDatabase;
	}

	public static void createEmptyTable(
			Connection connection, RowItemContainer<? extends RowItem<?>> itemContainer)
			throws SQLException {
		String tableName = itemContainer.getDatabaseTableName();
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();

		StringBuffer tableValuesForQuery = new StringBuffer(
			"\"" + Schema.PRIMARY_KEY + "\" " + getFieldTypeString(Schema.PRIMARY_KEY_CLASS));
		tableValuesForQuery.append(schemaToFieldsQueryString(schema));
		tableValuesForQuery.append(", PRIMARY KEY (\"" + Schema.PRIMARY_KEY + "\")");

		String createTableQuery =
			"CREATE TABLE " + tableName + "(" + tableValuesForQuery.toString() + ")";
		connection.createStatement().execute(createTableQuery);
	}

	public static void addForeignKeysToTable(
			Connection connection, RowItemContainer<? extends RowItem<?>> itemContainer)
			throws SQLException {
		String tableName = itemContainer.getDatabaseTableName();
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();
		Statement statement = connection.createStatement();

		// TODO: Alter table to have foreign keys.
		for (Schema.ForeignKey foreignKey : schema.getForeignKeys()) {
			String addForeignKeyQuery =
				"ALTER TABLE " +
				tableName +
				" CONSTRAINT \"" +
				foreignKey.getFieldName() +
				"\" FOREIGN KEY (\"" +
				Schema.PRIMARY_KEY +
				"\") REFERENCES " +
				foreignKey.getReferenceTo_TableName() +
				" (\"" +
				foreignKey.getFieldName() +
				"\")";
			System.err.println("foreign key: \"" + addForeignKeyQuery + "\"");
			statement.execute(addForeignKeyQuery);
		}
	}

	/**
 	 * Given all of the entities of the current entity type, form an SQL query that
	 *  constructs the corresponding entity table in the database, and then run
	 *  that query.
 	 */
	public static void fillTable(
			Connection connection, RowItemContainer<? extends RowItem<?>> itemContainer) {
		String tableName = itemContainer.getDatabaseTableName();
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();

		for (RowItem<?> item : itemContainer.getItems()) {
			Dictionary<String, Comparable<?>> attributes = item.getAttributes();

			for (Schema.Field field : schema.getFields()) {
				
			}
		}
	}

	public static String getFieldTypeString(Class<?> clazz) {
		if (clazz == Schema.PRIMARY_KEY_CLASS) {
			return "INT NOT NULL";
		} else if (clazz == Schema.FOREIGN_KEY_CLASS) {
			return "INT";
		} else if (clazz == Schema.TEXT_CLASS) {
			return "VARCHAR(" + MAX_VARCHAR_LENGTH + ")";
		} else if (clazz == Schema.INTEGER_CLASS) {
			return "INT";
		} else {
			// TODO: Error?  Just default to INT for now.
			return "INT";
		}
	}

	// TODO: New name.
	public static String schemaToFieldsQueryString(Schema<? extends RowItem<?>> schema) {
		StringBuffer fieldsQueryString = new StringBuffer();

		for (Schema.Field field : schema.getFields()) {
			fieldsQueryString.append(
				", \"" + field.getName() + "\" " + getFieldTypeString(field.getClazz()));
		}

		return fieldsQueryString.toString();
	}
}