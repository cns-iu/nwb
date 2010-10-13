package edu.iu.epic.spemshell.runner.single.preprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.FileUtilities;

import edu.iu.epic.spemshell.runner.single.SPEMShellSingleRunnerAlgorithm;
import edu.iu.epic.spemshell.runner.single.SPEMShellSingleRunnerAlgorithmFactory;

public class InFileMaker {
	public static final int DEFAULT_NUMBER_OF_SECONDARY_EVENTS = 0;
	public static final String FILENAME = "simul";
	public static final String FILE_EXTENSION = "in";
	
	public static final String IN_FILE_TEMPLATE_NAME = "inFile";	
	private static StringTemplateGroup inFileTemplateGroup =
		SPEMShellSingleRunnerAlgorithm.loadTemplates(
				"/edu/iu/epic/spemshell/runner/single/preprocessing/inFile.st");
	
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


	public File make() throws IOException, ParseException {
		StringTemplate template = prepareTemplate();

		// TODO For now, we will instead put such parameters in the mdl file.
//		for (Enumeration<String> parameterKeys = parameters.keys();
//				parameterKeys.hasMoreElements();) {
//			String key = parameterKeys.nextElement();
//			
//			if (key.startsWith(
//					SPEMShellSingleRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX)) {
//				Object value = parameters.get(key);
//			
//				inFileTemplate.setAttribute("parameters",
//						new Parameter(key, value));
//			}
//		}
		
		File file =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					FILENAME, FILE_EXTENSION);	
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(template.toString());
		writer.close();		
		
		return file;
	}
	
	private StringTemplate prepareTemplate() throws ParseException, IOException {
		StringTemplate template =
			inFileTemplateGroup.getInstanceOf(IN_FILE_TEMPLATE_NAME);
		template.setAttribute("modelFileName", this.modelFilePath);
		template.setAttribute(
				"numberOfSecondaryEvents",
				DEFAULT_NUMBER_OF_SECONDARY_EVENTS);
		template.setAttribute("population",
				this.parameters.get(
						SPEMShellSingleRunnerAlgorithmFactory.POPULATION_ID));
		template.setAttribute(
				"susceptibleCompartmentID",
				this.susceptibleCompartmentID);
		template.setAttribute(
				"numberOfDays",
				this.parameters.get(
						SPEMShellSingleRunnerAlgorithmFactory.NUMBER_OF_DAYS_ID));
		
		String rawDateString =
			(String) this.parameters.get(
					SPEMShellSingleRunnerAlgorithmFactory.START_DATE_ID);
		DateFormat dateFormat =
			new SimpleDateFormat(SPEMShellSingleRunnerAlgorithmFactory.DATE_PATTERN);
		Date date = dateFormat.parse(rawDateString);
		String formattedDateString = dateFormat.format(date);
		template.setAttribute("date", formattedDateString);
		template.setAttribute("seed", this.parameters.get("seed"));
		
		/*for (Entry<String, Object> compartmentPopulation
				: this.infectionCompartmentPopulations.entrySet()) {
			template.setAttribute(
					"compartmentPopulations",
					new CompartmentPopulationFormatter(
							"infection",
							compartmentPopulation.getKey(),
							compartmentPopulation.getValue()));
			compartmentsString += compartmentPopulation.getKey() + compartmentsSeparator;
		}
		
		for (Entry<String, Object> compartmentPopulation
				: this.latentCompartmentPopulations.entrySet()) {
			template.setAttribute(
					"compartmentPopulations",
					new CompartmentPopulationFormatter(
							"latent",
							compartmentPopulation.getKey(),
							compartmentPopulation.getValue()));
			compartmentsString += compartmentPopulation.getKey() + compartmentsSeparator;
		}
		
		for (Entry<String, Object> compartmentPopulation
				: this.recoveredCompartmentPopulations.entrySet()) {
			template.setAttribute(
					"compartmentPopulations",
					new CompartmentPopulationFormatter(
							"recovered",
							compartmentPopulation.getKey(),
							compartmentPopulation.getValue()));
			compartmentsString += compartmentPopulation.getKey() + compartmentsSeparator;
		}*/
		
		template.setAttribute("outVal", createOutVal());
		template.setAttribute("initialFile", createInitialFile().getAbsolutePath());
		return template;
	}
	
	private  String createOutVal() {
		String compartmentsSeparator = ";";
		String compartmentsString = "";
		
		for (String compartmentName : this.infectionCompartmentPopulations.keySet()) {
			compartmentsString += compartmentName + compartmentsSeparator;
		}
		
		for (String compartmentName : this.latentCompartmentPopulations.keySet()) {
			compartmentsString += compartmentName + compartmentsSeparator;
		}
		
		for (String compartmentName : this.recoveredCompartmentPopulations.keySet()) {
			compartmentsString += compartmentName + compartmentsSeparator;
		}
		
		return compartmentsString;
	}
	
	private  File createInitialFile() throws IOException {
		InitialFileMaker initialFileMaker = new InitialFileMaker((Integer) parameters.get(
											SPEMShellSingleRunnerAlgorithmFactory.POPULATION_ID));
		initialFileMaker.addInitialCompartmentPopulations(infectionCompartmentPopulations);
		initialFileMaker.addInitialCompartmentPopulations(latentCompartmentPopulations);
		initialFileMaker.addInitialCompartmentPopulations(recoveredCompartmentPopulations);
		return initialFileMaker.make(); 
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
