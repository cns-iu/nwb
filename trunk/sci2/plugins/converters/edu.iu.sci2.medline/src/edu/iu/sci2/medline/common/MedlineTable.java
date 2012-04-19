package edu.iu.sci2.medline.common;

import com.google.common.base.Preconditions;

import prefuse.data.Table;

/**
 * A wrapper for a {@link Table} to make sure it is a table from a
 * MEDLINE®/PubMed® file.
 * 
 */
public class MedlineTable {
	private Table table;

	/**
	 * Wrap a {@link Table} MEDLINE®/PubMed®.
	 */
	public MedlineTable(Table table) {
		Preconditions.checkNotNull(table);
		this.table = table;
	}

	/**
	 * Get the MEDLINE®/PubMed® table as a standard {@link Table}.
	 */
	public Table getTable() {
		return this.table;
	}

	@Override
	public String toString() {
		return "MedlineTable [table=" + this.table + "]";
	}
}
