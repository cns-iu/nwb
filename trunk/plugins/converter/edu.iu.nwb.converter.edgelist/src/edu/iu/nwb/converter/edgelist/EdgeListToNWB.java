package edu.iu.nwb.converter.edgelist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.edgelist.nwbwritable.Edge;
import edu.iu.nwb.converter.edgelist.nwbwritable.Node;

/**
 * Converts from edgelist format to NWB file format
 */
public class EdgeListToNWB implements Algorithm {
	private Data[] data;
	private CIShellContext ciContext;
	private LogService logger;

	public EdgeListToNWB(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.ciContext = context;
		this.logger = (LogService) ciContext.getService(LogService.class.getName());
	}

	/**
	 * Executes the conversion from edgelist format to NWB format
	 * 
	 * @return an NWB file derived from the provided edgelist
	 */
	public Data[] execute() throws AlgorithmExecutionException {
		File edgeFile = (File) data[0].getData();
		BufferedReader edgeReader = null;
		BufferedWriter nwbWriter = null;

		try {
			// (may throw FileNotFoundException)
			edgeReader = new BufferedReader(new FileReader(edgeFile));
			
			File nwbFile = getTempFile(); // TODO: make getTempFile() service
			
			// may throw FileIOException
			nwbWriter = new BufferedWriter(new FileWriter(nwbFile));

			// edge list information is given by various
			// methods of the parser.
			EdgeListParser parser = new EdgeListParser(edgeFile);
			// if it's a valid edgelist, go ahead with conversion
			transform(edgeReader, nwbWriter, parser);
			
			// should there be an else clause here?
			return new Data[] { new BasicData(nwbFile, "file:text/nwb") };
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException("Specified Edge list file not found! "
					+ ((File) edgeFile).getAbsolutePath() + "\n", e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error writing from the specified edge list to the specified .nwb file.\n", e);
		} catch (InvalidEdgeListFormatException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} finally {
			if (nwbWriter != null) {
				try {
					nwbWriter.close();
				} catch (IOException e) {
					throw new AlgorithmExecutionException("Unable to close file stream.", e);
				}
			}
		}
	}

	/*
	 * Reads edgelist data in from reader and outputs it as NWB data to writer
	 * @author Felix Terkhorn
	 */
	private void transform(BufferedReader reader, BufferedWriter writer, EdgeListParser parser)
			throws IOException {
		boolean badFormat = false;

		try {
			if (badFormat) {
				logger
						.log(
								LogService.LOG_ERROR,
								"Sorry, your file does not comply with edge-list format specifications.\n"
										+ "Please review the latest edge-list format specification at "
										+ "https://nwb.slis.indiana.edu/community/?n=LoadData.Edgelist, and update your file. \n");
				throw (new IOException("Improperly formatted edgelist file"));
			}
			// currentLine is null
		
			


			List nodes  = parser.getNodes();
			
			//write node header
			writer.write("*Nodes " + nodes.size() + "\n");
			writer.write("id*int  label*string\n");
			
			//write node lines 
			
			Iterator nodesIterator = nodes.iterator();
			while (nodesIterator.hasNext()) {
				Node node = (Node) nodesIterator.next();
				String nodeLine = node.getNWBLine();
				writer.write(nodeLine);
			}
			
			//write edge header
			
			if (parser.isUndirectedGraph()) {
				writer.write("*UndirectedEdges\n");
			} else {
				writer.write("*DirectedEdges\n");
			}
		
			if (parser.isWeighted()) {
				writer.write("source*int  target*int  weight*float\n");
			} else {
				writer.write("source*int  target*int\n");
			}
			
			List edges = parser.getEdges();
			
			//write edge lines
			Iterator edgesIterator = edges.iterator();
			while (edgesIterator.hasNext()) {
				Edge edge = (Edge) edgesIterator.next();
				String edgeLine = edge.getNWBLine();
				writer.write(edgeLine);
			}
			
			writer.flush();

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		// we need to take the edgelist and convert it into nwb format, then
		// dump that to the writer..

	}

	/**
	 * Creates a temporary file for the NWB file
	 * 
	 * @return The temporary file
	 */
	private File getTempFile() {
		File tempFile;

		String tempPath = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempPath + File.separator + "temp");
		if (!tempDir.exists())
			tempDir.mkdir();
		try {
			tempFile = File.createTempFile("NWB-Session-", ".nwb", tempDir);

		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, e.toString());
			tempFile = new File(tempPath + File.separator + "nwbTemp" + File.separator + "temp.nwb");

		}
		return tempFile;
	}
}