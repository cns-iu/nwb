package edu.iu.cns.database.loader.framework;

import java.util.Dictionary;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;


public abstract class Entity<T extends Entity<?>> extends RowItem<T> {
	public static final int BASE_HASH_MULTIPLIER = 31;

	private int primaryKey;

	public Entity(DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Comparable<?>> attributes) {
		super(attributes);
		this.primaryKey = keyGenerator.getNextKey();

		/*
		 * We DON'T want to add the primary key to attributes here because it causes any
		 *  otherwise-equal comparison to become unequal.
		 */
	}

	public final int getPrimaryKey() {
		return this.primaryKey;
	}

	public final String toString() {
		return Integer.toString(getPrimaryKey());
	}
}
