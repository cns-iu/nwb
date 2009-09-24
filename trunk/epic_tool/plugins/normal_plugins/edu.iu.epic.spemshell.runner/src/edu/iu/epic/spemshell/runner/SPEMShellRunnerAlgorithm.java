package edu.iu.epic.spemshell.runner;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Dictionary;
import java.util.Hashtable;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.service.log.LogService;

import edu.iu.epic.spemshell.runner.postprocessing.DatToCsv;
import edu.iu.epic.spemshell.runner.preprocessing.InFileMaker;
import edu.iu.epic.spemshell.runner.preprocessing.InfectionsFileMaker;
import edu.iu.epic.spemshell.runner.preprocessing.ModelFileMaker;

public class SPEMShellRunnerAlgorithm implements Algorithm {	
	public static final String PLAIN_TEXT_MIME_TYPE = "file:text/plain";
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String IN_FILE_MIME_TYPE = "file:text/in";

	public static final String SPEMSHELL_CORE_PID =
		"edu.iu.epic.spemshell.core";
	
	private Data[] data;
	private Dictionary<String, Object> parameters;
	private CIShellContext context;
	protected static LogService logger;


	public SPEMShellRunnerAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext context) {
		this.data = data;		
		this.parameters = parameters;
		this.context = context;
		
		SPEMShellRunnerAlgorithm.logger =
			(LogService) context.getService(LogService.class.getName());
	}

	
	public Data[] execute() throws AlgorithmExecutionException {		
		File datFile;
		try {
			Data[] spemShellData = createSPEMShellInData(data, parameters);
			datFile = executeSPEMShell(spemShellData, context);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error creating data for SPEMShell: " + e.getMessage(),
					e);
		}
		
		try {
			DatToCsv datToCSV = new DatToCsv(datFile);
			File csvFile = datToCSV.convert();
	    	
	    	return createOutData(csvFile, "Simulation results", data[0]);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error translating from .dat to .csv:" + e.getMessage(),
					e);
		}
	}

	private File executeSPEMShell(Data[] data, CIShellContext context)
			throws AlgorithmExecutionException {
		try {			
			AlgorithmFactory spemShellCoreAlgorithmFactory =
				AlgorithmUtilities.getAlgorithmFactoryByPID(
					SPEMSHELL_CORE_PID,
					SPEMShellRunnerAlgorithmFactory.getBundleContext());
			
			Dictionary<Object, Object> emptyParameters =
				new Hashtable<Object, Object>();
			
			Algorithm spemShellCoreAlgorithm =
				spemShellCoreAlgorithmFactory.createAlgorithm(
						data, emptyParameters, context);
			
			Data[] outData = spemShellCoreAlgorithm.execute();
			File outDatFile = (File) outData[0].getData();
		
			return outDatFile;
		} catch (AlgorithmExecutionException e) {
			throw new AlgorithmExecutionException(
				"Error running SPEMShell: " + e.getMessage(),
				e);
		}
	}


	private Data[] createSPEMShellInData(
			Data[] data, Dictionary<String, Object> parameters)
				throws IOException {
		File epicModelFile = (File) data[0].getData();
		ModelFileMaker modelFileMaker =
			new ModelFileMaker(epicModelFile, parameters);		
		File spemShellModelFile =
			modelFileMaker.make();
		
		InFileMaker inFileMaker =
			new InFileMaker(spemShellModelFile.getPath(), parameters);
		File inFile = inFileMaker.make();
		
		InfectionsFileMaker infectionsFileMaker = new InfectionsFileMaker();
		File infectionsFile = infectionsFileMaker.make();
		
		Data[] spemShellData =
			new Data[]{
				new BasicData(inFile, IN_FILE_MIME_TYPE),
				new BasicData(infectionsFile, PLAIN_TEXT_MIME_TYPE) };
		
		return spemShellData;
	}


	@SuppressWarnings("unchecked") // TODO
	private static Data[] createOutData(
			File outDatFile, String label, Data parentData) {
		Data outData = new BasicData(outDatFile, CSV_MIME_TYPE);
		Dictionary metadata = outData.getMetadata();
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		metadata.put(DataProperty.PARENT, parentData);
		
		return new Data[]{ outData };
	}
	
	public static File createTempFileWithNoSpacesInPath(String filename) {
		/* TODO As of September 23, SPEMShell can't handle paths containing
		 * spaces, so it would be dangerous to create Files in the user's
		 * default temporary file directory.  For now we hand-code paths that
		 * we know will not contain spaces. 
		 */		
		File file =
			new File("Z:\\jrbibers\\SPEMShell\\", filename);
		
		assert (!(file.getPath().contains(" ")));
		
		return file;
	}


	public static StringTemplateGroup loadTemplates(String templatePath) {
		return new StringTemplateGroup(
				new InputStreamReader(
						SPEMShellRunnerAlgorithm.class.getResourceAsStream(
								templatePath)));
	}
}
