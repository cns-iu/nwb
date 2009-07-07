package edu.iu.nwb.visualization.roundrussell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;

/**
 * Round Russell algorithm produces a circular visualization of the output produced by a 
 * multi-level aggregation method. That is it is used to visualize the community detection
 * done by the aggregation algorithm. 
 * @author Chintan Tank
 */


public class RoundRussellAlgorithm implements Algorithm {
	
	private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
	private LogService logger;
	
	private double DEFAULT_BETA_EDGE_BUNDLING_VALUE = 0.75;
	public static String NO_LEVEL_COLUMN_NAME = "no_level_column";
	public static String NO_STRENGTH_COLUMN_NAME = "no_strength_column";
	
	
    public RoundRussellAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
		this.logger = (LogService) context.getService(LogService.class.getName());
    }
    
    /*
     * Used to generate a circular visualization for the provided network. First the provided file is 
     * validated against the NWB file format. Then all the user inputs like name of edge weight, strength,
     * level columns & edge bundling degree are passed on to the RoundRussellComputation script for further
     * processing. After the processing is done a .ps file is created & its metadata are set.  
     * */
    public Data[] execute() throws AlgorithmExecutionException {
    	
		File inputData = (File)data[0].getData();
    	int numberOfNodes = 0;

    	try {
			numberOfNodes = validateInputFile(inputData);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Inappropriate NWB File provided.", e);
		}
    	
    	if(numberOfNodes > 0) {
			try {
				
				String edgeWeightColumnName = (String) parameters.get("weightcolumn");
				String nodeStrengthColumnName = (String) parameters.get("strengthcolumn");
				
				String level1ColumnName = (String) parameters.get("level1_column");
				String level2ColumnName = (String) parameters.get("level2_column");
				String level3ColumnName = (String) parameters.get("level3_column");
				String level4ColumnName = (String) parameters.get("level4_column");
				
				int numberOfLevelsSpecified = 0;
				double betaCurvedValue;
				
				List<String> levelColumnNames = new ArrayList<String>();
				
				if(!level1ColumnName.equalsIgnoreCase(RoundRussellAlgorithmFactory.NO_LEVEL_IDENTIFIER)) {
					levelColumnNames.add(level1ColumnName);
					numberOfLevelsSpecified++;
				}
				else {
					levelColumnNames.add(NO_LEVEL_COLUMN_NAME);
				}

				if(!level2ColumnName.equalsIgnoreCase(RoundRussellAlgorithmFactory.NO_LEVEL_IDENTIFIER)) {
					levelColumnNames.add(level2ColumnName);
					numberOfLevelsSpecified++;
				}
				else {
					levelColumnNames.add(NO_LEVEL_COLUMN_NAME);
				}
				
				if(!level3ColumnName.equalsIgnoreCase(RoundRussellAlgorithmFactory.NO_LEVEL_IDENTIFIER)) {
					levelColumnNames.add(level3ColumnName);
					numberOfLevelsSpecified++;
				}
				else {
					levelColumnNames.add(NO_LEVEL_COLUMN_NAME);
				}
				
				if(!level4ColumnName.equalsIgnoreCase(RoundRussellAlgorithmFactory.NO_LEVEL_IDENTIFIER)) {
					levelColumnNames.add(level4ColumnName);
					numberOfLevelsSpecified++;
				}
				else {
					levelColumnNames.add(NO_LEVEL_COLUMN_NAME);
				}
				
					betaCurvedValue = Double.parseDouble(parameters.get("betacolumn").toString());
				
				/*
				 * At least 1 "level" information must be specified for the visualization to work.
				 * If not, then throw an error.
				 * */
				if(numberOfLevelsSpecified < 1) {
					throw new AlgorithmExecutionException("Atleast 1 \"level\" column should be specified.");
				}
				
				NWBFileParser parser = new NWBFileParser(inputData);
				RoundRussellComputation russellRoundComputation = new RoundRussellComputation(numberOfNodes, 
						nodeStrengthColumnName, levelColumnNames, edgeWeightColumnName, betaCurvedValue);
				parser.parse(russellRoundComputation);
				
				Data outputPSData = generateOutputPSFile(russellRoundComputation);
				
				return new Data[]{outputPSData};
				
			} catch (FileNotFoundException e) {
				throw new AlgorithmExecutionException("NWB File'" + inputData.getAbsolutePath() + "' not found.");
			} catch (IOException e) {
				throw new AlgorithmExecutionException(e);
			} catch (ParsingException e) {
				throw new AlgorithmExecutionException(e);
			}
    	} 
    	else {
    		return data;
    	}
	}

	/**
	 * @param russellRoundComputation
	 * @return
	 * @throws IOException
	 */
	private Data generateOutputPSFile(
			RoundRussellComputation russellRoundComputation) throws IOException {
		File outputPSFile = File.createTempFile("ps-", ".ps");
		FileWriter out = null;
		try {
			out = new FileWriter(outputPSFile);
			out.write(GlobalConstants.PS_HEADER_CONTENT);
			out.write(russellRoundComputation.psFileContent.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		Data outputPSData = new BasicData(outputPSFile,"file:text/ps");
		outputPSData.getMetadata().put(DataProperty.LABEL, "Round Russell Visualization in ps format");
		outputPSData.getMetadata().put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
		outputPSData.getMetadata().put(DataProperty.PARENT, data[0]);
		return outputPSData;
	}

	private int validateInputFile(File inData) throws ParsingException, AlgorithmExecutionException {
		ValidateNWBFile validateParser = new ValidateNWBFile();
		try {
			validateParser.validateNWBFormat(inData);
			return validateParser.getTotalNumOfNodes();
		} catch (Exception e) {
			throw new AlgorithmExecutionException("Inappropriate NWB File provided. \n" + e.getMessage());
		}
		
	}

}
