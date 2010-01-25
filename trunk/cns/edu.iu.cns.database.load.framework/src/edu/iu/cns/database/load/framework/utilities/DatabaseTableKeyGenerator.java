package edu.iu.cns.database.load.framework.utilities;


public class DatabaseTableKeyGenerator {
	private int nextPrimaryKey = 1;

	public int getNextKey() {
		int nextPrimaryKey = this.nextPrimaryKey;
		this.nextPrimaryKey++;

		return nextPrimaryKey;
	}
}