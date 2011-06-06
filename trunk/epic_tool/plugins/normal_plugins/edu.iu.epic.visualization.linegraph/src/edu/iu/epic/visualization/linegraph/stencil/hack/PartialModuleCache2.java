package edu.iu.epic.visualization.linegraph.stencil.hack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import stencil.operator.module.Module;
import stencil.operator.module.ModuleCache;
import stencil.operator.module.ModuleData;
import stencil.operator.module.util.ModuleDataParser;

public class PartialModuleCache2 {
	/**
	 * Key on properties in a properties list that indicates module to be registered.
	 */
	public static final String MODULE_KEY = "module";
	
	/** Load module entries from a property set.
	 * 
	 * Given a properties listing, looks for entries
	 * that conform to the modules property entry style (dictated by the MODULE_KEY).
	 * Each such entry found will be recorded in the static part of the Module cache.
	 * 
	 * @param props
	 */
	public static void registerModules(Properties props) {
		for (Object keyString : props.keySet()) {
			String key = (String) keyString;
			
			if (key.startsWith(MODULE_KEY)) {
				String fileName = props.getProperty(key);
				
				/*try {
					System.err.println(
						fileName + ": " +
						FileUtilities.readEntireInputStream(
							ModuleCache.class.getResourceAsStream(fileName)));;
				} catch (Exception exception) {
					exception.printStackTrace();
				}*/

				BufferedReader stream = new BufferedReader(
					new InputStreamReader(ModuleCache.class.getResourceAsStream(fileName)));

				Module module;
				ModuleData moduleData;

				try {
					moduleData = ModuleDataParser.parse(stream);
				}  catch (Exception exception) {
					throw new RuntimeException(
						String.format("Error parsing meta-data file %1$s.", fileName), exception);
				}
				
				try {
					module = moduleData.getModule();
					
					ModuleCache.register(module);
				} catch (Exception exception) {
					exception.printStackTrace();
					System.err.println(exception.getMessage());

					throw new RuntimeException(
						String.format("Error instantiating module %1$s.", moduleData.getName()),
						exception);
				}
			}
		}
	}
}