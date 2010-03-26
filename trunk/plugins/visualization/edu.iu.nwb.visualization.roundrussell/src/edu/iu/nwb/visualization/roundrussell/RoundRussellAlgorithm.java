package edu.iu.nwb.visualization.roundrussell;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.nwb.visualization.roundrussell.interpolation.InterpolatorInversionException;
import edu.iu.nwb.visualization.roundrussell.interpolation.InputRangeException;
import edu.iu.nwb.visualization.roundrussell.utility.Constants;
import edu.iu.nwb.visualization.roundrussell.utility.Range;

/**
 * Round Russell algorithm produces a circular visualization of the output produced by a 
 * multi-level aggregation method. That is it is used to visualize the community detection
 * done by the aggregation algorithm. 
 * @author Chintan Tank
 */


public class RoundRussellAlgorithm implements Algorithm {
	
	public static final String OUTPUT_DATA_LABEL = "Circular Hierarchy Viz.ps";
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	public static final String WEIGHT_COLUMN_ID = "weightcolumn";
	public static final String STRENGTH_COLUMN_ID = "strengthcolumn";
	public static final String LEVEL0_COLUMN_ID = "level0_column";
	public static final String LEVEL1_COLUMN_ID = "level1_column";
	public static final String LEVEL2_COLUMN_ID = "level2_column";
	public static final String LEVEL3_COLUMN_ID = "level3_column";
	public static final String NODE_COLOR_COLUMN_ID = "nodecolorcolumn";
	public static final String NODE_COLOR_RANGE_ID = "nodecolorrange";
	
	private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
	private LogService logger;
	
	
	public static final String NO_LEVEL_COLUMN_NAME = "no_level_column";
	public static final String NO_STRENGTH_COLUMN_NAME = "no_strength_column";
	public static final String STRING_TEMPLATE_FILE_PATH = 
		"/edu/iu/nwb/visualization/roundrussell/stringtemplates/group.st";
	
	public static StringTemplateGroup group = loadTemplates();
	
    public RoundRussellAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
		this.logger = (LogService) context.getService(LogService.class.getName());
    }
    
    /*
     * Used to generate a circular visualization for the provided network. First the 
     * provided file is validated against the NWB file format. Then all the user inputs 
     * like name of edge weight, strength, level columns & edge bundling degree are 
     * passed on to the RoundRussellComputation script for further processing. After 
     * the processing is done a .ps file is created & its metadata are set.  
     * */
    public Data[] execute() throws AlgorithmExecutionException {
    	
		File inputData = (File) data[0].getData();
    	int numberOfNodes = 0;

    	try {
			numberOfNodes = countNodesInNWBFile(inputData);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Inappropriate NWB File provided.", e);
		}
    	
    	if (numberOfNodes > 0) {
			try {
				
				String edgeWeightColumnName = (String) parameters.get(WEIGHT_COLUMN_ID);
				String nodeStrengthColumnName = (String) parameters.get(STRENGTH_COLUMN_ID);
				
				boolean useStrength = true;
				if (RoundRussellAlgorithmFactory.NO_STRENGTH_IDENTIFIER.equals(
						nodeStrengthColumnName)) {
					useStrength = false;
				}
				
				String level0ColumnName = (String) parameters.get(LEVEL0_COLUMN_ID);
				String level1ColumnName = (String) parameters.get(LEVEL1_COLUMN_ID);
				String level2ColumnName = (String) parameters.get(LEVEL2_COLUMN_ID);
				String level3ColumnName = (String) parameters.get(LEVEL3_COLUMN_ID);

				String nodeColorColumnName = (String) parameters.get(NODE_COLOR_COLUMN_ID);
				boolean useNodeColor = true;
				if (RoundRussellAlgorithmFactory.NO_COLOR_IDENTIFIER.equals(nodeColorColumnName)) {
					useNodeColor = false;
				}
				
				
				String nodecolorRangeIdentifier = (String) parameters.get(NODE_COLOR_RANGE_ID);
				Range<Color> nodeColorRange = Constants.COLOR_RANGES.get(nodecolorRangeIdentifier);
				
				int numberOfLevelsSpecified = 0;
				double betaCurvedValue;
				
				List<String> levelColumnNames = new ArrayList<String>();
				
				numberOfLevelsSpecified = setLevelColumnNames(level0ColumnName,
						level1ColumnName, level2ColumnName, level3ColumnName,
						numberOfLevelsSpecified, levelColumnNames);
				
					betaCurvedValue = Double.parseDouble(parameters.get("betacolumn").toString());
				
				/*
				 * At least 1 "level" information must be specified for the visualization to work.
				 * If not, then throw an error.
				 * */
				if (numberOfLevelsSpecified < 1) {
					throw new AlgorithmExecutionException(
							"At least 1 \"level\" column should be specified.");
				}
				
				NWBFileParser parser = new NWBFileParser(inputData);
				RoundRussellComputation russellRoundComputation = 
					new RoundRussellComputation(numberOfNodes, 
												useStrength,
												nodeStrengthColumnName, 
												levelColumnNames, 
												edgeWeightColumnName,
												useNodeColor,
												nodeColorColumnName,
												nodeColorRange,
												betaCurvedValue,
												logger);
				parser.parse(russellRoundComputation);
				
				Data outputPSData = generateOutputPSFile(russellRoundComputation,
														 nodeColorColumnName,
														 nodeColorRange, 
														 nodecolorRangeIdentifier,
														 logger);
				
				return new Data[]{outputPSData};
				
			} catch (FileNotFoundException e) {
				throw new AlgorithmExecutionException(
						"NWB File'" + inputData.getAbsolutePath() + "' not found.", e);
			} catch (IOException e) {
				throw new AlgorithmExecutionException(e);
			} catch (ParsingException e) {
				throw new AlgorithmExecutionException(e);
			}
    	} else {
    		return data;
    	}
	}

	/**
	 * @param level0ColumnName
	 * @param level1ColumnName
	 * @param level2ColumnName
	 * @param level3ColumnName
	 * @param numberOfLevelsSpecified
	 * @param levelColumnNames
	 * @return
	 */
	private int setLevelColumnNames(String level0ColumnName,
			String level1ColumnName, String level2ColumnName,
			String level3ColumnName, int numberOfLevelsSpecified,
			List<String> levelColumnNames) {
		if (RoundRussellAlgorithmFactory.NO_LEVEL_IDENTIFIER.equalsIgnoreCase(
				level0ColumnName)) {
			levelColumnNames.add(NO_LEVEL_COLUMN_NAME);
		} else {
			levelColumnNames.add(level0ColumnName);
			numberOfLevelsSpecified++;			
		}

		if (RoundRussellAlgorithmFactory.NO_LEVEL_IDENTIFIER.equalsIgnoreCase(
				level1ColumnName)) {
			levelColumnNames.add(NO_LEVEL_COLUMN_NAME);
		} else {
			levelColumnNames.add(level1ColumnName);
			numberOfLevelsSpecified++;			
		}
		
		if (RoundRussellAlgorithmFactory.NO_LEVEL_IDENTIFIER.equalsIgnoreCase(
				level2ColumnName)) {
			levelColumnNames.add(NO_LEVEL_COLUMN_NAME);
		} else {
			levelColumnNames.add(level2ColumnName);
			numberOfLevelsSpecified++;			
		}
		
		if (RoundRussellAlgorithmFactory.NO_LEVEL_IDENTIFIER.equalsIgnoreCase(
				level3ColumnName)) {
			levelColumnNames.add(NO_LEVEL_COLUMN_NAME);
		} else {
			levelColumnNames.add(level3ColumnName);
			numberOfLevelsSpecified++;			
		}
		
		return numberOfLevelsSpecified;
	}

	/**
	 * @param russellRoundComputation
	 * @param nodeColorColumnName 
	 * @param nodeColorRange 
	 * @param nodecolorRangeIdentifier 
	 * @param logger 
	 * @return
	 * @throws IOException
	 */
	private Data generateOutputPSFile(RoundRussellComputation russellRoundComputation, 
									  String nodeColorColumnName, 
									  Range<Color> nodeColorRange, 
									  String nodecolorRangeIdentifier, LogService logger) 
		throws IOException {
		
		File outputPSFile = File.createTempFile("ps-", ".ps");
		FileWriter out = null;
		try {
			out = new FileWriter(outputPSFile);
			out.write(PostScriptOperations.getPostScriptHeaderContent());		
			out.write("\n");
			out.write(PostScriptOperations.getPostScriptUtilityDefinitions());
			out.write("\n");
			out.write(russellRoundComputation.psFileContent.toString());
			out.write("\n");
			
			if (!RoundRussellAlgorithmFactory.NO_COLOR_IDENTIFIER.equalsIgnoreCase(
					nodeColorColumnName)
					&& russellRoundComputation.nodeColorInterpolator != null) {
				out.write(PostScriptOperations.
						getColorLegendContent(russellRoundComputation.nodeColorInterpolator, 
											  russellRoundComputation.getNodeColorValueRange(), 
											  nodeColorRange,
											  nodeColorColumnName));
				out.write("\n");
			}
			
			out.close();
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, e.getMessage(), e);
		} catch (AlgorithmExecutionException e) {
			logger.log(LogService.LOG_ERROR, e.getMessage(), e);
		} catch (InputRangeException e) {
			logger.log(LogService.LOG_ERROR, e.getMessage(), e);
		} catch (InterpolatorInversionException e) {
			logger.log(LogService.LOG_ERROR, e.getMessage(), e);
		}		
		
		Data outputPSData = new BasicData(outputPSFile, POSTSCRIPT_MIME_TYPE);
		outputPSData.getMetadata().put(DataProperty.LABEL, "CircularHierarchy_" + FileUtilities.extractFileName(data[0].getMetadata().get(DataProperty.LABEL).toString()) + ".ps"); 
		outputPSData.getMetadata().put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
		outputPSData.getMetadata().put(DataProperty.PARENT, data[0]);
		return outputPSData;
	}

	private static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(
				new InputStreamReader(
					RoundRussellAlgorithm.class.getResourceAsStream(
						STRING_TEMPLATE_FILE_PATH)));
	}
	
	private int countNodesInNWBFile(File inNWBFile) 
		throws ParsingException, AlgorithmExecutionException {
		ValidateNWBFile validateParser = new ValidateNWBFile();
		try {
			validateParser.validateNWBFormat(inNWBFile);
			return validateParser.getTotalNumOfNodes();
		} catch (Exception e) {
			throw new AlgorithmExecutionException("Inappropriate NWB File provided. \n" 
													+ e.getMessage());
		}
		
	}

}
