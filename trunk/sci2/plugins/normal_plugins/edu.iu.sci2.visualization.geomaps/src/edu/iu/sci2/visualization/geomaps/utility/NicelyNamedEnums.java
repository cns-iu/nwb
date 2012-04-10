package edu.iu.sci2.visualization.geomaps.utility;

import java.util.EnumSet;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

/**
 * Utilities intended to ease the use of enums having unique, user-friendly names as algorithm
 * parameters.
 */
public class NicelyNamedEnums {
	private NicelyNamedEnums() {}
	
	/**
	 * An enum implementing this interface may take advantage of {@link NicelyNamedEnums}.  The nice
	 * name provided for each enum constant <strong>must</strong> be unique among the nice names
	 * defined by all constants of its type.
	 */
	public interface NicelyNamed {
		/**
		 * The user-friendly enum constant name returned <strong>must</strong> be unique among the
		 * nice names defined by all constants of its type.
		 */
		String getNiceName();
	}

	
	/**
	 * The nice names defined by all enum constants of {@code enumClass}.
	 * 
	 * @throw IllegalArgumentException	If any nice name found is not unique. 
	 */
	public static <T extends Enum<T> & NicelyNamed> ImmutableSet<String> allNiceNamesOf(
			Class<T> enumClass) {
		return mapNiceNamesToConstants(enumClass).keySet();
	}
	
	/**
	 * The enum constant with the given nice name.
	 * 
	 * @throw IllegalArgumentException	If any nice name found is not unique or if no enum constant
	 * 									has this nice name.
	 */
	public static <T extends Enum<T> & NicelyNamed> T getConstantNamed(
			Class<T> clazz, String niceName) {
		// Fails fast for *any* non-unique name
		T enumConstant = mapNiceNamesToConstants(clazz).get(niceName);
		
		if (enumConstant == null) {
			throw new IllegalArgumentException(String.format(
					"No enum constant from %s has the nice name \"%s\".", clazz, niceName));
		}
		
		return enumConstant;
	}

	/**
	 * @throw IllegalArgumentException	When nice names are not unique.
	 */
	private static <T extends Enum<T> & NicelyNamed> ImmutableBiMap<String, T> mapNiceNamesToConstants(
			Class<T> enumClass) {
		return ImmutableBiMap.copyOf(Maps.uniqueIndex(
				EnumSet.allOf(enumClass),
				new Function<NicelyNamed, String>() {
					@Override
					public String apply(NicelyNamed n) {
						return n.getNiceName();
					}
				}));
	}
}
