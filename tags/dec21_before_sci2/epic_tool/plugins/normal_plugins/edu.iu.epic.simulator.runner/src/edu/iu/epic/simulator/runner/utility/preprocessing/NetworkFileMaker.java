package edu.iu.epic.simulator.runner.utility.preprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.FileUtilities;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;
import edu.iu.nwb.util.nwbfile.ParsingException;

/**
 * Converts an NWB file to the EdgeList-style network file expected by the network
 * simulator core code.
 */
public class NetworkFileMaker {
	public static File make(File nwbFile) throws AlgorithmExecutionException {
		try {
			File simulatorNetworkFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"simulator_input_network", "dat");
		
			Writer simulatorNetworkFileWriter = new FileWriter(simulatorNetworkFile);
			
			NWBFileParser nwbReader = new NWBFileParser(nwbFile);
			
			NWBParserHandler handler = new NWBParserHandler(simulatorNetworkFileWriter);
			nwbReader.parse(handler);
			if (handler.exceptionOccurred()) {
				throw handler.getException();
			}
			
			return simulatorNetworkFile;
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Couldn't create network file for simulator: " + e.getMessage(), e);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException(
					"Couldn't create network file for simulator: " + e.getMessage(), e);
		}
	}
	
	
	protected static class NWBParserHandler extends NWBFileParserAdapter {
		private Writer simulatorNetworkFileWriter;
		private IOException exception = null;

		public NWBParserHandler(Writer simulatorNetworkFileWriter) {
			this.simulatorNetworkFileWriter = simulatorNetworkFileWriter;
		}

		@Override
		public void addUndirectedEdge(int node1, int node2, Map<String, Object> attributes) {
			try {
				simulatorNetworkFileWriter.write(String.format("%d %d\n", node1, node2));
			} catch (IOException e) {
				setException(e);
				haltParsingNow();
			}
		}
		
		@Override
		public void addDirectedEdge(
				int sourceNode, int targetNode,	Map<String, Object> attributes) {
			this.addUndirectedEdge(sourceNode, targetNode, attributes);
		}

		public boolean exceptionOccurred() {
			return (exception != null);
		}
		public IOException getException() {
			return exception;
		}
		public void setException(IOException exception) {
			this.exception = exception;
		}
	}
}
