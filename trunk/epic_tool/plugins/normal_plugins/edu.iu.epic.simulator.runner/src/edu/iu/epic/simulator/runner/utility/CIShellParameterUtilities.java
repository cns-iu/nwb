package edu.iu.epic.simulator.runner.utility;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public final class CIShellParameterUtilities {
	/* Returns a Map containing only those entries in the given Dictionary
	 * whose key.startsWith(prefix), and where those prefixes
	 * are stripped off of the key before adding the entry to the returned Map.
	 */
	public static <V> Map<String, V> filterByAndStripIDPrefixes(
			Dictionary<String, V> parameters,
			String prefix) {
		Map<String, V> filtered = new HashMap<String, V>();
		
		for (Enumeration<String> keys = parameters.keys(); keys.hasMoreElements();) {
			String key = keys.nextElement();
			
			if (key.startsWith(prefix)) {
				V value = parameters.get(key);
				
				String parameterName = key.replace(prefix, "");
			
				filtered.put(parameterName, value);
			}
		}
		
		return filtered;
	}
}
