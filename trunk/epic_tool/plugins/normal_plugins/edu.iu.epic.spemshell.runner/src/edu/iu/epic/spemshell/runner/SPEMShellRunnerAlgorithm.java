package edu.iu.epic.spemshell.runner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
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

public class SPEMShellRunnerAlgorithm implements Algorithm {
	public static final String IN_FILE_MIME_TYPE = "file:text/in";

	public static final String SPEMSHELL_CORE_PID =
		"edu.iu.epic.spemshell.core";
	
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
	private Dictionary parameters;
	private CIShellContext context;


	public SPEMShellRunnerAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		this.data = data;		
		this.parameters = parameters;
		this.context = context;
	}

	
	public Data[] execute() throws AlgorithmExecutionException {		
		try {
			File modelFile = (File) data[0].getData();
			System.out.println("Starting to clean model file");
			File cleanedModelFile =
				copyModelFileWithoutCompartmentDeclarations(modelFile);
			System.out.println("Finishing cleaning model file");
			
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
	    	
	    	return createOutData(outDatFile, "SPEMShell results (TODO: Good label)", data[0]);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e); // TODO
		}
	}
	
	private File copyModelFileWithoutCompartmentDeclarations(File modelFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(modelFile));
		
		File cleanedModelFile = new File("Z:\\jrbibers\\SPEMShell\\cleanedModelFile.mdl");
		BufferedWriter writer = new BufferedWriter(new FileWriter(cleanedModelFile));
		
		String line = reader.readLine();
		do {
			if (!(line.startsWith("susceptible") || line.startsWith("infection"))) {
				writer.write(line + "\n");
			}
			
			System.out.println("Advancing line");
			line = reader.readLine();
		} while (line != null);

		writer.close();
		reader.close();	
		
		return cleanedModelFile;
	}
	
	private static Data[] createOutData(
			File outDatFile, String label, Data parentData) {
		Data outData = new BasicData(outDatFile, "file:text/dat");
		outData.getMetadata().put(DataProperty.LABEL, label);
		outData.getMetadata().put(DataProperty.TYPE, DataProperty.OTHER_TYPE);
		outData.getMetadata().put(DataProperty.PARENT, parentData);
		
		return new Data[]{ outData };
	}

	private File createInFile(String modelFilePath, Dictionary parameters) throws IOException {		
		StringTemplate inFileTemplate =
			inFileTemplateGroup.getInstanceOf("inFile");
		inFileTemplate.setAttribute("modelFileName", modelFilePath);
		inFileTemplate.setAttribute("numberOfSecondaryEvents", 0);
		inFileTemplate.setAttribute("population", parameters.get("population"));
		inFileTemplate.setAttribute("susceptibleCompartmentID", "S");
		
		for (Enumeration<String> parameterKeys = parameters.keys(); parameterKeys.hasMoreElements();) {
			String key = parameterKeys.nextElement();
			
			if (key.startsWith(SPEMShellRunnerAlgorithmFactory.COMPARTMENT_POPULATION_PREFIX)) {
				Object value = parameters.get(key);
			
				inFileTemplate.setAttribute("compartmentPopulations", new Compartment(key, value));
			}
		}
		
		inFileTemplate.setAttribute("numberOfDays", parameters.get("days"));
		inFileTemplate.setAttribute("seed", 0);
		
		// TODO For now, put such parameters in the mdl file.
		for (Enumeration<String> parameterKeys = parameters.keys(); parameterKeys.hasMoreElements();) {
			String key = parameterKeys.nextElement();
			
			if (key.startsWith(SPEMShellRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX)) {
				Object value = parameters.get(key);
			
				inFileTemplate.setAttribute("parameters", new Parameter(key, value));
			}
		}
		
		/* TODO Switch to FileUtilities.createTemporaryFileInDefaultTemporaryDirectory
		 * when Bruno has modified SPEMShell to allow spaces in paths.
		 */
//		File inFile =
//			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
//					"SPEMShell", "in");
		File inFile = new File("Z:\\jrbibers\\SPEMShell\\simul.in");
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
		
		System.out.println(template.toString());
		
//		File inFile =
//			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
//					"SPEMShell", "in");
		File file = new File("Z:\\jrbibers\\SPEMShell\\infections.txt");
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
