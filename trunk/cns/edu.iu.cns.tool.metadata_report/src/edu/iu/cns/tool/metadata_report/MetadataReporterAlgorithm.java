package edu.iu.cns.tool.metadata_report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class MetadataReporterAlgorithm implements Algorithm {
	public static final String TEXT_FILE_EXTENSION = "txt";
	public static final String TEXT_MIME_TYPE = "file:text/txt";
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

    private Data[] data;
    private Dictionary parameters;
    private CIShellContext ciShellContext;
    private BundleContext bundleContext;

    public MetadataReporterAlgorithm(
    		Data[] data,
    		Dictionary parameters,
    		CIShellContext ciShellContext,
    		BundleContext bundleContext) {
        this.data = data;
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;
        this.bundleContext = bundleContext;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	FileWriter reportWriter = null;

    	try {
    		File reportFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
    			"algorithm_metadata_report", TEXT_FILE_EXTENSION);

    		ServiceReference[] serviceReferences =
    			this.bundleContext.getAllServiceReferences(null, null);
    		Multimap<String, String> symbolicNamesToReports =
    			HashMultimap.<String, String>create();

    		for (ServiceReference serviceReference : serviceReferences) {
    			String symbolicName = serviceReference.getBundle().getSymbolicName();

    			if (!SERVICE_SYMBOLIC_NAMES_TO_IGNORE.contains(symbolicName)) {
    				String pluginString = createPluginString(serviceReference);
    				symbolicNamesToReports.put(symbolicName, pluginString);
    			}
    		}

    		reportWriter = new FileWriter(reportFile);

    		for (Iterator<String> keys = symbolicNamesToReports.keySet().iterator();
    				keys.hasNext();) {
    			String key = keys.next();

    			for (String value : symbolicNamesToReports.get(key)) {
    				reportWriter.append(value);
    			}
    		}
//
//    		// TODO Sort by symbolic name first.
//    		for (ServiceReference serviceReference : serviceReferences) {
//    			String pluginString = createPluginString(serviceReference);
//    			reportWriter.append(pluginString);
//    		}
//
    		reportWriter.close();

    		return formOutData(reportFile);
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
    	}
    }

    private static String createPluginString(ServiceReference serviceReference) {
		String header = createPluginHeader(serviceReference);
		String values = createPluginValuesString(serviceReference);

		return header + values + "\r\n";
    }

    private static Data[] formOutData(File reportFile) {
		Data postScriptData =
			new BasicData(reportFile, TEXT_MIME_TYPE);

		Dictionary postScriptMetaData = postScriptData.getMetadata();

		postScriptMetaData.put(DataProperty.LABEL, "Algorithm Metadata Report");
		postScriptMetaData.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
    	
        return new Data[] { postScriptData };
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