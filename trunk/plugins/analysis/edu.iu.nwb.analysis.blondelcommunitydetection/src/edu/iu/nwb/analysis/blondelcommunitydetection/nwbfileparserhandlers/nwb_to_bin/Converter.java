package edu.iu.nwb.analysis.blondelcommunitydetection.nwbfileparserhandlers.nwb_to_bin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Map;

import edu.iu.nwb.analysis.blondelcommunitydetection.NetworkInfo;
import edu.iu.nwb.analysis.blondelcommunitydetection.Node;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class Converter extends NWBFileParserAdapter {
	private static final String NON_POSITIVE_WEIGHT_HALT_REASON =
		"Non-positive weights are not allowed.  To use this algorithm, " +
		"preprocess your network further.";
	
	private String haltParsingReason = "";
	private boolean shouldHaltParsing = false;
	
	NetworkInfo networkInfo;
	
	private RandomAccessFile outputBINFile;
	
	private String weightAttribute;
	private boolean isWeighted;
	
	public Converter(NetworkInfo networkInfo,
					 File outputFile,
					 String weightAttribute,
					 boolean isWeighted) {
		this.networkInfo = networkInfo;
		
		this.weightAttribute = weightAttribute;
		this.isWeighted = isWeighted;
		
		try {
			this.outputBINFile = new RandomAccessFile(outputFile, "rw");
		} catch (FileNotFoundException fileNotFoundException) {
			throw new RuntimeException(fileNotFoundException);
		}
		
		try {
			this.createBINFile();
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}
	
	public void addDirectedEdge(int sourceNode,
								int targetNode,
								Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void addUndirectedEdge(int sourceNode,
								  int targetNode,
								  Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void finishedParsing() {
	}
	
	public boolean haltParsingNow() {
		return this.shouldHaltParsing;
	}
	
	private void addEdge(int sourceNodeID, int targetNodeID, Map attributes) {
		int weight;
		
		if (this.isWeighted) {
			weight = ((Number)attributes.get(this.weightAttribute)).intValue();
		} else {
			weight = 1;
		}
		
		if (weight < 0.0) {
			this.haltParsingReason = NON_POSITIVE_WEIGHT_HALT_REASON;
			this.shouldHaltParsing = true;
		} else {
			Node sourceNode =
				this.networkInfo.findNodeByOriginalID(sourceNodeID);
			Node targetNode =
				this.networkInfo.findNodeByOriginalID(targetNodeID);
			
			try {
				this.writeEdgeAndWeightForNode(sourceNode, targetNode, weight);
				this.writeEdgeAndWeightForNode(targetNode, sourceNode, weight);
			} catch (IOException ioException) {
				throw new RuntimeException(ioException);
			}
		}
	}
	
	private void writeEdgeAndWeightForNode(Node sourceNode,
										   Node targetNode,
										   int weight) throws IOException {
		// Write the target node id.
		this.outputBINFile.seek(sourceNode.getWorkingEdgeOffsetInFile());
		this.writeLittleEndianInt(targetNode.getNewID());
		sourceNode.incrementWorkingEdgeOffsetInFile();
		
		// Write the target node weight.
		this.outputBINFile.seek(sourceNode.getWorkingWeightOffsetInFile());
		this.writeLittleEndianInt(weight);
		sourceNode.incrementWorkingWeightOffsetInFile();
	}
	
	private void createBINFile() throws IOException {
		long headerSize = this.writeHeader();
		this.writeEmptyBody(headerSize);
	}
	
	private long writeHeader() throws IOException {
		long headerSize = 0;
		
		ArrayList nodes = this.networkInfo.getNodes();
		int nodeCount = nodes.size();
		headerSize += this.writeLittleEndianInt(nodeCount);
		
		for (int ii = 0; ii < nodeCount; ii++) {
			Node node = (Node)nodes.get(ii);
			headerSize +=
				this.writeLittleEndianInt(node.getEdgeCountForOutput());
		}
		
		return headerSize;
	}
	
	private void writeEmptyBody(long headerSize) throws IOException {
		long numBytesWritten = headerSize;
		
		ArrayList nodes = this.networkInfo.getNodes();
		int nodeCount = nodes.size();
		
		// Write the fake edges.
		for (int ii = 0; ii < nodeCount; ii++) {
			Node node = (Node)nodes.get(ii);
			node.setStartingEdgeOffsetInFile(numBytesWritten);
			
			for (int jj = 0; jj < node.getActualEdgeCount(); jj++) {
				numBytesWritten += this.writeLittleEndianInt(0);
			}
		}
		
		// Write the fake weights.
		for (int ii = 0; ii < nodeCount; ii++) {
			Node node = (Node)nodes.get(ii);
			node.setStartingWeightOffsetInFile(numBytesWritten);
			
			for (int jj = 0; jj < node.getActualEdgeCount(); jj++) {
				numBytesWritten += this.writeLittleEndianInt(0);
			}
		}
	}
	
	private int writeLittleEndianInt(int bigEndianInt) throws IOException {
		this.outputBINFile.writeByte((byte)(0xff & bigEndianInt));
		this.outputBINFile.writeByte((byte)(0xff & (bigEndianInt >> 8)));
		this.outputBINFile.writeByte((byte)(0xff & (bigEndianInt >> 16)));
		this.outputBINFile.writeByte((byte)(0xff & (bigEndianInt >> 24)));
		
		return 4;
	}
}