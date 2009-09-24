package edu.iu.epic.spemshell.runner.preprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithm;

public class InfectionsFileMaker {
	public static final String FILENAME = "infections.txt";
	
	private static StringTemplateGroup infectionsFileTemplateGroup =
		SPEMShellRunnerAlgorithm.loadTemplates(
				"/edu/iu/epic/spemshell/runner/preprocessing/infectionsFile.st");
	
	
	public File make() throws IOException {		
		StringTemplate template =
			infectionsFileTemplateGroup.getInstanceOf("infectionsFile");
		// TODO Set non-dummy values here
		template.setAttribute(
				"infectionCompartmentPopulations",
				new CompartmentPopulationFormatter("Ia", 7));
		template.setAttribute(
				"infectionCompartmentPopulations",
				new CompartmentPopulationFormatter("It", 3));
		
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
		private int population;
		
		public CompartmentPopulationFormatter(
				String compartmentName, int population) {
			this.compartmentName = compartmentName;
			this.population = population;
		}
		
		public String toString() {
			return compartmentName + " " + population;
		}
	}
}
