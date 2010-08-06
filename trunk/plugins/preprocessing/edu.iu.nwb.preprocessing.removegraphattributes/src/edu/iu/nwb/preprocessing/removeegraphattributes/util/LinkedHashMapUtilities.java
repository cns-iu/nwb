package edu.iu.nwb.preprocessing.removeegraphattributes.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LinkedHashMapUtilities {
	/* Make a LinkedHashMap just like oldMap,
	 * except that any entry whose key is in keysToExclude has been excluded.
	 * 
	 * Note the types here.. from Map to LinkedHashMap.  This seems necessary
	 * unfortunately due to an apparent error in edu.iu.nwb.util -- in
	 * particular, node/edge schemas and attributes should be consistently
	 * treated as LinkedHashMaps since the NWB file format specification
	 * enforces a particular ordering on some of the entries.  
	 */
	public static<K, V> LinkedHashMap<K, V> excludeKeysFromMap(
			Map<K, V> oldMap, Collection<K> keysToExclude) {
		LinkedHashMap<K, V> prunedMap = new LinkedHashMap<K, V>();
		
		for (Iterator<Map.Entry<K, V>> entryIt = oldMap.entrySet().iterator();
				entryIt.hasNext();) {
			Entry<K, V> entry = entryIt.next();
			K key = entry.getKey();
			V value = entry.getValue();
	
			// If we want to exclude this key, don't add it to prunedMap
			if (keysToExclude.contains(key)) {
				// Do nothing
			} else {
				prunedMap.put(key, value);
			}
		}
		
		return prunedMap;
	}
}
