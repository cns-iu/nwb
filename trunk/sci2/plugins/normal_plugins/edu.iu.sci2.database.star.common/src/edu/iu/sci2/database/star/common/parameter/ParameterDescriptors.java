package edu.iu.sci2.database.star.common.parameter;

import org.osgi.service.metatype.AttributeDefinition;

import edu.iu.cns.database.load.framework.DerbyFieldType;

public class ParameterDescriptors {
	public static class Type {
		public static String STRING_OPTION = DerbyFieldType.VARCHAR.getHumanReadableName();
		public static String INTEGER_OPTION = DerbyFieldType.INTEGER.getHumanReadableName();
		public static String DOUBLE_OPTION = DerbyFieldType.DOUBLE.getHumanReadableName();
		public static String[] OPTIONS = new String[] {
			STRING_OPTION,
			INTEGER_OPTION,
			DOUBLE_OPTION,
		};

		public static String id(String columnName) {
			return columnName + "_type";
		}

		public static String name(String columnName) {
			return "Type of " + columnName;
		}

		public static String description(String columnName) {
			return "The type of values that appear in " + columnName + ".";
		}

		public static int valueType() {
			return AttributeDefinition.STRING;
		}

		public static String[] optionLabels() {
			return OPTIONS;
		}

		public static String[] optionValues() {
			return OPTIONS;
		}
	}

	public static class SeparateEntity {
		public static String id(String columnName) {
			return columnName + "_" + "isSeparateEntity";
		}

		public static String name(String columnName) {
			return "Should " + columnName + " be a separate type of entity?";
		}

		public static String description(String columnName) {
			return
				"Should " + columnName + " be a separate type of entity?  " +
				"If so and if it is multi-valued, specify the value separator below.  " +
				"Otherwise, " + columnName + " will be included in the core table, and the " +
				"value separator below will be ignored.";
		}

		public static int valueType() {
			return AttributeDefinition.BOOLEAN;
		}

		public static String defaultValue() {
			return Boolean.FALSE.toString();
		}
	}

	public static class MergeIdentical {
		public static String id(String columnName) {
			return columnName + "_" + "shouldMergeIdentical";
		}

		public static String name(String columnName) {
			return "Should " + columnName + "'s values be merged if identical?";
		}

		public static String description(String columnName) {
			return
				"Should " + columnName + "'s values be merged if identical?  " +
				"This will be ignored if this is a core entity column."; 
		}

		public static int valueType() {
			return AttributeDefinition.BOOLEAN;
		}

		public static String defaultValue() {
			return Boolean.FALSE.toString();
		}
	}

	public static class Separator {
		public static String id(String columnName) {
			return columnName + "_" + "separator";
		}

		public static String name(String columnName) {
			return "Separator for " + columnName;
		}

		public static String description(String columnName) {
			return
				"If " + columnName + " is a separate type of entity, this is the separator that " +
				"will be used to split each entry into multiple values.  If you do not want " +
				columnName + " split into multiple values, leave this field blank.";
		}

		public static int valueType() {
			return AttributeDefinition.STRING;
		}

		public static String defaultValue() {
			return "";
		}
	}

	public static class CoreEntityName {
		public static final String CORE_ENTITY_NAME_ID = "coreEntityName";
		public static final String CORE_ENTITY_NAME_NAME = "Core Entity Name";
		public static final String CORE_ENTITY_NAME_DESCRIPTION =
			"The name of the core entity in this generic-CSV database.  " +
			"(The database table name will also be based on this.)";
		public static final int CORE_ENTITY_NAME_TYPE = AttributeDefinition.STRING;
		public static final String DEFAULT_CORE_ENTITY_NAME_VALUE = "CORE";
	}
}
