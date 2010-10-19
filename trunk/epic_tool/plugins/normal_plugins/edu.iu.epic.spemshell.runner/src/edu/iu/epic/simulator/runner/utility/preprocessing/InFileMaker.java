package edu.iu.epic.simulator.runner.utility.preprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.FileUtilities;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithm;
import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithmFactory;

public class InFileMaker {
	public static final int DEFAULT_NUMBER_OF_SECONDARY_EVENTS = 0;
	public static final String FILENAME = "simul";
	public static final String FILE_EXTENSION = "in";
	
	public static final String IN_FILE_TEMPLATE_NAME = "inFile";	
	private static StringTemplateGroup inFileTemplateGroup =
		EpidemicSimulatorAlgorithm.loadTemplates(
				"/edu/iu/epic/simulator/runner/utility/preprocessing/inFile.st");
	
	private String modelFilePath;
	private Dictionary<String, Object> parameters;
	private String susceptibleCompartmentID;
	private File infectionsFile;
	private Map<String, Float> initialDistribution;
	
	
	public InFileMaker(
			String modelFilePath,
			Dictionary<String, Object> parameters,
			String susceptibleCompartmentID,
			File infectionsFile,
			Map<String, Float> initialDistribution) {
		this.modelFilePath = modelFilePath;
		this.parameters = parameters;
		this.susceptibleCompartmentID = susceptibleCompartmentID;
		this.infectionsFile = infectionsFile;
		this.initialDistribution = initialDistribution;
	}


	public File make() throws IOException, ParseException {
		StringTemplate template = prepareTemplate();
		
		File file =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					FILENAME, FILE_EXTENSION);	
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(template.toString());
		writer.close();		
		
		return file;
	}
	
	public static <T extends Number> float total(Collection<T> numbers) {
		float total = 0;
		
		for (T number : numbers) {			
			total += number.floatValue();
		}
		
		return total;
	}
	
	private StringTemplate prepareTemplate() throws ParseException, IOException {
		StringTemplate template =
			inFileTemplateGroup.getInstanceOf(IN_FILE_TEMPLATE_NAME);
		template.setAttribute("modelFileName", this.modelFilePath);
		template.setAttribute(
				"numberOfSecondaryEvents",
				DEFAULT_NUMBER_OF_SECONDARY_EVENTS);
		template.setAttribute("population",
				this.parameters.get(EpidemicSimulatorAlgorithmFactory.POPULATION_ID));
		template.setAttribute(
				"susceptibleCompartmentID",
				this.susceptibleCompartmentID);
		template.setAttribute(
				"numberOfDays",
				this.parameters.get(EpidemicSimulatorAlgorithmFactory.NUMBER_OF_DAYS_ID));

		String rawDateString =
			(String) this.parameters.get(
					EpidemicSimulatorAlgorithmFactory.START_DATE_ID);
		DateFormat dateFormat =
			new SimpleDateFormat(EpidemicSimulatorAlgorithmFactory.DATE_PATTERN);
		Date date = dateFormat.parse(rawDateString);
		String formattedDateString = dateFormat.format(date);
		template.setAttribute("date", formattedDateString);
		template.setAttribute("seed", this.parameters.get("seed"));
		
		template.setAttribute("outVal", createOutVal());
		template.setAttribute("initialFile", createInitialFile().getPath());
		template.setAttribute("infectionsFile", infectionsFile.getPath());
		
		return template;
	}
	
	private String createOutVal() {
		String compartmentsSeparator = ";";
		String compartmentsString = "";
		
		for (String compartmentName : this.initialDistribution.keySet()) {
			compartmentsString += compartmentName + compartmentsSeparator;
		}
		
		return compartmentsString;
	}
	
	private File createInitialFile() throws IOException {
		InitialFileMaker initialFileMaker =	new InitialFileMaker(initialDistribution);
		return initialFileMaker.make(); 
	}
}
