package edu.iu.epic.visualization.linegraph.stencil.hack;

import java.util.Properties;
import java.net.URL;


/**
 * Methods to configure the Stencil system based
 * upon properties files specified on the system.
 * The loadProperties method should be invoked at least
 * once per application instance before using the Stencil library.
 *
 * (Multiple invocations are allowed, but results are cumulative...
 * there is no guarantee that you will get a 'clean' environment).
 *
 */
public class Configure2 {
	private static final String DEFAULT_STENCIL_CONFIGURATION_FILE = "Stencil.properties";

	public static String stencilConfig = DEFAULT_STENCIL_CONFIGURATION_FILE;

	private Configure2() {
		/* Utility, non-instantiable class.*/
	}

	public static void loadProperties(Properties props) {
		// Setup database driver map.
		try {
			stencil.util.streams.sql.DriverManager.addDrivers(props);
		} catch (Exception exception) {
			System.err.println("Error loading database drivers.");
			exception.printStackTrace();
		}

		PartialModuleCache2.registerModules(props);
	}

	public static void loadProperties(String... urls) throws Exception {
		Properties properties = new Properties();
		URL baseURL;
		
		try {
			baseURL = new URL("file://" + System.getProperty("user.dir")+"/");
		} catch (Exception exception) {
			throw new Error("Error initailizing context.");
		}
		
		for (String url : urls) {
			properties.loadFromXML(new URL(baseURL, url).openStream());
		}

		loadProperties(properties);
	}
}
