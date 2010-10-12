package edu.iu.epic.spemshell.runner.single;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.spemshell.runner.single.postprocessing.DatToCsv;
import edu.iu.epic.spemshell.runner.single.preprocessing.InFileMaker;
import edu.iu.epic.spemshell.runner.single.preprocessing.InfectionsFileMaker;
import edu.iu.epic.spemshell.runner.single.preprocessing.SimulatorModelFileMaker;

public class SPEMShellSingleRunnerAlgorithm implements Algorithm {	
	public static final String PLAIN_TEXT_MIME_TYPE = "file:text/plain";
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String IN_FILE_MIME_TYPE = "file:text/in";

	public static final String SPEMSHELL_CORE_PID = "edu.iu.epic.spemshell.core";
	
	private Data[] data;
	private Dictionary<String, Object> parameters;
	private CIShellContext ciContext;
	private BundleContext bundleContext;
	private static LogService logger;


	public SPEMShellSingleRunnerAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext ciContext,
			BundleContext bundleContext) {
		this.data = data;		
		this.parameters = parameters;
		this.ciContext = ciContext;
		this.bundleContext = bundleContext;
		
		SPEMShellSingleRunnerAlgorithm.logger =
			(LogService) ciContext.getService(LogService.class.getName());
	}

	// Run simulator and interpret its output.
	public Data[] execute() throws AlgorithmExecutionException {		
		File datFile;
		try {
			Data[] simulatorInputData =
				createSimulatorInputData(this.data, this.parameters);
			datFile = executeSimulator(simulatorInputData, ciContext, bundleContext);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error creating data for SPEMShell: " + e.getMessage(), e);
		} catch (ParseException e) {
			throw new AlgorithmExecutionException(
					"Error parsing the given start date: " + e.getMessage(), e);
		}
		
		try {
			DatToCsv datToCSV = new DatToCsv(datFile);
			File csvFile = datToCSV.convert();
	    	
	    	return createOutData(csvFile, "Simulation results", this.data[0]);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error translating from .dat to .csv:" + e.getMessage(), e);
		}
	}

	private File executeSimulator(
			Data[] coreData, CIShellContext coreCIContext, BundleContext coreBundleContext)
				throws AlgorithmExecutionException {
		try {			
			Data[] simulatorOutData =
				AlgorithmUtilities.executeAlgorithm(
					AlgorithmUtilities.getAlgorithmFactoryByPID(
							SPEMSHELL_CORE_PID, coreBundleContext),
					null,
					coreData,
					new Hashtable<String, Object>(),
					coreCIContext);

			return (File) simulatorOutData[0].getData();
		} catch (AlgorithmExecutionException e) {
			throw new AlgorithmExecutionException(
				"Error running simulator: " + e.getMessage(), e);
		}
	}

	private Data[] createSimulatorInputData(
			Data[] runnerData, Dictionary<String, Object> runnerParameters)
				throws IOException, ParseException {
		/* Fetch the compartment initial populations from the algorithm parameters. */
		Map<String, Object> infectedCompartmentPopulations =
			CIShellParameterUtilities.filterByAndStripIDPrefixes(
					runnerParameters,
					SPEMShellSingleRunnerAlgorithmFactory.COMPARTMENT_POPULATION_PREFIX
					+ SPEMShellSingleRunnerAlgorithmFactory.INFECTION_PREFIX);
		
		Map<String, Object> latentCompartmentPopulations =
			CIShellParameterUtilities.filterByAndStripIDPrefixes(
					runnerParameters,
					SPEMShellSingleRunnerAlgorithmFactory.COMPARTMENT_POPULATION_PREFIX
					+ SPEMShellSingleRunnerAlgorithmFactory.LATENT_PREFIX);
		
		Map<String, Object> recoveredCompartmentPopulations =
			CIShellParameterUtilities.filterByAndStripIDPrefixes(
					runnerParameters,
					SPEMShellSingleRunnerAlgorithmFactory.COMPARTMENT_POPULATION_PREFIX
					+ SPEMShellSingleRunnerAlgorithmFactory.RECOVERED_PREFIX);
		
		
		// Create the simulator model file from the EpiC model file.
		Model epicModel = (Model) runnerData[0].getData();
		SimulatorModelFileMaker simulatorModelFileMaker =
			new SimulatorModelFileMaker(epicModel, runnerParameters);		
		File spemShellModelFile = simulatorModelFileMaker.make();		

		
		// Create the .in file.
		String initialCompartmentName =
			(String) runnerParameters.get(
					SPEMShellSingleRunnerAlgorithmFactory.INITIAL_COMPARTMENT_PARAMETER_ID);
		InFileMaker inFileMaker =
			new InFileMaker(
					spemShellModelFile.getPath(),
					runnerParameters,
					initialCompartmentName,
					infectedCompartmentPopulations,
					latentCompartmentPopulations,
					recoveredCompartmentPopulations);
		File inFile = inFileMaker.make();
		
		
		// Create the infections file.
		InfectionsFileMaker infectionsFileMaker = new InfectionsFileMaker();
		File infectionsFile =
			infectionsFileMaker.make(infectedCompartmentPopulations);
		

		return new Data[] {
				new BasicData(inFile, IN_FILE_MIME_TYPE),
				new BasicData(infectionsFile, PLAIN_TEXT_MIME_TYPE) };
	}
	
	private static Data[] createOutData(
			File outDatFile, String label, Data parentData) {
		Data outData = new BasicData(outDatFile, CSV_MIME_TYPE);
		Dictionary<String, Object> metadata = outData.getMetadata();
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		metadata.put(DataProperty.PARENT, parentData);
		
		return new Data[]{ outData };
	}

	public static StringTemplateGroup loadTemplates(String templatePath) {
		return new StringTemplateGroup(
				new InputStreamReader(
						SPEMShellSingleRunnerAlgorithm.class.getResourceAsStream(
								templatePath)));
	}

	public static LogService getLogger() {
		return logger;
	}
}
