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

import com.google.common.collect.ImmutableMap;

import edu.iu.nwb.analysis.communitydetection.slm.convertor.NWBAndTreeFilesMerger;
import edu.iu.nwb.analysis.communitydetection.slm.convertor.NWBToEdgeListConverter;
import edu.iu.nwb.analysis.communitydetection.slm.convertor.NetworkInfo;
import edu.iu.nwb.analysis.communitydetection.slm.convertor.Preprocessor;
import edu.iu.nwb.analysis.communitydetection.slm.convertor.PreprocessorException;
import edu.iu.nwb.analysis.communitydetection.slm.convertor.TreeFileParsingException;
import edu.iu.nwb.analysis.communitydetection.slm.vos.ModularityOptimizer;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class AbstractVosAlgorithm implements Algorithm {
	
	public static final String NO_EDGE_WEIGHT_VALUE = "unweighted";
	public static final String WEIGHT_FIELD_ID = "weight";
	public static final String MODULARITY_FUNCTION_FIELD_ID = "modularity";
	public static final String RESOLUTION_FIELD_ID = "resolution";
	public static final String RANDOM_START_FIELD_ID = "rstart";
	public static final String RANDOM_SEED_FIELD_ID = "rseed";
	public static final String ITERATIONS_FIELD_ID = "iterations";
	public static final String ALGORITHM_FIELD_ID = "algorithm";
	public static final Map<String, Integer> ALGORITHM_MAP = ImmutableMap.of(
			"Louvain Algorithm", 1,
			"Louvain Agorithm With Multilevel Refinement", 2, 
			"SLM Algorithm", 3);
	public static final Map<String, Integer> MODULARITY_FUCNTION_MAP = ImmutableMap.of(
			"Standard", 1,
			"Alternative", 2);

	private Data[] data;
	private CIShellContext ciShellContext;
	private String weightColumnTitle;
	private double resolution;
	private int randomStart;
	private int randomSeed;
	private int algorithm;
	private int iterations;
	private boolean isWeighted;
	private int modularityFunction;

	protected AbstractVosAlgorithm(Data[] data, Dictionary<String, Object> parameters,
			CIShellContext ciShellContext) {
		this.data = data;
		this.ciShellContext = ciShellContext;
		this.weightColumnTitle = parameters.get(WEIGHT_FIELD_ID).toString();
		this.resolution = (Double) parameters.get(RESOLUTION_FIELD_ID);
		this.randomStart = (Integer) parameters.get(RANDOM_START_FIELD_ID);
		this.randomSeed = (Integer) parameters.get(RANDOM_SEED_FIELD_ID);
		this.iterations = (Integer) parameters.get(ITERATIONS_FIELD_ID);
		this.algorithm = ALGORITHM_MAP.get(parameters.get(ALGORITHM_FIELD_ID)
				.toString());
		this.modularityFunction = MODULARITY_FUCNTION_MAP.get(parameters.get(MODULARITY_FUNCTION_FIELD_ID)
				.toString());

		if (this.weightColumnTitle.equals(NO_EDGE_WEIGHT_VALUE)) {
			this.isWeighted = false;
		} else {
			this.isWeighted = true;
		}
	}

	public Data[] execute() throws AlgorithmExecutionException {
		File inputNWBFile = (File) data[0].getData();
		NetworkInfo networkInfo = new NetworkInfo();
		Preprocessor preprocessor = new Preprocessor(networkInfo,
				weightColumnTitle, isWeighted);

		try {
			File vosInputFile = FileUtilities
					.createTemporaryFileInDefaultTemporaryDirectory(
							"TEMP-VOS-IN", "txt");
			File vosOutputFile = FileUtilities
					.createTemporaryFileInDefaultTemporaryDirectory(
							"TEMP-VOS-OUT", "txt");
			NWBFileParser nwbFileParser = new NWBFileParser(inputNWBFile);
			nwbFileParser.parse(preprocessor);
			NWBToEdgeListConverter.convert(vosInputFile, networkInfo);
			ModularityOptimizer optimizer = new ModularityOptimizer(
					algorithm, modularityFunction, randomStart, randomSeed, iterations, resolution);
			optimizer.OptimizeModularity(vosInputFile, vosOutputFile);
			File outputFile = NWBAndTreeFilesMerger
					.mergeCommunitiesFileWithNWBFile(vosOutputFile,
							inputNWBFile, networkInfo);
			return wrapFileAsOutputData(outputFile, data[0]);
		} catch (PreprocessorException e) {
			throw new AlgorithmExecutionException("Invalid NWB file.", e);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Invalid NWB file.", e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"VOS community detection error.", e);
		} catch (TreeFileParsingException e) {
			throw new AlgorithmExecutionException("Fail to generate output", e);
		}
	}

	private static Data[] wrapFileAsOutputData(File outputFile, Data parent) {
		Data outputFileData = new BasicData(outputFile, "file:text/nwb");
		Dictionary<String, Object> outputFileMetaData = outputFileData
				.getMetadata();
		outputFileMetaData.put(DataProperty.LABEL, "With community attributes");
		outputFileMetaData.put(DataProperty.PARENT, parent);
		outputFileMetaData.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);

		return new Data[] { outputFileData };
	}
}