package edu.iu.sci2.database.star.extract.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.cishell.utilities.ArrayListUtilities;
import org.cishell.utilities.MapUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

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

	public CoreTableDescriptor(StarDatabaseMetadata metadata) {
		this(
			metadata.getCoreEntityHumanReadableName(),
			metadata.getCoreEntityTableName(),
			constructColumnNamesByLabels(metadata));
	}

	public CoreTableDescriptor(
			String coreEntityHumanReadableName,
			String coreEntityTableName,
			Map<String, String> columnNamesByLabels) {
		this.coreEntityHumanReadableName = coreEntityHumanReadableName;
		this.coreEntityTableName = coreEntityTableName;
		this.columnNamesByLabels = columnNamesByLabels;
		this.columnNames = this.columnNamesByLabels.keySet();
		this.columnNamesForAggregates = MapUtilities.getValidKeysOfTypesInMap(
			columnNamesByLabels,
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

//	public Map<String, String> getColumnNamesByLabels() {
//	}

	private static Map<String, String> constructColumnNamesByLabels(StarDatabaseMetadata metadata) {
		Map<String, String> namesToTypes = new HashMap<String, String>();

		for (ColumnDescriptor columnDescriptor : metadata.getColumnDescriptorsByHumanReadableName().values()) {
			if (columnDescriptor.isCoreColumn()) {
				String label = columnDescriptor.getName();
				String name = columnDescriptor.getNameForDatabase();
				namesToTypes.put(label, name);
			}
		}

		return namesToTypes;
	}
}