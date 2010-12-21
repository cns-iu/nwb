package edu.iu.sci2.database.star.extract.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class StarDatabaseDescriptor {
	private StarDatabaseMetadata metadata;
	private CoreTableDescriptor coreTableDescriptor;
	private Map<String, ColumnDescriptor> allColumnDescriptorsByName =
		new HashMap<String, ColumnDescriptor>();
	private Map<String, ColumnDescriptor> allColumnDescriptorsBySQLName =
		new HashMap<String, ColumnDescriptor>();
	private Map<String, ColumnDescriptor> coreColumnDescriptorsByName;
//	private Map<String, ColumnDescriptor> coreColumnDescriptorsBySQLName;
	private Map<String, ColumnDescriptor> leafColumnDescriptorsByName;
	private Map<String, ColumnDescriptor> leafColumnDescriptorsBySQLName;

	public StarDatabaseDescriptor(StarDatabaseMetadata metadata) {
		this.metadata = metadata;

		this.allColumnDescriptorsByName.putAll(metadata.getColumnDescriptorsByHumanReadableName());
		this.allColumnDescriptorsBySQLName.putAll(metadata.getColumnDescriptorsByDatabaseName());

		this.coreColumnDescriptorsByName =
			constructCoreColumnDescriptorsByName(this.allColumnDescriptorsByName);

		this.leafColumnDescriptorsByName =
			constructLeafColumnDescriptorsByName(this.allColumnDescriptorsByName);
		this.leafColumnDescriptorsBySQLName =
			constructLeafColumnDescriptorsBySQLName(this.allColumnDescriptorsBySQLName);

		if (leafColumnDescriptorsByName.size() == 0) {
			// TODO: Hack!
//			ColumnDescriptor corePrimaryKeyColumnDescriptor = new ColumnDescriptor(
//				metadata.getColumnDescriptorsByHumanReadableName().size(),
//				metadata.getCoreEntityHumanReadableName() + " Primary Key",
//				"PK",
//				DerbyFieldType.INTEGER,
//				true,
//				false,
//				false,
//				"");
//			this.allColumnDescriptorsByName.put(
//				corePrimaryKeyColumnDescriptor.getName(),
//				corePrimaryKeyColumnDescriptor);
//			this.allColumnDescriptorsBySQLName.put(
//				corePrimaryKeyColumnDescriptor.getNameForDatabase(),
//				corePrimaryKeyColumnDescriptor);
		}

		this.coreTableDescriptor = new CoreTableDescriptor(
			this.metadata.getCoreEntityHumanReadableName(),
			this.metadata.getCoreEntityTableName(),
			this.coreColumnDescriptorsByName);
	}

	public StarDatabaseMetadata getMetadata() {
		return this.metadata;
	}

	public CoreTableDescriptor getCoreTableDescriptor() {
		return this.coreTableDescriptor;
	}

	public Map<String, ColumnDescriptor> getAllColumnDescriptorsByName() {
		return this.allColumnDescriptorsByName;
	}

	public Map<String, ColumnDescriptor> getAllColumnDescriptorsBySQLName() {
		return this.allColumnDescriptorsBySQLName;
	}

	public Map<String, ColumnDescriptor> getLeafColumnDescriptorsByName() {
		return this.leafColumnDescriptorsByName;
	}

	public Map<String, ColumnDescriptor> getLeafColumnDescriptorsBySQLName() {
		return this.leafColumnDescriptorsBySQLName;
	}

	public List<String> getLeafTableNames() {
		List<String> leafTableNames = new ArrayList<String>();
		leafTableNames.addAll(this.leafColumnDescriptorsByName.keySet());

		return leafTableNames;
	}

	public Map<String, String> createTableNameOptionsWithoutCore() {
		Map<String, String> options = new HashMap<String, String>();

		for (String key : this.leafColumnDescriptorsByName.keySet()) {
			options.put(key, this.leafColumnDescriptorsByName.get(key).getNameForDatabase());
		}

		return options;
	}

	public Map<String, String> createTableNameOptionsWithCore() {
		Map<String, String> options = createTableNameOptionsWithoutCore();
		options.put(
			getCoreTableDescriptor().getCoreEntityHumanReadableName(),
			getCoreTableDescriptor().getCoreEntityTableName());

		return options;
	}

	private static Map<String, ColumnDescriptor> constructCoreColumnDescriptorsByName(
			Map<String, ColumnDescriptor> allColumnDescriptorsByName) {
		Map<String, ColumnDescriptor> leafTableDescriptorsByName =
			new HashMap<String, ColumnDescriptor>();

		for (ColumnDescriptor columnDescriptor : allColumnDescriptorsByName.values()) {
			if (columnDescriptor.isCoreColumn()) {
				leafTableDescriptorsByName.put(columnDescriptor.getName(), columnDescriptor);
			}
		}

		return leafTableDescriptorsByName;
	}

	private static Map<String, ColumnDescriptor> constructLeafColumnDescriptorsByName(
			Map<String, ColumnDescriptor> allColumnDescriptorsByName) {
		Map<String, ColumnDescriptor> leafTableDescriptorsByName =
			new HashMap<String, ColumnDescriptor>();

		for (ColumnDescriptor columnDescriptor : allColumnDescriptorsByName.values()) {
			if (!columnDescriptor.isCoreColumn()) {
				leafTableDescriptorsByName.put(columnDescriptor.getName(), columnDescriptor);
			}
		}

		return leafTableDescriptorsByName;
	}

	private static Map<String, ColumnDescriptor> constructLeafColumnDescriptorsBySQLName(
			Map<String, ColumnDescriptor> allColumnDescriptorsBySQLName) {
		Map<String, ColumnDescriptor> leafTableDescriptorsBySQLName =
			new HashMap<String, ColumnDescriptor>();

		for (ColumnDescriptor columnDescriptor : allColumnDescriptorsBySQLName.values()) {
			if (!columnDescriptor.isCoreColumn()) {
				leafTableDescriptorsBySQLName.put(columnDescriptor.getName(), columnDescriptor);
			}
		}

		return leafTableDescriptorsBySQLName;
	}
}