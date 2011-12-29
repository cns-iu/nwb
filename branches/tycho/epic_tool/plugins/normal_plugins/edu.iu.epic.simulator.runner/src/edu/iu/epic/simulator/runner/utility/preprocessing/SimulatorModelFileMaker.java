package edu.iu.epic.simulator.runner.utility.preprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.FileUtilities;

import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.Transition;
import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithm;
import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithmFactory;
import edu.iu.epic.simulator.runner.utility.CIShellParameterUtilities;

// Converts from an EpiC-style model file to a simulator-style model file.
/* TODO If we decide to do away with compartment declarations permanently,
 * we should kill off the notion of a simulator model file (against an EpiC model file)
 * as no difference remains.  In that case, we can just use Model.toString.
 */
public class SimulatorModelFileMaker {
	public static final String FILENAME = "simul";
	public static final String FILE_EXTENSION = "mdl";	
	public static final String SIMULATOR_MODEL_FILE_TEMPLATE_NAME = "simulatorModelFile";
	public static final String PARAMETER_DEFINITIONS_ATTR = "parameterDefinitions";
	public static final String TRANSITIONS_ATTR = "transitions";
	private static StringTemplateGroup simulatorModelFileTemplateGroup =
		EpidemicSimulatorAlgorithm.loadTemplates(
				"/edu/iu/epic/simulator/runner/utility/preprocessing/simulatorModelFile.st");
	
	private Model epicModel;
	private Map<String, Object> modelParameterDefinitions;

	
	public SimulatorModelFileMaker(Model epicModel, Dictionary<String, Object> parameters) {
		this.epicModel = epicModel;
		
		this.modelParameterDefinitions = CIShellParameterUtilities.filterByAndStripIDPrefixes(
				parameters, EpidemicSimulatorAlgorithmFactory.MODEL_PARAMETER_PREFIX);
	}
	
	
	public File make() throws IOException {
		StringTemplate template =
			simulatorModelFileTemplateGroup.getInstanceOf(SIMULATOR_MODEL_FILE_TEMPLATE_NAME);

		List<Entry<String, ? extends Object>> parameterDefinitions =
			new ArrayList<Entry<String, ? extends Object>>();
		
		// Add all parameter definitions given by the input model file.
		//parameterDefinitions.addAll(epicModel.getParameterDefinitions().entrySet());
		
		
		for (Entry<String, String> parameterDefinition
				: epicModel.getParameterDefinitions().entrySet()) {
			parameterDefinition.setValue(fixRawDecimalPoints(parameterDefinition.getValue()));
			parameterDefinitions.add(parameterDefinition);
		}
		
		// Add all parameter definitions given by the algorithm user.
		parameterDefinitions.addAll(modelParameterDefinitions.entrySet());
		
		
		template.setAttribute(PARAMETER_DEFINITIONS_ATTR, parameterDefinitions);
		
		
		Set<Transition> transitions = epicModel.getTransitions();
		for (Transition transition : transitions) {
			transition.setRatio(fixRawDecimalPoints(transition.getRatio()));
		}		
		template.setAttribute(TRANSITIONS_ATTR, epicModel.getTransitions());

		
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