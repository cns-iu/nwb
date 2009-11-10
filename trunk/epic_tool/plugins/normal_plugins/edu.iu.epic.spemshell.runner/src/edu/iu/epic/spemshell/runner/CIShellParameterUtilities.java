package edu.iu.epic.spemshell.runner;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public final class CIShellParameterUtilities {
	private CIShellParameterUtilities() {
		// Utilities only; do not instantiate.
	}
	
	/* Returns a Map containing only those entries in the given Dictionary
	 * whose key.startsWith(prefix), and where those prefixes
	 * are stripped off of the key before adding the entry to the returned Map.
	 */
	public static <V> Map<String, V> filterByAndStripIDPrefixes(
			Dictionary<String, V> parameters,
			String prefix) {
		Map<String, V> filtered = new HashMap<String, V>();
		
		for (Enumeration<String> parameterKeys = parameters.keys();
				parameterKeys.hasMoreElements();) {
			String key = parameterKeys.nextElement();
			
			if (key.startsWith(prefix)) {
				V value = parameters.get(key);
				
				String parameterName =
					key.replace(prefix, "");
			
				filtered.put(parameterName, value);
			}
		}
		
		return filtered;
	}
}
