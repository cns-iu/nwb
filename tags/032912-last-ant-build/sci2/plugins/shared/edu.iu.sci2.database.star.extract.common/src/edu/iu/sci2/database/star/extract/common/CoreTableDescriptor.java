package edu.iu.sci2.database.star.extract.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.cishell.utilities.ArrayListUtilities;
import org.cishell.utilities.MapUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

/**
 * coreEntityHumanReadableName -- the display label of the core table.
 * coreEntityTableName -- the database name of the core table.
 * columnNamesByLabels -- the display labels of the core table columns mapped to the
 * database columns names.
 * columnNames -- the core table database column names.
 * columnNamesForAggregates -- the list of core table column names to use for the aggregate field
 * widgets.
 */
public class CoreTableDescriptor {
	public static final Collection<String> TYPES_OF_COLUMNS_FOR_AGGREGATES = Arrays.asList(
		DerbyFieldType.INTEGER.getDerbyQueryStringRepresentation());
	public static final Collection<String> OPTIONS_TO_SKIP_IN_TYPES_OF_COLUMNS_FOR_AGGREGATES =
		new HashSet<String>();
	public static final String CHOOSE_COLUMN_OPTION = "Choose Column:";
	public static final Collection<String> OPTIONS_TO_ADD_TO_TYPES_OF_COLUMNS_FOR_AGGREGATES =
		Arrays.asList(CHOOSE_COLUMN_OPTION);

	private String coreEntityHumanReadableName;
	private String coreEntityTableName;
	private Map<String, String> columnNamesByLabels;
	private Collection<String> columnNames;
	private Collection<String> columnNamesForAggregates;

	public CoreTableDescriptor(
			String coreEntityHumanReadableName,
			String coreEntityTableName,
			Map<String, ColumnDescriptor> coreColumnDescriptorsByName) {
		this.coreEntityHumanReadableName = coreEntityHumanReadableName;
		this.coreEntityTableName = coreEntityTableName;
		this.columnNamesByLabels = constructColumnNamesByLabels(coreColumnDescriptorsByName);
		this.columnNames = this.columnNamesByLabels.keySet();
		this.columnNamesForAggregates = MapUtilities.getValidKeysOfTypesInMap(
			this.columnNamesByLabels,
			TYPES_OF_COLUMNS_FOR_AGGREGATES,
			OPTIONS_TO_SKIP_IN_TYPES_OF_COLUMNS_FOR_AGGREGATES);
		this.columnNamesForAggregates = ArrayListUtilities.unionCollections(
			OPTIONS_TO_ADD_TO_TYPES_OF_COLUMNS_FOR_AGGREGATES,
			this.columnNamesForAggregates,
			null);
	}

	public String getCoreEntityHumanReadableName() {
		return this.coreEntityHumanReadableName;
	}

	public String getCoreEntityTableName() {
		return this.coreEntityTableName;
	}

	public Map<String, String> getColumnNamesByLabels() {
		return this.columnNamesByLabels;
	}

	public Collection<String> getColumnNames() {
		return this.columnNames;
	}

	private static Map<String, String> constructColumnNamesByLabels(
			Map<String, ColumnDescriptor> coreColumnDescriptorsByName) {
		Map<String, String> namesToTypes = new HashMap<String, String>();

		for (ColumnDescriptor columnDescriptor : coreColumnDescriptorsByName.values()) {
			String label = columnDescriptor.getName();
			String name = columnDescriptor.getNameForDatabase();
			namesToTypes.put(label, name);
		}

		return namesToTypes;
	}

//	private static Map<String, String> constructColumnNamesByLabels(
//			StarDatabaseMetadata metadata) {
//		Map<String, String> namesToTypes = new HashMap<String, String>();
//
//		for (ColumnDescriptor columnDescriptor :
//				metadata.getColumnDescriptorsByHumanReadableName().values()) {
//			if (columnDescriptor.isCoreColumn()) {
//				String label = columnDescriptor.getName();
//				String name = columnDescriptor.getNameForDatabase();
//				namesToTypes.put(label, name);
//			}
//		}
//
//		return namesToTypes;
//	}
}