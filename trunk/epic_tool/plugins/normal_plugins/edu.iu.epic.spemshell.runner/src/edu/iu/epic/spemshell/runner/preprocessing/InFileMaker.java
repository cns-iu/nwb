package edu.iu.epic.spemshell.runner.preprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithm;

public class InFileMaker {
	public static final String DEFAULT_SUSCEPTIBLE_COMPARTMENT_ID = "S";
	public static final int DEFAULT_NUMBER_OF_SECONDARY_EVENTS = 0;
	
	public static final String IN_FILE_EXTENSION = "in";
	public static final String FILENAME = "simul." + IN_FILE_EXTENSION;
	
	public static final String IN_FILE_TEMPLATE_NAME = "inFile";	
	private static StringTemplateGroup inFileTemplateGroup =
		SPEMShellRunnerAlgorithm.loadTemplates(
				"/edu/iu/epic/spemshell/runner/preprocessing/inFile.st");
	
	private String modelFilePath;
	private Dictionary<String, Object> parameters;
	private Map<String, Object> compartmentPopulations;
	

	public InFileMaker(
			String modelFilePath,
			Dictionary<String, Object> parameters,
			Map<String, Object> compartmentPopulations) {
		this.modelFilePath = modelFilePath;
		this.parameters = parameters;
		this.compartmentPopulations = compartmentPopulations;
	}


	public File make() throws IOException {
		StringTemplate template =
			prepareTemplate(
					this.modelFilePath,
					this.parameters,
					this.compartmentPopulations);

//		// TODO For now, we will instead put such parameters in the mdl file.
//		for (Enumeration<String> parameterKeys = parameters.keys();
//				parameterKeys.hasMoreElements();) {
//			String key = parameterKeys.nextElement();
//			
//			if (key.startsWith(
//					SPEMShellRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX)) {
//				Object value = parameters.get(key);
//			
//				inFileTemplate.setAttribute("parameters",
//						new Parameter(key, value));
//			}
//		}
		
		/* TODO Switch to FileUtilities.createTemporaryFileInDefaultTemporaryDirectory
		 * when Bruno has modified SPEMShell to allow spaces in paths.
		 */
//		File inFile =
//			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
//					"SPEMShell", "in");
		File file =
			SPEMShellRunnerAlgorithm.createTempFileWithNoSpacesInPath(
					FILENAME);		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(template.toString());
		writer.close();		
		
		return file;
	}
	
	private StringTemplate prepareTemplate(
			String modelFilePath,
			Dictionary<String, Object> parameters,
			Map<String, Object> compartmentPopulations) {
		StringTemplate template =
			inFileTemplateGroup.getInstanceOf(IN_FILE_TEMPLATE_NAME);
		template.setAttribute("modelFileName", modelFilePath);
		template.setAttribute(
				"numberOfSecondaryEvents",
				DEFAULT_NUMBER_OF_SECONDARY_EVENTS);
		template.setAttribute("population", parameters.get("population"));
		template.setAttribute(
				"susceptibleCompartmentID",
				DEFAULT_SUSCEPTIBLE_COMPARTMENT_ID);
		template.setAttribute("numberOfDays", parameters.get("days"));
		template.setAttribute("seed", 0);
		
		for (Entry<String, Object> compartmentPopulation : compartmentPopulations.entrySet()) {
			template.setAttribute(
					"compartmentPopulations",
					new CompartmentPopulationFormatter(
							compartmentPopulation.getKey(),
							compartmentPopulation.getValue()));
		}
		
		return template;
	}
	
	
	private static class CompartmentPopulationFormatter {
		private String name;
		private Object initialPopulation;
		
		public CompartmentPopulationFormatter(
				String name, Object initialPopulation) {
			this.name = name;
			this.initialPopulation = initialPopulation;
		}
		
		@Override
		public String toString() {
			return "compartment " + this.name + " " + this.initialPopulation;
		}
	}
}
