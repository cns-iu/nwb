package edu.iu.cns.database.load.framework;

import java.util.Dictionary;
import java.util.List;

import org.cishell.utilities.StringUtilities;

public abstract class RowItem<T extends RowItem<?>> {
	private Dictionary<String, Object> attributes;
	
	public RowItem(Dictionary<String, Object> attributes) {
		this.attributes = attributes;
	}

	public final Dictionary<String, Object> getAttributes() {
		return this.attributes;
	}
	
//	public final Dictionary<String, Object> getAttributes() {
//		getAttributes();
//	
//		return new modified attributes thing
//		
//		return this.attributes;
//	}
	//TODO change to Object
	public abstract Object createMergeKey();

	/**
	 * merge assumes that shouldMerge(otherAddress) would return true.
	 */
	public abstract void merge(T otherItem);

	/// Side-effects mergeKey.
	protected static void addStringOrAlternativeToMergeKey(
			List<Object> mergeKey, String string, Object alternative) {
		/*if (!StringUtilities.isNull_Empty_OrWhitespace(string)) {
			mergeKey.add(string);
		} else {
			mergeKey.add(alternative);
		}*/
		mergeKey.add(StringUtilities.alternativeIfNotNull_Empty_OrWhitespace(string, alternative));
	}
	
	/// Side-effects mergeKey.
	protected static void addCaseInsensitiveStringOrAlternativeToMergeKey(
			List<Object> mergeKey, String string, Object alternative) {
		/*if (!StringUtilities.isNull_Empty_OrWhitespace(string)) {
			mergeKey.add(string.toLowerCase());
		} else {
			mergeKey.add(alternative);
		}*/
		mergeKey.add(StringUtilities.alternativeIfNotNull_Empty_OrWhitespace_IgnoreCase(
			string, alternative));
	}
}