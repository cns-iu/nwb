package edu.iu.nwb.visualization.drl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.templates.staticexecutable.StaticExecutableAlgorithmFactory;
import org.cishell.utilities.FileUtilities;
import org.osgi.framework.BundleContext;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class VxOrdAlgorithm implements Algorithm {
    public static final String EDGE_CUT_ATTRIBUTE_ID = "edgeCut";
	public static final String OUTPUT_DATA_LABEL = "Laid out with DrL";
	public static final String TEMP_SIM_FILE_NAME = "temporarySIMFile";
	public static final String Y_POSITION_ATTRIBUTE_ID = "ypos";
	public static final String X_POSITION_ATTRIBUTE_ID = "xpos";
	public static final String VXORD_ALGORITHM_NAME = "vxord";
	public static final String EDGE_WEIGHT_ATTRIBUTE_ID = "edgeWeight";
	public static final double NO_CUT_EDGE_CUT_STRENGTH = 0.0;
	public static final String SHOULD_NOT_CUT_EDGES_ID = "shouldNotCutEdges";
	public static final String SIMINT_FILE_EXTENSION = "int";
	public static final String INTSIM_MIME_TYPE = "file:text/intsim";
	
	private Data[] data;
    private File inputNWBFile;
    private Dictionary<String, Object> parameters;
    private CIShellContext ciShellContext;
	
    private static StaticExecutableAlgorithmFactory staticAlgorithmFactory;
    
    
    public VxOrdAlgorithm(
    		Data[] data,
    		Dictionary<String, Object> parameters,
    		CIShellContext ciShellContext,
    		BundleContext bundleContext) {
    	this.data = data;
    	this.inputNWBFile = (File) data[0].getData();
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;

        if (staticAlgorithmFactory == null) {
        	staticAlgorithmFactory =
        		new StaticExecutableAlgorithmFactory(VXORD_ALGORITHM_NAME, bundleContext);
        }
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		/* If the user chooses to not cut edges, assume edge cut
    		 * strength is 0.0, no matter what they entered.
    		 */
    		boolean shouldNotCutEdges =
    			((Boolean) parameters.get(SHOULD_NOT_CUT_EDGES_ID)).booleanValue();
    		if ( shouldNotCutEdges ) {
    			parameters.put(EDGE_CUT_ATTRIBUTE_ID, new Double(NO_CUT_EDGE_CUT_STRENGTH));
    		}
    		
    		// Translate inputNWBFile to a SIM file to give DrL
    		Data simData = createSimFileData();
    		
    		// Run DrL and grab the layout data that it generates
    		Data[] layoutData = generateLayoutData(simData);
			
			// Write the layout data to NWB
    		File outputNWBFile = writeLayoutDataToNWB(layoutData);
			
			return createOutData(outputNWBFile);
			
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"There was a problem parsing the input data.",
					e);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException(
					"There was a problem parsing the input data.",
					e);
		}
    }


	private Data createSimFileData()
			throws IOException, ParsingException, FileNotFoundException {
		/* Determine whether to use edge weights from inputNWBFile
		 * according to the user parameter EDGE_WEIGHT_ATTRIBUTE_ID.
		 */
		String edgeWeightAttributeKey =
			(String) parameters.get(EDGE_WEIGHT_ATTRIBUTE_ID);    		
		boolean shouldUseEdgeWeight = true;
		if (VxOrdAlgorithmFactory.USE_NO_EDGE_WEIGHT_TOKEN.equals(
				edgeWeightAttributeKey)) {
			shouldUseEdgeWeight = false;
		}
		
		File tmpSimFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					TEMP_SIM_FILE_NAME,
					SIMINT_FILE_EXTENSION);			
		
		NWBFileParser parser = new NWBFileParser(inputNWBFile);
		String weightAttr =
			(String) parameters.get(EDGE_WEIGHT_ATTRIBUTE_ID);
		parser.parse(
				new NWBToSimFileHandler(shouldUseEdgeWeight,
										weightAttr,
										new FileOutputStream(tmpSimFile)));
		Data simData = new BasicData(tmpSimFile, INTSIM_MIME_TYPE);
		return simData;
	}


	private Data[] generateLayoutData(Data simData)
			throws AlgorithmExecutionException {
		// Run DRL (VxOrd) on SIM File
		Algorithm layoutAlg = staticAlgorithmFactory.createAlgorithm(
				new Data[]{ simData }, parameters, ciShellContext);
		Data[] layoutData;
		try {
			layoutData = layoutAlg.execute();
		} catch (AlgorithmExecutionException e) {
			throw new AlgorithmExecutionException(
					"Unable to execute the DrL layout algorithm.",
					e);
		}
		return layoutData;
	}


	private File writeLayoutDataToNWB(Data[] layoutData)
			throws IOException, ParsingException {
		File outputNWBFile = NWBFileUtilities.createTemporaryNWBFile();
		
		File coordFile = (File) layoutData[0].getData();
		String xposAttr = (String) parameters.get(X_POSITION_ATTRIBUTE_ID);
		String yposAttr = (String) parameters.get(Y_POSITION_ATTRIBUTE_ID);
		
		if (layoutData != null) {
			NWBCoordMerger merger = new NWBCoordMerger(coordFile,
													   inputNWBFile,
													   xposAttr,
													   yposAttr,
													   outputNWBFile);
			merger.merge();
		} else {
			throw new ParsingException("Layout Failed!");
		}
		
		return outputNWBFile;
	}


	@SuppressWarnings("unchecked")
	private Data[] createOutData(File outputNWBFile) {
		Data outNWBData = new BasicData(outputNWBFile, NWBFileProperty.NWB_MIME_TYPE);
		Dictionary<String, Object> metadata = outNWBData.getMetadata();
		metadata.put(DataProperty.LABEL, OUTPUT_DATA_LABEL);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		metadata.put(DataProperty.PARENT, data[0]);

		return new Data[] { outNWBData };
	}
}