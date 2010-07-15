package edu.iu.sci2.database.star.extract.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class StarDatabaseDescriptor {
	private CoreTableDescriptor coreTableDescriptor;
	private Map<String, ColumnDescriptor> leafTableDescriptorsByName;

	public StarDatabaseDescriptor(StarDatabaseMetadata metadata) {
		this(new CoreTableDescriptor(metadata), constructLeafTableDescriptorsByName(metadata));
	}

	public StarDatabaseDescriptor(
			CoreTableDescriptor coreTableDescriptor,
			Map<String, ColumnDescriptor> leafTableDescriptorsByName) {
		this.coreTableDescriptor = coreTableDescriptor;
		this.leafTableDescriptorsByName = leafTableDescriptorsByName;
	}

	public CoreTableDescriptor getCoreTableDescriptor() {
		return this.coreTableDescriptor;
	}

	public Map<String, ColumnDescriptor> getLeafTableDescriptorsByName() {
		return this.leafTableDescriptorsByName;
	}

	public List<String> getLeafTableNames() {
		List<String> leafTableNames = new ArrayList<String>();
		leafTableNames.addAll(this.leafTableDescriptorsByName.keySet());

		return leafTableNames;
	}

	public Map<String, String> getTableNameOptionsWithoutCore() {
		Map<String, String> options = new HashMap<String, String>();

		for (String key : this.leafTableDescriptorsByName.keySet()) {
			options.put(key, this.leafTableDescriptorsByName.get(key).getNameForDatabase());
		}

		return options;
	}

	public Map<String, String> getTableNameOptionsWithCore() {
		Map<String, String> options = getTableNameOptionsWithoutCore();
		options.put(
			getCoreTableDescriptor().getCoreEntityHumanReadableName(),
			getCoreTableDescriptor().getCoreEntityTableName());

		return options;
	}

	private static Map<String, ColumnDescriptor> constructLeafTableDescriptorsByName(
			StarDatabaseMetadata metadata) {
		Map<String, ColumnDescriptor> leafTableDescriptorsByName =
			new HashMap<String, ColumnDescriptor>();

		for (ColumnDescriptor columnDescriptor : metadata.getColumnDescriptors().values()) {
			if (!columnDescriptor.isCoreColumn()) {
				leafTableDescriptorsByName.put(columnDescriptor.getName(), columnDescriptor);
			}
		}

		return leafTableDescriptorsByName;
	}
}