package edu.iu.epic.simulator.runner.stochastic.network;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.framework.BundleContext;

import edu.iu.epic.simulator.runner.EpidemicSimulatorAlgorithm;
import edu.iu.epic.simulator.runner.utility.postprocessing.DatToCsv;
import edu.iu.epic.simulator.runner.utility.postprocessing.StateToNwb;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class NetworkSingleRunnerAlgorithm extends EpidemicSimulatorAlgorithm {
	public static final String NETWORK_CORE_PID = "edu.iu.epic.simulator.core.network";
	
	public NetworkSingleRunnerAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext ciContext,
			BundleContext bundleContext) {
		super(data, parameters, ciContext, bundleContext);
	}

	@Override
	protected String getCoreAlgorithmPID() {
		return NETWORK_CORE_PID;
	}
	
	@Override
	protected Data[] prepareForDataManager(Data[] simulationOutputData)
			throws AlgorithmExecutionException {
		try {
			File networkLevelDatFile = (File) simulationOutputData[0].getData();
			DatToCsv datToCsv = new DatToCsv(networkLevelDatFile);
			File networkLevelCsvFile = datToCsv.convert();
			List<String> compartments = datToCsv.getCompartments();
			
			String networkLevelLabel =
				(String) simulationOutputData[0].getMetadata().get(DataProperty.LABEL);
			
			Data networkLevelData =
				datafyCsvFile(networkLevelCsvFile, networkLevelLabel, this.data[0]);
			
			Data nodeLevelData =
				prepareNodeLevelForDataManager(
						simulationOutputData[1], nwbFile, compartments, this.data[0]);			
			
			return new Data[]{ networkLevelData, nodeLevelData };
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Problem preparing results: " + e.getMessage(), e);
		} catch (NumberFormatException e) {
			throw new AlgorithmExecutionException(
					"Problem preparing results: " + e.getMessage(), e);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException(
					"Problem preparing results: " + e.getMessage(), e);
		}
	}

	private static Data prepareNodeLevelForDataManager(
			Data simulationOutputData, File nwbFile, List<String> compartments, Data parentData)
				throws IOException, ParsingException {
		File nodeLevelStateFile = (File) simulationOutputData.getData();
		File nodeLevelNwbFile = new StateToNwb(nodeLevelStateFile, nwbFile, compartments).convert();
		
		// TODO Might want to use a different label
		String nodeLevelLabel =
			(String) simulationOutputData.getMetadata().get(DataProperty.LABEL);
		
		return datafyNwbFile(nodeLevelNwbFile, nodeLevelLabel, parentData);
	}

	private static Data datafyNwbFile(File nwbFile, String label, Data parentData) {
		Data nwbData = new BasicData(nwbFile, NWBFileProperty.NWB_MIME_TYPE);
		
		Dictionary<String, Object> metadata = nwbData.getMetadata();
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		metadata.put(DataProperty.PARENT, parentData);
		
		return nwbData;
	}
}
