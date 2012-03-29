package edu.iu.cns.database.load.framework;

import java.util.Dictionary;
import java.util.List;

import org.cishell.utilities.StringUtilities;

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

	public abstract Object createMergeKey();

	/**
	 * merge assumes that shouldMerge(otherAddress) would return true.
	 */
	public abstract void merge(T otherItem);

	/// Side-effects mergeKey.
	protected static void addStringOrAlternativeToMergeKey(
			List<Object> mergeKey, String string, Object alternative) {
		mergeKey.add(StringUtilities.alternativeIfNotNull_Empty_OrWhitespace(string, alternative));
	}
	
	/// Side-effects mergeKey.
	protected static void addCaseInsensitiveStringOrAlternativeToMergeKey(
			List<Object> mergeKey, String string, Object alternative) {
		mergeKey.add(StringUtilities.alternativeIfNotNull_Empty_OrWhitespace_IgnoreCase(
			string, alternative));
	}
}
