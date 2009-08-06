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
	public static LinkedHashMap excludeKeysFromMap(
			Map oldMap, Collection keysToExclude) {
		LinkedHashMap prunedMap = new LinkedHashMap();
		
		for (Iterator entryIt = oldMap.entrySet().iterator(); entryIt.hasNext();) {
			Entry entry = (Entry) entryIt.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
	
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
