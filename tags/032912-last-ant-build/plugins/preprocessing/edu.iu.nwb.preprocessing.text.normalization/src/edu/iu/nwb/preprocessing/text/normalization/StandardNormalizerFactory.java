package edu.iu.nwb.preprocessing.text.normalization;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Schema;
import prefuse.data.Table;


public class StandardNormalizerFactory implements AlgorithmFactory, ParameterMutator {
	public static final String SEPARATOR_KEY = "separator";
	public static final String STOP_WORD_LIST_FILE = "stopWordListFile";

    public static final String PREFIX = "column_";
    public static final String DEFAULT_STOP_WORD_LIST_FILE_NAME = "stopwords.txt";
    public static final String DEFAULT_STOP_WORD_LIST_FILE_PATH = 
		"/edu/iu/nwb/preprocessing/text/normalization/" + DEFAULT_STOP_WORD_LIST_FILE_NAME;
    
    private BundleContext bundleContext;
    private boolean usingDefaultStopWords = false;
  
    protected void activate(ComponentContext componentContext) {
    	this.bundleContext = componentContext.getBundleContext();
    }
    
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		Data inputData = data[0];
		Table inputTable = (Table) inputData.getData();
		LogService logger = (LogService) ciShellContext.getService(LogService.class.getName());
		String separator = (String) parameters.get(SEPARATOR_KEY);
		String stopWordListFilePath = (String) parameters.get(STOP_WORD_LIST_FILE);
		Set<String> columnsToNormalize = determineColumnsToNormalize(parameters);
    	String[] stopWords = getStopWords(stopWordListFilePath);

		return new StandardNormalizer(
			inputData,
			inputTable,
			logger,
			separator,
			columnsToNormalize,
			stopWords,
			this.usingDefaultStopWords);
	}

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		Table inputTable = (Table) data[0].getData();
		String[] columnNames = createKeyArray(inputTable.getSchema());
		
		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);
		AttributeDefinition[] attributeDefinitions =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for (AttributeDefinition attributeDefinition : attributeDefinitions) {
			String id = attributeDefinition.getID();

			if (STOP_WORD_LIST_FILE.equals(id)) {
				String name = attributeDefinition.getName();
				String description = attributeDefinition.getDescription();
				int type = attributeDefinition.getType();
				String defaultValue =
					"file:" +
					getDefaultStopWordListFileLocation(attributeDefinition.getDefaultValue()[0]);
				AttributeDefinition newAttributeDefinition = new BasicAttributeDefinition(
					id, name, description, type, defaultValue) {
					@Override
					public String validate(String value) {
						/* TODO: If we ever want to read the chosen file to make sure it's of a
						 * valid format.
						 */
						return super.validate(value);
					}
				};

				newParameters.addAttributeDefinition(
					ObjectClassDefinition.OPTIONAL, newAttributeDefinition);
			}
		}

		for (AttributeDefinition attributeDefinition : attributeDefinitions) {
			String id = attributeDefinition.getID();

			if (!STOP_WORD_LIST_FILE.equals(id)) {
				newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, attributeDefinition);
			}
		}

		addBooleanOptions(oldParameters, newParameters, columnNames, PREFIX);

		return newParameters;
	}

	private Set<String> determineColumnsToNormalize(Dictionary<String, Object> parameters) {
		Set<String> columns = new TreeSet<String>();
        Enumeration<String> keys = parameters.keys();

        while (keys.hasMoreElements()) {
        	String key = keys.nextElement();
        	Boolean truth = new Boolean(true);

        	if (key.startsWith(StandardNormalizerFactory.PREFIX) &&
        			truth.equals(parameters.get(key))) {
        		String column = key.substring(StandardNormalizerFactory.PREFIX.length());
        		columns.add(column);
        	}
        }

		return columns;
	}

	private String[] getStopWords(String stopWordListFilePath)
			throws AlgorithmCreationFailedException {
    	InputStream inStream = null;
    	BufferedReader input = null;
    	Collection<String> stopWordsSoFar = new ArrayList<String>();

    	try {
    		inStream = determineStopWordListFilePath(stopWordListFilePath);
            input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
            String line;
    		    		
    	    while (null != (line = input.readLine())) {
    	         stopWordsSoFar.add(line);
    	    }
    	} catch (Exception e) {
    		throw new AlgorithmCreationFailedException(e.getMessage(), e);
    	} finally {
    		try {
    			if (input != null) {
    				input.close();
    			}

    	        if (inStream != null) {
    	        	inStream.close();
    	        }
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	}

    	int index = 0;
	    for (String stopWord : stopWordsSoFar) {
    		System.err.println(String.format(
    			">>Debug: index = %d, value = %s", index, stopWord));
    		index++;
    	}

    	return stopWordsSoFar.toArray(new String[0]);
    }

	private String[] createKeyArray(Schema schema) {
		List<String> keys = new ArrayList<String>();

		for (int ii = 0; ii < schema.getColumnCount(); ii++) {
			if (schema.getColumnType(ii).equals(String.class)) {
				keys.add(schema.getColumnName(ii));
			}
		}

		return (String[]) keys.toArray(new String[]{});
	}

	private String getDefaultStopWordListFileLocation(String defaultValue) {
		try {
			URL stopWordListFileURL = new URL(new URL(
				System.getProperty("osgi.configuration.area")), DEFAULT_STOP_WORD_LIST_FILE_NAME);
			String filePath = stopWordListFileURL.getFile();

			// TODO: Hack?
			if (filePath.startsWith("\\") || filePath.startsWith("/")) {
				filePath = filePath.substring(1);
			}

			return filePath;
		} catch (MalformedURLException e) {
			return defaultValue;
		}
	}

	private void addBooleanOptions(
			ObjectClassDefinition oldParameters,
			BasicObjectClassDefinition newParameters,
			String[] columnNames,
			String prefix) {
		for (String columnName : columnNames) {
			String id = prefix + columnName;
			String name = columnName;
			String description = String.format("Normalize column %s?", columnName);
			int type = AttributeDefinition.BOOLEAN;
			newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition(id, name, description, type));
		}
	}

	/** Side-effects usingDefaultStopWords.
	 */
	private InputStream determineStopWordListFilePath(String stopWordListFilePath)
			throws IOException {
		try {
			File stopWordListFile = new File(stopWordListFilePath);

			if (stopWordListFile.exists() &&
					stopWordListFile.canRead() &&
					stopWordListFile.isFile()) {
				return new BufferedInputStream(new FileInputStream(stopWordListFile));
			}
		} catch (Exception e) {}

		this.usingDefaultStopWords = true;
		URL filePathURL =
			this.bundleContext.getBundle().getResource(DEFAULT_STOP_WORD_LIST_FILE_PATH);
		URLConnection connection = filePathURL.openConnection();
        connection.setDoInput(true);

        return connection.getInputStream();
	}
}