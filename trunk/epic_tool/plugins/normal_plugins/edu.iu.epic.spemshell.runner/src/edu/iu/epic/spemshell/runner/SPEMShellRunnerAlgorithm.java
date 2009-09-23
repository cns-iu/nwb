package edu.iu.epic.spemshell.runner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Dictionary;
import java.util.Enumeration;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

public class SPEMShellRunnerAlgorithm implements Algorithm {
	public static final String MODEL_FILE_EXTENSION = "mdl";
	public static final String IN_FILE_TEMPLATE_NAME = "inFile";
	public static final String DEFAULT_SUSCEPTIBLE_COMPARTMENT_ID = "S";
	public static final int DEFAULT_NUMBER_OF_SECONDARY_EVENTS = 0;
	public static final String DAT_FILE_FIRST_COLUMN_NAME = "time";
	public static final String DAT_FILE_COMMENT_MARKER = "#";
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String IN_FILE_MIME_TYPE = "file:text/in";
	public static final String SPEMSHELL_CORE_PID =
		"edu.iu.epic.spemshell.core";
	private static final String IN_FILE_EXTENSION = "in";
	
	public static StringTemplateGroup inFileTemplateGroup =
		loadTemplates("/edu/iu/epic/spemshell/runner/inFile.st");
	public static StringTemplateGroup infectionsFileTemplateGroup =
		loadTemplates("/edu/iu/epic/spemshell/runner/infectionsFile.st");
	private static StringTemplateGroup loadTemplates(String templatePath) {
		return new StringTemplateGroup(
				new InputStreamReader(
						SPEMShellRunnerAlgorithm.class.getResourceAsStream(
								templatePath)));
	}
	
	private Data[] data;
	private Dictionary<String, Object> parameters;
	private CIShellContext context;
	protected static LogService logger;


	public SPEMShellRunnerAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext context) {
		this.data = data;		
		this.parameters = parameters;
		this.context = context;
		
		SPEMShellRunnerAlgorithm.logger =
			(LogService) context.getService(LogService.class.getName());
	}

	
	public Data[] execute() throws AlgorithmExecutionException {		
		try {
			File modelFile = (File) data[0].getData();
			File cleanedModelFile =
				copyToSPEMShellStyleModelFile(modelFile, parameters);
			
			File inFile = createInFile(cleanedModelFile.getPath(), parameters);			
			File infectionsFile = createInfectionsFile();			
			Data[] spemData =
				new Data[]{
					new BasicData(inFile, IN_FILE_MIME_TYPE),
					new BasicData(infectionsFile, "file:text/plain") };		

			AlgorithmFactory spemShellCoreAlgorithmFactory =
	    		AlgorithmUtilities.getAlgorithmFactoryByPID(
	    			SPEMSHELL_CORE_PID,
	    			SPEMShellRunnerAlgorithmFactory.bundleContext);
	    	
	    	Algorithm spemShellCoreAlgorithm =
	    		spemShellCoreAlgorithmFactory.createAlgorithm(
	    				spemData, parameters, context);
	    	
	    	Data[] outData = spemShellCoreAlgorithm.execute();
	    	File outDatFile = (File) outData[0].getData();
	    	
	    	File csvFile = dumpDatFileToCSV(outDatFile);
	    	
	    	return createOutData(csvFile, "Simulation results", data[0]);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e); // TODO
		}
	}
	
	private File dumpDatFileToCSV(File datFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(datFile));
		
		File csvFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					"spemshell_output", "csv");
		BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
		
		String line = reader.readLine();
		do {
			boolean isColumnNamesLine =
				(line.startsWith(
						DAT_FILE_COMMENT_MARKER + " " + DAT_FILE_FIRST_COLUMN_NAME));
			if (isColumnNamesLine) {
				writer.write(commaSeparate(line.substring(line.indexOf(DAT_FILE_FIRST_COLUMN_NAME))) + "\n");
			} else if (line.startsWith(DAT_FILE_COMMENT_MARKER)) {
				// Skip other comment lines by doing nothing
			} else {
				writer.write(removeLastEntryFromCSVLine(commaSeparate(line)) + "\n");
			}

			line = reader.readLine();
		} while (line != null);

		writer.close();
		reader.close();	
		
		return csvFile;
	}

	/* TODO Obviously broken when there are few comma-separated entries in the
	 * given String.
	 */	
	private static String removeLastEntryFromCSVLine(String commaSeparated) {
		String[] tokens = commaSeparated.split(",");
		
		StringBuilder builder = new StringBuilder();
		
		for (int ii = 0; ii < tokens.length; ii++) {			
			boolean isFinalToken = (ii == tokens.length - 1);
			if (!isFinalToken) {
				builder.append(tokens[ii]);
				
				boolean isSecondToLastEntry = (ii == tokens.length - 2);
				if (!isSecondToLastEntry) {
					builder.append(",");
				}
			}
		}
		
		return builder.toString();
	}


	private String commaSeparate(String spaceSeparated) {
		String[] tokens = spaceSeparated.split(" +");
		
		StringBuilder builder = new StringBuilder();
		
		for (int ii = 0; ii < tokens.length; ii++) {
			builder.append(tokens[ii]);
			
			boolean isFinalToken = (ii == tokens.length - 1);
			if (!isFinalToken) {
				builder.append(",");
			}
		}
		
		return builder.toString();
	}
	
	private static File createTempFileWithNoSpacesInPath(String filename) {
		/* TODO As of September 23, SPEMShell can't handle paths containing
		 * spaces, so it would be dangerous to create Files in the user's
		 * default temporary file directory.  For now we hand-code paths that
		 * we know will not contain spaces. 
		 */		
		File file =
			new File("Z:\\jrbibers\\SPEMShell\\", filename);
		
		assert (!(file.getPath().contains(" ")));
		
		return file;
	}


	private File copyToSPEMShellStyleModelFile(File inModelFile, Dictionary<String, Object> parameters) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(inModelFile));
		
		File outModelFile =
			createTempFileWithNoSpacesInPath(
					"SPEMShell-ready_model." + MODEL_FILE_EXTENSION);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outModelFile));
		
		String line = reader.readLine();
		do {
			boolean isACompartmentDeclaration = (line.startsWith("susceptible") || line.startsWith("infection"));
			if (!isACompartmentDeclaration) {
				if (line.startsWith("# Variable Definition")) {
					for (Enumeration<String> parameterKeys = parameters.keys(); parameterKeys.hasMoreElements();) {
						String key = parameterKeys.nextElement();
						
						if (key.startsWith(SPEMShellRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX)) {
							Object value = parameters.get(key);
							
							String parameterName = key.replace(SPEMShellRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX, "");
						
							writer.write(parameterName + " = " + value + "\n");
						}
					}
				}
				
				writer.write(line + "\n");
			}

			line = reader.readLine();
		} while (line != null);

		writer.close();
		reader.close();	
		
		return outModelFile;
	}
	
	@SuppressWarnings("unchecked") // TODO
	private static Data[] createOutData(
			File outDatFile, String label, Data parentData) {
		Data outData = new BasicData(outDatFile, CSV_MIME_TYPE);
		Dictionary metadata = outData.getMetadata();
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		metadata.put(DataProperty.PARENT, parentData);
		
		return new Data[]{ outData };
	}

	private File createInFile(String modelFilePath, Dictionary<String, Object> parameters) throws IOException {		
		StringTemplate inFileTemplate =
			inFileTemplateGroup.getInstanceOf(IN_FILE_TEMPLATE_NAME);
		inFileTemplate.setAttribute("modelFileName", modelFilePath);
		inFileTemplate.setAttribute("numberOfSecondaryEvents", DEFAULT_NUMBER_OF_SECONDARY_EVENTS);
		inFileTemplate.setAttribute("population", parameters.get("population"));
		inFileTemplate.setAttribute("susceptibleCompartmentID", DEFAULT_SUSCEPTIBLE_COMPARTMENT_ID);
		
		for (Enumeration<String> parameterKeys = parameters.keys(); parameterKeys.hasMoreElements();) {
			String key = parameterKeys.nextElement();
			
			if (key.startsWith(SPEMShellRunnerAlgorithmFactory.COMPARTMENT_POPULATION_PREFIX)) {
				Object value = parameters.get(key);
			
				inFileTemplate.setAttribute("compartmentPopulations", new Compartment(key, value));
			}
		}
		
		inFileTemplate.setAttribute("numberOfDays", parameters.get("days"));
		inFileTemplate.setAttribute("seed", 0);
		
//		// TODO For now, we will instead put such parameters in the mdl file.
//		for (Enumeration<String> parameterKeys = parameters.keys(); parameterKeys.hasMoreElements();) {
//			String key = parameterKeys.nextElement();
//			
//			if (key.startsWith(SPEMShellRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX)) {
//				Object value = parameters.get(key);
//			
//				inFileTemplate.setAttribute("parameters", new Parameter(key, value));
//			}
//		}
		
		/* TODO Switch to FileUtilities.createTemporaryFileInDefaultTemporaryDirectory
		 * when Bruno has modified SPEMShell to allow spaces in paths.
		 */
//		File inFile =
//			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
//					"SPEMShell", "in");
		File inFile = createTempFileWithNoSpacesInPath("simul." + IN_FILE_EXTENSION);
		BufferedWriter writer = new BufferedWriter(new FileWriter(inFile));
		writer.write(inFileTemplate.toString());
		writer.close();		
		
		return inFile;
	}
	
	private File createInfectionsFile() throws IOException {		
		StringTemplate template =
			infectionsFileTemplateGroup.getInstanceOf("infectionsFile");
		template.setAttribute(
				"infectionCompartmentPopulations",
				new InfectionCompartmentPopulation("Ia", 7));
		template.setAttribute(
				"infectionCompartmentPopulations",
				new InfectionCompartmentPopulation("It", 3));
		
//		File inFile =
//			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
//					"infections", "txt");
		File file = createTempFileWithNoSpacesInPath("infections.txt");
		FileWriter writer = new FileWriter(file);
		writer.write(template.toString());
		writer.close();
		
		return file;
	}
	
	
	/* TODO Figure out the proper, StringTemplate-y way to do this.
	 */
	static class Parameter {
		private String name;
		private Object value;
		
		public Parameter(String name, Object value) {
			this.name = name;
			this.value = value;
		}
		
		public String toString() {
			return name + " = " + value;
		}
	}
	
	static class Compartment {
		private String name;
		private Object initialPopulation;
		
		public Compartment(String name, Object initialPopulation) {
			this.name = name;
			this.initialPopulation = initialPopulation;
		}
		
		public String toString() {
			return "compartment " + name + " " + initialPopulation;
		}
	}
	
	static class InfectionCompartmentPopulation {
		private String compartmentName;
		private int population;
		
		public InfectionCompartmentPopulation(String compartmentName, int population) {
			this.compartmentName = compartmentName;
			this.population = population;
		}
		
		public String toString() {
			return compartmentName + " " + population;
		}
	}
}
