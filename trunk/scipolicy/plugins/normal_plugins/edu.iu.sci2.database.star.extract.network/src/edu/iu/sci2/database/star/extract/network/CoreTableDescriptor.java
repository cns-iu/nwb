package edu.iu.sci2.database.star.extract.network;

import java.util.Map;

import org.cishell.utilities.MapUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import edu.iu.cns.database.load.framework.DerbyFieldType;

public class CoreTableDescriptor {
	public static final String[] TYPES_OF_COLUMNS_FOR_AGGREGATES = new String[] {
		DerbyFieldType.INTEGER.getDerbyQueryStringRepresentation(),
	};
	public static final String[] OPTIONS_TO_SKIP_IN_TYPES_OF_COLUMNS_FOR_AGGREGATES =
		new String[0];
	public static final String CHOOSE_COLUMN_OPTION = "Choose Column:";
	public static final String[] OPTIONS_TO_ADD_TO_TYPES_OF_COLUMNS_FOR_AGGREGATES = new String[] {
		CHOOSE_COLUMN_OPTION
	};

	private Map<String, String> columnNamesToTypes;
	private String[] columnNames;
	private String[] columnNamesForAggregates;

	public CoreTableDescriptor(Map<String, String> columnNamesToTypes) {
		this.columnNamesToTypes = columnNamesToTypes;
		this.columnNames = this.columnNamesToTypes.keySet().toArray(new String[0]);
		this.columnNamesForAggregates = MapUtilities.getValidKeysOfTypesInMap(
			columnNamesToTypes,
			TYPES_OF_COLUMNS_FOR_AGGREGATES,
			OPTIONS_TO_SKIP_IN_TYPES_OF_COLUMNS_FOR_AGGREGATES,
			OPTIONS_TO_ADD_TO_TYPES_OF_COLUMNS_FOR_AGGREGATES);
	}

	public Map<String, String> getColumnNamesToTypes() {
		return this.columnNamesToTypes;
	}

	public Combo createLeafSelectionInputField(
			Composite parent, int style, boolean isForAggregate) {
		Combo leafSelectionInputField =
			new Combo(parent, style | SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);

		if (isForAggregate) {
			leafSelectionInputField.setItems(this.columnNamesForAggregates);
		} else {
			leafSelectionInputField.setItems(this.columnNames);
		}

		leafSelectionInputField.select(0);

		return leafSelectionInputField;
	}
}