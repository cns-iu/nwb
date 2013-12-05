package edu.iu.nwb.analysis.communitydetection.slm;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;

import edu.iu.nwb.analysis.communitydetection.slm.convertor.NWBAndTreeFilesMerger;
import edu.iu.nwb.analysis.communitydetection.slm.convertor.NWBToEdgeListConverter;
import edu.iu.nwb.analysis.communitydetection.slm.convertor.NetworkInfo;
import edu.iu.nwb.analysis.communitydetection.slm.convertor.Preprocessor;
import edu.iu.nwb.analysis.communitydetection.slm.convertor.PreprocessorException;
import edu.iu.nwb.analysis.communitydetection.slm.convertor.TreeFileParsingException;
import edu.iu.nwb.analysis.communitydetection.slm.vos.ModularityOptimizer;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class SLMAlgorithm implements Algorithm {
    public static final String NO_EDGE_WEIGHT_VALUE = "unweighted";
	public static final Object WEIGHT_FIELD_ID = "weight";
	private Data[] data;
    private CIShellContext ciShellContext;
	private String weightAttribute;
	private boolean isWeighted;
    
    public SLMAlgorithm(Data[] data,
    				  Dictionary<String, Object> parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.ciShellContext = ciShellContext;
        
        this.weightAttribute = parameters.get(WEIGHT_FIELD_ID).toString();
        
        if (this.weightAttribute.equals(NO_EDGE_WEIGHT_VALUE)) {
        	this.isWeighted = false;
        } else {
        	this.isWeighted = true;
        }
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	File inputNWBFile = (File)data[0].getData();
    	System.out.println("Start");
    	NetworkInfo networkInfo = new NetworkInfo();
    	Preprocessor preprocessor = new Preprocessor(networkInfo,null, false);

    	try {
    		File vosInputFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("TEMP-VOS", "txt");
        	File vosOutputFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("TEMP-VOS-OUT", "txt");
			NWBFileParser nwbFileParser = new NWBFileParser(inputNWBFile);
			nwbFileParser.parse(preprocessor);
			NWBToEdgeListConverter converter = new NWBToEdgeListConverter(networkInfo, vosInputFile, null, false);
			ModularityOptimizer optimizer = new ModularityOptimizer();
			optimizer.OptimizeModularity(vosInputFile, vosOutputFile);
			File outputFile = NWBAndTreeFilesMerger.mergeCommunitiesFileWithNWBFile(vosOutputFile, inputNWBFile, networkInfo);
			return wrapFileAsOutputData(outputFile, data[0]);
    	} catch (PreprocessorException e) {
			throw new AlgorithmExecutionException("Invalid NWB file.", e);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Invalid NWB file.", e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException("VOS community detection error.", e);
		} catch (TreeFileParsingException e) {
			throw new AlgorithmExecutionException("Fail to generate output", e);
		}
    }

	private static Data[] wrapFileAsOutputData(File outputFile, Data parent) {
		Data outputFileData = new BasicData(outputFile, "file:text/nwb");
		Dictionary<String, Object> outputFileMetaData = outputFileData.getMetadata();
		outputFileMetaData.put(DataProperty.LABEL, "With community attributes");
		outputFileMetaData.put(DataProperty.PARENT, parent);
		outputFileMetaData.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		
		return new Data[] { outputFileData };
    }
}