package edu.iu.cns.database.loader.framework.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.Schema.Field;

public class DerbyDatabaseCreator {
	public static final int MAX_VARCHAR_LENGTH = 32000;
	public static final String NULL_VALUE = "null";

	/**
	 * dataType should be the human-readable display name of the type of originating data,
	 *  e.g. ISI or NSF.
	 */
	public static Database createFromModel(
			DatabaseService databaseProvider, DatabaseModel model, String dataType)
			throws DatabaseCreationException, SQLException {
		Database database = databaseProvider.createNewDatabase();
		Connection databaseConnection = database.getConnection();

		for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			createEmptyTable(databaseConnection, itemContainer);
		}

		for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			fillTable(databaseConnection, itemContainer);
		}

		for (RowItemContainer<? extends RowItem<?>> itemContainer : model.getRowItemLists()) {
			addForeignKeysToTable(databaseConnection, itemContainer);
		}

		databaseConnection.close();

		return database;
	}

	public static void createEmptyTable(
			Connection connection, RowItemContainer<? extends RowItem<?>> itemContainer)
			throws SQLException {
		String tableName = itemContainer.getDatabaseTableName();
		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();

		// TODO: Refactor this to not use StringBuffer (since it doesn't need to anymore)?
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
				foreignKey.getFieldName() + "_CONSTRAINT" +
				"\" FOREIGN KEY (\"" +
				foreignKey.getFieldName() +
				"\") REFERENCES " +
				foreignKey.getReferenceTo_TableName() +
				" (\"" +
				Schema.PRIMARY_KEY +
				
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
			Connection connection, RowItemContainer<? extends RowItem<?>> itemContainer)
			throws SQLException {
		String tableName = itemContainer.getDatabaseTableName();
		Collection<? extends RowItem<?>> items = itemContainer.getItems();

		if (items.size() == 0) {
			return;
		}

		Schema<? extends RowItem<?>> schema = itemContainer.getSchema();
		
		String singleRowPlaceholderContents =
			StringUtilities.multiplyWithSeparator("?", ", ", schema.getFields().size());
		String singleRowPlaceholders = "(" + singleRowPlaceholderContents + ")";
		String allPlaceholders = StringUtilities.multiplyWithSeparator(
			singleRowPlaceholders, ", ", items.size());
		
		String insertQuery =
			"INSERT INTO " +
			tableName +
			" VALUES " + allPlaceholders;
		
		PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
		
		int fieldIndex = 1; //(PreparedStatement uses 1-based indexing)
		for (RowItem<?> item : items) {
			Dictionary<String, Comparable<?>> attributes = item.getAttributes();
			for (Field field : schema.getFields()) {
			
				Object value = attributes.get(field.getName());

				if (value != null) {
					insertStatement.setObject(fieldIndex, value);
				} else {
					insertStatement.setNull(fieldIndex, field.getType().getSQLType());
				}

				fieldIndex++;
			}
		}
		insertStatement.executeUpdate();
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