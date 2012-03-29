package edu.iu.epic.simulator.runner.utility.preprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.FileUtilities;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithm;

public class InfectionsFileMaker {
	public static final String FILENAME = "infections";
	public static final String FILE_EXTENSION = "txt";
	
	private static StringTemplateGroup infectionsFileTemplateGroup =
		EpidemicSimulatorAlgorithm.loadTemplates(
				"/edu/iu/epic/simulator/runner/utility/preprocessing/infectionsFile.st");
	
	
	public File make(Map<String, Integer> infectionCompartmentPopulations)
			throws IOException {		
		StringTemplate template =
			infectionsFileTemplateGroup.getInstanceOf("infectionsFile");

		for (Entry<String, Integer> compartmentPopulation
				: infectionCompartmentPopulations.entrySet()) {
			template.setAttribute(
					"compartmentPopulations",
					new CompartmentPopulationFormatter(
							compartmentPopulation.getKey(),
							compartmentPopulation.getValue()));
		}
		
		File file =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					FILENAME, FILE_EXTENSION);
		FileWriter writer = new FileWriter(file);
		writer.write(template.toString());
		writer.close();
		
		return file;
	}
	
	
	private static class CompartmentPopulationFormatter {
		private String compartmentName;
		private Object population;
		
		public CompartmentPopulationFormatter(
				String compartmentName, Integer population) {
			this.compartmentName = compartmentName;
			this.population = population;
		}
		
		@Override
		public String toString() {
			return this.compartmentName + " " + this.population;
		}
	}
}
