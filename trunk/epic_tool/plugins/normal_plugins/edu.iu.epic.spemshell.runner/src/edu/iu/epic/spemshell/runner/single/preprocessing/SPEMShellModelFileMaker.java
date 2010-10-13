package edu.iu.epic.spemshell.runner.single.preprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.FileUtilities;

import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.Transition;
import edu.iu.epic.spemshell.runner.single.CIShellParameterUtilities;
import edu.iu.epic.spemshell.runner.single.SPEMShellSingleRunnerAlgorithm;
import edu.iu.epic.spemshell.runner.single.SPEMShellSingleRunnerAlgorithmFactory;

// Converts from an EpiC-style model file to a SPEMShell-style model file.
/* TODO If we decide to do away with compartment declarations permanently,
 * we should kill off the notion of a SPEMShell model file (against an EpiC model file)
 * as no difference remains.  In that case, we can just use Model.toString.
 */
public class SPEMShellModelFileMaker {
	public static final String FILENAME = "simul";
	public static final String FILE_EXTENSION = "mdl";	
	
	private static StringTemplateGroup spemShellModelFileTemplateGroup =
		SPEMShellSingleRunnerAlgorithm.loadTemplates(
				"/edu/iu/epic/spemshell/runner/single/preprocessing/spemShellModelFile.st");
	
	private Model epicModel;
	private Map<String, Object> modelParameterDefinitions;

	
	public SPEMShellModelFileMaker(Model epicModel, Dictionary<String, Object> parameters) {
		this.epicModel = epicModel;
		
		this.modelParameterDefinitions = CIShellParameterUtilities.filterByAndStripIDPrefixes(
				parameters, SPEMShellSingleRunnerAlgorithmFactory.MODEL_PARAMETER_PREFIX);
	}
	
	
	public File make() throws IOException {
		StringTemplate template =
			spemShellModelFileTemplateGroup.getInstanceOf("spemShellModelFile");

		List<Entry<String, ? extends Object>> parameterDefinitions =
			new ArrayList<Entry<String, ? extends Object>>();
		
		// Add all parameter definitions given by the input model file.
		//parameterDefinitions.addAll(epicModel.getParameterDefinitions().entrySet());
		
		
		for (Entry<String, String> parameterDefinition : epicModel.getParameterDefinitions().entrySet()) {
			parameterDefinition.setValue(fixRawDecimalPoints(parameterDefinition.getValue()));
			parameterDefinitions.add(parameterDefinition);
		}
		
		// Add all parameter definitions given by the algorithm user.
		parameterDefinitions.addAll(modelParameterDefinitions.entrySet());
		
		
		template.setAttribute("parameterDefinitions", parameterDefinitions);
		
		
		Set<Transition> transitions = epicModel.getTransitions();
		for (Transition transition : transitions) {
			transition.setRatio(fixRawDecimalPoints(transition.getRatio()));
		}		
		template.setAttribute("transitions", epicModel.getTransitions());

		
		File file = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
				FILENAME, FILE_EXTENSION);
		FileWriter writer = new FileWriter(file);
		writer.write(template.toString());
		writer.close();

		return file;
	}

	private static String fixRawDecimalPoints(String parameterExpression) {
		return parameterExpression.replaceAll("([^0-9]|^)\\.", "$1\\0.");
	}
}