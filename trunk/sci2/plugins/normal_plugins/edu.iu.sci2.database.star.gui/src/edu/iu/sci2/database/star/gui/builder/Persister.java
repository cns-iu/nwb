package edu.iu.sci2.database.star.gui.builder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.cishell.utilities.StringUtilities;
import org.cishell.utility.swt.FileSaveAs;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import edu.iu.sci2.database.star.common.StarDatabaseCSVDataValidationRules;

// TODO: Code Review this.
public class Persister {
	public static final String CORE_ENTITY_NAME_PROPERTY = "coreEntityName";
	public static final String COLUMN_INDEX_PROPERTY = "columnIndex";
	public static final String NAME_PROPERTY = "name";
	public static final String NAME_FOR_DATABASE_PROPERTY = "nameForDatabase";
	public static final String SEPARATOR_PROPERTY = "separator";
	public static final String TYPE_PROPERTY = "type";
	public static final String IS_CORE_COLUMN_PROPERTY = "isCoreColumn";
	public static final String IS_MULTI_VALUED_PROPERTY = "isMultiValued";
	public static final String SHOULD_MERGE_IDENTICAL_VALUES_PROPERTY =
		"shouldMergeIdenticalValues";

	public static final String PROPERTIES_SEPARATOR = ",";
	public static final Collection<String> SKIP_TOKENS_WHEN_LOADING =
		Arrays.asList(PROPERTIES_SEPARATOR);

	// TODO: Figure out a better extension than GCL?  *Generic*-*C*SV *L*oading
	public static final String[] FILE_EXTENSIONS = new String[] { "*.gcl" };

	public static boolean saveSession(
			final Shell shell,
			final CoreEntityNameWidget coreEntityNameWidget,
			final ColumnListWidget columnListWidget) {
		try {
			String coreEntityName = coreEntityNameWidget.getCoreEntityName();
			List<String> columnIndices = new ArrayList<String>();
			List<String> names = new ArrayList<String>();
			List<String> namesForDatabase = new ArrayList<String>();
			List<String> separators = new ArrayList<String>();
			List<String> types = new ArrayList<String>();
			List<String> isCoreColumns = new ArrayList<String>();
			List<String> isMultiValueds = new ArrayList<String>();
			List<String> shouldMergeIdenticalValues = new ArrayList<String>();
			int index = 0;

			for (ColumnWidget columnWidget : columnListWidget.getColumnWidgets()) {
				ColumnHeaderWidget headerWidget = columnWidget.getHeader();
				ColumnPropertiesWidget propertiesWidget = columnWidget.getProperties();
				String name = headerWidget.getColumnName();
				String databaseName = StarDatabaseCSVDataValidationRules.normalizeName(name);
				// TODO: Handle this error better?
				String type = headerWidget.getType();
				boolean isCoreColumn = propertiesWidget.getIsCoreColumn().isCoreColumn();
				NonCoreColumnPropertiesWidget isNotCoreColumnPropertiesWidget =
				propertiesWidget.getNonCoreColumnProperties();
				boolean mergeIdenticalValues = isNotCoreColumnPropertiesWidget.
					getMergeIdenticalValuesInputField().isSelected();
				boolean isMultiValued =
					isNotCoreColumnPropertiesWidget.getMultiValuedFieldInputField().isSelected();
				String separator =
					isNotCoreColumnPropertiesWidget.getSeparatorInputField().getText();

				columnIndices.add(String.format("\"%d\"", index));
				names.add(String.format("\"%s\"", name));
				namesForDatabase.add(String.format("\"%s\"", databaseName));
				separators.add(String.format("\"%s\"", separator));
				types.add(String.format("\"%s\"", type));
				isCoreColumns.add(
					String.format("\"%s\"", Boolean.toString(isCoreColumn)));
				isMultiValueds.add(
					String.format("\"%s\"", Boolean.toString(isMultiValued)));
				shouldMergeIdenticalValues.add(
					String.format("\"%s\"", Boolean.toString(mergeIdenticalValues)));

				index++;
			}

			Properties properties = new Properties();
			properties.setProperty(CORE_ENTITY_NAME_PROPERTY, coreEntityName);
			properties.setProperty(
				COLUMN_INDEX_PROPERTY,
				StringUtilities.implodeItems(columnIndices, PROPERTIES_SEPARATOR));
			properties.setProperty(
				NAME_PROPERTY, StringUtilities.implodeItems(names, PROPERTIES_SEPARATOR));
			properties.setProperty(
				NAME_FOR_DATABASE_PROPERTY,
				StringUtilities.implodeItems(namesForDatabase, PROPERTIES_SEPARATOR));
			properties.setProperty(
				SEPARATOR_PROPERTY, StringUtilities.implodeItems(separators, PROPERTIES_SEPARATOR));
			properties.setProperty(
				TYPE_PROPERTY, StringUtilities.implodeItems(types, PROPERTIES_SEPARATOR));
			properties.setProperty(
				IS_CORE_COLUMN_PROPERTY,
				StringUtilities.implodeItems(isCoreColumns, PROPERTIES_SEPARATOR));
			properties.setProperty(
				IS_MULTI_VALUED_PROPERTY,
				StringUtilities.implodeItems(isMultiValueds, PROPERTIES_SEPARATOR));
			properties.setProperty(
				SHOULD_MERGE_IDENTICAL_VALUES_PROPERTY,
				StringUtilities.implodeItems(shouldMergeIdenticalValues, PROPERTIES_SEPARATOR));

			FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
			saveDialog.setText("Save Column Attributes for Generic-CSV Database Loading");
			saveDialog.setFilterExtensions(FILE_EXTENSIONS);
			String selectedFilePath = FileSaveAs.saveFileAs(saveDialog);

			if (!StringUtilities.isNull_Empty_OrWhitespace(selectedFilePath)) {
				properties.store(new FileOutputStream(selectedFilePath), null);

				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean loadSession(
			final Shell shell,
			final CoreEntityNameWidget coreEntityNameWidget,
			final ColumnListWidget columnListWidget) {
		FileDialog saveDialog = new FileDialog(shell, SWT.OPEN);
		saveDialog.setText("Restore Column Attributes for Generic-CSV Database Loading");
		saveDialog.setFilterExtensions(FILE_EXTENSIONS);
		String selectedFilePath = saveDialog.open();

		if (!StringUtilities.isNull_Empty_OrWhitespace(selectedFilePath)) {
			try {
				Properties properties = new Properties();
				properties.load(new FileInputStream(selectedFilePath));
				String coreEntityName = StringUtilities.stripSurroundingQuotes(
					properties.getProperty(CORE_ENTITY_NAME_PROPERTY));
				List<String> separators = tokenize(
					properties.getProperty(SEPARATOR_PROPERTY), SKIP_TOKENS_WHEN_LOADING);
				List<String> types = tokenize(
					properties.getProperty(TYPE_PROPERTY), SKIP_TOKENS_WHEN_LOADING);
				List<String> isCoreColumns = tokenize(
					properties.getProperty(IS_CORE_COLUMN_PROPERTY), SKIP_TOKENS_WHEN_LOADING);
				List<String> isMultiValueds = tokenize(
					properties.getProperty(IS_MULTI_VALUED_PROPERTY), SKIP_TOKENS_WHEN_LOADING);
				List<String> shouldMergeIdenticalValuess = tokenize(properties.getProperty(
					SHOULD_MERGE_IDENTICAL_VALUES_PROPERTY), SKIP_TOKENS_WHEN_LOADING);

				coreEntityNameWidget.setCoreEntityName(coreEntityName);

				int index = 0;

				for (ColumnWidget columnWidget : columnListWidget.getColumnWidgets()) {
					boolean isCoreColumn = Boolean.parseBoolean(isCoreColumns.get(index));
					String separator = separators.get(index);
					Boolean isMultiValued = Boolean.parseBoolean(isMultiValueds.get(index));
					Boolean shouldMergeIdenticalValues =
						Boolean.parseBoolean(shouldMergeIdenticalValuess.get(index));
					String type = types.get(index);

					ColumnHeaderWidget headerWidget = columnWidget.getHeader();
					ColumnPropertiesWidget propertiesWidget = columnWidget.getProperties();
					IsCoreColumnWidget isCoreColumnWidget = propertiesWidget.getIsCoreColumn();
					NonCoreColumnPropertiesWidget nonCoreColumnProperties =
						propertiesWidget.getNonCoreColumnProperties();

					isCoreColumnWidget.setIsCoreColumn(isCoreColumn);
					nonCoreColumnProperties.setSeparator(separator);
					nonCoreColumnProperties.setIsMultiValued(isMultiValued);
					nonCoreColumnProperties.setMergeIdenticalValues(shouldMergeIdenticalValues);
					headerWidget.setType(type);

					if (!GUIBuilder.GRAY_OUT_NON_CORE_COLUMN_CONTROLS) {
						nonCoreColumnProperties.setExpanded(!isCoreColumn);
					} else {
						nonCoreColumnProperties.setEnabled(!isCoreColumn);
					}

					index++;
				}

				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return true;
		}

		return false;
	}

	public static List<String> tokenize(String original, Collection<String> skip) {
		List<String> tokens = new ArrayList<String>();
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(original));

		try {
			while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
				if ((tokenizer.sval != null) &&(tokenizer.ttype == StreamTokenizer.TT_WORD)) {
					if (!skip.contains(tokenizer.sval)) {
						tokens.add(tokenizer.sval);
					}
				} else if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
					String token = "" + tokenizer.nval;

					if (!skip.contains(token)) {
						tokens.add(token);
					}
				} else if ((tokenizer.sval != null) &&
						(tokenizer.ttype != StreamTokenizer.TT_EOL)) {
					tokens.add(tokenizer.sval);
				}
			}
		} catch (IOException e) {}

		return tokens;
	}
}