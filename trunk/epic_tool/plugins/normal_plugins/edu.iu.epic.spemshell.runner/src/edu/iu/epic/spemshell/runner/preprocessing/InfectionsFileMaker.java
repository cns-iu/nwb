package edu.iu.epic.spemshell.runner.preprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithm;

public class InfectionsFileMaker {
	public static final String FILENAME = "infections.txt";
	
	private static StringTemplateGroup infectionsFileTemplateGroup =
		SPEMShellRunnerAlgorithm.loadTemplates(
				"/edu/iu/epic/spemshell/runner/preprocessing/infectionsFile.st");
	
	
	public File make(Map<String, Object> infectionCompartmentPopulations)
			throws IOException {		
		StringTemplate template =
			infectionsFileTemplateGroup.getInstanceOf("infectionsFile");

		for (Entry<String, Object> compartmentPopulation
				: infectionCompartmentPopulations.entrySet()) {
			template.setAttribute(
					"compartmentPopulations",
					new CompartmentPopulationFormatter(
							compartmentPopulation.getKey(),
							compartmentPopulation.getValue()));
		}
		
//		File inFile =
//			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
//					"infections", "txt");
		File file =
			SPEMShellRunnerAlgorithm.createTempFileWithNoSpacesInPath(
					FILENAME);
		FileWriter writer = new FileWriter(file);
		writer.write(template.toString());
		writer.close();
		
		return file;
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
