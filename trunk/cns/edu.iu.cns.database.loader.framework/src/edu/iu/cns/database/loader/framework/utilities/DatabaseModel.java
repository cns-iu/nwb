package edu.iu.cns.database.loader.framework.utilities;

import java.util.ArrayList;
import java.util.List;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.RowItemContainer;

public class DatabaseModel {
	private List<RowItemContainer<?>> rowItemLists = new ArrayList<RowItemContainer<?>>();

	public DatabaseModel(RowItemContainer<?>... rowItemLists) {
		for (RowItemContainer<?> rowItemList : rowItemLists) {
			if (!this.rowItemLists.contains(rowItemList)) {
				this.rowItemLists.add(rowItemList);
			}
		}
	}

	public List<RowItemContainer<?>> getRowItemLists() {
		return this.rowItemLists;
	}

	public RowItemContainer<?> getRowItemListByHumanReadableName(
			String humanReadableName, boolean caseMatters) {
		for (RowItemContainer<?> list : this.rowItemLists) {
			if (caseMatters && list.getHumanReadableName().equals(humanReadableName)) {
				return list;
			} else if (list.getHumanReadableName().equalsIgnoreCase(humanReadableName)) {
				return list;
			}
		}

		return null;
	}

	public<T extends RowItem<T>> RowItemContainer<T> getRowItemListOfTypeByHumanReadableName(
			String humanReadableName, boolean caseMatters) {
		return (RowItemContainer<T>)getRowItemListByHumanReadableName(
			humanReadableName, caseMatters);
	}

	public RowItemContainer<?> getRowItemListByDatabaseTableName(String databaseTableName) {
		for (RowItemContainer<?> list : this.rowItemLists) {
			if (list.getDatabaseTableName().equals(databaseTableName)) {
				return list;
			}
		}

		return null;
	}

	public<T extends RowItem<T>> RowItemContainer<T> getRowItemListOfTypeByDatabaseTableName(
			String humanReadableName) {
		return (RowItemContainer<T>)getRowItemListByDatabaseTableName(humanReadableName);
	}
}