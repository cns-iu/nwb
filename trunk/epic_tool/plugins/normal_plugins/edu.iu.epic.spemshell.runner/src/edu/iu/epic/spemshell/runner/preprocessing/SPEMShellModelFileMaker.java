package edu.iu.epic.spemshell.runner.preprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.FileUtilities;

import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.spemshell.runner.CIShellParameterUtilities;
import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithm;
import edu.iu.epic.spemshell.runner.SPEMShellRunnerAlgorithmFactory;

// Converts from an EpiC-style model file to a SPEMShell-style model file.
/* TODO If we decide to do away with compartment declarations permanently,
 * we should kill off the notion of a SPEMShell model file (against an EpiC model file)
 * as no difference remains.  In that case, we can just use Model.toString.
 */
public class SPEMShellModelFileMaker {
	public static final String FILENAME = "simul";
	public static final String FILE_EXTENSION = "mdl";	
	
	private static StringTemplateGroup spemShellModelFileTemplateGroup = SPEMShellRunnerAlgorithm
			.loadTemplates("/edu/iu/epic/spemshell/runner/preprocessing/spemShellModelFile.st");
	
	private Model epicModel;
	private Map<String, Object> modelParameterDefinitions;

	
	public SPEMShellModelFileMaker(Model epicModel, Dictionary<String, Object> parameters) {
		this.epicModel = epicModel;
		
		this.modelParameterDefinitions = CIShellParameterUtilities.filterByAndStripIDPrefixes(
				parameters, SPEMShellRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX);
	}
	
	
	public File make() throws IOException {
		StringTemplate template =
			spemShellModelFileTemplateGroup.getInstanceOf("spemShellModelFile");

		// Add all parameter definitions given by the algorithm user.
		template.setAttribute(
				"parameterDefinitions",
				new ArrayList<Entry<String, Object>>(modelParameterDefinitions.entrySet()));
		
		// Add all parameter definitions given by the input model file.
		template.setAttribute(
				"parameterDefinitions",
				new ArrayList<Entry<String, String>>(
						epicModel.getParameterDefinitions().entrySet()));
		
		template.setAttribute("transitions", epicModel.getTransitions());

		File file = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
				FILENAME, FILE_EXTENSION);
		FileWriter writer = new FileWriter(file);
		writer.write(template.toString());
		writer.close();

		return file;
	}
}
