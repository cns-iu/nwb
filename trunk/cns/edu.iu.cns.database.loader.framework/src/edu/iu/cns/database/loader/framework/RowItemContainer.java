package edu.iu.cns.database.loader.framework;

import java.util.ArrayList;
import java.util.List;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;

public class RowItemContainer<RowItemType extends RowItem<RowItemType>> {
	private DatabaseTableKeyGenerator keyGenerator = new DatabaseTableKeyGenerator();
	private List<RowItemType> items = new ArrayList<RowItemType>();
	private String humanReadableName;
	private String databaseTableName;

	public RowItemContainer(String humanReadableName) {
		this(humanReadableName, humanReadableName.toUpperCase());
	}

	public RowItemContainer(String humanReadableName, String databaseTableName) {
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
				originalItem.merge(newItem);

				return originalItem;
			}
		}

		this.items.add(newItem);

		return newItem;
	}
}