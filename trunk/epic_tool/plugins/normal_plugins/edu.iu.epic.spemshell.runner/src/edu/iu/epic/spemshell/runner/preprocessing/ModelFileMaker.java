package edu.iu.epic.spemshell.runner.preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.iu.epic.spemshell.runner.CIShellParameterUtilities;
import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithm;
import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithmFactory;

/* Converts from an EpiC-style model file to a SPEMShell-style model file.
 * For now that means removing all compartment declarations
 * (like "susceptible S" or "infection I" for example) and defining any
 * model parameter values that are otherwise free.
 */
public class ModelFileMaker {
	public static final Set<String> COMPARTMENT_TYPES;
	static {
		Set<String> s = new HashSet<String>();
		s.add("susceptible");
		s.add("infection");
		s.add("latent");
		s.add("recovered");
		COMPARTMENT_TYPES = Collections.unmodifiableSet(s);
	}
	
	public static final String MODEL_FILE_EXTENSION = "mdl";
	public static final String COMMENT_MARKER = "#";
	
	private File epicModelFile;
	private Map<String, Object> modelParameterDefinitions;
	
		
	public ModelFileMaker(
			File epicModelFile,
			Dictionary<String, Object> parameters) {
		this.epicModelFile = epicModelFile;
		
		this.modelParameterDefinitions =
			CIShellParameterUtilities.filterByAndStripIDPrefixes(
					parameters,
					SPEMShellRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX);
	}


	public File make() throws IOException {
		File withoutCompartmentDeclarations =
			removeCompartmentDeclarations(this.epicModelFile);
		
		File withParameterDefinitions =
			insertParameterDefinitions(withoutCompartmentDeclarations);
		
		return withParameterDefinitions;
	}
	
	private File removeCompartmentDeclarations(File inFile) throws IOException {
		File outFile =
			SPEMShellRunnerAlgorithm.createTempFileWithNoSpacesInPath(
					"SPEMShellModelWithoutCompartmentDeclarations" + 
					"." + 
					MODEL_FILE_EXTENSION);
		BufferedWriter writer =	new BufferedWriter(new FileWriter(outFile));
		
		BufferedReader reader =
			new BufferedReader(new FileReader(inFile));
		
		String line = reader.readLine();
		do {
			if (!isACompartmentDeclaration(line)) {		
				writer.write(line + "\n");
			}

			line = reader.readLine();
		} while (line != null);

		reader.close();
		writer.close();
		
		return outFile;
	}
	
	private File insertParameterDefinitions(File inFile) throws IOException {
		File file =
			SPEMShellRunnerAlgorithm.createTempFileWithNoSpacesInPath(
					"SPEMShellModelWithParameterDefinitions" + 
					"." + 
					MODEL_FILE_EXTENSION);
		BufferedWriter writer =	new BufferedWriter(new FileWriter(file));
		
		BufferedReader reader =
			new BufferedReader(new FileReader(inFile));
		
		boolean haveWrittenParameterDefinitions = false;
		
		String line = reader.readLine();
		do {
			if ((!(haveWrittenParameterDefinitions))
					&& isASuitablePlaceForParameterDefinitions(line)) {
				writer.write(
						COMMENT_MARKER +
						"  Start: Model parameters defined by the EpiC user " +
						"at algorithm runtime" +
						"\n");
				
				for (Entry<String, Object> modelParameterDefinition
						: this.modelParameterDefinitions.entrySet()) {
					writer.write(
							modelParameterDefinition.getKey() + 
							" = " + 
							modelParameterDefinition.getValue() +
							"\n");
				}
				
				haveWrittenParameterDefinitions = true;
				
				writer.write(
						COMMENT_MARKER +
						" Finish: Model parameters defined by the EpiC user " +
						"at algorithm runtime" +
						"\n");
			}

			writer.write(line + "\n");
			
			line = reader.readLine();
		} while (line != null);

		reader.close();
		writer.close();
		
		return file;
	}
	
	/* We'll heuristically choose to place comment declarations just before
	 * the first line in the File where this is true. 
	 */
	private static boolean isASuitablePlaceForParameterDefinitions(
			String line) {
		return (!(isAComment(line)) && (!isEmpty(line)));
	}
	
	private static boolean isAComment(String line) {
		return line.startsWith(COMMENT_MARKER);
	}
	
	private static boolean isEmpty(String line) {
		return "".equals(line);
	}
	
	private static boolean isACompartmentDeclaration(String line) {
		String[] tokens = line.split(" ");
		String firstToken = tokens[0];
		return COMPARTMENT_TYPES.contains(firstToken);
	}
}
