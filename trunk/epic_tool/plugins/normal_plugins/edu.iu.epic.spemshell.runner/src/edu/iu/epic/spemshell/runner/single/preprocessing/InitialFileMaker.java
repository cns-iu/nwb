package edu.iu.epic.spemshell.runner.single.preprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.FileUtilities;

import edu.iu.epic.spemshell.runner.single.SPEMShellSingleRunnerAlgorithm;

public class InitialFileMaker {
	public static final String FILENAME = "initial";
	public static final String FILE_EXTENSION = "txt";
	private float totalPopulation;
	private List<CompartmentPopulationFormatter> compartmentPopulationFormatterList;
	
	private static StringTemplateGroup initialFileTemplateGroup =
		SPEMShellSingleRunnerAlgorithm.loadTemplates(
				"/edu/iu/epic/spemshell/runner/single/preprocessing/initialFile.st");
	
	public InitialFileMaker(Integer totalPopulation) {
		this.totalPopulation = totalPopulation.floatValue();
		compartmentPopulationFormatterList = new ArrayList<CompartmentPopulationFormatter>();
	}
	
	public File make() throws IOException {		
		StringTemplate template = initialFileTemplateGroup.getInstanceOf("initialFile");

		for (CompartmentPopulationFormatter compartmentPopulationFormatter
				: compartmentPopulationFormatterList) {
			template.setAttribute("compartmentPopulations", compartmentPopulationFormatter);
			System.out.println("Initial Compartments: " + compartmentPopulationFormatter);
		}
		
		File file = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					FILENAME, FILE_EXTENSION);
		
		FileWriter writer = new FileWriter(file);
		writer.write(template.toString());
		writer.close();
		
		return file;
	}
	
	public void addInitialCompartmentPopulations(Map<String, Object> compartmentPopulations) {
		for (Entry<String, Object> compartmentPopulation : compartmentPopulations.entrySet()) {
			compartmentPopulationFormatterList.add(new CompartmentPopulationFormatter(
					compartmentPopulation.getKey(),
					((Integer) compartmentPopulation.getValue()).floatValue() / totalPopulation));
		}
	}
	
	private static class CompartmentPopulationFormatter {
		private String compartmentName;
		private Object population;
		
		public CompartmentPopulationFormatter(
				String compartmentName, Object population) {
			this.compartmentName = compartmentName;
			this.population = population;
		}
		
		@Override
		public String toString() {
			return this.compartmentName + " " + this.population;
		}
	}
}
