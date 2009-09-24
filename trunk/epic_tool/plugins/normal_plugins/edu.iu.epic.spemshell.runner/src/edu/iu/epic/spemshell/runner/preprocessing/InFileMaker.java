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
	public static final int DEFAULT_NUMBER_OF_SECONDARY_EVENTS = 0;
	
	public static final String IN_FILE_EXTENSION = "in";
	public static final String FILENAME = "simul." + IN_FILE_EXTENSION;
	
	public static final String IN_FILE_TEMPLATE_NAME = "inFile";	
	private static StringTemplateGroup inFileTemplateGroup =
		SPEMShellRunnerAlgorithm.loadTemplates(
				"/edu/iu/epic/spemshell/runner/preprocessing/inFile.st");
	
	private String modelFilePath;
	private Dictionary<String, Object> parameters;
	private String susceptibleCompartmentID;
	private Map<String, Object> infectionCompartmentPopulations;
	private Map<String, Object> latentCompartmentPopulations;
	private Map<String, Object> recoveredCompartmentPopulations;
	
	
	public InFileMaker(
			String modelFilePath,
			Dictionary<String, Object> parameters,
			String susceptibleCompartmentID,
			Map<String, Object> infectionCompartmentPopulations,
			Map<String, Object> latentCompartmentPopulations,
			Map<String, Object> recoveredCompartmentPopulations) {
		this.modelFilePath = modelFilePath;
		this.parameters = parameters;
		this.susceptibleCompartmentID = susceptibleCompartmentID;
		this.infectionCompartmentPopulations = infectionCompartmentPopulations;
		this.latentCompartmentPopulations = latentCompartmentPopulations;
		this.recoveredCompartmentPopulations = recoveredCompartmentPopulations;
	}


	public File make() throws IOException {
		StringTemplate template = prepareTemplate();

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
	
	private StringTemplate prepareTemplate() {
		StringTemplate template =
			inFileTemplateGroup.getInstanceOf(IN_FILE_TEMPLATE_NAME);
		template.setAttribute("modelFileName", this.modelFilePath);
		template.setAttribute(
				"numberOfSecondaryEvents",
				DEFAULT_NUMBER_OF_SECONDARY_EVENTS);
		template.setAttribute("population", this.parameters.get("population"));
		template.setAttribute(
				"susceptibleCompartmentID",
				this.susceptibleCompartmentID);
		template.setAttribute("numberOfDays", this.parameters.get("days"));
		template.setAttribute("seed", 0);
		
		for (Entry<String, Object> compartmentPopulation
				: this.infectionCompartmentPopulations.entrySet()) {
			template.setAttribute(
					"compartmentPopulations",
					new CompartmentPopulationFormatter(
							"infection",
							compartmentPopulation.getKey(),
							compartmentPopulation.getValue()));
		}
		
		for (Entry<String, Object> compartmentPopulation
				: this.latentCompartmentPopulations.entrySet()) {
			template.setAttribute(
					"compartmentPopulations",
					new CompartmentPopulationFormatter(
							"latent",
							compartmentPopulation.getKey(),
							compartmentPopulation.getValue()));
		}
		
		for (Entry<String, Object> compartmentPopulation
				: this.recoveredCompartmentPopulations.entrySet()) {
			template.setAttribute(
					"compartmentPopulations",
					new CompartmentPopulationFormatter(
							"recovered",
							compartmentPopulation.getKey(),
							compartmentPopulation.getValue()));
		}
		
		return template;
	}
	
	
	private static class CompartmentPopulationFormatter {
		private String type;
		private String name;
		private Object initialPopulation;
		
		public CompartmentPopulationFormatter(
				String type, String name, Object initialPopulation) {
			this.type = type;
			this.name = name;
			this.initialPopulation = initialPopulation;
		}
		
		@Override
		public String toString() {
			return this.type + " " + this.name + " " + this.initialPopulation;
		}
	}
}
