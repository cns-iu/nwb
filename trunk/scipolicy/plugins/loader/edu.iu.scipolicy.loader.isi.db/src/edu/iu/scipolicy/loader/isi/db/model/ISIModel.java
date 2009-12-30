package edu.iu.scipolicy.loader.isi.db.model;

import java.util.ArrayList;
import java.util.List;

import edu.iu.cns.database.loader.framework.RowItemContainer;

public class ISIModel {
	private List<RowItemContainer<?>> rowItemLists = new ArrayList<RowItemContainer<?>>();

	public ISIModel(RowItemContainer<?>... rowItemLists) {
		for (RowItemContainer<?> rowItemList : rowItemLists) {
			if (!this.rowItemLists.contains(rowItemList)) {
				this.rowItemLists.add(rowItemList);
			}
		}
	}

	public List<RowItemContainer<?>> getRowItemLists() {
		return this.rowItemLists;
	}
}