package edu.iu.cns.database.loader.framework;

import java.util.ArrayList;
import java.util.List;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;

public class DatabaseTableType<RowItemType extends RowItem<RowItemType>> {
	private DatabaseTableKeyGenerator keyGenerator = new DatabaseTableKeyGenerator();
	private List<RowItemType> items = new ArrayList<RowItemType>();
	private String humanReadableName;
	private String databaseTableName;

	public DatabaseTableType(String humanReadableName) {
		this(humanReadableName, humanReadableName.toUpperCase());
	}

	public DatabaseTableType(String humanReadableName, String databaseTableName) {
		this.humanReadableName = humanReadableName;
		this.databaseTableName = databaseTableName;
	}

	public DatabaseTableKeyGenerator getKeyGenerator() {
		return this.keyGenerator;
	}

	public final List<? extends RowItem<RowItemType>> getItems() {
		return this.items;
	}

	public final String getHumanReadableName() {
		return this.humanReadableName;
	}

	public final String getDatabaseTableName() {
		return this.databaseTableName;
	}

	public RowItemType addOrMerge(RowItemType newItem) {
		for (RowItemType originalItem : this.items) {
			if (originalItem.shouldMerge(newItem)) {
				return originalItem.merge(newItem);
			}
		}

		this.items.add(newItem);

		return newItem;
	}

	private RowItemType findDuplicateItem(RowItemType newEntity) {
		for (RowItemType originalEntity : this.items) {
			if (originalEntity.equals(newEntity)) {
				return originalEntity;
			}
		}

		return null;
	}
}