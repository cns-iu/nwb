package edu.iu.cns.database.loader.framework.utilities;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.Schema.Field;

public class DerbyDatabaseCreator {
	public static final int MAX_VARCHAR_LENGTH = 32000;
	public static final String NULL_VALUE = "null";

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
		}

		for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			fillTable(databaseConnection, itemContainer);
		}

		/*for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			addForeignKeysToTable(databaseConnection, itemContainer);
		}*/

		databaseConnection.close();

		return isiDatabase;
	}

	public static void createEmptyTable(
			Connection connection, RowItemContainer<? extends RowItem<?>> itemContainer)
			throws SQLException {
		String tableName = itemContainer.getDatabaseTableName();
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();

		StringBuffer fieldNamesForQuery = new StringBuffer();
		fieldNamesForQuery.append(schemaToFieldsForCreateTableQueryString(schema));
		fieldNamesForQuery.append(schemaToPrimaryKeysForCreateTableQueryString(schema));
		//fieldNamesForQuery.append(", PRIMARY KEY (\"" + Schema.PRIMARY_KEY + "\")");

		String createTableQuery =
			"CREATE TABLE " + tableName + "(" + fieldNamesForQuery.toString() + ")";
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
				" ADD CONSTRAINT \"" +
				foreignKey.getFieldName() +
				"\" FOREIGN KEY (\"" +
				Schema.PRIMARY_KEY +
				"\") REFERENCES " +
				foreignKey.getReferenceTo_TableName() +
				" (\"" +
				Schema.PRIMARY_KEY +
				//foreignKey.getFieldName() +
				"\")";
			//System.err.println("foreign key: \"" + addForeignKeyQuery + "\"");
			statement.execute(addForeignKeyQuery);
		}
	}

	/**
 	 * Given all of the entities of the current entity type, form an SQL query that
	 *  constructs the corresponding entity table in the database, and then run
	 *  that query.
 	 */
	public static void fillTable(
			Connection connection, RowItemContainer<? extends RowItem<?>> itemContainer)
			throws SQLException {
		String tableName = itemContainer.getDatabaseTableName();
		List<? extends RowItem<?>> items = itemContainer.getItems();

		if (items.size() == 0) {
			return;
		}

		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();
		List<String> itemValuesStrings = new ArrayList<String>();

		for (RowItem<?> item : items) {
			Dictionary<String, Comparable<?>> attributes = item.getAttributes();
			itemValuesStrings.add(createAttributesStringAccordingToSchemaForInsertQuery(
				schema, attributes));
		}
		/*for (int ii = 0; ii < 1; ii++) {
			RowItem<?> item = items.get(ii);
			Dictionary<String, Comparable<?>> attributes = item.getAttributes();
			itemValuesStrings.add(createAttributesStringAccordingToSchemaForInsertQuery(
				schema, attributes));
		}*/

		String fieldsString = schemaToFieldsForInsertQueryString(schema);
		String insertQuery =
			"INSERT INTO " +
			tableName +
			" (" +
			fieldsString +
			") VALUES " +
			StringUtilities.implodeList(itemValuesStrings, ", ");
		System.err.println("\n\"" + insertQuery + "\"");
		connection.createStatement().execute(insertQuery);
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
	public static String schemaToFieldsForCreateTableQueryString(
			Schema<? extends RowItem<?>> schema) {
		// TODO: Use PreparedStatement.
		List<Field> fields = schema.getFields();
		int fieldCount = fields.size();

		if (fieldCount == 0) {
			return "";
		}

		StringBuffer fieldsForCreateTableQueryString = new StringBuffer();

		for (Schema.Field field : schema.getFields()) {
			fieldsForCreateTableQueryString.append(
				"\"" + field.getName() + "\" " + getFieldTypeString(field.getClazz()) + ", ");
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
		Class<?> clazz = field.getClazz();

		if (clazz == Schema.PRIMARY_KEY_CLASS) {
			return "" + IntegerParserWithDefault.parse(value);
		} else if (clazz == Schema.FOREIGN_KEY_CLASS) {
			if ("".equals(value)) {
				return NULL_VALUE;
			} else {
				return "" + IntegerParserWithDefault.parse(value);
			}
		} else if (clazz == Schema.TEXT_CLASS) {
			if (value == null) {
				return NULL_VALUE;
			} else {
				return "\'" + value + "\'";
			}
		} else if (clazz == Schema.INTEGER_CLASS) {
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