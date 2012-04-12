package edu.iu.cns.database.load.framework.utilities;


/* FIXME Use an Iterator. Also consider synchronization. */
public class DatabaseTableKeyGenerator {
	private int nextPrimaryKey = 1;

	public int getNextKey() {
		int newPrimaryKey = this.nextPrimaryKey;
		this.nextPrimaryKey++;

		return newPrimaryKey;
	}
}