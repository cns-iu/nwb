package edu.iu.sci2.database.star.extract.network;

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
}