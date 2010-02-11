package edu.iu.cns.database.load.framework;

import java.util.Dictionary;
import java.util.List;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;


public abstract class Entity<T extends Entity<?>> extends RowItem<T> {
	private int primaryKey;

	public Entity(DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Object> attributes) {
		super(attributes);
		this.primaryKey = keyGenerator.getNextKey();

		getAttributes().put(Schema.PRIMARY_KEY, this.primaryKey);
	}

	public final int getPrimaryKey() {
		return this.primaryKey;
	}
}
