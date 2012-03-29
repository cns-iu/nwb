package edu.iu.sci2.database.star.extract.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

/**
 * metadata -- the metadata for this Generic-CSV database. It's created when the Generic-CSV is
 * created, and passed as a CIShell Data metadata entry.
 * coreTableDescriptor -- metadata on the core table.
 * allColumnDescriptorsByName -- all of the columns' metadata, mapped by their respective
 * display name.
 * allColumnDescriptorsBySQLName -- all of the columns' metadata, mapped by their respective
 * database name.
 * coreColumnDescriptorsByName -- all of the core table columns' metadata, mapped by their
 * respective display name.
 * leafColumnDescriptorsByName -- all of the leaf table columns' metadata, mapped by their
 * respective display name.
 * leafColumnDescriptorsBySQLName -- all of the leaf table columns' metadata, mapped by their
 * respective database name.
 */
public class StarDatabaseDescriptor {
	private StarDatabaseMetadata metadata;
	private CoreTableDescriptor coreTableDescriptor;
	private Map<String, ColumnDescriptor> allColumnDescriptorsByName =
		new HashMap<String, ColumnDescriptor>();
	private Map<String, ColumnDescriptor> allColumnDescriptorsBySQLName =
		new HashMap<String, ColumnDescriptor>();
	private Map<String, ColumnDescriptor> coreColumnDescriptorsByName;
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