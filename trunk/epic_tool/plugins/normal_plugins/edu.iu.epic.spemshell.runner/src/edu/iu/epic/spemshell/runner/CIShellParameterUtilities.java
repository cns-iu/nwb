package edu.iu.epic.spemshell.runner;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CIShellParameterUtilities {
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
