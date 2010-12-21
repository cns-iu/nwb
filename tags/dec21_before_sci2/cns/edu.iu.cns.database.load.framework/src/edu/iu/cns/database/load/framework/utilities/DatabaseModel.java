package edu.iu.cns.database.load.framework.utilities;

import java.util.ArrayList;
import java.util.List;

import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.RowItemContainer;

public class DatabaseModel {
	private List<RowItemContainer<? extends RowItem<?>>> rowItemLists =
		new ArrayList<RowItemContainer<? extends RowItem<?>>>();

	public DatabaseModel(RowItemContainer<?>... rowItemLists) {
		for (RowItemContainer<?> rowItemList : rowItemLists) {
			if (!this.rowItemLists.contains(rowItemList)) {
				this.rowItemLists.add(rowItemList);
			}
		}
	}

	public DatabaseModel(List<RowItemContainer<? extends RowItem<?>>> rowItemLists) {
		this.rowItemLists = rowItemLists;
	}

	public List<RowItemContainer<? extends RowItem<?>>> getRowItemLists() {
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

	@SuppressWarnings("unchecked")	// Cast ? To T.
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

	@SuppressWarnings("unchecked")	// Cast ? To T.
	public<T extends RowItem<T>> RowItemContainer<T> getRowItemListOfTypeByDatabaseTableName(
			String humanReadableName) {
		return (RowItemContainer<T>)getRowItemListByDatabaseTableName(humanReadableName);
	}
}