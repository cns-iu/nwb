package edu.iu.sci2.database.star.extract.network;

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
	private Map<String, String> columnNamesToTypes;
	private Collection<String> columnNames;
	private Collection<String> columnNamesForAggregates;

	public CoreTableDescriptor(StarDatabaseMetadata metadata) {
		this(
			metadata.getCoreEntityHumanReadableName(),
			metadata.getCoreEntityTableName(),
			constructColumnNamesToTypes(metadata));
	}

	public CoreTableDescriptor(
			String coreEntityHumanReadableName,
			String coreEntityTableName,
			Map<String, String> columnNamesToTypes) {
		this.coreEntityHumanReadableName = coreEntityHumanReadableName;
		this.coreEntityTableName = coreEntityTableName;
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

	public String getCoreEntityHumanReadableName() {
		return this.coreEntityHumanReadableName;
	}

	public String getCoreEntityTableName() {
		return this.coreEntityTableName;
	}

	public Map<String, String> getColumnNamesToTypes() {
		return this.columnNamesToTypes;
	}

	public Collection<String> getColumnNames() {
		return this.columnNames;
	}

	private static Map<String, String> constructColumnNamesToTypes(StarDatabaseMetadata metadata) {
		Map<String, String> namesToTypes = new HashMap<String, String>();

		for (ColumnDescriptor columnDescriptor : metadata.getColumnDescriptors().values()) {
			if (columnDescriptor.isCoreColumn()) {
				String name = columnDescriptor.getName();
				String type = columnDescriptor.getType().getHumanReadableName();
				namesToTypes.put(name, type);
			}
		}

		return namesToTypes;
	}
}