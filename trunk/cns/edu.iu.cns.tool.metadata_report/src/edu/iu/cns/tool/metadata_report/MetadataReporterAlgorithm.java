package edu.iu.cns.tool.metadata_report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class MetadataReporterAlgorithm implements Algorithm {
	public static final String TEXT_FILE_EXTENSION = "txt";
	public static final String CSV_FILE_EXTENSION = "csv";
	public static final String TEXT_MIME_TYPE = "file:text/txt";
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final Collection<String> SERVICE_SYMBOLIC_NAMES_TO_IGNORE;

	static {
		Collection<String> serviceSymbolicNamesToIgnore = new ArrayList<String>();
		serviceSymbolicNamesToIgnore.add("org.eclipse.core.jobs");
		serviceSymbolicNamesToIgnore.add("org.eclipse.core.runtime");
		serviceSymbolicNamesToIgnore.add("org.eclipse.equinox.app");
		serviceSymbolicNamesToIgnore.add("org.eclipse.equinox.cm");
		serviceSymbolicNamesToIgnore.add("org.eclipse.equinox.common");
		serviceSymbolicNamesToIgnore.add("org.eclipse.equinox.ds");
		serviceSymbolicNamesToIgnore.add("org.eclipse.equinox.event");
		serviceSymbolicNamesToIgnore.add("org.eclipse.equinox.log");
		serviceSymbolicNamesToIgnore.add("org.eclipse.equinox.metatype");
		serviceSymbolicNamesToIgnore.add("org.eclipse.equinox.preferences");
		serviceSymbolicNamesToIgnore.add("org.eclipse.equinox.registry");
		serviceSymbolicNamesToIgnore.add("org.eclipse.osgi");
		serviceSymbolicNamesToIgnore.add("org.eclipse.update.configurator");

		SERVICE_SYMBOLIC_NAMES_TO_IGNORE =
			Collections.unmodifiableCollection(serviceSymbolicNamesToIgnore);
	}

	public static final Collection<String> SERVICE_PROPERTY_KEYS_TO_IGNORE;

	static {
		Collection<String> servicePropertyKeysToIgnore = new ArrayList<String>();
		servicePropertyKeysToIgnore.add("Algorithm-Directory");
		servicePropertyKeysToIgnore.add("component.id");
		servicePropertyKeysToIgnore.add("edge_query");
		servicePropertyKeysToIgnore.add("node_query");
		servicePropertyKeysToIgnore.add("objectClass");
		servicePropertyKeysToIgnore.add("query");
		servicePropertyKeysToIgnore.add("service.ranking");
		servicePropertyKeysToIgnore.add("service.pid");
		servicePropertyKeysToIgnore.add("service.vendor");
		servicePropertyKeysToIgnore.add("service.id");

		SERVICE_PROPERTY_KEYS_TO_IGNORE =
			Collections.unmodifiableCollection(servicePropertyKeysToIgnore);
	}

    private BundleContext bundleContext;

    public MetadataReporterAlgorithm(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	FileWriter reportWriter = null;
    	FileWriter headersWriter = null;
    	FileWriter bundleNameWriter = null;

    	try {
    		ServiceReference[] serviceReferences =
    			this.bundleContext.getAllServiceReferences(null, null);

    		Multimap<String, String> symbolicNamesToReports =
    			HashMultimap.<String, String>create();

    		Map<String, List<String>> bundleNamesToProperties =
    			new HashMap<String, List<String>>();
    		
    		Set<Dictionary> bundleHeaders = new HashSet<Dictionary>();

    		for (ServiceReference serviceReference : serviceReferences) {
    			Bundle bundle = serviceReference.getBundle();
    			String symbolicName = bundle.getSymbolicName();

    			if (!SERVICE_SYMBOLIC_NAMES_TO_IGNORE.contains(symbolicName)) {
    				String pluginString = createPluginString(serviceReference);
    				symbolicNamesToReports.put(symbolicName, pluginString);

    				List<String> properties = new ArrayList<String>();
    				Object pid = serviceReference.getProperty("service.pid");

    				if (pid != null) {
    					properties.add(pid.toString());
    				} else {
    					properties.add("null");
    				}

    				Object label = serviceReference.getProperty("label");

    				if (label != null) {
    					properties.add(label.toString());
    				} else {
    					properties.add("null");
    				}

    				bundleNamesToProperties.put(symbolicName, properties);

    				bundleHeaders.add(bundle.getHeaders());
    			}
    		}

    		File reportFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
    			"algorithm_metadata_report", TEXT_FILE_EXTENSION);
    		reportWriter = new FileWriter(reportFile);
    		File bundleNameFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
    			"pluginNames", CSV_FILE_EXTENSION);
    		bundleNameWriter = new FileWriter(bundleNameFile);

    		bundleNameWriter.append("Plugin Name,Algorithm PID,Label\n");

    		for (Iterator<String> keys = symbolicNamesToReports.keySet().iterator();
    				keys.hasNext();) {
    			String key = keys.next();

    			for (String value : symbolicNamesToReports.get(key)) {
    				reportWriter.append(value);
    			}

   				bundleNameWriter.append(
    				key + "," +
    				StringUtilities.implodeList(bundleNamesToProperties.get(key), ",") +
    				"\n");
    		}

    		reportWriter.close();
    		bundleNameWriter.close();

    		File headersFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
    			"bundle_headers", TEXT_FILE_EXTENSION);
    		headersWriter = new FileWriter(headersFile);

    		for (Dictionary headers : bundleHeaders) {
    			headersWriter.append(headers.toString() + ":\n");

    			for (Enumeration keys = headers.keys(); keys.hasMoreElements();) {
    				String key = keys.nextElement().toString();
    				headersWriter.append("\t" + key + ": \"" + headers.get(key) + "\"\n");
    			}

    			headersWriter.append("\n");
    		}

    		headersWriter.close();

    		
//
//    		// TODO Sort by symbolic name first.
//    		for (ServiceReference serviceReference : serviceReferences) {
//    			String pluginString = createPluginString(serviceReference);
//    			reportWriter.append(pluginString);
//    		}

    		return formOutData(reportFile, headersFile, bundleNameFile);
    	} catch (InvalidSyntaxException e) {
    		String exceptionMessage = "No one should ever see this: \"" + e.getMessage() + "\"";
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	} catch (IOException e) {
    		String exceptionMessage =
    			"An error occurred when trying to write to the algorithm metadata report " +
    			"to a temporary file: \"" + e.getMessage() + "\"";
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	} finally {
    		if (reportWriter != null) {
    			try {
    				reportWriter.close();
    			} catch (IOException e) {
    				String exceptionMessage =
    					"An error occurred when trying to close the algorithm metadata report " +
    					"file: \"" + e.getMessage() + "\"";
    				throw new AlgorithmExecutionException(exceptionMessage, e);
    			}
    		}

    		if (headersWriter != null) {
    			try {
    				headersWriter.close();
    			} catch (IOException e) {
    				String exceptionMessage =
    					"An error occurred when trying to close the bundle headers file: \"" +
    					e.getMessage() + "\"";
    				throw new AlgorithmExecutionException(exceptionMessage, e);
    			}
    		}

			if (bundleNameWriter != null) {
    			try {
    				bundleNameWriter.close();
    			} catch (IOException e) {
    				String exceptionMessage =
    					"An error occurred when trying to close the plugin names file: \"" +
    					e.getMessage() + "\"";
    				throw new AlgorithmExecutionException(exceptionMessage, e);
    			}
    		}
    	}
    }

    private static String createPluginString(ServiceReference serviceReference) {
		String header = createPluginHeader(serviceReference);
		String values = createPluginValuesString(serviceReference);

		return header + values + "\r\n";
    }

    private static Data[] formOutData(File reportFile, File headersFile, File bundleNameFile) {
		Data reportFileData = new BasicData(reportFile, TEXT_MIME_TYPE);
		Dictionary<String, Object> reportFileMetadata = reportFileData.getMetadata();
		reportFileMetadata.put(DataProperty.LABEL, "Algorithm Metadata Report");
		reportFileMetadata.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);

		Data headersFileData = new BasicData(headersFile, TEXT_MIME_TYPE);
		Dictionary<String, Object> headersFileMetadata = headersFileData.getMetadata();
		headersFileMetadata.put(DataProperty.LABEL, "Bundle Headers Report");
		headersFileMetadata.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);

		Data bundleNameFileData = new BasicData(bundleNameFile, CSV_MIME_TYPE);
		Dictionary<String, Object> bundleNameFileMetadata = bundleNameFileData.getMetadata();
		bundleNameFileMetadata.put(DataProperty.LABEL, "Bundle Names Report");
		bundleNameFileMetadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
    	
        return new Data[] { reportFileData, headersFileData, bundleNameFileData };
    }

    private static String createPluginHeader(ServiceReference serviceReference) {
    	String pluginHeader = serviceReference.getBundle().getSymbolicName() + ":\r\n";

    	return pluginHeader;
    }

    private static String createPluginValuesString(ServiceReference serviceReference) {
    	StringBuilder pluginValues = new StringBuilder();
    	
    	List<String> keys = Arrays.asList(serviceReference.getPropertyKeys());
    	Collections.sort(keys);
    	
    	for (String key : keys) {
    		String keyValueString = createPluginValueString(serviceReference, key);
    		pluginValues.append(keyValueString);
    	}

    	return pluginValues.toString();
    }

    private static String createPluginValueString(ServiceReference serviceReference, String key) {
    	if (!SERVICE_PROPERTY_KEYS_TO_IGNORE.contains(key)) {
    		Object value = serviceReference.getProperty(key);

    		if (value != null) {
    			return "\t" + key + ": " + value.toString() + "\r\n";
    		} else {
    			return "";
    		}
    	} else {
    		return "";
    	}
    }
}