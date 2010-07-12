package edu.iu.sci2.database.star.extract.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cishell.service.database.Database;

public class StarDatabase {
	private Database database;
	private CoreTableDescriptor coreTableDescriptor;
	private Map<String, LeafTableDescriptor> leafTableDescriptorsByName;

	public StarDatabase(
			Database database,
			CoreTableDescriptor coreTableDescriptor,
			Map<String, LeafTableDescriptor> leafTableDescriptorsByName) {
		this.database = database;
		this.coreTableDescriptor = coreTableDescriptor;
		this.leafTableDescriptorsByName = leafTableDescriptorsByName;
	}

	public Database getDatabase() {
		return this.database;
	}

	public CoreTableDescriptor getCoreTableDescriptor() {
		return this.coreTableDescriptor;
	}

	public Map<String, LeafTableDescriptor> getLeafTableDescriptorsByName() {
		return this.leafTableDescriptorsByName;
	}

	public Collection<String> getLeafTableNames() {
		List<String> leafTableNames = new ArrayList<String>();
		leafTableNames.addAll(this.leafTableDescriptorsByName.keySet());

		return leafTableNames;
	}
}