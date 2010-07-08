package edu.iu.sci2.database.star.extract.network;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.cishell.utilities.ArrayListUtilities;
import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.swt.model.GUIModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import edu.iu.cns.database.load.framework.DerbyFieldType;

public class CoreTableDescriptor {
	public static final Collection<String> TYPES_OF_COLUMNS_FOR_AGGREGATES = Arrays.asList(
		DerbyFieldType.INTEGER.getDerbyQueryStringRepresentation());
	public static final Collection<String> OPTIONS_TO_SKIP_IN_TYPES_OF_COLUMNS_FOR_AGGREGATES =
		new HashSet<String>();
	public static final String CHOOSE_COLUMN_OPTION = "Choose Column:";
	public static final Collection<String> OPTIONS_TO_ADD_TO_TYPES_OF_COLUMNS_FOR_AGGREGATES =
		Arrays.asList(CHOOSE_COLUMN_OPTION);

	private Map<String, String> columnNamesToTypes;
	private Collection<String> columnNames;
	private Collection<String> columnNamesForAggregates;

	public CoreTableDescriptor(Map<String, String> columnNamesToTypes) {
		this.columnNamesToTypes = columnNamesToTypes;
		this.columnNames = this.columnNamesToTypes.keySet();
		this.columnNamesForAggregates = MapUtilities.getValidKeysOfTypesInMap(
			columnNamesToTypes,
			TYPES_OF_COLUMNS_FOR_AGGREGATES,
			OPTIONS_TO_SKIP_IN_TYPES_OF_COLUMNS_FOR_AGGREGATES);
		this.columnNamesForAggregates = ArrayListUtilities.unionCollections(
			OPTIONS_TO_ADD_TO_TYPES_OF_COLUMNS_FOR_AGGREGATES,
			this.columnNamesForAggregates,
			null);
	}

	public Map<String, String> getColumnNamesToTypes() {
		return this.columnNamesToTypes;
	}

	public Combo createLeafSelectionInputField(
			GUIModel model, String name, Composite parent, int style, boolean isForAggregate) {
		if (isForAggregate) {
			return model.addDropDown(
				name,
				0,
				this.columnNamesForAggregates,
				MapUtilities.mirror(this.columnNamesForAggregates),
				parent,
				style | SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		} else {
			return model.addDropDown(
				name,
				0,
				this.columnNames,
				MapUtilities.mirror(this.columnNames),
				parent,
				style | SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		}
	}
}