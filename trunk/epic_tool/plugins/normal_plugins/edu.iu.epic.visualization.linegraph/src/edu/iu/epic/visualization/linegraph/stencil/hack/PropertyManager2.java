/* Copyright (c) 2006-2008 Indiana University Research and Technology Corporation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * - Neither the Indiana University nor the names of its contributors may be used
 *  to endorse or promote products derived from this software without specific
 *  prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.iu.epic.visualization.linegraph.stencil.hack;

import static stencil.explore.Application.reporter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import stencil.WorkingDirectory;
import stencil.explore.Application;
import stencil.explore.model.AdapterOpts;
import stencil.explore.ui.interactive.Interactive;
import edu.iu.epic.visualization.linegraph.stencil.hack.PropertiesSource.LoadPropertiesException;


public class PropertyManager2 {
	private static final String DEFAULT_SESSION_CONFIGURATION_FILE = "ExploreSession.properties";
	private static final String DEFAULT_EXPLORE_CONFIGURATION_FILE = "Explore.properties";

	public static String stencilConfig = stencil.Configure.stencilConfig;
	public static String exploreConfig = DEFAULT_EXPLORE_CONFIGURATION_FILE;
	public static String sessionConfig = DEFAULT_SESSION_CONFIGURATION_FILE;

	
	// Keys configuration file information.
	public static final String SESSION_FILE_KEY = "defaultStencil";
	public static final String FONT_SIZE_KEY = "fontSize";
	public static final String RASTER_RESOLUTION_KEY = "exportResolution";
	public static final String ADAPTER_PREFIX = "adapter:";
	public static final String WORKING_DIR_KEY = "workingDir";
	public static final String CONCURRENT_BIAS_KEY = "concurrentBias";
	public static final String DEFAULT_ADAPTER = "defaultAdapter";
	
	/**
	 * Load properties from either the default file or an argument specified in args.
	 * If the settings flag appears in args, it will load that file.  Otherwise, it
	 * uses the default.
	 *
	 * @param arguments
	 * @return
	 */
	public static String[] getConfigFiles(String[] arguments) {
		List<String> files = new ArrayList<String>();
		
		if (arguments != null) {
			for (int ii = 0; ii < arguments.length; ii++) {
				if (Application.SETTINGS_FLAG.equals(arguments[ii])) {
					files.add(arguments[ii + 1]);
				}
			}
		}

		return files.toArray(new String[files.size()]);
	}

	/**
	 * Loads the configuration information from the specified files.
	 * Elements in the array have precedence over elements directly passed
	 * (and thus are loaded later).
	 */
	public static Properties loadProperties(
			String[] configurationFileNames, String... additionalConfigurationFileNames) {
		String[] allConfigurationFileNames =
			new String[configurationFileNames.length + additionalConfigurationFileNames.length];
		
		System.arraycopy(
			additionalConfigurationFileNames,
			0,
			allConfigurationFileNames,
			0,
			additionalConfigurationFileNames.length);
		System.arraycopy(
			configurationFileNames,
			0,
			allConfigurationFileNames,
			additionalConfigurationFileNames.length,
			configurationFileNames.length);
		
		return loadProperties(allConfigurationFileNames);
	}
	
	/**
	 * Loads the configuration information from the specified files.
	 * The file is assumed to conform to the java standard XML properties file schema.
	 *
	 * To provide 'cascading' values, files are loaded first to last.
	 * The last setting of a property 'wins'.
	 *
	 * @param configFile
	 * @return
	 */
	public static Properties loadProperties(String... configurationFileNames) {
		URL baseURL = getUserDirectoryURL();
		PropertiesSource[] propertiesSources = new PropertiesSource[configurationFileNames.length];
		
		for (int ii = 0; ii < configurationFileNames.length; ii++) {
			try {
				propertiesSources[ii] = new PropertiesSource.FileNamePropertiesSource(
					baseURL, configurationFileNames[ii]);
			} catch (MalformedURLException malformedURLException) {
				malformedURLException.printStackTrace();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
		return loadProperties(propertiesSources);
	}

	public static Properties loadProperties(PropertiesSource... propertiesSources) {
		Properties allProperties = new Properties();
		Properties mostRecentProperties = null;
		
		for (PropertiesSource propertiesSource : propertiesSources) {
			try {
				mostRecentProperties = propertiesSource.loadProperties();
				allProperties.putAll(mostRecentProperties);
			} catch (LoadPropertiesException propertiesLoadException) {
				System.err.println(propertiesLoadException.getMessage());
			}
		}
		
		if (allProperties.size() == 0) {
			throw new Error("No configuration files were loaded.  Aborting Stencil startup.");
		}

		// Setup session file information for interactive application.
		Interactive.sessionFile = allProperties.getProperty(SESSION_FILE_KEY);

		// Setup database driver map.
		try {
			stencil.util.streams.sql.DriverManager.addDrivers(mostRecentProperties);
		} catch (Exception exception) {
			reporter.addError("Error loading database drivers: %1$s.", exception.getMessage());
			exception.printStackTrace();
		}

		// Inform stencil of its options.
		Configure2.loadProperties(mostRecentProperties);

		// Setup adapter map.
		Map<String, String> adapterMap = new TreeMap<String, String>();
		
		for (Object keyObject : allProperties.keySet()) {
			String key = (String)keyObject;

			if (key.startsWith(ADAPTER_PREFIX)) {
				String name = key.substring(ADAPTER_PREFIX.length());
				adapterMap.put(name, allProperties.getProperty(key));
			}
		}

		String defaultAdapter = allProperties.getProperty(DEFAULT_ADAPTER);

		if (defaultAdapter == null) {
			defaultAdapter = adapterMap.values().iterator().next();
		}

		if (defaultAdapter == null) {
			throw new Error("No stencil adapters found.  Stencil configuration invalid.");
		}

		adapterMap.put("(" + defaultAdapter + ")", adapterMap.get(defaultAdapter));
		
		AdapterOpts.adapterMap = adapterMap;

		if (allProperties.containsKey(FONT_SIZE_KEY)) {
			try {
				Interactive.FONT_SIZE = Float.parseFloat(allProperties.getProperty(FONT_SIZE_KEY));
			} catch(Exception exception) {
				System.err.println("Error setting font size from settings file.");
			}
		}

		if (allProperties.containsKey(RASTER_RESOLUTION_KEY)) {
			try {
				Application.EXPORT_RESOLUTION =
					Integer.parseInt(allProperties.getProperty(RASTER_RESOLUTION_KEY));
			} catch(Exception exception) {
				System.err.println("Error setting raster resolution from settings file.");
			}
		}

		if (allProperties.containsKey(CONCURRENT_BIAS_KEY)) {
			try {
				Application.CONCURRENT_BIAS =
					Integer.parseInt(allProperties.getProperty(CONCURRENT_BIAS_KEY));
			} catch(Exception exception) {
				System.err.println("Error setting concurrent bias from settings file.");
			}
		}

		if (allProperties.containsKey(WORKING_DIR_KEY)) {
			try {
				WorkingDirectory.setWorkingDir(allProperties.getProperty(WORKING_DIR_KEY));
			} catch(Exception exception) {
				System.err.println("Error setting last file from settings file.");
			}
		} else {
			// If no working directory is specified, use the system user directory.
			try {
				WorkingDirectory.setWorkingDir(System.getProperty("user.dir"));
			} catch (Exception exception) {
				System.out.println(
					"Error determining working directory (default is user directory).");
			}
		}
		
		return mostRecentProperties;
	}

	/**
	 * Save a configuration file name.
	 */
	public static void setExploreConfig(String filename) {
		exploreConfig = filename;
	}

	/**
	 * Save explore-based configuration information to the indicated file.
	 * This does not save information that is stencil-wide.  That must be added
	 * to the stencil configuration file manually.
	 */
	public static void saveSessionProperties(String filename) {
		Properties sessionProperties = new Properties();

		sessionProperties.setProperty(WORKING_DIR_KEY, WorkingDirectory.getWorkingDir());

		try {
			sessionProperties.storeToXML(new java.io.FileOutputStream(filename), "");
		} catch (Exception exception) {
			throw new RuntimeException(
				String.format("Error saving configuration info to %1$s.", filename), exception);
		}
	}
	
	public static Properties[] loadPropertiesFromConfigurationFiles(
			String[] configurationFileNames, URL baseURL) {
		Properties allProperties = new Properties();
		Properties mostRecentProperties = 
			loadPropertiesFromConfigurationFiles(configurationFileNames, baseURL, allProperties);
		
		return new Properties[] { allProperties, mostRecentProperties };
	}
	
	public static Properties loadPropertiesFromConfigurationFiles(
			String[] configurationFileNames, URL baseURL, Properties allProperties) {
		Properties mostRecentProperties = null;

		for (String configurationFileName : configurationFileNames) {
			try {
				mostRecentProperties =
					loadPropertiesFromConfigurationFile(configurationFileName, baseURL);
				allProperties.putAll(mostRecentProperties);
			} catch (Exception exception) {
				System.err.println(String.format(
					"Error loading properties from %1$s. (%2$s) Ignoring error and continuing.",
					configurationFileName,
					exception.getMessage()));
			}
		}
		
		return mostRecentProperties;
	}
	
	public static Properties loadPropertiesFromConfigurationFile(
			String configurationFileName, URL baseURL) throws Exception {
		Properties properties = new Properties();
		properties.loadFromXML(new URL(baseURL, configurationFileName).openStream());
		
		return properties;
	}

	private static URL getUserDirectoryURL() throws Error {
		try {
			return new java.io.File(System.getProperty("user.dir")).toURI().toURL();
		} catch (Exception exception) {
			throw new Error("Error getting system property \'user.dir\'.");
		}
	} 
}
