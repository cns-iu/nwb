package edu.iu.epic.spemshell.runner.preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;

import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithm;
import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithmFactory;

/* Converts from an EpiC-style model file to a SPEMShell-style model file.
 */
public class ModelFileMaker {
	public static final String MODEL_FILE_EXTENSION = "mdl";
	
	private File epicModelFile;
	private Dictionary<String, Object> parameters;
	
		
	public ModelFileMaker(
			File epicModelFile, Dictionary<String, Object> parameters) {
		this.epicModelFile = epicModelFile;
		this.parameters = parameters;
	}


	public File make() throws IOException {
		BufferedReader reader =
			new BufferedReader(new FileReader(epicModelFile));
		
		File file =
			SPEMShellRunnerAlgorithm.createTempFileWithNoSpacesInPath(
					"SPEMShell-ready_model." + MODEL_FILE_EXTENSION);
		BufferedWriter writer =
			new BufferedWriter(new FileWriter(file));
		
		String line = reader.readLine();
		do {
			boolean isACompartmentDeclaration =
				(line.startsWith("susceptible") || line.startsWith("infection"));
			if (!isACompartmentDeclaration) {
				if (line.startsWith("# Variable Definition")) {
					for (Enumeration<String> parameterKeys = parameters.keys();
							parameterKeys.hasMoreElements();) {
						String key = parameterKeys.nextElement();
						
						if (key.startsWith(
								SPEMShellRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX)) {
							Object value = parameters.get(key);
							
							String parameterName =
								key.replace(
										SPEMShellRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX,
										"");
						
							writer.write(parameterName + " = " + value + "\n");
						}
					}
				}
				
				writer.write(line + "\n");
			}

			line = reader.readLine();
		} while (line != null);

		writer.close();
		reader.close();	
		
		return file;
	}
}
