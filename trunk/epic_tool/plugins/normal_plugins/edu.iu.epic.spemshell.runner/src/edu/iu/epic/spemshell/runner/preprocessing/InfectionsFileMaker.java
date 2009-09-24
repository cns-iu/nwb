package edu.iu.epic.spemshell.runner.preprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithm;
import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithmFactory;

public class InfectionsFileMaker {
	public static final String FILENAME = "infections.txt";
	
	private static StringTemplateGroup infectionsFileTemplateGroup =
		SPEMShellRunnerAlgorithm.loadTemplates(
				"/edu/iu/epic/spemshell/runner/preprocessing/infectionsFile.st");
	
	
	public File make(Dictionary<String, Object> parameters) throws IOException {		
		StringTemplate template =
			infectionsFileTemplateGroup.getInstanceOf("infectionsFile");
		/* IMPORTANT TODO: This has no smarts about whether the given compartment
		 * population is an infection.  Presumably only infection populations
		 * should be given here.
		 */
		for (Enumeration<String> parameterKeys = parameters.keys();
				parameterKeys.hasMoreElements();) {
			String key = parameterKeys.nextElement();
		
			if (key.startsWith(
					SPEMShellRunnerAlgorithmFactory.COMPARTMENT_POPULATION_PREFIX)) {
				Integer value = (Integer) parameters.get(key);
				
				String parameterName =
					key.replace(
							SPEMShellRunnerAlgorithmFactory.COMPARTMENT_POPULATION_PREFIX,
							"");
			
				template.setAttribute(
						"infectionCompartmentPopulations",
						new CompartmentPopulationFormatter(
								parameterName, value));
			}
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
